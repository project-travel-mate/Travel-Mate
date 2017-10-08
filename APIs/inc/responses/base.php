<?php

header('Content-Type: application/json');
http_response_code(200);

/**
	reference : http://php.net/manual/en/function.error-reporting.php
	
	Set $error_reporting_flag to "0" when no error reporting is required
	Set it to "E_ERROR | E_WARNING | E_PARSE" for runtime errors
	Set it to "E_ALL" for all errors (including Notices)
**/

// $error_reporting_flag = 0;
$error_reporting_flag = E_ERROR | E_WARNING | E_PARSE;
// $error_reporting_flag = E_ALL;

error_reporting($error_reporting_flag);
