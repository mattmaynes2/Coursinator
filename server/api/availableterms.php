<?php
	require_once("lib/db.php");

	$q = new Query("course_offerings");	
			
	$q->select("DISTINCT term");
	$q->select("year");
	$rows = $q->executeFetchAll();
	
	$response = '<terms>';
	foreach ($rows as $offering)
	{
		$year=$offering[1];
		$term = $offering[0] == 0 ? "Fall" : "Winter";
		$response .= "<term>$term $year</term>";
	}
	$response .= '<terms>';
	
	header("Content-Type", "application/xml; charset=utf-8");
	echo $response;