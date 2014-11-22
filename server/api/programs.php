<?php
	/**
	 * /api/programs.php
 	 *
 	 * @method GET
 	 *
 	 * Returns the programs that are in the database. If the program name is specified
	 * then only programs with names like it are returned.
	 *
	 * @param {optional} program - The name of the program to return
 	 *
 	 * @return An XML response containing programs that matched the search
	 *
	 * @example programs.php - This would return all of the programs
	 * @example programs.php?program=Software - This would return all programs with a name like software
	 */

	if($_SERVER['REQUEST_METHOD'] != 'GET'){
		http_response_code(405);
		echo '{"e":1, "msg":"Method must be GET."}';
		exit;
	}

	require_once('lib/db.php');
	$has_param = in_array('program', $_GET);

	$q = new Query("programs");
	$q->select("id");
	$q->select("year");
	$q->select("name");
	
	if($has_param){
		$q->where('name LIKE ?', [$_GET['program']]);
	}

	$rows = $q->executeFetchAll();

	$response = '<response e="0">';
	foreach($rows as $program){
		$response .= '<program>';
		$response .= "<id>$program[0]</id>";
		$response .= "<year>$program[1]</year>";
		$response .= "<name>$program[2]</name>";
		$response .='</program>';
	}
	$response .= '</response>';
	
	header("Content-Type", "application/xml; charset=utf-8");
	echo $response;

?>
