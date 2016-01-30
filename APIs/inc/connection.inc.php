<?php
	$connect_error = 'Could not connect';
	$mysql_host = 'localhost';
	$mysql_user = 'csinseew_pg_69';
	$mysql_pass = 'aloogobhi123';
	$mysql_data = 'csinseew_pg_tie';
	
	if(!@$connection = mysqli_connect($mysql_host , $mysql_user , $mysql_pass ,$mysql_data))
		die($connect_error);
?>