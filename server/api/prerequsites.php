<?php
	require_once('lib/xml.php');
	require_once('lib/db.php');
	require_once('lib/Course.php');
	
	function makeprereq($code, $xml) {
		global $db;
		
		static $scoursegroup  = NULL;
		static $scoursegroupperson = NULL;
		static $sprerequsite  = NULL;
		
		if (!$scoursegroup) {
			$scoursegroup  = $db->prepare(
				'INSERT INTO coursegroups(name) VALUES (?)'
			);
			$scoursegroupperson = $db->prepare('
				INSERT INTO coursegroup_courses(id, course_code, concurrent)
				VALUES (?,?,?)
			');
			$sprerequsite  = $db->prepare('
				INSERT INTO prerequisites(course_code, eligible, credits)
				VALUES (?,?,?)
			');
		}
		
		if ($xml->tagName == 'group') {
			$preq = getChildrenByTag($xml, 'prerequsite');
		} else if ($xml->tagName == 'prerequsite') {
			$preq = [$xml];
		} else return;
		
		$scoursegroup->execute([
			"Prerequsites for $code",
		]);
		$scoursegroup->closeCursor();
		
		$gid = +$db->lastInsertId('coursegroups_id_seq');
		
		foreach ($preq as $e) {
			try {
				$scoursegroupperson->execute([
					$gid,
					$e->getAttribute('code'),
					$e->hasAttribute('concurrent')? 1 : 0,
				]);
				$scoursegroupperson->closeCursor();
			} catch (Exception $e) {
				// Ingore, we don't know about this course to add prereqs.
			}
		}
		
		try {
			$sprerequsite->execute([
				$code,
				$gid,
				1,
			]);
			$sprerequsite->closeCursor();
		} catch (Exception $e) {
			// Ingore, we don't know about this course to add prereqs.
		}
	}
	
	switch ($_SERVER['REQUEST_METHOD']) {
	case 'POST':
		$doc = readxml();
		
		$root = $doc->documentElement;
		
		if ($root->tagName != 'prerequisites') {
			http_response_code(400);
			header('Content-Type: text/xml; charset=utf-8');
			echo '<response e="1" msg="Root element must be \'prerequisites\'"/>';
			return;
		}
		
		$db->beginTransaction();
		
		foreach (getChildrenByTag($root, 'course') as $course) {
			$code = $course->getAttribute('code');
			
			foreach (getChildrenByTag($course) as $preq) {
				makeprereq($code, $preq);
			}
		}
		
		$db->commit();
		
		$r = '<response e="0">';
		$r .= '</response>';
		
		header('Content-Type: text/xml; charset=utf-8');
		echo $r;
		break;
	default:
		header('Content-Type: text/xml; charset=utf-8');
		http_response_code(405);
		echo '<response e="1" msg="Method must be POST."/>';
	}
