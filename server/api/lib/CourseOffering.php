<?php
	require_once('db.php');
	
	class CourseOffering {
		static function fetch($id){
			$q = new Query('CourseOffering');
			$q->select_object('CourseOffering')->where_eq(CourseOffering::id, $id);
			return $q->executeFetchScalar();
		}
		
		const TERM_FALL   = 0;
		const TERM_WINTER = 1;
		const TERM_SUMMER = 2;
		
		const TYPE_LEC = 0;
		const TYPE_LAB = 1;
		const TYPE_TUT = 2;
		
		const sql_table   = 'course_offerings';
		const id          = 'course_offerings.id';
		const course_code = 'course_offerings.course_code';
		const year        = 'course_offerings.year';
		const section     = 'course_offerings.section';
		const term        = 'course_offerings.term';
		const days        = 'course_offerings.days';
		const enrolled    = 'course_offerings.enrolled';
		const capacity    = 'course_offerings.capacity';
		const room        = 'course_offerings.room';
		const type        = 'course_offerings.type';
		
		public static $sql_fields = [
			self::id,
			self::course_code,
			self::year,
			self::section,
			self::term,
			self::days,
			self::enrolled,
			self::capacity,
			self::room,
			self::type,
		];
		
		private $id          = NULL;
		private $course_code = NULL;
		private $year        = NULL;
		private $section     = NULL;
		private $term        = NULL;
		private $days        = NULL;
		private $enrolled    = NULL;
		private $capacity    = NULL;
		private $room        = NULL;
		private $type        = NULL;
		
		static function sql_from($row){
			$r = new self();
			$r->sql_load($row);
			return $r;
		}
		function sql_load($row) {
			list(
				$this->id,
				$this->course_code,
				$this->year,
				$this->section,
				$this->term,
				$this->days,
				$this->enrolled,
				$this->capacity,
				$this->room,
				$this->type,
			) = $row;
			return $this;
		}
		
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
			return $this->year = +$year;
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
			return $this->term = +$term;
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
			return $this->enrolled = +$enr;
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
			return $this->capacity = +$cap;
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
			$r = '<course-offering>';
			$r .= '<id>'.htmlspecialchars($this->id).'</id>';
			$r .= '<code>'.htmlspecialchars($this->course_code).'</code>';
			$r .= '<year>'.htmlspecialchars($this->year).'</year>';
			$r .= '<section>'.htmlspecialchars($this->section).'</section>';
			$r .= '<term>'.htmlspecialchars($this->term).'</term>';
			$r .= '<days>'.htmlspecialchars($this->days).'</days>';
			$r .= '<enrolled>'.htmlspecialchars($this->enrolled).'</enrolled>';
			$r .= '<capacity>'.htmlspecialchars($this->capacity).'</capacity>';
			$r .= '<room>'.htmlspecialchars($this->room).'</room>';
			$r .= '<type>'.htmlspecialchars($this->type).'</type>';
			$r .= '</course-offering>';
			return $r;
		}
	}
