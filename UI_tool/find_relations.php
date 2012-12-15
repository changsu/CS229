<?php
	// this is the util function help find relaions from cs229corpus
	// based on query result of e1, e2 and R
	function find_relations($e1, $R, $e2, $conn) {
		/* We support */
		$conditions = "";
		if (!empty($e1)) {
			$conditions .= "e1 = '$e1'"; 
		}
		if (!empty($R)) {
			if (!empty($e1))
				$conditions.= " and ";
			$conditions .= " R = '$R'";
		}
 		if (!empty($e2)) {
			if (!empty($R) || (empty($R) && !empty($e1)))
				$conditions .= " and ";
			$conditions .= " e2 = '$e2'";
		}
		
		$sql = "SELECT * FROM 229corpus where " . $conditions;
		$result = mysql_query($sql,$conn);
		$records = convert_array($result);
		return $records;
	}
?>