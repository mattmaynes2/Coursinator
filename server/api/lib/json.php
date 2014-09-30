<?php
	function readjson($method='POST'){
		if ($_SERVER['CONTENT_TYPE'] != 'application/json'){
			http_response_code(400);
			echo '{"e":1,"msg":"Content-Type must be application/json"}';
			exit;
		}
		$json = file_get_contents('php://input');
		return json_decode($json);
	}
