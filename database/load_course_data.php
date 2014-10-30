<?php

require_once('../server/api/lib/db.php');

$year = 2014;
$term = 0; //fall term

$file = fopen('course_data.csv', 'r') or die('Could not open file');

fgets($file); //Skip header line
	
//get course offering data
while (!feof($file))
{
	$insert_course = "INSERT IGNORE INTO `coursinator`.`courses`
						(`code`, `title`, `description`, `level`)
						VALUES
						(?,?,?,?)";
						
	$insert_offering = "INSERT INTO `coursinator`.`course_offerings`
				  (`course_code`, `year`, `section`, `term`, `days`, `enrolled`, `capacity`, `room`, `type`)
				  VALUES
				  (?,?,?,?,?,?,?,?,?)";
	
	$raw = explode(';', str_replace('"', '',fgets($file)));

	$course_offering[0] = $raw[0].$raw[1]; //course code
	$course_offering[1] = $year;
	$course_offering[2] = $raw[2];
	$course_offering[3] = $term;
	$course_offering[4] = $raw[5];
	$course_offering[5] = 0;
	$course_offering[6] = $raw[8];
	$course_offering[7] = '';	//no data for room number provided
	$course_offering[8] = $raw[4];
	
	$course = array(0 => $course_offering[0],
					1 => $raw[3],
					2 => $raw[3],
					3 => substr($raw[0],4,1)
					);	
					
	db_exec($insert_course, $course);
	db_exec($insert_offering, $course_offering);
}

