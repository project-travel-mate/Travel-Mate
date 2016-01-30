<?php


	require 'inc/connection.inc.php';
	require 'inc/function.inc.php';

	$url = 'http://developer.echonest.com/api/v4/song/search?api_key=' . $echonest_api_key . '&format=json&results=1&bucket=audio_summary&';

	if(isset($_GET['artist']) && isset($_GET['song']) && isset($_GET['userid'])){
		$user_id 	= (int)$_GET['userid'];
		$artist_name 	= urlencode(trim(strtolower($_GET['artist'])));
		$song_title 	= urlencode(trim(strtolower($_GET['song'])));
		
 		$url .= 'artist=' . $artist_name . '&title=' . $song_title;
 		$json_response = json_decode(curl_URL_call($url), true);
		$mood_score = (float)$json_response['response']['songs'][0]['audio_summary']['energy'];
		
		$update_flag = false;
		
		if($mood_score >= 0.6){
			$query = "UPDATE `users` SET `mood` = `mood` + 1 WHERE `id`='$user_id' ";
			$update_flag = true;
		} else if($mood_score <= 0.4 && $mood_score > 0){
			$query = "UPDATE `users` SET `mood` = `mood` - 1 WHERE `id`='$user_id'";
			$update_flag = true;
		}
		
		if($update_flag){
			mysqli_query($connection, $query);
		}
		
		$query = "SELECT `mood` FROM `users` WHERE `id`='$user_id' LIMIT 1";
		$query_row = mysqli_fetch_assoc(mysqli_query($connection, $query));
		
 		$mood_score = (int)$query_row['mood'];
 		
 		echo json_encode(array('mood' => $mood_score));
		
	}