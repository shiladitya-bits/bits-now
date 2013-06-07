<?php
$con = mysql_connect("localhost","root","");
if (!$con)
  {
  die('-1'. mysql_error());
  }
mysql_select_db("bitsnow_db", $con);
if($_REQUEST['type'] == 's')
{
	$query = "select * from students where email = '".$_REQUEST['user']."' and password = '".$_REQUEST['pwd']."'";
	$result = mysql_query($query,$con); 
	if (mysql_num_rows($result)==0)
	{
		echo "0";
		//die('0' . mysql_error());
	}
	else echo "1";
}
else if($_REQUEST['type'] == 't')
{
	$query = "select * from teachers where email = '".$_REQUEST['user']."' and password = '".$_REQUEST['pwd']."'";
	$result = mysql_query($query,$con);
	if (mysql_num_rows($result)==0)
	{
		echo "0";
		//die('0' . mysql_error());
	}
	else echo "1";
}
else
{	
	//echo "2";
	die('2' . mysql_error());
}
mysql_close($con);
?>