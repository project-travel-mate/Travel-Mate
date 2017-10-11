<?php

	require_once '../inc/connection.inc.php';

	$trip_id = (int)$_GET['trip'];
	$response = array();

	if ($trip_id > 0) {	
		$query = "SELECT T.title, T.start_time, T.end_time, C.city_name 
			FROM `trips` T INNER JOIN `cities` C ON T.city_id = C.id 
			WHERE T.trip_id='$trip_id'";

		$query_row = mysqli_fetch_assoc(mysqli_query($connection, $query));

		if (isset($query_row)) {
			$end_time_temp = isset($query_row['end_time']) ? (int)$query_row['end_time'] : null;
			
			$response = array(
				'id'            => $trip_id,
				'title'         => trim($query_row['title']),
				'start_time'    => (int)$query_row['start_time'],
				'end_time'      => $end_time_temp,
				'city'          => trim($query_row['city_name']),
			);

			$users_array = array();
			$users_query = "SELECT U.id, U.name 
				FROM `trip_users` TU INNER JOIN `users` U ON TU.user_id = U.id
				WHERE TU.trip_id='$trip_id'
				ORDER BY U.name ASC";

			$users_query_run = mysqli_query($connection, $users_query);

			while ($users_query_row = mysqli_fetch_assoc($users_query_run)) {
				$temp_array = array(
					'id'    => (int)$users_query_row['id'],
					'name'  => trim($users_query_row['name']),
				);

				array_push($users_array, $temp_array);
			}

			$response['users'] = $users_array;
		}
	}

	echo json_encode($response);
