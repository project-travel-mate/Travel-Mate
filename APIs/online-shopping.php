<?php

	require_once 'inc/connection.inc.php';
	require_once 'inc/function.inc.php';
	require_once 'inc/constants.inc.sample';

	$ebay_app_name = EBAY_TOKEN;
	$url = 'http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&RESPONSE-DATA-FORMAT=JSON&SECURITY-APPNAME=' . $ebay_app_name . '&REST-PAYLOAD&keywords=';

	$final_response = array();

	if(isset($_GET['string'])){
		$url .= trim(strtolower($_GET['string']));
		$response = json_decode(curl_URL_call($url), true);
		foreach($response['findItemsAdvancedResponse'][0]['searchResult'][0]['item'] as $item){
			$item_value = (float)$item['sellingStatus'][0]['currentPrice'][0]['__value__'] * 6.638;
			$temp_array = array(
				'name'		=> $item['title'][0],
				'url'		=> $item['viewItemURL'][0],
				'image'		=> $item['galleryURL'][0],
				'value'		=> round($item_value, 0) * 10,
			);
			array_push($final_response, $temp_array);
		}
	}

	echo json_encode(array('results' => $final_response));
