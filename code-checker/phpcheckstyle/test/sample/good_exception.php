<?php


/**
 * This file is an exemple of PHP file containing good style (according to the default ruleset).
 */
class GoodTestFinally {

	/**
	 * This function is documented.
	 * This function does not throw an exception
	 */
	function toto() {

		try {
			// something
			throw new Exception('my exception');
		} catch (Exception $e) {
			// something else
			echo $e->getMessage();
		} finally {
			echo "Finally does it!";
		}

	}
	
}