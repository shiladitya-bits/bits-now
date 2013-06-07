<?php
	$con = mysql_connect("localhost","root","");
	if (!$con)
	  {
	  die('-1'. mysql_error());
	  }
	mysql_select_db("bitsnow_db", $con);
	//$query = "select * from students where email = '".$_REQUEST['user']."' and password = '".$_REQUEST['pwd']."'";
	$query = "select depId,depName from departments";
	$result = mysql_query($query,$con); 
	$return_arr = array();
	while ($row = mysql_fetch_array($result, MYSQL_ASSOC)) 
	{
		$row_array['did'] = $row['depId'];
		$row_array['dname'] = $row['depName'];
		array_push($return_arr,$row_array);
	}
	echo json_encode($return_arr);
	mysql_close($con);
?>