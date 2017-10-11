<?php
/**
 * This file is an exemple of PHP file containing unused code.
 * This file should generate 4 warnings with the default config.
 *
 * @SuppressWarnings localScopeVariableLength
 */
class Unused {

	// 1 - checkUnusedPrivateFunctions (never called inside the class)
	/**
	 * Unused private function
	 *
	 * @param String $a
	 * @param String $b
	 * @return String
	 */
	private function _testUnused($a, $b) { // 2- checkUnusedFunctionParameters ($b is never used)

		$c = $a + 1;
		$c .= $a; // 3 - checkUnusedVariables ($c is declared twice but never used)

		return $a;

		$a = $a + 2; // 4 - checkUnusedCode (code after the return)

	}

}
