<?php 

//connection to the database

function connect() {
	 // Modify the four lines below to configure to your database
	 $username = "gchinaconnectc";
	 $password = "sheicohl";
	 $hostname = "mysql-user-master.stanford.edu"; 
	 $dbname = "g_chinaconnect_ceowebsite";
	 
    $conn = @mysql_connect($hostname, $username, $password);
    if (!$conn){
       die("connection failed" . mysql_error());
    }
    mysql_select_db($dbname, $conn);
    mysql_query("set names 'utf-8'");
    return $conn;
}

/* operation result output*/
function execute($sql, $conn) {
    if(!mysql_query($sql,$conn)) {
    //echo "Execution failed: ".mysql_error();
    	   return "fail:".mysql_error();
    } else {
      return "success";
    }
}

/* convert sql query result to array */
function convert_array($result) {
    $array = array();
    $counter = 0;
    while($row = mysql_fetch_array($result)) {
    	       $array[$counter] = $row;
	       			$counter++;
    }
    return $array;
}

?>