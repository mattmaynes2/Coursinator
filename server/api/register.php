<?php
require_once("lib/db.php");
require_once("lib/CourseOffering.php");

	//Figure out  which term to register for
	$year = date('Y');
	$month = date('n');
	
	$year = date('Y');
	$month = date('n');
	
	if ($month >= 6 and $month <= 7)
	{
		$term = 0;
	}
	else
	{
		$term = 1;
		if ($month > 7)
		{
			$year = $year + 1;
		}
	}

	if (!isset($_POST['enroll']))
	{
		echo "<h2>Registration completed</h2>";
	}
	else
	{
		foreach($_POST['enroll'] as $sectioncode)
		{
			$course_code = substr($sectioncode,0,8);
			$section_id = substr($sectioncode,8);
			
			$q = new Query('course_offerings');
			$q->select_object('CourseOffering');
			$q->where("term=? 
						AND year=?
						AND course_code = ?
						AND section = ?",[$term,$year,$course_code, $section_id]);
			$result = $q->executeFetchAll();

			if (count($result) < 1)
			{
				echo '<p style="color:red">Could not find section '.$sectioncode.'</p>';
				continue;
			}

			if (($result[0][0]->getcapacity() - $result[0][0]->getenrolled() > 0) or $result[0][0]->getcapacity() == 0)
			{
				$sql = "UPDATE course_offerings
						SET enrolled=enrolled+1
						WHERE 
						term=?
						AND year=?
						AND course_code=?
						AND section=?
						AND (enrolled < capacity OR capacity=0)";
				$result = db_exec($sql, [$term,$year,$course_code,$section_id]);
				if ($result->rowCount() == 0)
				{
					echo '<p style="color:red">Section '.$sectioncode.' is now full. Continued registration in remaining courses</p>';
				}
				else
				{
					echo '<p>Registered in '.$sectioncode.'</p>';
				}
			}
			else
			{
				echo '<p style="color:red">Section '.$sectioncode.' is now full. Continued registration in remaining courses</p>';
			}
		}
		echo '<h2>Registration completed</h2>';
	}
?>