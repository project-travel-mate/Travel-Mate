<?php
/**
 * This file is an exemple of PHP file containing bad spacing.
 * This file should generate 9 warnings with the default config.
 *
 * @SuppressWarnings localScopeVariableLength
 */
class Space {

	/**
	 * Test spacing
	 *
	 * @param String $a
	 * @param String $b
	 * @return String
	 */
	function testSpaces($a,$b){ // 1 - noSpaceAfterFunctionName, 2 - checkWhiteSpaceAfter (space after ,)

		if($a === null) {// 3 - spaceAfterControlStmt (if statement)
			return $b;
		}

		$c=$b+$a; // 4-7 - checkWhiteSpaceBefore & checkWhiteSpaceAfter (tokens + and =)

		$c = [$b , $a]; // 8 - noSpaceBeforeToken
		$c = ! $a; // 9 - noSpaceAfterToken

		return $c;

	}

}
