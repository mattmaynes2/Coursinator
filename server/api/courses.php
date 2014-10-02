<?php
	require_once('lib/db.php');
	require_once('lib/Course.php');
	require_once('lib/CourseOffering.php');
	
	switch ($_SERVER['REQUEST_METHOD']) {
	case 'GET':
		$q = new Query('Course');
		$q->select('Course');
		$q->where_startswith(Course::code, 'WGST');
		$q->execute();
		
		$r = [
			'e' => 0,
			// 'sql' => $q->sql(),
			'courses' => $q->fetchAllScalar(),
		];
		
		$q->closeCursor();
		
		header("Content-Type", "application/javascript; charset=utf-8");
		echo json_encode($r);
		break;
	default:
		header("Content-Type", "application/javascript; charset=utf-8");
		http_response_code(405);
		echo '{"e":1,"msg":"Method must be GET."}';
	}
