<?php

	class Elective
	{
		static function isElective($program,$course,$elective_type,$elective_group)
		{
			foreach(explode("|",$elective_group) as $group)
			{
				$q = new Query("electives");
				$q->select("course_code");
				$q->where("program_id=? AND course_code=? AND elective_type=? AND note=?", [$program,$course,$elective_type,$group]);
				$result = $q->executeFetchAll();
				if (count($result) > 0)
				{
					return true;
				}
			}
			return false;
		}
		
		static function getElectives($program, $elective_type, $elective_group)
		{
			$results = array();
			foreach(explode("|",$elective_group) as $group)
			{
				$q = new Query("electives");
				$q->select("course_code");
				$q->where("program_id=?
							AND elective_type=?
							AND note=?", [$program,$elective_type, $group]);
				$results = array_merge($results, $q->executeFetchAll());
			}
			return $results;
		}
	}
?>