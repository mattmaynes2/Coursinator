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
		
		static function buildConflictFreeSchedule($course_codes)
		{
			$offerings = array();
			$s = new Schedule();
			
			if (count($course_codes) <= 0)
				return null;
			
			//Get all of the offerings for the desired courses
			foreach($course_codes as $code)
			{
				array_push($offerings, [Course::fetch($code), array()]);
				$q = new Query("course_offerings");
				$q->select_object("CourseOffering");
				$q->filter(CourseOffering::course_code.'=?', [$code]);
				$offerings[$code] = $q->fetchAll();
			}
			
			if ($s->scheduleOfferings[$offerings])
			{
				return s;
			}
			else
			{
				return false;
			}
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
			
				// foreach($this->timeslots as $name=>$day)
				// {
					// echo "<day name='$name'>";
					// foreach($day as $offset=>$slot)
					// {
						// echo "<slot start='".Schedule::slotToTime($offset)."'>";
						// if ($slot != "NOCOURSE")
						// {
							// echo $slot->to_xml();
							// for($i=0; $i<getLengthForRange($slot->getstarttime(), $slot->getendtime()); $i++)
							// {
								// $day->next();
							// }
						// }
						// else
						// {
							// echo '<emptyslot>'.'</emptyslot>';
						// }
						// echo '</slot>';
					// }
					// echo'</day>';
				// }
			
			echo '</schedule>';
		}
	}