<?php
	if ($_SERVER['REQUEST_METHOD'] != 'GET'){
		http_response_code(405);
		echo '{"e":1,"msg":"Method must be GET."}';
		exit;
	}
	
	require_once('lib/db.php');
	
	$r = ['e' => 0,
	];
	
	header("Content-Type", "application/javascript; charset=utf-8");
	echo json_encode($r);
