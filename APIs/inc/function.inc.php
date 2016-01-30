<?php
	
	$app_code = "AJKnXv84fjrb0KIHawS0Tg";
	$app_id = "DemoAppId01082013GAL";
	
	$echonest_api_key = '0BOM42L07CR77AZZJ';
	
	$websites = ['http://www.saharastar.com/','http://www.mumbai.grand.hyatt.com','http://www.tridenthotels.com/mumbai_bandra_kurla/index.asp','http://international.bawahotels.com'];
	$phones = ['+912240851800','+912261427328','+912266761234','+912266727777'];

	
	function getTimeStamp($time){
		return date('Y-m-d h:i:s', $time);
	}
	
	function curl_URL_call($url){
		$ch = curl_init();
		curl_setopt($ch, CURLOPT_URL, $url);
		curl_setopt($ch, CURLOPT_HEADER, 0);
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1); 
		$output = curl_exec($ch);
		curl_close($ch);
		return $output;
	}
	
	function call_here_api($params, $latitude, $longitude, $limit = 0){
		$here_api_url = 'https://places.demo.api.here.com/places/v1/discover/explore?at=' . $latitude . '%2C' . $longitude . '&cat=' . $params . '&app_id=' .$GLOBALS['app_id'] . '&app_code=' . $GLOBALS['app_code'];
		
		if($limit != 0)
			$here_api_url .= '&size=' . $limit;
		
		$response = json_decode(curl_URL_call($here_api_url), true);
		return $response['results']['items'];
	}