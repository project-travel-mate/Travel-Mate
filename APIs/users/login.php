<?php
/**
 * @Author: prabhakar
 * @Date:   2016-03-16 17:51:46
 * @Last Modified by:   Prabhakar Gupta
 * @Last Modified time: 2016-03-16 18:19:29
 */

require_once '../inc/connection.inc.php';

$user_contact	= trim($_GET['contact']);
$user_password	= md5($_GET['password']);

$query = "SELECT * FROM `users` WHERE `contact`='$user_contact' AND `password`='$user_password' LIMIT 1";
$query_row = mysqli_fetch_assoc(mysqli_query($connection, $query));

if(isset($query_row)){
	$success = 1;
	$user = array(
		'id'		=> (int)$query_row['id'],
		'name'		=> trim($query_row['name']),
	);
} else {
	$success = 0;
	$user = null;
}

$response = array(
	'success'	=> (bool)$success,
	'user_id'	=> $user,
);

echo json_encode($response);
