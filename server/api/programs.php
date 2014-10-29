<?php
	if ($_SERVER['REQUEST_METHOD'] != 'GET'){
		http_response_code(405);
		echo '{"e":1,"msg":"Method must be GET."}';
		exit;
	}
	
	require_once('lib/db.php');

	$r = ['e' => 0,
	];
	
	$requested_program = $_GET['program'];
	$response = '';
	
	$q = new Query("program_elements");
	$q->select("course_code");
	$q->where("program_id = ?", [$requested_program]);
	$rows = $q->executeFetchAll();
	
	foreach($rows as $course)
	{
		$response .= $course[0].',';
	}
	
	trim($response, ',');
	
	header("Content-Type", "application/javascript; charset=utf-8");
	echo json_encode($rows);
