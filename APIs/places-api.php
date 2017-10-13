<?php

	require_once 'inc/connection.inc.php';
	require_once 'inc/function.inc.php';
	require_once 'inc/constants.inc.php';
	require_once 'inc/responses/base.php';
	require_once 'inc/responses/errors.php';

	
	if(isset($_GET['lat']) && isset($_GET['lng']) && isset($_GET['mode'])){
		$response 	= array();
		$latitude 	= (float)$_GET['lat'];
		$longitude 	= (float)$_GET['lng'];

		if($latitude != 0 && $longitude != 0){
			$mode = (int)$_GET['mode'];

			$response['mode']		= $HERE_API_MODES[$mode];
			$response['results'] 	= array();

			$here_response = call_here_api($HERE_API_MODES[$mode], $latitude, $longitude);

			if(empty($here_response)){
				noResultsError();
			} else {
				foreach($here_response as $item){
					$temp_array = array(
						'name'		=> strip_tags($item['title']),
						'lat'		=> (float)$item['position'][0],
						'lng'		=> (float)$item['position'][1],
						'address'	=> strip_tags($item['vicinity']),
						'phone'		=> null,
						'website'	=> strip_tags($item['href']),
						'icon'		=> strip_tags($item['icon']),
					);
					array_push($response['results'], $temp_array);
				}
				echo json_encode($response);
			}
		} else {
			// incorrect parameters passed
			invalidParametersError();
		}
	} else {
		// incorrect parameters passed
		invalidParametersError();
	}

