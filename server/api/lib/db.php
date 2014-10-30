<?php
	$dsn = getenv('COURSINATOR_DB');
	$user = getenv('COURSINATOR_USER');
	$pass = getenv('COURSINATOR_PASS');
	if ($user)
		$db = new PDO($dsn, $user, $pass);
	else
		$db = new PDO($dsn);
	
	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	switch ($db->getAttribute(PDO::ATTR_DRIVER_NAME)) {
	case 'sqlite':
		$db->exec('PRAGMA foreign_keys=ON');
		break;
	}
	
	function db_exec($sql, $values) {
		global $db;
		
		$r = $db->prepare($sql);
		$r->execute($values);
		
		return $r;
	}
	
	/** A SQL query wrapper.
	 *
	 * This allows a user to query the database without knowing implementation
	 * details such as table and column names, storage types and query info.
	 *
	 * A user can use this to build up a query bit by bit without putting user
	 * values into the query.
	 *
	 * Note: While values are inserted using prepared statements the column
	 * names are assumend to be clean and put directally into the statement.
	 * Never use a dynamic value as a column name or you will be vulnrable to
	 * SQL injection.
	 *
	 * Example usage:
	 *     $q = new Query('Song');
	 *     $q->select_object('Song');
	 *     $q->filter(Song::title . ' like ?', ['Piano M']);
	 *     $q->execute();
	 *     while ($row = $q->fetch())
	 *         $row[0]; // A Song object.
	 *     $q->closeCursor();
	 *
	 * In order for a class to work with this query mechinism it must provide
	 * the following.
	 *
	 * const sql_table = 'tablename';
	 * const sql_fields = ['tablename.columname', ...];
	 * static function sql_from($row) {
	 *     // $row is an array of values corrisponding to sql_fields.
	 *     // Must return an object corrisponding to the values.
	 * }
	 *
	 * It is also recomended to provide constants corrisponding to the name of
	 * fields so that users can use them in select() and where() clauses.
	 *
	 */
	class Query {
		/** Return a list of placeholders.
		 *
		 * Example: Query::valuelistsql([1,2,3]) == '(?,?,?)'
		 */
		static function valuelistsql($values) {
			return '('.implode(',', array_fill(0, count($values), '?')).')';
		}
		
		private $select = '';
		private $from = NULL;
		private $where = '';
		private $join = '';
		private $classes = NULL;
		private $values = NULL;
		private $order = NULL;
		
		private $result = NULL;
		
		/** Construct a Query.
		 *
		 * \param $from The class to select from.
		 */
		function __construct($from){
			if (class_exists($from))
				$from = $from::sql_table;
			
			$this->from    = ' FROM '.$from;
			$this->classes = [];
			$this->values  = [];
		}
		
		/** Select and convert to object.
		 *
		 * This determines what to return from the query.
		 *
		 * \param $class The name of the class.
		 *
		 * Example:
		 *     $q->select_object('Song'); // Each row will contain a Song object.
		 *     $q->select_object('Artist'); // And an Artist.
		 */
		function select_object($class){
			if (!$this->select) $this->select  = 'SELECT ';
			else                $this->select .= ', ';
			
			$this->select .= join(',', $class::sql_fields);
			$this->classes[] = $class;
			return $this;
		}
		
		/** Select from the database.
		 *
		 * Example:
		 *     $q->select(Song::title); // Fetch just the string title.
		 */
		function select($field){
			if (!$this->select) $this->select  = 'SELECT ';
			else                $this->select .= ', ';
			
			$this->select .= $field;
			$this->classes[] = NULL;
		}
		
		/** Join another table.
		 */
		function join($what, $on){
			if (class_exists($what))
				$what = $what::sql_table;
			
			$this->join .= "\nJOIN $what ON $on";
			
			return $this;
		}
		
		/** Filter the results.
		 *
		 * \param $expr The filter expression.
		 * \param $vals The values corrisponding to '?' in the expression.
		 */
		function where($expr, $vals = []){
			if (!$this->where) $this->where  = "\nWHERE ";
			else               $this->where .= ' AND ';
			
			$this->where  .= $expr;
			$this->values = array_merge($this->values, $vals);
			
			return $this;
		}
		
		/** Filter where the given column equals the given value.
		 */
		function where_eq($col, $val){
			return $this->where("$col = ?", [$val]);
		}
		
		/** Filter where the given column starts with the given string.
		 */
		function where_startswith($col, $str){
			return $this->where("? <= $col AND $col < ?", [$str, ++$str]);
		}
		
		/** Filter where the given column value is in provided array.
		 */
		function where_in($col, $arr, $in = TRUE) {
			if (!$arr) {
				if ($in) // Never true.
					$this->where('1=0');
				
				return $this; // Can't do an empty array.
			}
			
			$sql = $col.' IN '.self::valuelistsql($arr);
			
			if (!$in)
				$sql = "NOT $sql";
			
			return $this->where($sql, $arr);
		}
		
		/** Filter where the given query would return rows.
		 */
		function where_exists($query, $existsp = TRUE) {
			$sql = 'EXISTS ('.$query->sql().')';
			
			if (!$existsp)
				$sql = "NOT $sql";
			
			return $this->where($sql, $query->values());
		}
		
		/** Get the SQL representation of the query.
		 *
		 * The string may be multiple lines and ends with a newline.
		 */
		function sql(){
			return $this->select.$this->from.$this->join.$this->where."\n";
		}
		
		/** Get the values to interpolate into the expression.
		 */
		function values(){
			return $this->values;
		}
		
		/** Execute the query.
		 */
		function execute() {
			$this->result = db_exec($this->sql(), $this->values);
			return $this;
		}
		
		/** Execute and return all rows.
		 */
		function executeFetchAll(){
			$this->execute();
			$r = $this->fetchAll();
			$this->closeCursor();
			return $r;
		}
		
		/** Execute and fetch one row.
		 *
		 * Fetches the row and closes the cursor.
		 */
		function executeFetchOne(){
			$this->execute();
			$r = $this->fetch();
			$this->closeCursor();
			return $r;
		}
		
		/** Execute and fetch one element.
		 *
		 * Fetches the first element from the first row and closes the cursor.
		 */
		function executeFetchScalar(){
			$r = $this->executeFetchOne();
			if ($r === FALSE) return FALSE;
			return $r[0];
		}
		
		/** Close the cursor.
		 *
		 * \see PDOStatement->closeCursor();
		 */
		function closeCursor(){
			$this->result->closeCursor();
		}
		
		/** Return a row.
		 *
		 * Returns a set of objects requested by select() calls.  They are
		 * returned in an array in the order of the select() calls.
		 *
		 * \see PDOStatement->fetch();
		 */
		function fetch(){
			$row = $this->result->fetch(PDO::FETCH_NUM);
			if ($row === FALSE) return FALSE;
			
			return $this->transform_row($row);
		}
		
		/** Return a scalar.
		 *
		 * Returns the first element of the row.
		 */
		function fetchScalar(){
			$r = $this->fetch();
			if ($r === FALSE) return FALSE;
			return $r[0];
		}
		
		/** Return all rows.
		 *
		 * Return all results of the query.
		 */
		function fetchAll(){
			$f = $this->result->fetchAll(PDO::FETCH_NUM);
			if ($f === FALSE) return FALSE;
			
			$r = [];
			foreach ($f as $row) {
				$r[] = $this->transform_row($row);
			}
			return $r;
		}
		
		/** Return all scalars.
		 *
		 * Returns the first element of every row.
		 */
		function fetchAllScalar(){
			$r = [];
			$res = $this->fetchAll();
			if ($res === FALSE) return FALSE;
			foreach ($res as $row)
				$r[] = $row[0];
			return $r;
		}
		
		/** Convert row to objects.
		 *
		 * Converts the database row of the raw values to the PHP objects
		 * requested by the querier.
		 */
		private function transform_row($row){
			$r = [];
			$off = 0;
			foreach($this->classes as $i => $c){
				if (!$c) {
					$r[] = $row[$off++];
				} else {
					$num = count($c::sql_fields);
					$r[] = $c::sql_from(array_slice($row, $off, $num));
					$off += $num;
				}
			}
			return $r;
		}
		
		function __debuginfo() {
			return [
				'sql'    => $this->sql(),
				'values' => $this->values(),
			];
		}
	}
