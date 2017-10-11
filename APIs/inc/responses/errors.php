<?php

/**
 * function to return json response
 * corresponding to a status code and message
 * @param  status_code 		default 200
 * @param  message 
 * @return message JSON object
 */
function base_error($message, $status_code = 200) {
	http_response_code($status_code);

	$response = array(
		"message" => $message,
	);

	return json_encode($response);
}

/**
 * function to return no results found error
 * @return JSON object with status code 204
 */
function no_results_error() {
	$error = base_error("No results found!", 204);
	echo $error;
}

/**
 * function to return invalid parameters response
 * @return JSON object with status code 400
 */
function invalid_parametes_error() {
	$error = base_error("Invlid parameters passed!", 400);
	echo $error;
}
