<?php

require_once '../inc/connection.inc.php';

$user_name = trim($_GET['name']);
$user_contact = trim($_GET['contact']);
$user_password = md5($_GET['password']);
$connection = get_mysql_connection();

$query = "INSERT INTO `users` (`name`,`contact`,`password`)
	VALUES ('$user_name','$user_contact','$user_password')";

if (mysqli_query($connection, $query)) {
	$response = array(
		'success' => (bool)1,
		'user_id' => (int)mysqli_insert_id($connection),
	);
} else {
	$response = array(
		'success' => (bool)0,
	);
}

echo json_encode($response);
