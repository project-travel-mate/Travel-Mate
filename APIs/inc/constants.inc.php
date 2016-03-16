<?php
/**
 * @Author: prabhakar
 * @Date:   2016-03-14 21:07:46
 * @Last Modified by:   Prabhakar Gupta
 * @Last Modified time: 2016-03-17 03:07:48
 */

// open weather map API key used to get the 
// weather of city from city name
define("OPENWEATHERMAP_API_KEY", "1aa19f7dca713b11c29ec2cda827994c");


// here api codes
define("HERE_APP_CODE", "AJKnXv84fjrb0KIHawS0Tg");
define("HERE_APP_ID", "DemoAppId01082013GAL");


// different modes for different functionalities
// used in here API
$HERE_API_MODES = array(
	'eat-drink',						// 0
	'going-out,leisure-outdoor',		// 1
	'sights-museums',					// 2
	'transport',						// 3
	'shopping',							// 4
	'petrol-station',					// 5
	'atm-bank-exchange',				// 6
	'hospital-health-care-facility',	// 7
);