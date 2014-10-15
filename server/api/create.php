<?php
	require_once('lib/db.php');
	
	switch ($_SERVER['REQUEST_METHOD']) {
	case 'POST':
		switch ($db->getAttribute(PDO::ATTR_DRIVER_NAME)){
		case 'sqlite':
			$autoincrement = 'INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL';
			break;
		case 'mysql':
			$autoincrement = 'INTEGER PRIMARY KEY AUTO_INCREMENT';
			break;
		case 'pgsql':
			$autoincrement = 'SERIAL PRIMARY KEY NOT NULL';
			break;
		default:
			$autoincrement = 'NEED AUTOINCREMT SYNTAX';
		}
		
		$db->beginTransaction();
		
		if (isset($_GET['force'])) {
			$db->exec("
				DROP TABLE IF EXISTS program_elements;
				DROP TABLE IF EXISTS programs;
				DROP TABLE IF EXISTS course_offerings;
				DROP TABLE IF EXISTS prerequisites;
				DROP TABLE IF EXISTS coursegroup_courses;
				DROP TABLE IF EXISTS coursegroups;
				DROP TABLE IF EXISTS courses;
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
				level INTEGER NOT NULL
			);
			
			CREATE TABLE IF NOT EXISTS coursegroups (
				id $autoincrement,
				name VARCHAR(255)
			);
			
			INSERT INTO coursegroups(name) VALUES ('Empty');
			
			CREATE TABLE IF NOT EXISTS coursegroup_courses (
				id INTEGER
					REFERENCES coursegroups(id)
						ON DELETE CASCADE
					NOT NULL
				,
				course_code VARCHAR(15) NOT NULL,
				
				-- If the prerequsite can be taken concurrently.
				concurrent BOOLEAN NOT NULL DEFAULT TRUE,
				
				PRIMARY KEY (id, course_code)
			);
			
			CREATE TABLE IF NOT EXISTS prerequisites (
				course_code VARCHAR(15)
					REFERENCES courses(code)
						ON DELETE CASCADE
					NOT NULL
				,
				eligible INTEGER
					REFERENCES coursegroups(id)
						ON DELETE CASCADE
					NOT NULL
				,
				excluded INTEGER
					REFERENCES coursegroups(id)
						ON DELETE CASCADE
					NOT NULL DEFAULT 1 -- The empty group.
				,
				credits INTEGER NOT NULL,
				PRIMARY KEY (course_code, eligible, excluded)
			);
			
			
			CREATE TABLE IF NOT EXISTS course_offerings (
				-- The CRN.
				id $autoincrement NOT NULL,
				course_code VARCHAR(15)
					NOT NULL
					REFERENCES courses(code)
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
				term INTEGER NOT NULL,
				
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
				type INTEGER NOT NULL
			);
			
			
			CREATE TABLE IF NOT EXISTS programs (
				id $autoincrement NOT NULL,
				
				-- The year the program starts.
				year INTEGER NOT NULL,
				
				-- Human readable name.
				name VARCHAR(127) NOT NULL
			);
			
			CREATE TABLE IF NOT EXISTS program_elements (
				program_id INTEGER
					NOT NULL
					REFERENCES programs(id)
						ON DELETE CASCADE
				,
				course_code VARCHAR(15)
					NOT NULL
					REFERENCES courses(code)
						ON DELETE CASCADE
				,
				
				-- The credit type.
				-- - 0: Regular.
				-- - 1: Complementary Studies.
				-- - 2: Related Elective.
				credit_type INTEGER NOT NULL,
				
				term INTEGER NOT NULL,
				
				-- The year this course should be taken.
				element_year INTEGER NOT NULL
			);
		");
		
		$db->commit();
		
		header('Content-Type: text/xml; charset=utf-8');
		echo '<response e="0"/>';
		break;
	default:
		http_response_code(405);
		header('Content-Type: text/xml; charset=utf-8');
		echo '<response e="1" msg="Method must be POST."/>';
	}
