<?php
/**
 * This file is an exemple of PHP file containing deprecated code.
 * This file should generate 5 warnings with the default config.
 */

class Deprecation {

	/**
	 * Depretacted functions.
	 *
	 * @param String $aVarVar
	 * @param String $bVarVar
	 */
	function testDeprecation($aVar, $bVar) {

		$aVar = split(",", $aVar);  // 1 - checkDeprecation

		$aVar = ereg("([0-9]{4})-([0-9]{1,2})-([0-9]{1,2})", $bVar); // 2

		session_register("barney"); // 3

		$aVar = mysql_db_query('mysql_database', 'mysql_query'); // 4

		$aVar = $HTTP_GET_VARS['var']; // 5
	}

}
