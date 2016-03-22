<?php
/**
 * @Author: prabhakar
 * @Date:   2016-03-17 23:44:57
 * @Last Modified by:   Prabhakar Gupta
 * @Last Modified time: 2016-03-18 00:04:20
 */

$dir = 'uploads/';

if(isset($_REQUEST['image'])){
	$base = $_REQUEST['image'];
	$current_timestamp = time();
	$current_timestamp = (string)$current_timestamp;
	$filename = $current_timestamp . '-' . basename($_REQUEST['filename']);
	
	$binary = base64_decode($base);
	header('Content-Type: bitmap; charset=utf-8');
	$file = fopen($dir . $filename, 'wb');
	
	fwrite($file, $binary);
	fclose($file);
	

	// $query = "INSERT INTO "


	header('Content-Type: text/html');
	echo $filename;
}
