<?php

	class Elective
	{
		static function isElective($program,$course,$elective_type,$elective_group)
		{
			$q = new Query("electives");
			$q->select("course_code");
			$q->where("program_id=? AND course_code=? AND elective_type=? AND note=?", [$program,$course,$elective_type,$elective_group]);
			$result = $q->executeFetchAll();
			return count($result) > 0;
		}
	}
?>