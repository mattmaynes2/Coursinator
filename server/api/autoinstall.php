<?php
	require_once('lib/db.php');
	
	$root = realpath(dirname(__FILE__).'/../../database').'/';
	
	function post($url, $file = FALSE, $mime = 'application/xml') {
		global $root;
		
		$c = curl_init("$_SERVER[HTTP_HOST]$_SERVER[PHP_SELF]/../..$url");
		
		curl_setopt($c, CURLOPT_RETURNTRANSFER, TRUE);
		curl_setopt($c, CURLOPT_PUT, TRUE);
		curl_setopt($c, CURLOPT_CUSTOMREQUEST, 'POST');
		curl_setopt($c, CURLOPT_HTTPHEADER, ["Content-Type: $mime"]);
		
		if ($file) {
			$file = fopen($root.$file, 'r');
			curl_setopt($c, CURLOPT_INFILE, $file);
		}
		
		$r = curl_exec($c);
		
		if ($file) fclose($file);
		
		return $r;
	}
	
	switch ($_SERVER['REQUEST_METHOD']) {
	case 'POST':
		post('/api/create.php?force');
		post('/api/courseofferings.php', 'course_data.csv');
		post('/api/courseofferings.php', 'datawinter.csv');
		
		post('/api/prerequsites.php', 'prerequsites.xml');
		post('/api/prerequsites.php', 'math_prereqs.xml');
		post('/api/prerequsites.php', 'sysc_preres.xml');
		
		post('/api/addPattern.php', 'software_pattern_fall2012.csv');
		
		header('Content-Type: text/xml; charset=utf-8');
		echo '<response e="0"/>';
		break;
	default:
		http_response_code(405);
		header('Content-Type: text/xml; charset=utf-8');
		echo '<response e="1" msg="Method must be POST."/>';
	}
