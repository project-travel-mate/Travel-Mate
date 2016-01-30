<?php

	$ebay_app_name = 'NSIT22619-c5e7-41ee-9311-cbde5b60ca2';
	$url = 'http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&RESPONSE-DATA-FORMAT=JSON&SECURITY-APPNAME=' . $ebay_app_name . '&REST-PAYLOAD&keywords=';
	
	require 'inc/connection.inc.php';
	require 'inc/function.inc.php';
	
	$final_response = array();
	
	if(isset($_GET['string'])){
		$url .= trim(strtolower($_GET['string']));
		$response = json_decode(curl_URL_call($url), true);
		foreach($response['findItemsAdvancedResponse'][0]['searchResult'][0]['item'] as $item){
			$temp_array = array(
				'name'		=> $item['title'][0],
				'url'		=> $item['viewItemURL'][0],
				'image'		=> $item['galleryURL'][0],
				'value'		=> (float)$item['sellingStatus'][0]['currentPrice'][0]['__value__'] * 64.85,
				
			);
			array_push($final_response, $temp_array);
		}

	}
	
	echo json_encode(array('results' => $final_response));