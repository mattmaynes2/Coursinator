<?php
	chdir(__DIR__);
	$lib = '../server/api/lib/';
	
	require_once("$lib/db.php");
	require_once("$lib/Course.php");
	
	/*
	 * Wrapper for fgetcsv() so we have a single place
	 * to pass format arguments.
	 */
	function gcsv($fd){
		return fgetcsv($fd, 0, ';');
	}
	
	$db->beginTransaction();
	
	$courses = [];
	
	$fd = fopen("course_data.csv", "r");
	fgets($fd); // Drop header line.
	
	while ($e = gcsv($fd)){
		if (count($e) != 9)
			die("Got line with ".count($e)."lines!");
		
		$dept     = $e[0];
		$num      = $e[1];
		$section  = $e[2];
		$title    = $e[3];
		$type     = $e[4];
		$days     = $e[5];
		$start    = $e[6];
		$end      = $e[7];
		$capacity = $e[8];
		
		# Create the course.
		$code = $dept.str_pad($num, 4, '0', STR_PAD_LEFT);
		if (isset($courses[$code])) {
			$c = $courses[$code];
		} else {
			$c = new Course($code);
			$c->settitle($title);
			$c->setlevel(Course::LEVEL_UNDERGRAD);
			$c->save();
			$courses[$c->getcode()] = $c;
		}
	}
	
	$db->commit();
