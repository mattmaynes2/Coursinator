<?php
	require_once('db.php');
	
	class Program {
		static function fetch($code){
			$q = new Query('Program');
			$q->select('Program')->where_eq(Program::id, $code);
			return $q->executeFetchScalar();
		}
		
		const sql_table = 'programs';
		const id   = 'programs.id';
		const year = 'programs.year';
		const name = 'programs.name';
		
		const sql_fields = [
			self::id,
			self::year,
			self::name,
		];
		
		static function sql_from($row) {
			$r = new self();
			$r->sql_load($row);
			return $r;
		}
		function sql_load($row) {
			list(
				$this->id,
				$this->year,
				$this->name
			) = $row;
			return $this;
		}
		
		private $id = NULL;
		private $year = NULL;
		private $name = NULL;
		
		function __construct(){
		}
		
		function save(){
			static $s = NULL;
			if (!$s) {
				global $db;
				$s = $db->prepare('
					INSERT INTO programs(id, year, name)
					VALUES (:id, :year, :name);
				');
			}
			
			$s->execute([
				'id'   => $this->id,
				'year' => $this->year,
				'name' => $this->name,
			]);
			$s->closeCursor();
			return $s;
		}
		
		function to_xml(){
			$r = "<program>";
			
			$r += "</program>";
			return $r;
		}
	}
