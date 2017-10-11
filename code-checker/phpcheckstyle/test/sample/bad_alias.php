<?php
/**
 * This file is an exemple of PHP file containing function aliases that could be replaced.
 * This file should generate 4 warnings with the default config.
 */

class Aliases {

	/**
	 * Function.
	 */
	function test() {

		$alias = chop('test');   // 1 - replace with rtrim();

		fputs($alias); // 2 - replace with fwrite();

		die(); // 3 - replace with exit();

	}

}
