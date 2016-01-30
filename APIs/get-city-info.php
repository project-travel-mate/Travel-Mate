<?php

	require 'inc/connection.inc.php';
	require 'inc/function.inc.php';
	
	if(isset($_GET['id'])){
		
		$id = (int)$_GET['id'];
		
		$query = "SELECT `city_name`,`description`,`lat`,`lng` FROM `cities` WHERE `id`='$id' LIMIT 1";
		$query_run = mysqli_query($connection, $query);
		
		while($query_row = mysqli_fetch_assoc($query_run)){
			$latitude = (float)$query_row['lat'];
			$longitude = (float)$query_row['lng'];
			$city_name = ucfirst($query_row['city_name']);
			
			$response = array(
				'id'			=> $id,
				'name'			=> $city_name,
				'description'		=> $query_row['description'],
				'lat'			=> $latitude,
				'lng'			=> $longitude,
			);
		}		
		
		$response['food'] = array();
		$here_response = call_here_api('eat-drink', $latitude, $longitude, 4);
		foreach($here_response as $item){
			$temp_array = array(
				'name'		=> strip_tags($item['title']),
				'lat'		=> $item['position'][0],
				'lng'		=> $item['position'][1],
				'address'	=> strip_tags($item['vicinity'])
			);
			array_push($response['food'], $temp_array);
		}
		
		$response['hangout-places'] = array();
		$here_response = call_here_api('going-out,leisure-outdoor', $latitude, $longitude, 4);
		foreach($here_response as $item){
			$temp_array = array(
				'name'		=> strip_tags($item['title']),
				'lat'		=> $item['position'][0],
				'lng'		=> $item['position'][1],
				'address'	=> strip_tags($item['vicinity'])
			);
			array_push($response['hangout-places'], $temp_array);
		}
		
		$response['monuments'] = array();
		$here_response = call_here_api('sights-museums,natural-geographical', $latitude, $longitude, 4);
		foreach($here_response as $item){
			$temp_array = array(
				'name'		=> strip_tags($item['title']),
				'lat'		=> $item['position'][0],
				'lng'		=> $item['position'][1],
				'address'	=> strip_tags($item['vicinity'])
			);
			array_push($response['monuments'], $temp_array);
		}
		
		$response['shopping'] = array();
		$here_response = call_here_api('shopping', $latitude, $longitude, 4);
		foreach($here_response as $item){
			$temp_array = array(
				'name'		=> strip_tags($item['title']),
				'lat'		=> $item['position'][0],
				'lng'		=> $item['position'][1],
				'address'	=> strip_tags($item['vicinity'])
			);
			array_push($response['shopping'], $temp_array);
		}
		
		if(strtolower($city_name) == 'bangalore')
			$city_name = "bengaluru";
		
		$weather_api_key = "655221df4dd542a5af5bca990d6f784d0f044dfd";
		$date = date('Ymd', time());
		$weather_url = 'http://api.dataweave.in/v1/indian_weather/findByCity/?api_key=' . $weather_api_key . '&city=' . ucfirst($city_name) . '&date=' . $date;
		
		$weather_response = json_decode(curl_URL_call($weather_url), true);
		$response['weather'] = array(
			'min'	=> (float)$weather_response['data'][0]['Minimum Temp'][0],
			'max'	=> (float)$weather_response['data'][0]['Maximum Temp'][0],
		);
		
		echo json_encode($response);
	}