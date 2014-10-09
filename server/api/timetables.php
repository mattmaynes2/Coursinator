<?php
	if ($_SERVER['REQUEST_METHOD'] != 'POST'){
		exit;
	}
	
	switch ($_SERVER['REQUEST_METHOD']) {
	case "POST":
		header('Content-Type: text/xml; charset=utf-8');
		$r = '<response e="0"><timetables>';
		$r .= '</timetables></response>';
		return $r;
		break;
	default:
		http_response_code(405);
		header('Content-Type: text/xml; charset=utf-8');
		echo '<response e="1" msg="Method must be POST."/>';
	}
