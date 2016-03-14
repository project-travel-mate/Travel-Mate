<?php
	
	require 'inc/connection.inc.php';
	
	$response_array = array();
	
	$device_id = trim(strtolower($_GET['device_id']));
	$user_id = 0;
	
	$query = "SELECT `id` FROM `users` WHERE `device_id`='$device_id' LIMIT 1";
	$query_row = mysqli_fetch_assoc(mysqli_query($connection, $query));
	
	if(isset($query_row)){
		$user_id = (int)$query_row['id'];
	} else {
		$query = "INSERT INTO `users` (`device_id`) VALUES ('$device_id')";
		mysqli_query($connection, $query);
		
		$user_id = (int)mysqli_insert_id($connection);
	}
	
	$response_array = array(
		'user_id'	=> $user_id,
	);
		
	echo json_encode($response_array);