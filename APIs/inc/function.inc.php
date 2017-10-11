<?php

$websites = ['http://www.saharastar.com/',
	'http://www.mumbai.grand.hyatt.com',
	'http://www.tridenthotels.com/mumbai_bandra_kurla/index.asp',
	'http://international.bawahotels.com'];
$phones = ['+912240851800',
	'+912261427328',
	'+912266761234',
	'+912266727777'];


/**
 * function to convert the passed timestamp to a human readable format
 * @param  integer 	$time 	timestamp
 * @return string 		 	desired time string
 */
function get_timestamp($time) {
	return date('Y-m-d h:i:s', $time);
}


/**
 * function to make cURL calls
 * @param  string 	$url 	URL at which cURL call is to be made
 * @return string      		output string
 */
function curl_url_call($url) {
	$ch = curl_init();
	curl_setopt($ch, CURLOPT_URL, $url);
	curl_setopt($ch, CURLOPT_HEADER, 0);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1); 
	$output = curl_exec($ch);
	curl_close($ch);
	return $output;
}


/**
 * Here Places API called and response is returned
 * @param  array 	$params    
 * @param  float 	$latitude  
 * @param  float 	$longitude 
 * @param  integer 	$limit     
 * @return array
 */
function call_here_api($params, $latitude, $longitude, $limit = 0) {
	$here_api_url = "https://places.demo.api.here.com/places/v1/discover/explore";

	$here_api_url = $here_api_url
		. '?at=' . $latitude . '%2C' . $longitude
		. '&cat=' . $params
		. '&app_id=' . HERE_APP_ID
		. '&app_code=' . HERE_APP_CODE;
	
	if ($limit !== 0) {
		$here_api_url .= '&size=' . $limit;
	}

	$response = json_decode(curl_url_call($here_api_url), true);
	return $response['results']['items'];
}

/**
 * get monument ID from estimote beacon ID
 * @param  connection_object	$connection 
 * @param  string 				$beacon_id  
 * @return integer
 */
function get_monument_id($connection, $beacon_id) {
	$monument_id = 0;
	
	$query = "SELECT `id`
		FROM `monuments_estimote`
		WHERE `major`='$beacon_id'
		LIMIT 1";
	$query_row = mysqli_fetch_assoc(mysqli_query($connection, $query));
	$monument_id = (int)$query_row['id'];
	
	return $monument_id;
}

/**
 * returns string which is safe for database
 * @param  string 	$text 
 * @return string 			encrypted string
 */
function encrypt_text($text) {
	return strip_tags(addslashes($text));
}


/**
 * returns string which is safe for output
 * @param  string 	$text 
 * @return string 			decrypted string
 */
function decrypt_text($text) {
	return stripcslashes($text);
}


/**
 * function to get city weather details
 * from city name using openweathermap API
 * @param  string	$city_name
 * @return array
 */
function get_city_weather($city_name) {
	$url = "http://api.openweathermap.org/data/2.5/weather?q=" . $city_name . "&APPID=" . OPENWEATHERMAP_API_KEY;
	$api_response = json_decode(curl_url_call($url), true);

	$return_array = array(
		'icon'        => "http://openweathermap.org/img/w/" . trim($api_response['weather'][0]['icon']) . ".png",
		'humidity'    => (float)$api_response['main']['humidity'],
		'temprature'  => (float)$api_response['main']['temp'] - 273.15,
		'description' => trim($api_response['weather'][0]['description']),
	);

	return $return_array;
}


/**
 * capitalize first letter for each city name
 * @param  string	$string 	city name 
 * @return string 				capatilized city name
 */
function change_city_name($string) {
	return ucwords(strtolower(trim($string)));
}


/**
 * function to increase trip count for a city
 * @param  connection_object	$connection
 * @param  integer 				$city_id
 * @return boolean
 */
function increase_trip_count($connection, $city_id) {
	$query = "UPDATE `cities`
		SET `number_of_trips`=`number_of_trips`+1
		WHERE `id`='$city_id'";

	return (bool)mysqli_query($connection, $query);
}


/**
 * get name of city from city ID
 * @param  connection_object	$connection
 * @param  integer				$city_id    
 * @return string 								city name
 */
function get_city_name($connection, $city_id) {
	$query = "SELECT `city_name`
		FROM `cities`
		WHERE `id`='$city_id'
		LIMIT 1";

	return mysqli_fetch_assoc(mysqli_query($connection, $query));
}


/**
 * get coordinates of city from city ID
 * @param  connection_object	$connection
 * @param  integer				$city_id    
 * @return array 								lat, lng
 */
function get_city_coordinates($connection, $city_id) {
	$query = "SELECT `lat`,`lng`
		FROM `cities`
		WHERE `id`='$city_id'
		LIMIT 1";

	return mysqli_fetch_assoc(mysqli_query($connection, $query));
}
