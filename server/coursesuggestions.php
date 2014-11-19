<?xml-stylesheet type="text/xsl" href="suggestedCourses.xsl"?>
<?php //require_once("api/courses.php");
	require_once("api/lib/db.php");
	require_once("api/lib/Course.php");
	require_once("api/scheduler.php");
	
	/*
	* The following must be true or this is an invalid submission
	* pattern must be set to either onpattern or offpattern
	* if completed is not set, pattern must be set to onpattern
	* program_select must be set
	* year_select must be set if on pattern
	*/
	if(!isset($_GET['pattern']) or !isset($_GET['term']))
	{
		echo '<response>Invalid Submission</response>';
		exit;
	}
	
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
		$completedQuery = new Query('program_elements');
		$completedQuery->select('course_code');
		$completedQuery->where("element_year <".$_GET['year_select']);
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

	/*
	* This Query will retrieve all courses that the student has not completed, 
	* exist as a program element for their program, and are running a section in the upcoming term
	* The results are ordered by the order in which they are to be completed according to the program pattern
	* Scheduler will pick the first several of these to attempt to build a schedule
	*/
	$q = new Query("course_offerings o, program_elements e, courses c");
	
	$t = explode(",",$_GET['term']);
	
	$year = $t[1];
	$session = $t[0];
	
	$q->select("DISTINCT c.code");
	$q->select("c.title");	
	$q->where(" c.code = e.course_code
				AND o.course_code = c.code
				AND o.term=$session 
				AND o.year=$year
				AND NOT c.code IN ".Query::valuelistsql($completed)."
				AND c.code IN 
				(SELECT course_code 
				FROM program_elements 
				WHERE program_id = ".$_GET['program_select'].")
				ORDER BY e.element_year ASC, e.term ASC", $completed);
	
	$rows = $q->executeFetchAll();
	
	header('Content-Type: text/xml; charset=utf-8');
	
	echo '<response e="0"><courses>';
	
	$completed_as_courses = array();
	
	
	foreach ($completed as $c)
	{
		array_push($completed_as_courses, Course::fetch($c));
	}

	$possible_courses = array_slice($rows, 0, 5);
	$taking = array();
	$unsatisfied_prerequisites = array();
	$possible_concurrent = array();

	foreach ($rows as $course) 
	{
		$c = Course::fetch($course[0]);
		$p = $c->unsatisfied_prerequisites($completed_as_courses);
		$unsatisfied_prerequisites[$c->getcode()] = $p;

		if (count($p) == 0)
		{
			$r = '<course>';
			$r .= '<code>'.htmlspecialchars($course[0]).'</code>';
			$r .= '<title>'.htmlspecialchars($course[1]).'</title>';
			$r .= '</course>';
			echo $r;
			array_push($taking, $c);
		}
		else
		{
			$possible_concurrent[$c->getcode()] = $c;
		}
	}

	foreach($possible_concurrent as $code=>$prereq)
	{
		$c = Course::fetch($code);
		if (count($c->unsatisfied_prerequisites($completed_as_courses,$taking) == 0))
		{
			$r = '<course>';
			$r .= '<code>'.htmlspecialchars($c->getcode()).'</code>';
			$r .= '<title>'.htmlspecialchars($c->gettitle()).'</title>';

			foreach($unsatisfied_prerequisites[$code] as $with)
			{
				$r .= '<with>'.$with[0].'</with>';
			}
			$r .= '</course>';
			echo $r;
		}
	}
	
	echo '</courses>';
	
	$s = new Schedule();
	$s->to_xml();
	echo '</response>';
	
?>