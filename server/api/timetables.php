<?php
	include('lib/json.php');
	
	if ($_SERVER['REQUEST_METHOD'] != 'POST'){
		http_response_code(400);
		echo '{"e":1,"msg":"Method must be POST."}';
		exit;
	}
	
	$in = readjson();
	
	header("Content-Type", "application/javascript; charset=utf-8");
	
	$r = ['e' => 0,
		'timetables' => [],
	];
	
	echo json_encode($r);
