<?php

/**
 * This file is an exemple of PHP file containing good style (according to the default ruleset).
 */
class GoodTest {

	/**
	 * This function is documented.
	 * 
	 * @param Integer $max The max size of the salt
	 * @return String a salt
	 */
	function genereSalt($max = 15) {
		$characterList = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		$i = 0;
		$salt = "";
		do {
			$salt .= $characterList[mt_rand(0, strlen($characterList)-1)];
			$i++;
		} while ($i < $max);
		return $salt;
	}

}