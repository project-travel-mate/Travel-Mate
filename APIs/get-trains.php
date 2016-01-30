<?php

	require 'inc/connection.inc.php';
	require 'inc/function.inc.php';
	
	$train_api_code = "ffhzn9117";
	$final_response = array();
	
	if(isset($_GET['src_city']) && isset($_GET['dest_city']) && isset($_GET['date'])){
	
		$source = strtolower(trim($_GET['src_city']));
		$destination = strtolower(trim($_GET['dest_city']));
		
		$src_url = 'http://api.railwayapi.com/name_to_code/station/' . $source . '/apikey/' . $train_api_code . '/';
 		$response = json_decode(curl_URL_call($src_url), true);
 		$src_code = $response['stations'][0]['code'];
 		
		$dest_url = 'http://api.railwayapi.com/name_to_code/station/' . $destination . '/apikey/' . $train_api_code . '/';
 		$response = json_decode(curl_URL_call($dest_url), true);
 		$dest_code = $response['stations'][0]['code'];
 		
		$trains_url = 'http://api.railwayapi.com/between/source/' . $src_code . '/dest/' . $dest_code . '/date/' . $_GET['date'] . '/apikey/' . $train_api_code . '/';
		
		$response = json_decode(curl_URL_call($trains_url), true);
		
		foreach($response['train'] as $train){
			$temp_array = array(
				'train_number'		=> $train['number'],
				'name'			=> $train['name'],
				'departure_time'	=> $train['src_departure_time'],
				'arrival_time'		=> $train['dest_arrival_time'],
			);
			
			$temp_array['days'] = array();
			
			foreach($train['days'] as $day){
			
				if(strtolower($day['runs']) == 'y')
					array_push($temp_array['days'], 1);
				else
					array_push($temp_array['days'], 0);
			}
			
			array_push($final_response, $temp_array);
		}
	}	
	
	echo json_encode(array('trains' => $final_response));