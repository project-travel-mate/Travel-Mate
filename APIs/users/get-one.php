<?php

require_once '../inc/connection.inc.php';

$user_id = (int)$_GET['user'];
$final_response = array();
$connection = get_mysql_connection();

$query = "SELECT `name`,`contact`
	FROM `users`
	WHERE `id`='$user_id'
	LIMIT 1";

$query_row = mysqli_fetch_assoc(mysqli_query($connection, $query));

if (isset($query_row)) {
	$final_response = array(
		'id'      => $user_id,
		'name'    => trim($query_row['name']),
		'contact' => trim($query_row['contact']),
	);
}

echo json_encode($final_response);
