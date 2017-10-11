<?php

require_once '../inc/connection.inc.php';

$query_searched = trim($_GET['search']);
$final_response = array();
$connection = get_mysql_connection();

$query = "SELECT `id`,`name`,`contact`
	FROM `users`
	WHERE `name` LIKE '%$query_searched%'
	ORDER BY `name` ASC";

$query_run = mysqli_query($connection, $query);

while ($query_row = mysqli_fetch_assoc($query_run)) {
	$temp_array = array(
		'id'      => (int)$query_row['id'],
		'name'    => trim($query_row['name']),
		'contact' => trim($query_row['contact']),
	);

	array_push($final_response, $temp_array);
}

echo json_encode($final_response);
