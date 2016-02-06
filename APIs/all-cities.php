<?php

	require 'inc/connection.inc.php';
	$response_array = array();
	
	$query = "SELECT `id`,`city_name`,`lat`,`lng`,`image` FROM `cities` WHERE 1 ORDER BY `city_name` ASC";
	$query_run = mysqli_query($connection, $query);
	
	while($query_row = mysqli_fetch_assoc($query_run)){
		$image_url = trim($query_row['image']);
		
		$temp_array = array(
			'id'	=> (int)$query_row['id'],
			'name'	=> ucwords(strtolower($query_row['city_name'])),
			'lat'	=> (float)$query_row['lat'],
			'lng'	=> (float)$query_row['lng'],
			'image'	=> ($image_url == '') ? null : $image_url,
		);
		
		array_push($response_array, $temp_array);
	}
	
	echo json_encode(array('cities' => $response_array));