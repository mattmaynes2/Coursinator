<?php
	require_once('lib/Course.php');
	require_once('lib/db.php');
	require_once('lib/CourseOffering.php');
	
	//Schedules are divided into half-hour time slots
	class Schedule
	{
		private $timeslots;

		function __construct()
		{
			$this->timeslots = array();
			$this->timeslots['M'] = array_fill(0,26,'NOCOURSE');
			$this->timeslots['T'] = array_fill(0,26,'NOCOURSE');
			$this->timeslots['W'] = array_fill(0,26,'NOCOURSE');
			$this->timeslots['R'] = array_fill(0,26,'NOCOURSE');
			$this->timeslots['F'] = array_fill(0,26,'NOCOURSE');
		}
		
		//This will create a conflict free schedule from the list of course codes given
		static function buildConflictFreeSchedule($courses, $year, $term)
		{
			$offerings = array();
			$s = new Schedule();
			
			if (count($courses) <= 0)
				return null;
			
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
							AND (capacity - enrolled) > 0 
							AND type=0', [$course->getcode(),$term,$year]);
		
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
								 [$course->getcode(), $row[0]->getsection()]);
					array_push($offerings[$i], ['labs' =>$sub->executeFetchAll(), 'lecture' => $row[0]]);
				}
				$i++;
			}

			$s->scheduleCourses($offerings);
			return $s;
		}
		
		
		function scheduleCourses($offerings)
		{	
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
				}
				else
				{
					continue;
				}
				if (count($lecture['labs']) == 0)
				{
					if ($this->scheduleCourses(array_slice($offerings, 1)))
					{
						return true;
					}
				}
				foreach($lecture['labs'] as $lab)
				{
					//This lab doesn't have a time 
					if (($lab[0]->getstarttime() == '' and $lab[0]->getendtime() == ''))
					{
						return true;
					}
					//Add this lab to the schedule if the slot is free
					if ($this->isTimeFree($lab[0]))
					{
						$this->setCourseAt($lab[0]);
						if ($this->scheduleCourses(array_slice($offerings, 1)))
						{
							return true;
						}
						else
						{
							$this->removeCourse($lab[0]);
						}
					}
				}
				$this->removeCourse($lecture['lecture']);
			}
			return false;
		}
		
		static function getLengthForRange($startTime, $endTime)
		{	
			$halfHours1 = ($endTime['hours'] - $startTime['hours']) * 2 ;
			$halfHours2 = ($endTime['minutes'] - $startTime['minutes']) == 20 ? 1:0;
			return $halfHours2 + $halfHours1;
		}
		
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
		
		function removeCourse($offering)
		{
			foreach(str_split($offering->getdays()) as $day)
			{
				$this->getLengthForRange(Schedule::getTimeFromTimestamp($offering->getstarttime()), Schedule::getTimeFromTimestamp($offering->getendtime()));
				for ($i=0; $i<$this->getLengthForRange(Schedule::getTimeFromTimestamp($offering->getstarttime()), Schedule::getTimeFromTimestamp($offering->getendtime())); $i++)
				{
					$this->timeslots[$day][Schedule::timeToSlot(Schedule::getTimeFromTimestamp($offering->getstarttime()))+$i] = 'NOCOURSE';
				}
			}
		}
		
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
		
		function to_xml()
		{
			echo '<schedule>';
			for($slot=0; $slot<26; $slot++)
			{
				echo "<slot index='".Schedule::slotToTime($slot)."'>";
				foreach($this->timeslots as $name=>$day)
				{
					if ($this->timeslots[$name][$slot] != 'NOCOURSE')
					{
						echo "<value day='$name'>".$this->timeslots[$name][$slot]->getcourse()->getcode().' '.$this->timeslots[$name][$slot]->getsection().'</value>';
					}
					else
					{
						echo "<value day='$name'></value>";
					}
				}
				echo "</slot>";
			}
			echo '</schedule>';
		}
	}