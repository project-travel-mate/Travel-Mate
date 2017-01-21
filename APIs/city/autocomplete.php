<?php

require_once '../inc/connection.inc.php';

$query_searched = trim($_GET['search']);
$final_response = array();

$query = "SELECT `id`,`city_name` FROM `cities` WHERE `city_name` LIKE '%$query_searched%' ORDER BY `city_name` ASC";
$query_run = mysqli_query($connection, $query);

while($query_row = mysqli_fetch_assoc($query_run)){
	$temp_array = array(
		'id'	=> (int)$query_row['id'],
		'name'	=> ucwords(strtolower(trim($query_row['city_name']))),
	);

	array_push($final_response, $temp_array);
}

echo json_encode($final_response);
