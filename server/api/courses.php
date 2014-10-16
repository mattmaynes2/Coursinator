<?php
	error_reporting(E_ALL);
	require_once('lib/db.php');
	require_once('lib/Course.php');
	require_once('lib/CourseOffering.php');
	
	switch ($_SERVER['REQUEST_METHOD']) {
	case 'GET':
		$q = new Query('Course');
		$q->select_object('Course');
		
		// Filter based on course code (or prefix thereof)
		if (isset($_GET['code']))
			$q->where_startswith(Course::code, strtoupper($_GET['code']));
		
		// Filter where dependancies are satisfied.
		if (isset($_GET['qualified'])) {
			$completed = isset($_GET['completed']) ? $_GET['completed'] : [];
			foreach ($completed as &$c) {
				$c = strtoupper($c);
			}
			
			$preqquery = Course::query_prerequisites(Course::code, $completed);
			$q->where_exists($preqquery, false);
		}
		
		$q->execute();
		
		header('Content-Type: text/xml; charset=utf-8');
		echo '<response e="0"><courses>';
		foreach ($q->fetchAllScalar() as $course) {
			echo $course->to_xml();
		}
		echo '</courses></response>';
		break;
	default:
		header('Content-Type: text/xml; charset=utf-8');
		http_response_code(405);
		echo '<response e="1" msg="Method must be GET."/>';
	}
