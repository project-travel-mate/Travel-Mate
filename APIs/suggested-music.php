<?php

	require 'inc/connection.inc.php';
	require 'inc/function.inc.php';
	
	$echonest_mood_strings = array(
		'mood=fun&mood=joyous&mood=lively',
		'mood=bouncy&mood=happy&mood=playful',
		'mood=carefree&mood=peaceful',
		'mood=disturbing&mood=sad',
		'mood=sentimental&mood=aggressive&mood=angry'							
	);
	
	$final_response = array();

	if(isset($_GET['userid'])){
		$user_id = (int)$_GET['userid'];
		$query = "SELECT `mood` FROM `users` WHERE `id`='$user_id' LIMIT 1";
		$query_row = mysqli_fetch_assoc(mysqli_query($connection, $query));
		
		$mood_score = (int)$query_row['mood'];
		
		if($mood_score > 10){
			$mood_mode = 0;
		} else if($mood_score > 2){
			$mood_mode = 1;
		} else if($mood_score > -2){
			$mood_mode = 2;
		} else if($mood_score > -10){
			$mood_mode = 3;
		} else {
			$mood_mode = 4;
		}
  		$url = 'http://developer.echonest.com/api/v4/song/search?api_key=' . $echonest_api_key . '&format=json&results=15&' . $echonest_mood_strings[$mood_mode] . '&bucket=song_type';
  		
  		$response = json_decode(curl_URL_call($url), true);
  		foreach($response['response']['songs'] as $song){
  			$temp_array = array(
  				'title'		=> $song['title'],
  				'artist'	=> $song['artist_name'],
  				'url'		=> 'http://www.saavn.com/s/' . urlencode(strtolower($song['title'])),
  			);
  			
  			array_push($final_response, $temp_array);
  		}
	}
	
	echo json_encode(array('songs' => $final_response));