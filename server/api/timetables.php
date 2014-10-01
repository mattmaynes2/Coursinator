<?php
	if ($_SERVER['REQUEST_METHOD'] != 'POST'){
		http_response_code(405);
		echo '{"e":1,"msg":"Method must be POST."}';
		exit;
	}
	
	require_once('lib/json.php');
	
	$in = readjson();
	
	$r = ['e' => 0,
		'timetables' => [],
	];
	
	header("Content-Type", "application/javascript; charset=utf-8");
	echo json_encode($r);
