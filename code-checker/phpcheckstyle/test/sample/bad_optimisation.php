<?php
/**
 * This file is an exemple of PHP file containing unoptimized code.
 * This file should generate 2 warnings with the default config.
 */

class Optimisation {

	/**
	 * Unoptimized function.
	 *
	 * @param String $aVar
	 * @param String $bVar
	 */
	function testOptimisation($aVar, $bVar) {


		while ($aVar < count($bVar)) {
			// 1 - functionInsideLoop : store count($bVar) in a temp variable :: PHPCHECKSTYLE_FUNCTION_INSIDE_LOOP
			$aVar++;
		}

		for ($i = 0; $i < count($bVar); $i++) {
			// 2- functionInsideLoop : store count($bVar) in a temp variable :: PHPCHECKSTYLE_FUNCTION_INSIDE_LOOP
			echo $i;
		}
	}

}
