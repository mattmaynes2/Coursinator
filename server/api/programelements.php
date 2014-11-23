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
	$q->where("program_id = ? and credit_type = 0", [$requested_program]);
	$rows = $q->executeFetchAll();
	
	$response = '<program>';
	
	foreach ($rows as $course)
	{
		$code = $course[0];
		$response .= "<course>$code</course>";
	}
	
	$response .= "</program>";
	
	header("Content-Type", "application/xml; charset=utf-8");
	echo $response;
