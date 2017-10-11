<?php

require_once '../inc/connection.inc.php';
require_once '../inc/function.inc.php';

define("GOOGLE_GEOCODE_KEY", "AIzaSyDWr-d-W8_579-jbY4lzUSpcDUeEjkFbQw");

$google_url = "https://maps.googleapis.com/maps/api/geocode/json?key=" . GOOGLE_GEOCODE_KEY . "&address=";

$random_text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam pharetra mauris ut metus rutrum luctus.
	Quisque vitae turpis at metus tincidunt dictum. Curabitur sed lobortis orci. Vestibulum nibh massa, interdum nec
	vulputate ut, laoreet porta orci. Donec aliquam risus semper, convallis lorem sed, euismod ipsum. Sed sit amet
	fringilla urna, eu sodales tortor. Curabitur sagittis mollis hendrerit. Mauris tempor id ex in fermentum.";

$random_image = "https://s-media-cache-ak0.pinimg.com/236x/4a/77/5a/4a775a917383db15640d321e3ddacf5f.jpg";

$connection = get_mysql_connection();

$handle = fopen("cities", "r");
if ($handle) {
	while (($line = fgets($handle)) !== false) {
		$city_name = trim($line);
		$url = $google_url . $city_name;

		$response = json_decode(curl_url_call($url), true);

		$lat = (float)$response['results'][0]['geometry']['location']['lat'];
		$lng = (float)$response['results'][0]['geometry']['location']['lng'];

		$query = "INSERT INTO `cities` (`city_name`,`description`,`lat`,`lng`,`image`)
			VALUES ('$city_name','$random_text','$lat','$lng','$random_image')";

		if (mysqli_query($connection, $query)) {
			echo "success\n";
		} else {
			echo "fail\n";
		}
	}

	fclose($handle);
}
