<?php 
	/*
		RESPONSE FORMAT
		
		If there is an error, the response format will be:
		<response>
			<errormessage>The Message</errormessage>
		</response>
		
		If there is no submission error, but no conflict-free schedule could be generated, the response format is:
		<response>
			<noschedulemessage>The Message</noschedulemessage>
		</response>
		
		If the conflict-free schedule could be generated, the response format is:
		
		<reponse>
			<schedule>
				<slot index='courseTime'> <!-- A schedule contains a list of half-hour long time slots, if added to a table in html/gridlayout ordering (row by row) they form the correct schedule (doesn't look fancy though)
					<value day="dayofweek"></value> <!-- If there is no course in this time slot -->
					<value dat="dayofweek">CourseCode&SectionNumber</value>
				</slot>
				<sections><!-- This is a summary of the sections sceduled. You may or may not use this, depending on how you want to display the schedule
					<section>
						Contains a CourseOffering object. See CourseOffering.php for description of this format 
					</section>
				</sections>
			</schedule>
			<electives>
				<elective group="elecive group">
					<option>One for each option</option>
				</elective>
			</electives>
		</response>
	*/
	require_once("api/lib/db.php");
	require_once("api/lib/Course.php");
	require_once("api/scheduler.php");
	require_once("api/electives.php");
	
	echo "<?xml-stylesheet type='text/xsl' href='suggestedCourses.xsl'?>";
	
	//Must select a pattern
	if(!isset($_GET['pattern']))
	{
		echo '<response><errormessage>Invalid Submission</errormessage></response>';
		exit;
	}
	
	//Figure out the registration year and term
	$year = date('Y');
	$month = date('n');
	
	if ($month >= 6 and $month <= 7)
	{
		$term = 0;
	}
	else
	{
		$term = 1;
		if ($month > 7)
		{
			$year = $year + 1;	//TODO CHANGE THIS TO MATCH THE ACTUAL TERM TO SCHEDULE FOR
		}
		$term = 0;
		$year = $year-1;
	}

	//Get the list of courses that they have completed.
	//If on pattern query their pattern in the database
	if(isset($_GET['completed']))
	{
		$completed = $_GET['completed'];
	}
	else if ($_GET['pattern'] == 'offpattern')
	{
		$completed = array();
	}
	else if ($_GET['pattern'] == 'onpattern' and isset($_GET['year_select']))
	{
		$completed_year = $_GET['year_select'];
		$completedQuery = new Query('program_elements');
		$completedQuery->select('course_code');
		$completedQuery->where("element_year <".$completed_year."
								 OR (element_year=$completed_year AND term=0 AND $term=1)");
		$completedRows = $completedQuery->executeFetchAll();
		$completed = [];
		foreach($completedRows as $row)
		{
			array_push($completed,$row[0]);
		}
	}
	else
	{
		echo "<response><erromessage>Invalid Submission</errormessage></response>";
		exit;
	}
	
	header('Content-Type: text/xml; charset=utf-8');
	
	echo '<response e="0">';

	//Get the actual course objects from the database
	$completed_as_courses = array();
	
	foreach ($completed as $c)
	{
		array_push($completed_as_courses, Course::fetch($c));
	}
	
	//IF THEY ARE ON PATTERN ASSUME ELECTIVES ARE FULFILLED UP TO THIS TERM
	//Get the user's program elements, sorted by the term and year they should take them in
	//Only select those courses which have a section available in the scheduling term
	if ($_GET['pattern'] == 'onpattern')
	{
		$programQuery = new Query("program_elements e");
		$programQuery->select("course_code");
		$programQuery->select("credit_type");
		$programQuery->select("elective_note");
		$programQuery->select("term");
		$programQuery->select("element_year");

		$programQuery->where("program_id = ?
							  AND ((credit_type <> 0 AND e.element_year>=$completed_year AND e.term >= $term) OR course_code NOT IN
							  ".Query::valuelistsql($completed).
							  ") AND 
							  ((EXISTS 
							   (SELECT * FROM course_offerings o
							   WHERE o.course_code = e.course_code
							   AND o.year=$year
							   AND o.term=$term))
							   OR
							   (credit_type <> 0))
							   ORDER BY element_year ASC, term ASC, credit_type DESC", array_merge([$_GET['program_select']], $completed));
		$pattern = $programQuery->executeFetchAll(); 
	}
	else
	{
		$programQuery = new Query("program_elements e");
		$programQuery->select("course_code");
		$programQuery->select("credit_type");
		$programQuery->select("elective_note");
		$programQuery->select("term");
		$programQuery->select("element_year");

		$programQuery->where("program_id = ?
		                      AND ((credit_type <> 0) OR course_code NOT IN
		                      ".Query::valuelistsql($completed).
		                      ") AND
		                      ((EXISTS
		                       (SELECT * FROM course_offerings o
		                       WHERE o.course_code = e.course_code
		                       AND o.year=$year
		                       AND o.term=$term))
		                       OR
		                       (credit_type <> 0))
		                       ORDER BY element_year ASC, term ASC, credit_type DESC", array_merge([$_GET['program_select']], $completed));
		$pattern = $programQuery->executeFetchAll(); 
		
		//See which of their electives they have fulfilled and remove them from consideration in the pattern
		foreach ($pattern as $key=>$course)
		{
			if ($course[1] != '0')
			{
				foreach($completed as $considering)
				{
					if (Elective::isElective($_GET['program_select'],$considering[0], $course[1], $course[2]))
					{
						unset($pattern[$key]);
					}
				}
			}
		}
	}


	$found = 0;
	$discarded = array();
	$scheduling = array();
	$electives = array();
	$alternatives = array();
	$endIndex = 0;
	
	for($i=0; $i<count($pattern); $i++)
	{
	
		if ($found ==5)
			break;
			
		if ($pattern[$i][1] != '0')
		{
			array_push($electives, $pattern[$i]);
			$found++;
			continue;
		}
		
		$course = Course::fetch($pattern[$i][0]);
		if (count($course->unsatisfied_prerequisites($completed_as_courses, $scheduling)) == 0)
		{
			array_push($scheduling, $course);
			$found++;
		}
		else
		{
			array_push($discarded, $course);
			continue;
		}
		
		//Check and see if we can fit the discarded courses in now
		foreach($discarded as $key=>$tryagain)
		{
			if (isset($tryagain))
			{
				if(count($tryagain->unsatisfied_prerequisites($completed_as_courses, $scheduling)) == 0)
				{
					array_push($scheduling, $tryagain);
					$found++;
					unset($discarded[$key]);
				}
			}
		}
		$endIndex = $i;
	}
	
	$s = Schedule::buildConflictFreeSchedule($scheduling, $year, $term, $alternatives);

	if ($s == null && count($electives) == 0)
	{
		$iter = 0;
		while ($iter < count($scheduling) && $s==null)
		{
				$s = Schedule::buildConflictFreeSchedule(array_slice($scheduling,$iter), $year, $term, array_slice($pattern, $endIndex+1));
				$iter++;
		}
		echo '<noschedulemessage> Could not generate a conflict free schedule</noschedulemessage>';
	}
	else
	{
		if ($s == null)
		{
			$s = new Schedule();
		}
		
		foreach($s->getSchedules() as $sched)
		{
			$sched->to_xml();
		}
		
		echo '<electives>';
		foreach($electives as $elective)
		{
			echo '<elective group="'.$elective[2].'">';

			foreach(Elective::getElectives($_GET['program_select'], $elective[1], $elective[2]) as $option)
			{
				echo "<option>".$option[0]."</option>";
			}
			echo '</elective>';
		}
		echo '</electives>';
	}
	echo '</response>';
	
?>
