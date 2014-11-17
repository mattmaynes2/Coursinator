<?php
	error_reporting(E_ALL);
	require_once('lib/db.php');
	require_once('lib/Course.php');
	require_once('lib/CourseOffering.php');
	
	switch ($_SERVER['REQUEST_METHOD']) {
	case 'GET':
		/** Query Courses
		 *
		 * Parameters:
		 * - code: Filter courses by the given code or prefix.
		 *     Examples:
		 *         /api/courses.php?code=ECOR101
		 *         /api/courses.php?code=SYSC
		 * - qualified: Only return courses that can be taken given the
		 *              courses given in the `completed` and `taking`
		 *              parameters.
		 *     Examples:
		 *         /api/courses.php?qualified&completed[]=SYSC1005
		 *         /api/courses.php?qualified&completed[]=SYSC1005&completed[]=ECOR1010&taking[]=SYSC2006
		 * - completed: An array of completed courses. Ignored if
		 *              `qualified` not set
		 * - taking: An array of courses that are being taken in the same
		 *           term. Ignored if `qualified` not set.
		 *
		 * Returns:
		 *     <response e="0">
		 *       <courses>
		 *         <!-- One course element per course in the result. -->
		 *         <course>...</course>
		 *       </courses>
		 *     </response>
		 */
		
		$q = new Query('Course');
		$q->select_object('Course');
		
		// Filter based on course code (or prefix thereof)
		if (isset($_GET['code']))
			$q->where_startswith(Course::code, strtoupper($_GET['code']));
		
		// Filter where dependancies are satisfied.
		if (isset($_GET['qualified'])) {
			$completed = isset($_GET['completed']) ? $_GET['completed'] : [];
			$taking    = isset($_GET['taking'])    ? $_GET['taking']    : [];
			foreach ($completed as &$c)
				$c = strtoupper($c);
			foreach ($taking as &$c)
				$c = strtoupper($c);
			
			$preqquery = Course::query_prerequisites(Course::code,
			                                         $completed,
			                                         $taking);
			$q->where_exists($preqquery, false);
		}
		
		$q->execute();
		
		header('Content-Type: text/xml; charset=utf-8');
		echo '<response e="0"><courses>';
		foreach ($q->fetchAllScalar() as $course) {
			echo $course->to_xml();
		}
		echo '</courses></response>';
		break;
	default:
		header('Content-Type: text/xml; charset=utf-8');
		http_response_code(405);
		echo '<response e="1" msg="Method must be GET."/>';
	}
