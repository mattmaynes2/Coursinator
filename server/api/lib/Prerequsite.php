<?php
	require_once('db.php');
	
	class Prerequsite {
		const sql_table  = 'prerequisites';
		const type       = 'prerequisites.type';
		const course     = 'prerequisites.course_code';
		const exclude    = 'prerequisites.exclude';
		const concurrent = 'prerequisites.concurrent';
		
		const sql_fields = [
			self::type,
			self::course_code,
			self::course,
			self::exclude,
			self::concurrent,
		];
		
		private $type        = NULL;
		private $course_code = NULL;
		private $course      = NULL;
		private $exclude     = NULL;
		private $concurrent  = NULL;
		
		function __construct(){
		}
		
		function getcourse(){
			return new Course($this->course_code);
		}
		function setcourse($course){
			return $this->course_code = $course->getcode();
		}
		
		function getyear(){
			return $this->year;
		}
		function setyear($year){
			return $this->year = $year;
		}
		
		function getsection(){
			return $this->section;
		}
		function setsection($sect){
			return $this->section = $sect;
		}
		
		function getterm(){
			return $this->term;
		}
		function setterm($term){
			return $this->term = $term;
		}
		
		function getdays(){
			return $this->days;
		}
		function setdays($days){
			return $this->days = $days;
		}
		
		function getenrolled(){
			return $this->enrolled;
		}
		function setenrolled($enr){
			return $this->enrolled = $enr;
		}
		function enroll(){
			return ++$this->enrolled;
		}
		function withdraw(){
			return --$this->enrolled;
		}
		
		function getcapacity(){
			return $this->capacity;
		}
		function setcapacity($cap){
			return $this->capacity = $cap;
		}
		
		function getroom(){
			return $this->room;
		}
		function setroom($room){
			return $this->room = $room;
		}
		
		function gettype(){
			return $this->type;
		}
		function settype($type){
			switch ($type){
			case self::TYPE_LAB:
			case "LAB":
				$type = self::TYPE_LAB;
				break;
			case self::TYPE_LEC:
			case "LEC":
				$type = self::TYPE_LEC;
				break;
			case self::TYPE_TUT:
			case "TUT":
				$type = self::TYPE_TUT;
				break;
			default:
				throw new Exception("Unknown type '$type'.");
			}
			return $this->type = $type;
		}
		
		function save(){
			static $s = NULL;
			if (!$s) {
				global $db;
				$s = $db->prepare('
					INSERT INTO course_offerings (
						course_code,
						year,
						section,
						term,
						days,
						enrolled,
						capacity,
						room,
						type
					)
					VALUES (
						:course_code,
						:year,
						:section,
						:term,
						:days,
						:enrolled,
						:capacity,
						:room,
						:type
					);
				');
			}
			
			$s->execute([
				"course_code" => $this->course_code,
				"year"        => $this->year,
				"section"     => $this->section,
				"term"        => $this->term,
				"days"        => $this->days,
				"enrolled"    => $this->enrolled,
				"capacity"    => $this->capacity,
				"room"        => $this->room,
				"type"        => $this->type,
			]);
			$s->closeCursor();
			return $s;
		}
		
		function to_xml(){
			$r = '<course-offering';
			$r .= ' id="'.htmlspecialchars($this->id).'"';
			$r .= ' course="'.htmlspecialchars($this->course_code).'"';
			$r .= ' year="'.htmlspecialchars($this->year).'"';
			$r .= ' section="'.htmlspecialchars($this->section).'"';
			$r .= ' term="'.htmlspecialchars($this->term).'"';
			$r .= ' days="'.htmlspecialchars($this->days).'"';
			$r .= ' enrolled="'.htmlspecialchars($this->enrolled).'"';
			$r .= ' capacity="'.htmlspecialchars($this->capacity).'"';
			$r .= ' room="'.htmlspecialchars($this->room).'"';
			$r .= ' type="'.htmlspecialchars($this->type).'"';
			$r .= '/>';
			return $r;
		}
	}
