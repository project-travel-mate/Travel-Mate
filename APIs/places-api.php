<?php
/**
 * @Author: prabhakar
 * @Date:   2016-03-17 02:36:24
 * @Last Modified by:   Prabhakar Gupta
 * @Last Modified time: 2016-03-17 03:06:29
 */

require_once 'inc/connection.inc.php';
require_once 'inc/function.inc.php';
require_once 'inc/constants.inc.php';

$response 	= array();
$latitude 	= (float)$_GET['lat'];
$longitude 	= (float)$_GET['lng'];
		
if($latitude != 0 && $longitude != 0){
	$mode = (int)$_GET['mode'];
	
	$response['mode']		= $HERE_API_MODES[$mode];
	$response['results'] 	= array();

	$here_response = call_here_api($HERE_API_MODES[$mode], $latitude, $longitude);
	
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
}

echo json_encode($response);
