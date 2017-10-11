<?php

	require_once '../inc/connection.inc.php';
	require_once '../inc/function.inc.php';

	$user_id = (int)$_GET['user'];
	$response = array();
	$connection = get_mysql_connection();

	if ($user_id > 0) {
		$query = "SELECT TU.trip_id, T.title, T.start_time, T.end_time, C.city_name, C.image
			FROM `trips` T INNER JOIN `trip_users` TU ON T.trip_id = TU.trip_id
			INNER JOIN `cities` C ON T.city_id = C.id
			WHERE TU.user_id='$user_id'
			ORDER BY T.start_time DESC";

		$query_run = mysqli_query($connection, $query);

		while ($query_row = mysqli_fetch_assoc($query_run)) {
			$end_time_temp = isset($query_row['end_time']) ? (int)$query_row['end_time'] : null;
			
			$temp_array = array(
				'id'         => (int)$query_row['trip_id'],
				'title'      => change_city_name($query_row['title']),
				'start_time' => (int)$query_row['start_time'],
				'end_time'   => $end_time_temp,
				'city'       => trim($query_row['city_name']),
				'image'      => trim($query_row['image']),
			);

			array_push($response, $temp_array);
		}
	}

	echo json_encode($response);
