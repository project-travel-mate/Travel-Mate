<?php
/**
 * This file is an exemple of PHP file containing unsafe comparison.
 */

$findme    = 'a';
$mystring1 = 'xyz';
$mystring2 = 'ABC';

$pos1 = stripos($mystring1, $findme);
$pos2 = stripos($mystring2, $findme);

// Should be ===
if ($pos1 == false) {
	echo 'Not found';
}

// Should be !==
if (false != $pos2) {
	echo 'Found';
}

// Should be ===
if (false == stripos($mystring2, $findme)) {
	echo 'Found';
}

// Should be ===  
if (stripos($mystring2, $findme) == false) {
	echo 'Found';
}

// OK
if (false === stripos($mystring2, $findme)) {
	echo 'Found';
}