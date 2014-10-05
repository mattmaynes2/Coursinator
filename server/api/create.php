<?php
	require_once('lib/db.php');
	
	switch ($_SERVER['REQUEST_METHOD']) {
	case 'POST':
		$type = $db->getAttribute(PDO::ATTR_DRIVER_NAME);
		
		switch ($type){
		case 'sqlite':
			$autoincrement = 'AUTOINCREMENT';
			break;
		case 'mysql':
			$autoincrement = 'AUTO_INCREMENT';
			break;
		default:
			$autoincrement = '';
		}
		
		
		$db->beginTransaction();
		
		if (isset($_GET['force'])) {
			$db->exec("
				DROP TABLE courses;
				DROP TABLE prerequisites;
				DROP TABLE course_offerings;
				DROP TABLE programs;
				DROP TABLE program_elements;
			");
		}
		
		$db->exec("
			CREATE TABLE IF NOT EXISTS courses (
				-- The code is the four digit number that is given for each course.
				-- The first number represents the year that the course should be take.
				-- (ex. MATH 3004, the code is 3004).
				code VARCHAR(15) PRIMARY KEY NOT NULL,
				title VARCHAR(63) NOT NULL,
				description VARCHAR(255) NOT NULL,
				
				-- The level of the course.
				-- 0: Undergraduate.
				-- 1: Graduate.
				level TINYINT NOT NULL
			);
			
			CREATE TABLE IF NOT EXISTS prerequisites (
				id INTEGER PRIMARY KEY $autoincrement NOT NULL,
				
				course_code VARCHAR(15)
					NOT NULL
					REFERENCES course(code)
						ON DELETE CASCADE
				,
				allow_concurrent BOOLEAN NOT NULL,
				required_credits FLOAT NULL,
				course_group VARCHAR(255) NULL
			);
			
			
			CREATE TABLE IF NOT EXISTS course_offerings (
				-- The CRN.
				id INTEGER PRIMARY KEY $autoincrement NOT NULL,
				course_code VARCHAR(15)
					NOT NULL
					REFERENCES course(code)
						ON DELETE CASCADE
				,
				year INTEGER NOT NULL,
				
				-- Course section (A, B1, L3)
				section VARCHAR(3) NOT NULL,
				
				-- Term the course is offered in.
				--
				-- 0: Fall
				-- 1: Winter
				-- 2: Summer
				term TINYINT NOT NULL,
				
				-- A set of days that this course is offered.
				--
				-- (M)onday, (T)uesday, (W)ednesday, thu(R)sday, (F)riday.
				days VARCHAR(5) NOT NULL,
				
				enrolled INTEGER NOT NULL DEFAULT 0,
				capacity INTEGER NOT NULL,
				room VARCHAR(63) NOT NULL,
				
				-- Class type.
				--
				-- 0: Lecture.
				-- 1: Lab.
				-- 2: Tutorial.
				type TINYINT NOT NULL
			);
			
			
			CREATE TABLE IF NOT EXISTS programs (
				id INTEGER PRIMARY KEY $autoincrement NOT NULL,
				
				-- The year the program starts.
				year INTEGER NOT NULL,
				
				-- Human readable name.
				name VARCHAR(127) NOT NULL
			);
			
			CREATE TABLE IF NOT EXISTS program_elements (
				program_id INTEGER
					NOT NULL
					REFERENCES program(id)
						ON DELETE CASCADE
				,
				course_code VARCHAR(15)
					NOT NULL
					REFERENCES course(code)
						ON DELETE CASCADE
				,
				
				-- The credit type.
				-- - 0: Regular.
				-- - 1: Complementary Studies.
				-- - 2: Related Elective.
				credit_type TINYINT NOT NULL,
				
				term INTEGER NOT NULL,
				
				-- The year this course should be taken.
				element_year INTEGER NOT NULL
			);
		");
		
		$db->commit();
		
		header("Content-Type", "application/javascript; charset=utf-8");
		echo '{"e":0}';
		break;
	default:
		header("Content-Type", "application/javascript; charset=utf-8");
		http_response_code(405);
		echo '{"e":1,"msg":"Method must be POST."}';
	}
