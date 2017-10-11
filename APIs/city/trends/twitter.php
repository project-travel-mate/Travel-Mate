<?php

require_once '../../inc/responses/base.php';
require_once '../../inc/responses/errors.php';

require_once '../../inc/connection.inc.php';
require_once '../../inc/constants.inc.php';
require_once '../../inc/function.inc.php';
require_once 'lib/twitteroauth.php';

$city_id = @(int)$_GET['city'];
$twitter_trends_url = "https://api.twitter.com/1.1/trends/";
$connection = get_mysql_connection();

if ($city_id > 0) {
	$city_coordinates = get_city_coordinates($connection, $city_id);

	if (isset($city_coordinates)) {
		$city_lat = $city_coordinates['lat'];
		$city_lng = $city_coordinates['lng'];

		$twitter_connection = new TwitterOAuth(
			TWITTER_CONSUMER_KEY,
			TWITTER_CONSUMER_SECRET,
			TWITTER_OAUTH_TOKEN,
			TWITTER_OAUTH_TOKEN_SECRET
		);

		$woeid_url = $twitter_trends_url . "closest.json?lat=" . $city_lat . "&long=" . $city_lng;
		$response = $twitter_connection->get($woeid_url);
		$city_woeid = $response[0]->woeid;

		$trends_url = $twitter_trends_url . "place.json?id=" . $city_woeid;
		$response = $twitter_connection->get($trends_url);

		if (isset($response->errors)) {
			no_results_error();
		} else {
			$trends = $response[0]->trends;
			$final_response = $trends;
		}
	}
}

if (isset($final_response)) {
	echo json_encode($final_response, true);
} else {
	invalid_parametes_error();
}
