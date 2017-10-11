<?php
/**
 * This file is an exemple of PHP file containing various warnings.
 * This file should generate 15 warnings with the default config.
 *
 * @SuppressWarnings localScopeVariableLength
 */
class Other {

	/**
	 * Prohibited functions.
	 *
	 * @param String $a
	 * @param String $b
	 * @param String $c
	 */
	function testAlCapone($a, $b = "c", $c) {
		// All arguments with default values must be at the end of the block
		// 1 - defaultValuesOrder

		$a = @count($c);  // 2 - checkSilencedError

		$c = "this is $a test"; // 3-4 - encapsedVariablesInsideString
		// Prefer single-quoted strings when you don't need string interpolation

		$b++;
	}

	/**
	 * Fooooo.
	 *
	 * @param String $var (by reference)
	 */
	function foo(&$var) {
		// 5 - avoidPassingReferences

		// 6 - TODO Show todos
		$var++;

		$a = $var;; // 7 - checkEmptyStatement (;;)

		if ($a AND $var) {
			// 8 - useBooleanOperators
			// 9 - checkEmptyBlock
		}

		// 10 - checkHeredoc
		$a = <<<EOT
		heredoc
EOT;

		if ($a) echo $a; // 11 - needBraces with "if"

		while ($a == true) echo $a; // 12 - needBraces with "while"
		// Consider using a strict comparison operator instead of ==
	}


	/**
	 * switch.
	 *
	 * @param String $a
	 */
	function aswitch($a) {

		switch ($a) {
			case "a":
				break;
			case "b":
				break;
			case "c":
				break;
				// 13 - switchNeedDefault
		}

		switch ($a) {
			default: // 14 - switchDefaultOrder
				break;
			case "c":
				break;

		}


	}

	/**
	 * unary.
	 *
	 * @param String $a
	 */
	function unary($a) {
		if ($a++) {
			// 15 - checkUnaryOperator
			while ($a = "1") {
				// 16 - checkInnerAssignment
				echo $a;
			}
		}
	}

}

/**
 * Other class inside the same file
 */
class Other2 { // 17 - oneClassPerFile

}
