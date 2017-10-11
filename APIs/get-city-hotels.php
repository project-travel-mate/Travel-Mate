<?php

require_once 'inc/connection.inc.php';
require_once 'inc/function.inc.php';
require_once 'inc/constants.inc.php';
require_once 'inc/responses/base.php';
require_once 'inc/responses/errors.php';

$connection = get_mysql_connection();

if (isset($_GET['id'])) {
	// fetch city ID
	$id = (int)$_GET['id'];

	$query = "SELECT `lat`,`lng`
		FROM `cities`
		WHERE `id`='$id'
		LIMIT 1";

	$query_run = mysqli_query($connection, $query);

	while ($query_row = mysqli_fetch_assoc($query_run)) {
		$latitude 	= (float)$query_row['lat'];
		$longitude 	= (float)$query_row['lng'];
	}

	$response['hotels'] = array();
	$here_response = call_here_api('accommodation', $latitude, $longitude);

	foreach ($here_response as $item) {
		$temp_array = array(
			'name'     => strip_tags($item['title']),
			'lat'      => $item['position'][0],
			'lng'      => $item['position'][1],
			'address'  => strip_tags($item['vicinity']),
			'icon'     => $item['icon'],
			'phone'    => $phones[rand() % count($phones)],
			'websites' => $websites[rand() % count($websites)],
		);
		
		array_push($response['hotels'], $temp_array);
	}

	if (empty($response)) {
		no_results_error();
	} else {
		echo json_encode($response);
	}
} else {
	// incorrect parameters passed
	invalid_parametes_error();
}
