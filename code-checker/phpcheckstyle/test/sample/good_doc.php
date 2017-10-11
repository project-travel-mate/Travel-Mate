<?php
/**
 * This file is an exemple of PHP file containing good style (according to the default ruleset).
 * @SuppressWarnings localScopeVariableLength
 */
class GoodTest {

	/**
	 * Correctly documented function.
	 *
	 * @param String $a a string
	 * @param array $b an array
	 * @param Boolean $c a flag
	 * @return String a result
	 */
	function privateFunction($a, array $b = array(), $c = false) {

		if ($c > $a) {
			return $a + $b;
		} else {
			return $a;
		}
	}

	/**
	 * Correctly documented function 2.
	 *
	 * The "return" should not count.
	 *
	 * @param String $a a string
	 * @param array $b an array
	 */
	function privateFunction2($a, array $b = array()) {

		if ($b > $a) {
			return;
		}

		// do something else
	}


	/**
	 * Doc is inherited.
	 *
	 * @inheritdoc
	 */
	function privateFunction2($a, array $b = array()) {

		// function parameters are not used, but we don't care
	}

}