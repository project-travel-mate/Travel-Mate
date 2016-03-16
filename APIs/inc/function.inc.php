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

function getMonumentID($connection, $beacon_id){
	$monument_id = 0;
	
	$query = "SELECT `id` FROM `monuments_estimote` WHERE `major`='$beacon_id' LIMIT 1";
	$query_row = mysqli_fetch_assoc(mysqli_query($connection, $query));
	$monument_id = (int)$query_row['id'];
	
	return $monument_id;
}

function encryptText($text){
	return strip_tags(addslashes($text));
}

function decryptText($text){
	return stripcslashes($text);
}

/**
 * function to get city weather details
 * from city name using openweathermap API
 * @param  string	$city_name
 * @return array
 */
function getCityWeather($city_name){
	$url = "http://api.openweathermap.org/data/2.5/weather?q=" . $city_name . "&APPID=" . OPENWEATHERMAP_API_KEY;
	$api_response = json_decode(curl_URL_call($url), true);

	$return_array = array(
		'icon'			=> "http://openweathermap.org/img/w/" . trim($api_response['weather'][0]['icon']),
		'humidity'		=> (float)$api_response['main']['humidity'],
		'temprature'	=> (float)$api_response['main']['temp'] - 273.15,
		'description'	=> trim($api_response['weather'][0]['description']),
	);

	return $return_array;
}
