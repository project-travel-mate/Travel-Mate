<?php

	$val = 10;

	switch ($val) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
			echo "break missing here";
		case 6:
		case 7:
		case 8:
		case 9:
			echo "break ok";
			break;
		case 10:
		default:
			echo "Good val";
			break;
	}