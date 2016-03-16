<?php
/**
 * @Author: prabhakar
 * @Date:   2016-03-16 23:10:52
 * @Last Modified by:   Prabhakar Gupta
 * @Last Modified time: 2016-03-17 02:33:53
 */

require_once '../inc/connection.inc.php';

$user_id = (int)$_GET['user'];
$final_response = array();

$query = "SELECT `name`,`contact` FROM `users` WHERE `id`='$user_id' LIMIT 1";
$query_row = mysqli_fetch_assoc(mysqli_query($connection, $query));

if(isset($query_row)){
	$final_response = array(
		'id'		=> $user_id,
		'name'		=> trim($query_row['name']),
		'contact'	=> trim($query_row['contact']),
	);
}

echo json_encode($final_response);
