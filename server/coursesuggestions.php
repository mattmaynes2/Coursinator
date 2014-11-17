<?xml-stylesheet type="text/xsl" href="suggestedCourses.xsl"?>
<?php //require_once("api/courses.php");
	require_once("api/lib/db.php");
	require_once("api/lib/Course.php");
	
	/*
	* The following must be true or this is an invalid submission
	* pattern must be set to either onpattern or offpattern
	* if completed is not set, pattern must be set to onpattern
	* program_select must be set
	* year_select must be set if on pattern
	*/
	if(!isset($_GET['pattern']))
	{
		echo '<response>Invalid Submission</response>';
		exit;
	}
	
	if(isset($_GET['completed']))
	{
		$completed = $_GET['completed'];
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
		echo "<response>Invalid Submission</response>";
		exit;
	}

	/*
	* This Query will retrieve all courses that the student has not completed, 
	* exist as a program element for their program, and are running a section in the upcoming term
	*/
	$q = new Query("courses c");

	$q->select("code");
	$q->select("title");	
	$q->where(" EXISTS (SELECT o.course_code FROM course_offerings o WHERE c.code = o.course_code) 
				AND NOT c.code IN ".Query::valuelistsql($completed)."
				AND c.code IN (SELECT course_code FROM program_elements WHERE program_id = ".$_GET['program_select'].")", $completed);
	
	$rows = $q->executeFetchAll();
	
	header('Content-Type: text/xml; charset=utf-8');
	echo '<response e="0"><courses>';
	
	foreach ($rows as $course) {
		$r = '<course>';
		$r .= '<code>'.htmlspecialchars($course[0]).'</code>';
		$r .= '<title>'.htmlspecialchars($course[1]).'</title>';
		$r .= '</course>';
		echo $r;
	}
	
	echo '</courses></response>';
?>