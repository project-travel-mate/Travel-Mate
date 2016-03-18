<?php
/**
 * @Author: prabhakar
 * @Date:   2016-03-18 23:13:45
 * @Last Modified by:   Prabhakar Gupta
 * @Last Modified time: 2016-03-19 01:54:51
 */

require_once '../../inc/connection.inc.php';
require_once '../../inc/constants.inc.php';
require_once '../../inc/function.inc.php';
require_once 'lib/twitteroauth.php';

$city_id = @(int)$_GET['city'];
$yahoo_url = "http://where.yahooapis.com/v1/places";

if($city_id > 0){
	$city_name = getCityName($connection, $city_id);
	if(isset($city_name)){
		$city_name = trim($city_name['city_name']);
		$url = $yahoo_url . ".q('" . $city_name . "')?appid=" . YAHOO_WOEID_KEY;
		$response = curl_URL_call($url);
		$xml = simplexml_load_string($response);

		$woeid_city1 = (int)$xml->place->woeid;

		$xml = $xml->place->admin1;

		foreach($xml->attributes() as $a => $b) {
			if($a == "woeid"){
				$woeid_city2 = (int)$b;
				break;
			}
		}
		$twitter_connection = new TwitterOAuth(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET, TWITTER_OAUTH_TOKEN, TWITTER_OAUTH_TOKEN_SECRET);
		$response = $twitter_connection->get("https://api.twitter.com/1.1/trends/place.json?id=" . $woeid_city1);

		if(isset($response->errors))
			$response = $twitter_connection->get("https://api.twitter.com/1.1/trends/place.json?id=" . $woeid_city2);

		if(isset($response->errors)){
			$response = array(
				'error'	=> true,
				'message' => $response->errors[0]->message,
			);
		} else {
			$response = $response[0]->trends;
		}
		echo json_encode($response);
	}
}
