<?php

require_once '../inc/connection.inc.php';
require_once '../inc/function.inc.php';
require_once '../inc/constants.inc.php';

$city_id = (int)$_GET['id'];
$response = array();
$connection = get_mysql_connection();

if ($city_id > 0) {
	$query = "SELECT `city_name`,`description`,`lat`,`lng`,`image`
		FROM `cities`
		WHERE `id`='$city_id'
		LIMIT 1";

	$query_row = mysqli_fetch_assoc(mysqli_query($connection, $query));

	if (isset($query_row)) {
		$city_name = ucfirst($query_row['city_name']);

		$response = array(
			'id'          => (int)$city_id,
			'name'        => $city_name,
			'description' => trim($query_row['description']),
			'lat'         => (float)$query_row['lat'],
			'lng'         => (float)$query_row['lng'],
			'image_url'   => trim($query_row['image'])
		);

		$response['weather'] = get_city_weather($city_name);
	}
}

echo json_encode($response);
