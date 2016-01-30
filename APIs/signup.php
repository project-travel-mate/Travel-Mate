<?php
	$error_messages = array(
		'Email ID already Exists',
		'Could Not Register, Try Again Later',
		'success',
		'Incorrect Parameters'
	);
	require 'inc/connection.inc.php';
	
	if(!isset($_GET['pass']) || !isset($_GET['email']) || !isset($_GET['name'])){
		$error = 3;
	} else {
		$password = md5(trim($_GET['pass']));
		$email = trim(strtolower($_GET['email']));
		$name = trim($_GET['name']);
		
		$query = "SELECT `id` FROM `users` WHERE `email`='$email'";
		$query_run = mysqli_query($connection,$query);
		
		if(mysqli_num_rows($query_run) > 0 ){
			$error = 0;
		} else {
			$query = "INSERT INTO `users` (`name`,`email`,`password`) VALUES ('$name','$email','$password')";
			if(!mysqli_query($connection,$query))
				$error = 1;
			else {
				$last_id = mysqli_insert_id($connection);					
				$error = 2;
			}
		}
	}
	$response = array(
		'success'	=> ($error == 2) ? true : false
	);
	
	if($error == 2)
		$response['id'] = $last_id;
	else
		$response['message'] = $error_messages[$error];
	
	echo json_encode($response);