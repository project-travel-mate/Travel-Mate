<?php

	function baseError($status_code=200, $message){
		http_response_code($status_code);

		$response = array(
			"message" => $message,
		);

		return json_encode($response);
	}


	function noResultsError(){
		$error = baseError(204, "No results found!");
		echo $error;
	}


	function invalidParametesError(){
		$error = baseError(400, "Invlid parameters passed!");
		echo $error;
	}
