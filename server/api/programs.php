<?php
	if ($_SERVER['REQUEST_METHOD'] != 'GET'){
		http_response_code(405);
		echo '{"e":1,"msg":"Method must be GET."}';
		exit;
	}
	
	require_once('lib/db.php');
	require_once('lib/Program.php');
	$r = ['e' => 0,
	];
	
	$requested_program = $_GET['program'];
	$r['program_xml'] = Program.sql_from(Program.fetch($requested_program))->to_xml();
	
	header("Content-Type", "application/javascript; charset=utf-8");
	echo json_encode($r);
