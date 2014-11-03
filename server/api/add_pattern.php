<?php
	require_once('lib/db.php');

	if ($_SERVER['REQUEST_METHOD'] != 'POST'){
		http_response_code(405);
		echo '{"e":1,"msg":"Method must be POST."}';
		exit;
	}

	$db->beginTransaction();
	
	$file = fopen("php://input", "r");
	fgets($file);
	
	while (!feof($file))
	{
		$insert = "INSERT INTO `coursinator`.`program_elements` VALUES (?,?,?,?,?)";
		$line =  explode(',', trim(fgets($file)));
		db_exec($insert, $line);
	}
	
	$db->commit();
	
	