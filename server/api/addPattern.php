<?php
	require_once('lib/db.php');

	if ($_SERVER['REQUEST_METHOD'] != 'POST'){
		http_response_code(405);
		echo '{"e":1,"msg":"Method must be POST."}';
		exit;
	}

	$db->beginTransaction();
	
	$file = fopen("php://input", "r");
	$line = trim(fgets($file));
	$firstLine = explode(',', $line);
	$name = $firstLine[0];
	$year = $firstLine[1];

	$addProgram = "INSERT INTO programs (year,name)
					VALUES (?, ?)";

	db_exec($addProgram,[$year, $name]);				
	
	$programQuery = new Query("programs");
	$programQuery->select("id");
	$programQuery->where("name = ?
						AND year = ?", [$name,$year]);
	$id = $programQuery->executeFetchAll();

	//insert the program elements
	while (!feof($file))
	{
		$insert = "INSERT INTO `coursinator`.`program_elements` 
				VALUES (?,?,?,?,?,?)";
		$line = trim(fgets($file));
		if ($line == 'ELECTIVES')
		{
			break;
		}
		$newline =  explode(',', $line);
		$newline[0] = $id[0][0];
		db_exec($insert, $newline);
	}
	
	//insert the electives
	while (!feof($file))
	{
		$insert = "INSERT INTO `coursinator`.`electives` 
					VALUES (?,?,?,?)";
		$newline = explode(',', trim(fgets($file)));
		$newline[0] = $id[0][0];
		db_exec($insert, $newline);
	}
	
	$db->commit();
	
	
