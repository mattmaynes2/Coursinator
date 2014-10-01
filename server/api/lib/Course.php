<?php
	require_once('db.php');
	
	class Course {
		const LEVEL_UNDERGRAD = 0;
		const LEVEL_GRAD      = 1;
		
		/** Normalize a course code.
		 *
		 * This function will return a course code in the form ABCD1234 or
		 * FALSE if the code was invalid.
		 */
		static function code_normalize($code){
			$code = strtoupper($code);
			$code = preg_replace('/[^A-Z0-9]/', '', $code);
			
			if (preg_match('/^[A-Z]{1,8}[0-9]{4}$/', $code))
				return $code;
			else
				return NULL;
		}
		
		/** Validate a course code.
		 *
		 * Like `code_normalize()` but it throws on an invalid code.
		 */
		static function code_validate($code){
			$r = self::code_normalize($code);
			if (!$r)
				throw new Exception("Invalid code '$code'");
			return $r;
		}
		
		private $code = NULL;
		private $title = NULL;
		private $desc = "";
		private $level = NULL;
		
		function __construct($code){
			$this->setcode($code);
		}
		
		function setcode($code){
			$this->code = self::code_validate($code);
		}
		function getcode(){
			return $this->code;
		}
		
		function settitle($title){
			$this->title = $title;
		}
		function gettitle($title){
			return $this->tilte;
		}
		
		function setlevel($level){
			switch ($level) {
			case self::LEVEL_UNDERGRAD:
			case self::LEVEL_GRAD:
				break; // Do nothing, already good.
			
			case "undergrad":
				$level = self::LEVEL_UNDERGRAD;
				break;
			
			case "grad":
				$level = self::LEVEL_GRAD;
				break;
			
			default:
				throw new Exception("Bad level '$level'");
			}
			
			$this->level = $level;
		}
		function getlevel(){
			return $this->level;
		}
		
		function save(){
			static $s = NULL;
			if (!$s) {
				global $db;
				$s = $db->prepare('
					INSERT INTO courses(code, title, level, description)
					VALUES (:code, :title, :level, :desc);
				');
			}
			
			$s->execute([
				'code'  => $this->code,
				'title' => $this->title,
				'level' => $this->level,
				'desc'  => $this->desc,
			]);
			$s->closeCursor();
			return $s;
		}
	}
