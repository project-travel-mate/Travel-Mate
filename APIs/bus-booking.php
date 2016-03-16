<?php

	require 'inc/connection.inc.php';
	require 'inc/function.inc.php';
	
	$redbus_url = "https://mobapi.redbus.in/wdsvc/v1_1/bussearch";
	// ?fromCityId=1443&toCityId=733&doj=25-Mar-2016
	$final_response = array();
	
	if(isset($_GET['src']) && isset($_GET['dest']) && isset($_GET['date'])){
		// source city name string
		$source_city_name = trim($_GET['src']);

		// destination city name string
		$destination_city_name = trim($_GET['dest']);

		// timestamp to be searched in seconds
		$timestamp_to_be_searched = (int)$_GET['date'];
		$date_string = date("j-M-Y", $timestamp_to_be_searched);
		
		$cities_json = json_decode(file_get_contents('res/redbus-cities.json'), true);
		
		$soruce_redbus_id = array_search(strtolower($source_city_name),array_map('strtolower',$cities_json));
		$destination_redbus_id = array_search(strtolower($destination_city_name),array_map('strtolower',$cities_json));
		
		$redbus_url = $redbus_url . '?fromCityId=' . $soruce_redbus_id . '&toCityId=' . $destination_redbus_id . '&doj=' . $date_string;
		
		$redbus_response = json_decode(curl_URL_call($redbus_url), true);
		
		
		foreach($redbus_response['data'] as $plan){
			$temp_array = array(
				'name'		=> $plan['serviceName'],
				'type'		=> $plan['BsTp'],
				'is_AC'		=> (bool)$plan['IsAc'],
				'owner'		=> $plan['Tvs'],
				'contact'	=> $plan['BPLst'][0]['BpContactNo'],
				'dep_add'	=> $plan['BPLst'][0]['BpAddress'],
				'fair'		=> $plan['FrLst'][0]
			);
			
			array_push($final_response, $temp_array);
		}
		
		$temp_array = array(
			'name'		=> 'Suryansh Bus travels and NON-AC',
			'type'		=> 'Non A/C Seater/Sleeper (2+1)',
			'is_AC'		=> (bool)0,
			'owner'		=> 'suryansh Travels',
			'contact'	=> '+91 - 8860870822',
			'dep_add'	=> 'Rajesh mandi, Pranav road',
			'fair'		=> 2100
		);
		array_push($final_response, $temp_array);
	}
	
	echo json_encode(array('results' => $final_response));