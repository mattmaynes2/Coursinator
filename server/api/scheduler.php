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
			
			//Get all of the offerings for each of the desired courses
			foreach($courses as $course)
			{
				$offerings[$course->getcode()] = array();
				
				//Get all the lecture sections for a particular course
				$q = new Query("course_offerings");
				$q->select_object("CourseOffering");
				$q->where('course_code=?
							AND term=?
							AND year=?
							AND type=0', [$course->getcode(),$term,$year]);
		
				$rows = $q->executeFetchAll();

				//For each lecture section get the available lab/tutorial sections
				foreach($rows as $row)
				{
					$section = array('attached'=>array());
					$sub = new Query("course_offerings");
					$sub->select_object('CourseOffering');
					$sub->where("course_code=?
								 AND section LIKE CONCAT(?,'%')
								 AND type <> 0",
								 [$course->getcode(), $row[0]->getsection()]);
					$section['id'] = $row[0]->getsection();
					$offerings[$course->getcode()][$row[0]->getsection()] = ['attached' =>$sub->executeFetchAll()];
				}
			}
			var_dump($offerings);
		}
		
		function scheduleOfferings($offerings)
		{
			if(count($offerings) == 0)
				return true;
			foreach($offerings[0] as $offering)
			{
				if (!hasConflict($offering))
				{
					setCourseAt($offering);
					if (scheduleOfferings(array_slice($offerings,1)))
					{
						return true;
					}
					else
					{
						removeCourse($offering);
					}
				}
			}
			return false;
		}
		
		function hasConflict($offering)
		{
		}
		
		static function getLengthForRange($startTime, $endTime)
		{
			$time1 = explode(':', $timeSlot);
			$time2 = explode(':', $timeSlot);
			$halfHours1= $time1[0] * 2 + ($time1[1] == '30' ? 1:0);
			$halfHours2 = $time2[0] * 2 + ($time2[1] == '30' ? 1:0);
			return $halfHours2 - $halfHours1;
		}
		
		//TIME FORMATS MUST USE 24-HOUR FORMAT
		static function getIndexForTime($timeSlot)
		{
			$time = explode(':', $timeSlot);
			$halfHours = $time[0] * 2 + ($time[1] == '30' ? 1:0);
			$offset = $halfHours;		
		}
		
		function removeCourse($offering)
		{
			foreach(str_split($offerings[$course_codes[0]]->getdays()) as $day)
			{
				for ($i=0; i<getLengthForRange($offering); $i++)
				{
					$timeslots[$day][$$offerings->getstarttime()+$i] = 'NOCOURSE';
				}
			}
		}
		
		function setCourseAt($offering)
		{
		
			foreach(str_split($offerings[$course_codes[0]]->getdays()) as $day)
			{
				for ($i=0; i<getLengthForRange($offering); $i++)
				{
					$timeslots[$day][$$offerings->getstarttime()+$i] = $offering;
				}
			}
		}
		
		function isTimeFree($day, $startTime, $endTime)
		{
			for ($i=0; $i < getIndexForTime($endTime)-getIndexForTime($startTime); $i++)
			{
				if ($timeslots[$day][getIndexForTime($startTime)+$i] != 'NOCOURSE')
				{
					return false;
				}
			}
			return true;
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
					echo "<value day='$name'>".$this->timeslots[$name][$slot].'</value>';
				}
				echo "</slot>";
			}
			echo '</schedule>';
		}
	}