	<?xml-stylesheet type="text/xsl" href="suggestedCourses.xsl"?>
<?php //require_once("api/courses.php");
	require_once("api/lib/db.php");
	require_once("api/lib/Course.php");
	require_once("api/scheduler.php");
	require_once("api/electives.php");
	
	//Must select a pattern
	if(!isset($_GET['pattern']))
	{
		echo '<response>Invalid Submission</response>';
		exit;
	}
	
	//Get the list of courses that they have completed
	if(isset($_GET['completed']))
	{
		$completed = $_GET['completed'];
	}
	else if ($_GET['pattern'] == 'offpattern')
	{
		$completed = array();
	}
	else if ($_GET['pattern'] == 'onpattern' and isset($_GET['year_select']))
	{
		$completed_year = $_GET['year_select'];
		$completedQuery = new Query('program_elements');
		$completedQuery->select('course_code');
		$completedQuery->where("element_year <".$completed_year);
		$completedRows = $completedQuery->executeFetchAll();
		$completed = [];
		foreach($completedRows as $row)
		{
			array_push($completed,$row[0]);
		}
	}
	else
	{
		echo "<response>Invalid Submission: completed not set, not on pattern and no year selected</response>";
		exit;
	}
	
	header('Content-Type: text/xml; charset=utf-8');
	
	echo '<response e="0"><courses>';

	//Get the actual course objects from the database
	$completed_as_courses = array();
	
	foreach ($completed as $c)
	{
		array_push($completed_as_courses, Course::fetch($c));
	}

	//Figure out  which term to register for
	$year = date('Y');
	$month = date('n');
	
	if ($month >= 6 and $month <= 7)
	{
		$term = 0;
	}
	else
	{
		$term = 0;
		$year = 2013 + 1;	//TODO CHANGE THIS TO MATCH THE ACTUAL TERM TO SCHEDULE FOR
	}
	
	//IF THEY ARE ON PATTERN ASSUME ELECTIVES ARE FULFILLED UP TO THIS TERM
	//Get the user's program elements, sorted by the term and year they should take them in
	//Only select those courses which have a section available in the scheduling term
	if ($_GET['pattern'] == 'onpattern')
	{
		$programQuery = new Query("program_elements e");
		$programQuery->select("course_code");
		$programQuery->select("credit_type");
		$programQuery->select("elective_note");
		$programQuery->select("term");
		$programQuery->select("element_year");

		$programQuery->where("program_id = ?
							  AND ((credit_type <> 0 AND e.element_year>=$completed_year AND e.term >= $term) OR course_code NOT IN
							  ".Query::valuelistsql($completed).
							  ") AND 
							  ((EXISTS 
							   (SELECT * FROM course_offerings o
							   WHERE o.course_code = e.course_code
							   AND o.year=$year
							   AND o.term=$term))
							   OR
							   (credit_type <> 0))
							   ORDER BY element_year ASC, term ASC, credit_type DESC", array_merge([$_GET['program_select']], $completed));
		$pattern = $programQuery->executeFetchAll(); 
	}
	else
	{
		$programQuery = new Query("program_elements e");
		$programQuery->select("course_code");
		$programQuery->select("credit_type");
		$programQuery->select("elective_note");
		$programQuery->select("term");
		$programQuery->select("element_year");

		$programQuery->where("program_id = ?
							  AND ((credit_type <> 0) OR course_code NOT IN
							  ".Query::valuelistsql($completed).
							  ") AND 
							  ((EXISTS 
							   (SELECT * FROM course_offerings o
							   WHERE o.course_code = e.course_code
							   AND o.year=$year
							   AND o.term=$term))
							   OR
							   (credit_type <> 0))
							   ORDER BY element_year ASC, term ASC, credit_type DESC", array_merge([$_GET['program_select']], $completed));
		$pattern = $programQuery->executeFetchAll(); 
		
		//See which of their electives they have fulfilled and remove them from consideration in the pattern
		foreach ($pattern as $key=>$course)
		{
			if ($course[1] != '0')
			{
				foreach($completed as $considering)
				{
					if (Elective::isElective($_GET['program_select'],$considering[0], $course[1], $course[2]))
					{
						unset($pattern[$key]);
					}
				}
			}
		}
	}


	$found = 0;
	$discarded = array();
	$scheduling = array();
	
	for($i=0; $i<count($pattern); $i++)
	{
		if ($found == 5)
		{
			break;
		}
		
		if ($pattern[$i][1] != '0')
		{
			$found++;
			continue;
		}
		
		$course = Course::fetch($pattern[$i][0]);
		if (count($course->unsatisfied_prerequisites($completed_as_courses, $scheduling)) == 0)
		{
			array_push($scheduling, $course);
			$found++;
		}
		else
		{
			array_push($discarded, $course);
			continue;
		}
		
		//Check and see if we can fit the discarded courses in now
		foreach($discarded as $key=>$tryagain)
		{
			if (isset($tryagain))
			{
				if(count($tryagain->unsatisfied_prerequisites($completed_as_courses, $scheduling)) == 0)
				{
					array_push($scheduling, $tryagain);
					$found++;
					unset($discarded[$key]);
				}
			}
		}
	}

	foreach ($scheduling as $course) 
	{
		$r = '<course>';
		$r .= '<code>'.htmlspecialchars($course->getcode()).'</code>';
		$r .= '<title>'.htmlspecialchars($course->gettitle()).'</title>';
		$r .= '</course>';
		echo $r;
	}
	echo '</courses>';
	$s = Schedule::buildConflictFreeSchedule($scheduling, $year, $term);
	$s->to_xml();
	echo '</response>';
	
?>