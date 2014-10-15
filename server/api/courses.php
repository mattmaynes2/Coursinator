<?php
	error_reporting(E_ALL);
	require_once('lib/db.php');
	require_once('lib/Course.php');
	require_once('lib/CourseOffering.php');
	
	switch ($_SERVER['REQUEST_METHOD']) {
	case 'GET':
		$q = new Query('Course');
		$q->select('Course');
		
		if (isset($_GET['code']))
			$q->where_startswith(Course::code, strtoupper($_GET['code']));
		
		$q->execute();
		
		$r = '<response e="0"><courses>';
		foreach ($q->fetchAllScalar() as $course) {
			$r .= $course->to_xml();
		}
		$r .= '</courses></response>';
		
		header('Content-Type: text/xml; charset=utf-8');
		echo $r;
		break;
	default:
		header('Content-Type: text/xml; charset=utf-8');
		http_response_code(405);
		echo '<response e="1" msg="Method must be GET."/>';
	}
