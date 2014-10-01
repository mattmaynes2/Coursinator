<?php
	$dsn = getenv('COURSINATOR_DB');
	$user = getenv('COURSINATOR_USER');
	$pass = getenv('COURSINATOR_PASS');
	$db = new PDO($dsn, $user, $pass);
	
	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_WARNING);
