<?php
	require_once('lib/db.php');
	require_once('lib/Course.php');
	require_once('lib/CourseOffering.php');
	
	switch ($_SERVER['REQUEST_METHOD']) {
	case 'GET':
		$q = new Query('CourseOffering');
		$q->select_object('CourseOffering');
		$q->where_startswith(CourseOffering::course_code, 'WGST');
		$q->execute();
		
		$r = '<response e="0"><courses>';
		foreach ($q->fetchAllScalar() as $co) {
			$r .= $co->to_xml();
		}
		$r .= '</courses></response>';
		
		header('Content-Type: text/xml; charset=utf-8');
		echo $r;
		break;
	case 'POST':
		/*
		 * Wrapper for fgetcsv() so we have a single place
		 * to pass format arguments.
		 */
		function gcsv($fd){
			return fgetcsv($fd, 0, ';');
		}

		$db->beginTransaction();
		
		$added = 0;
		$coursecache = [];
		function fetchcourse($code){
			$code = Course::code_normalize($code);
			
			if (isset($coursecache[$code])) return $coursecache[$code];
			return $coursecache[$code] = Course::fetch($code);
		}
		
		$fd = fopen("php://input", "r");
		$first = fgets($fd);
		$firstLine = explode(",",$first); // header line contains term and year (0=fall 1=winter)
		$term = $firstLine[0];
		$year = $firstLine[1];
		
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
			$code = $dept . str_pad($num, 4, '0', STR_PAD_LEFT);
			$c = fetchcourse($code);
			if (!$c) {
				$c = new Course($code);
				$c->settitle($title);
				$c->setlevel(Course::LEVEL_UNDERGRAD);
				$c->save();
				$coursecache[$c->getcode()] = $c;
				
				$added++;
			}
			
			$co = new CourseOffering();
			$co->setcourse($c);
			$co->setsection($section);
			$co->settype($type);
			$co->setyear($year);
			$co->setterm($term);
			$co->setdays($days);
			$co->setenrolled(0);
			$co->setcapacity($capacity);
			$co->setroom("TBD");
			$co->setstarttime($start);
			$co->setendtime($end);
			$co->save();
		}
		
		$db->commit();
		
		header('Content-Type: text/xml; charset=utf-8');
		echo '<response e="0"><added>'.$added.'</added></response>';
		break;
	default:
		header('Content-Type: text/xml; charset=utf-8');
		http_response_code(405);
		echo '<response e="1" msg="Method must be GET or POST"/>';
	}
