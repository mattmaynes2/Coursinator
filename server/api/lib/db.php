<?php
	$dsn = getenv('COURSINATOR_DB');
	$user = getenv('COURSINATOR_USER');
	$pass = getenv('COURSINATOR_PASS');
	$db = new PDO($dsn, $user, $pass);
	
	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_WARNING);
	
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
	 *     $q->select('Song');
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
	 * fields so that users can use them in select_field() and where() clauses.
	 *
	 */
	class Query {
		private $select = '';
		private $from = NULL;
		private $where = '';
		private $join = '';
		private $classes = [];
		private $values = [];
		private $order = NULL;
		
		private $result = NULL;
		
		/** Construct a Query.
		 *
		 * \param $from The class to select from.
		 */
		function __construct($from){
			$this->from = ' FROM '.$from::sql_table;
		}
		
		/** Select values.
		 *
		 * This determines what to return from the query.
		 *
		 * \param $class The name of the class.
		 *
		 * Example:
		 *     $q->select('Song'); // Each row will contain a Song object.
		 *     $q->select('Artist'); // And an Artist.
		 */
		function select($class){
			if (!$this->select) $this->select  = 'SELECT ';
			else                $this->select .= ', ';
			
			$this->select .= join(',', $class::sql_fields);
			$this->classes[] = $class;
			return $this;
		}
		
		/** Select individual fields.
		 *
		 * Like select() but returns the value of the field rather then an
		 * object.
		 *
		 * Example:
		 *     $q->select_field(Song::title); // Fetch just the string title.
		 */
		function select_field($field){
			if (!$this->select) $this->select  = 'SELECT ';
			else                $this->select .= ', ';
			
			$this->select .= $field;
			$this->classes[] = NULL;
		}
		
		/** TODO Join another table.
		 */
		function join($class, $on){
			
		}
		
		/** Filter the results.
		 *
		 * \param $expr The filter expression.
		 * \param $vals The values corrisponding to '?' in the expression.
		 */
		function where($expr, $vals){
			if (!$this->where) $this->where  = "\nWHERE ";
			else               $this->where .= ' AND ';
			
			$this->where  .= $expr;
			$this->values += $vals;
			
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
		
		/** Get the SQL representation of the query.
		 *
		 * The string may be multiple lines and ends with a newline.
		 */
		function sql(){
			return $this->select.$this->from.$this->join.$this->where.";\n";
		}
		
		/** Get the values to interpolate into the expression.
		 */
		function values(){
			return $this->values;
		}
		
		/** Execute the query.
		 */
		function execute(){
			global $db;
			
			$this->result = $db->prepare($this->sql());
			$this->result->execute($this->values);
			
			return $this;
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
			if (!$r) return FALSE;
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
			if (!$row) return FALSE;
			
			return $this->transform_row($row);
		}
		
		/** Return a scalar.
		 *
		 * Returns the first element of the row.
		 */
		function fetchScalar(){
			$r = $this->fetch();
			if (!$r) return FALSE;
			return $r[0];
		}
		
		/** Return all rows.
		 *
		 * Return all results of the query.
		 */
		function fetchAll(){
			$f = $this->result->fetchAll(PDO::FETCH_NUM);
			if (!$f) return FALSE;
			
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
			if (!$res) return FALSE;
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
	}
