<?php

	require_once '../inc/connection.inc.php';

	$response = array();

	$user_id = (int)$_GET['user'];
	$trip_id = (int)$_GET['trip'];

	$query_check = "SELECT `trip_user_id` 
		FROM `trip_users`
		WHERE `user_id`='$user_id' AND `trip_id`='$trip_id'
		LIMIT 1";

	if (mysqli_num_rows(mysqli_query($connection, $query_check)) > 0) {
		$success = 0;
	} else {
		$query = "INSERT INTO `trip_users` (`trip_id`,`user_id`)
			VALUES ('$trip_id','$user_id')";

		mysqli_query($connection, $query);
		$success = 1;
	}

	$response['success'] = (bool)$success;

	echo json_encode($response);
