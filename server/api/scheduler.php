<?php
	require_once('lib/Course.php');
	require_once('lib/db.php');
	require_once('lib/CourseOffering.php');
	
	//Schedules are divided into half-hour time slots
	class Schedule
	{
		/* 2D array of days and timeslots
		* Each day is broken down into 26 half-hour time slots
		* Each timeslot has a CourseOffering object associated with it, or the string "NOCOURSE" if it is an empty slot
		*/
		private $timeslots;
		
		//The sections registered in this schedule
		private $registeredSections;
		
		//The term this schedule is built for
		private $term;
		
		//The year this schedule if built for
		private $year;
		
		//A list of alternative schedules
		private $schedules;
		
		//Courses which have no assigned meeting time
		private $notimes;
		
		const MAX_SCHEDULES = 5;
		
		function __construct()
		{
			$this->timeslots = array();
			$this->timeslots['M'] = array_fill(0,26,'NOCOURSE');
			$this->timeslots['T'] = array_fill(0,26,'NOCOURSE');
			$this->timeslots['W'] = array_fill(0,26,'NOCOURSE');
			$this->timeslots['R'] = array_fill(0,26,'NOCOURSE');
			$this->timeslots['F'] = array_fill(0,26,'NOCOURSE');
			$this->registeredSections = array();
			$this->notimes = array();
			$this->schedules = array();
		}
		
		function reinitialize()
		{
			$this->notimes = array();
			$this->timeslots = array();
			$this->timeslots['M'] = array_fill(0,26,'NOCOURSE');
			$this->timeslots['T'] = array_fill(0,26,'NOCOURSE');
			$this->timeslots['W'] = array_fill(0,26,'NOCOURSE');
			$this->timeslots['R'] = array_fill(0,26,'NOCOURSE');
			$this->timeslots['F'] = array_fill(0,26,'NOCOURSE');
			$this->registeredSections = array();
		}
		
		function getSchedules()
		{
			return $this->schedules;
		}
		
		function setTerm($term)
		{
			$this->term = $term;
		}
		
		function setYear($year)
		{
			$this->year = $year;
		}
		
		//This will create a conflict free schedule from the list of course codes given
		static function buildConflictFreeSchedule($courses, $year, $term, $alternates)
		{
			$offerings = array();
			$s = new Schedule();
			$s->setTerm($term);
			$s->setYear($year);
			
			if (count($courses) <= 0)
				return new Schedule();
			
			$i = 0;
			//Get all of the offerings for each of the desired courses
			foreach($courses as $key=>$course)
			{
				$offerings[$i] = array();
				
				//Get all the lecture sections for a particular course
				$q = new Query("course_offerings");
				$q->select_object("CourseOffering");
				$q->where('course_code=?
							AND term=?
							AND year=?
							AND ((capacity - enrolled > 0) OR (capacity=0))
							AND type=0', [$course->getcode(),$term,$year]);
		
				$rows = $q->executeFetchAll();

				//For each lecture section get the available lab/tutorial sections
				foreach($rows as $row)
				{
					$sub = new Query("course_offerings");
					$sub->select_object('CourseOffering');
					$sub->where("((course_code=?
								 AND (section LIKE CONCAT(?,'%')
								 OR section LIKE ('L%'))
								 AND type <> 0))
								 AND (((capacity-enrolled) > 0) OR (capacity=0))",
								 [$course->getcode(), $row[0]->getsection()]);
					$results = $sub->executeFetchAll();

					array_push($offerings[$i], ['labs' =>$results, 'lecture' => $row[0]]);
				}
				$i++;
			}
			$list = [];
			$s->scheduleCourses($offerings,0,Schedule::MAX_SCHEDULES,$alternates, $list);
			return $s;
		}
		
		/*
		* Main Scheduling Algorithm
		* Builds all possible schedules with the given course offerings, up to a maximum number $scheduleLimit
		*/
		function scheduleCourses($offerings, $depth, $scheduleLimit, $alternates = [], & $list=[], $numGenerated = 0)
		{	
			$success = false;	
			if (count($offerings) == 0)
			{
				return true;
			}
			//For each lecture section of the course
			foreach($offerings[0] as $lecture)
			{
				//Add this lecture to the schedule if the slot is free
				if ($this->isTimeFree($lecture['lecture']))
				{
					$this->setCourseAt($lecture['lecture']);
					if ($lecture['lecture']->getstarttime() == 0 or $lecture['lecture']->getendtime() == 0)
					{
						array_push($this->notimes, $lecture['lecture']);
					}
				}
				else
				{
					continue;
				}
				//If there are no labs attached to this course, try to schedule remaining courses
				if (count($lecture['labs']) == 0)
				{
					if ($this->scheduleCourses(array_slice($offerings, 1),$depth+1,$scheduleLimit,$list,$numGenerated))
					{
						$this->registeredSections[$lecture['lecture']->getcourse()->getcode().$lecture['lecture']->getsection()] = ['depth'=>$depth,$lecture['lecture']];
						if ($depth == 0 && $numGenerated < $scheduleLimit)
						{
							array_push($this->schedules, clone $this);
							$this->reinitialize();
							$this->setCourseAt($lecture['lecture']);
							$success = true;
							$numGenerated++;
						}
						else
						{
							return true;
						}
					}
				}
				//If there are labs attached to this course, for each lab section, attempt to schedule the remaining courses
				foreach($lecture['labs'] as $lab)
				{
					//This lab doesn't have a time 
					if (($lab[0]->getstarttime() == '' and $lab[0]->getendtime() == ''))
					{
						$this->registeredSections[$lecture['lecture']->getcourse()->getcode().$lecture['lecture']->getsection()] = ['depth'=>$depth,$lecture['lecture']];
						$this->registeredSections[$lab[0]->getcourse()->getcode().$lab[0]->getsection()] = ['depth'=>$depth,$lab[0]];
						
						if ($depth == 0 && $numGenerated < $scheduleLimit)
						{
							array_push($this->schedules, clone $this);
							$this->reinitialize();
							$this->setCourseAt($lecture['lecture']);
							$success = true;
							$numGenerated++;
						}
						else
						{
							return true;
						}
					}
					//Add this lab to the schedule if the slot is free
					if ($this->isTimeFree($lab[0]))
					{
						$this->setCourseAt($lab[0]);
						if ($this->scheduleCourses(array_slice($offerings, 1),$depth+1,$scheduleLimit,$list,$numGenerated))
						{
							$this->registeredSections[$lecture['lecture']->getcourse()->getcode().$lecture['lecture']->getsection()] = ['depth'=>$depth,$lecture['lecture']];
							$this->registeredSections[$lab[0]->getcourse()->getcode().$lab[0]->getsection()] = ['depth'=>$depth,$lab[0]];
							if ($depth == 0 && $numGenerated < $scheduleLimit)
							{
								array_push($this->schedules, clone $this);
								$this->reinitialize();
								$this->setCourseAt($lecture['lecture']);
								$numGenerated++;
								$success = true;
							}
							else
							{
								return true;
							}
						}
						else
						{
							$this->removeCourse($lab[0]);
						}
					}
				}
				$this->removeCourse($lecture['lecture']);
			}
			//If we have failed to build any schedule, try and replace this one with alternate courses to satisfy the schedule
			$result = false;
			while(isset($alternatives[0]))
			{
				$offerings[0] = $this->getOfferingInfo(array_shift($alternates)[0]);
				$result = $this->scheduleCourses($offerings,$depth,$alternates);
				
			}
			return $result;
		}
		
		//This will get a list of all attached labs required to schedule a course
		function getOfferingInfo($course_code)
		{
				$offerings = array();
				
				//Get all the lecture sections for a particular course
				$q = new Query("course_offerings");
				$q->select_object("CourseOffering");
				$q->where('course_code=?
							AND term=?
							AND year=?
							AND (capacity - enrolled) > 0 
							AND type=0', [$course_code,$this->term,$this->year]);
		
				$rows = $q->executeFetchAll();

				//For each lecture section get the available lab/tutorial sections
				foreach($rows as $row)
				{
					$sub = new Query("course_offerings");
					$sub->select_object('CourseOffering');
					$sub->where("course_code=?
								 AND (section LIKE CONCAT(?,'%')
								 OR section LIKE ('L%'))
								 AND (((capacity-enrolled) > 0) OR (capacity=0))
								 AND type <> 0",
								 [$course_code, $row[0]->getsection()]);
					array_push($offerings, ['labs' =>$sub->executeFetchAll(), 'lecture' => $row[0]]);
				}
				return $offerings;
		}
		
		//Converts a range of times to the number of timeslots occupied
		static function getLengthForRange($startTime, $endTime)
		{	
			$halfHours1 = ($endTime['hours'] - $startTime['hours']) * 2 ;
			$halfHours2 = ($endTime['minutes'] - $startTime['minutes']) == 20 ? 1:0;
			return $halfHours2 + $halfHours1;
		}
		//Converts a timestamp as stored in the database to a useable format
		static function getTimeFromTimestamp($t)
		{
			if (strlen($t) == 3)
			{
				$t = '0'.$t;
			}

			$r = array();
			$r['minutes'] = substr($t,strlen($t)-2, 2);
			$r['hours'] = substr($t,0, 2);

			return $r;
		}
		
		//Removes a course offering from this schedule
		function removeCourse($offering)
		{
			foreach(str_split($offering->getdays()) as $day)
			{
				$this->getLengthForRange(Schedule::getTimeFromTimestamp($offering->getstarttime()), Schedule::getTimeFromTimestamp($offering->getendtime()));
				for ($i=0; $i<$this->getLengthForRange(Schedule::getTimeFromTimestamp($offering->getstarttime()), Schedule::getTimeFromTimestamp($offering->getendtime())); $i++)
				{
					if (isset($this->timeslots[$day][Schedule::timeToSlot(Schedule::getTimeFromTimestamp($offering->getstarttime()))+$i]))
					{
						$this->timeslots[$day][Schedule::timeToSlot(Schedule::getTimeFromTimestamp($offering->getstarttime()))+$i]= 'NOCOURSE';
					}
				}
			}
		}
		
		//Adds a course offering to this schedule
		function setCourseAt($offering)
		{
			foreach(str_split($offering->getdays()) as $day)
			{
				for ($i=0; $i<$this->getLengthForRange(Schedule::getTimeFromTimestamp($offering->getstarttime()), Schedule::getTimeFromTimestamp($offering->getendtime())); $i++)
				{
					$this->timeslots[$day][Schedule::timeToSlot(Schedule::getTimeFromTimestamp($offering->getstarttime()))+$i] = $offering;
				}
			}
		}
		
		//Checks if a time range is free on this schedule (accepts a course offering as input and uses its start and end times as the range)
		function isTimeFree($offering)
		{
			foreach(str_split($offering->getdays()) as $day)
			{
				$this->getLengthForRange(Schedule::getTimeFromTimestamp($offering->getstarttime()), Schedule::getTimeFromTimestamp($offering->getendtime()));
				for ($i=0; $i<$this->getLengthForRange(Schedule::getTimeFromTimestamp($offering->getstarttime()), Schedule::getTimeFromTimestamp($offering->getendtime())); $i++)
				{
					if ($this->timeslots[$day][Schedule::timeToSlot(Schedule::getTimeFromTimestamp($offering->getstarttime()))+$i] != 'NOCOURSE')
					{
						return false;
					}
				}
			}
			return true;
		}
		
		//Converts a time to an index in the timeslots array
		static function timeToSlot($t)
		{
			$hourOffset = ($t['hours'] - 8) * 2;
			if ($t['minutes'] == 55)
			{
				$minutesOffset = 1;
			}
			else if($t['minutes'] == 35)
			{
				$minuteOffset = 0;
			}
			else
			{
				$minuteOffset = -1;
			}
			return $hourOffset + $minuteOffset;
		}
		
		//Converts an index in the timeslots array to a timestamp hour:minute
		static function slotToTime($offset)
		{
			$hour = floor(($offset+1)/2) + 8;
			$minute = ($offset+1)%2 * 30;
			if ($minute ==0)
			{
				$minute = '00';
			}
			return $hour.':'.$minute;
		}
		
		//Output this schedule to a client, XML format described coursesuggestions.php
		function to_xml()
		{
			echo '<schedule>';
			for($slot=0; $slot<26; $slot++)
			{
				echo "<slot index='".Schedule::slotToTime($slot)."'>";
				foreach($this->timeslots as $name=>$day)
				{
					if ($this->timeslots[$name][$slot] == 'NOCOURSE')
					{
						echo "<value day='$name'></value>";
					}
					else
					{
						$firstCourse = $this->timeslots[$name][$slot]->getcourse()->getcode().' '.$this->timeslots[$name][$slot]->getsection();
						$span = 1;
						do
						{
							if ($this->timeslots[$name][$slot+$span] != 'NOCOURSE')
							{
								$toCheck = $this->timeslots[$name][$slot+$span]->getcourse()->getcode().' '.$this->timeslots[$name][$slot+$span]->getsection();
							}
							else
							{
								$toCheck = null;
							}
							$span++;
						}while($toCheck != null and $toCheck == $firstCourse);
						echo "<value day='$name' span='$span' depth='".$this->registeredSections[$this->timeslots[$name][$slot]->getcourse()->getcode().$this->timeslots[$name][$slot]->getsection()]['depth'] ."'>".$this->timeslots[$name][$slot]->getcourse()->getcode().$this->timeslots[$name][$slot]->getsection();
						echo '</value>';
					}
				}
				echo "</slot>";
			}
			echo '<sections>';
			foreach($this->registeredSections as $section)
			{
				if ($section)
					echo $section[0]->to_xml();
			}
			echo '</sections>';
			echo '<notimes>';
			
			foreach($this->notimes as $notime)
			{
				echo $notime->to_xml();
			}
			echo '</notimes>';
			
			echo '</schedule>';
		}
	}