<?php
/**
 * This file is an exemple of PHP file containing prohibited functions.
 * This file should generate 2 warnings with the default config.
 *
 * @SuppressWarnings localScopeVariableLength
 */
class Prohibition {

	/**
	 * Prohibited functions.
	 *
	 * @param String $a
	 * @param String $b
	 */
	function testAlCapone($a, $b) {

		exec($a);  // 1 - checkProhibitedFunctions

		print($b); // 2 - checkProhibitedTokens
	}

}
