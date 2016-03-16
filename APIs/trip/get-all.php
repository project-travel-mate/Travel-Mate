<?php
/**
 * @Author: prabhakar
 * @Date:   2016-03-16 17:39:42
 * @Last Modified by:   Prabhakar Gupta
 * @Last Modified time: 2016-03-16 17:51:10
 */

require_once '../inc/connection.inc.php';

$user_id = (int)$_GET['user'];
$response = array();

if($user_id > 0){
	$query = "SELECT TU.trip_id, T.title, T.start_time, T.end_time, C.city_name FROM `trips` T INNER JOIN `trip_users` TU ON T.trip_id = TU.trip_id INNER JOIN `cities` C ON T.city_id = C.id WHERE TU.user_id='$user_id'";
	$query_run = mysqli_query($connection, $query);

	while($query_row = mysqli_fetch_assoc($query_run)){
		$end_time_temp = isset($query_row['end_time']) ? (int)$query_row['end_time'] : null;
		
		$temp_array = array(
			'id'			=> (int)$query_row['trip_id'],
			'title'			=> trim($query_row['title']),
			'start_time'	=> (int)$query_row['start_time'],
			'end_time'		=> $end_time_temp,
			'city'			=> trim($query_row['city_name']),
		);

		array_push($response, $temp_array);
	}
}

echo json_encode($response);
