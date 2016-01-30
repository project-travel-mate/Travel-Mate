<?php

	require 'inc/connection.inc.php';
	require 'inc/function.inc.php';

	$modes = ['eat-drink','going-out,leisure-outdoor','sights-museums','transport','shopping','petrol-station','atm-bank-exchange','hospital-health-care-facility'];
	
	if(isset($_GET['lat']) && isset($_GET['lng']) && isset($_GET['mode'])){
		
		$latitude 	= (float)$_GET['lat'];
		$longitude 	= (float)$_GET['lng'];
		$mode 		= (int)$_GET['mode'];
	
		$response['mode'] = $modes[$mode];
		$response['results'] = array();
		
		$here_response = call_here_api($modes[$mode], $latitude, $longitude);
		foreach($here_response as $item){
			$temp_array = array(
				'name'		=> strip_tags($item['title']),
				'lat'		=> $item['position'][0],
				'lng'		=> $item['position'][1],
				'address'	=> strip_tags($item['vicinity']),
				'phone'		=> $phones[rand()%count($phones)],
				'website'	=> $websites[rand()%count($websites)],
			);
			array_push($response['results'], $temp_array);
		}
		
		echo json_encode($response);
	}