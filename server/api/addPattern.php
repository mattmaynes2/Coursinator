<?php
	require_once('lib/db.php');
	
	if ($_SERVER['REQUEST_METHOD'] != 'POST') {
		http_response_code(405);
		echo '<response e="1" msg="Method must be POST."/>';
		exit;
	}
	
	$db->beginTransaction();
	
	$file = fopen("php://input", "r");
	$line = trim(fgets($file));
	$firstLine = str_getcsv($line);
	$name = $firstLine[0];
	$year = $firstLine[1];
	
	$addProgram = "INSERT INTO programs (year,name)
	               VALUES (?, ?)";
	
	db_exec($addProgram,[$year, $name]);
	
	$programQuery = new Query("programs");
	$programQuery->select("id");
	$programQuery->where("name = ?", [$name]);
	$programQuery->where("year = ?", [$year]);
	$id = $programQuery->executeFetchAll();
	
	//insert the program elements
	while (!feof($file))
	{
		$insert = "INSERT INTO program_elements(
			program_id,
			course_code,
			credit_type,
			term,
			element_year,
			elective_note
		) VALUES (?,?,?,?,?,?)";
		
		$line = trim(fgets($file));
		if ($line == 'ELECTIVES')
			break;
		
		$id = $id[0][0];
		list(
			$code,
			$type,
			$term,
			$year,
			$note
		) = str_getcsv($line);
		if ($code == '########') $code = NULL;
		try {
			db_exec($insert, [
				$id,
				$code,
				$type,
				$term,
				$year,
				$note,
			]);
		} catch (Exception $e) {
			echo "Skipping $code\n";
		}
	}
	
	//insert the electives
	while (!feof($file))
	{
		$insert = "INSERT INTO electives(
			program_id,
			course_code,
			elective_type,
			note
		) VALUES (?,?,?,?)";
		
		$id = $id[0][0];
		$line = fgetcsv($file);
		if ($line === FALSE) break;
		list(
			$code,
			$type,
			$note
		) = $line;
		
		try {
			db_exec($insert, [
				$id,
				$code,
				$type,
				$note,
			]);
		} catch (Exception $e) {
			echo "Skipping $code\n";
		}
	}
	
	$db->commit();
