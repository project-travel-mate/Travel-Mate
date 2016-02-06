<?php

	require_once 'inc/connection.inc.php';
	
	$final_response = array();
	$city_id = (int)$_GET['id'];
	
	if($city_id > 0){
		$images = array();
		$query = "SELECT `image_url` FROM `city_images` WHERE `city_id`='$city_id' ORDER BY `image_url` ASC";
		$query_run = mysqli_query($connection, $query);
		while($query_row = mysqli_fetch_assoc($query_run)){
			array_push($images, $query_row['image_url']);
		}
		
		$query = "SELECT `fact` FROM `city_facts` WHERE `city_id`='$city_id' ORDER BY `fact` ASC";
		$query_run = mysqli_query($connection, $query);
		
		while($query_row = mysqli_fetch_assoc($query_run)){
			$random_index = rand(0, count($images)-1);
			$temp_array = array(
				'fact'	=> $query_row['fact'],
				'image'	=> $images[$random_index],
			);
			
			array_push($final_response, $temp_array);
		}
	}
	
	echo json_encode(array('facts' => $final_response));