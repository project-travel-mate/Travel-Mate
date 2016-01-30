<?php
	
	$error_messages = array(
		'Incorrect Credentials',
		'Success',
		'Could Not Register, Try Again Later'
	);
	
	require 'inc/connection.inc.php';
	
	if(!isset($_GET['email']) || !isset($_GET['pass'])){
		echo 'Incorrect Parameters';
	} else {
		$password = md5(trim($_GET['pass']));
		$email = trim(strtolower($_GET['email']));
		
		$response_array = array();
		
		$query = "SELECT `id`,`name` FROM `users` WHERE `email`='$email' AND `password`='$password'";
		if($query_run = mysqli_query($connection, $query)){
			if(mysqli_num_rows($query_run) == 1){
				while($query_row = mysqli_fetch_assoc($query_run)){
					$error = 1;
					$response_array = array(
						'id'		=> (int)$query_row['id'],
						'name'		=> $query_row['name']
					);
				}
			} else {
				$error = 0;
			}
		} else {
			$error = 2;
		}
		
		$response_array['success'] = ($error == 1) ? true : false;
			
		echo json_encode($response_array);
	}