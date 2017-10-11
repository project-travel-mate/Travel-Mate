<?php

define("CONSTANT", 100);  // Constant Naming correct

/**
 * This file is an exemple of PHP file containing good style (according to the default ruleset).
 * @SuppressWarnings localScopeVariableLength
 */
class GoodTest {

	/**
	 * This function is documented.
	 */
	function toto() {

		echo "NoShortTags";

		echo "This is a not so very long line xxxxxxxxxxxx";

	}


	/**
	 * This function is documented.
	 */
	function totoTwo() {

		$a = $b = $c = 0;

		$a['Test'] = 'Test';

		$text = "";  // correct variable naming

		if ($a === $b) {
			echo $b;
		} else {
			echo $c;
		}


		$a = -12; // Should not ask for a space between - and 12

		echo " toto " . $a;

		$a = array(	$a,
				$b,
				$c   // this used to generate a false positive for noSpaceBeforeToken rule
		);


		switch ($text) {
			case "a":
				switch ($text) {
					case "a":
						break;
					case "b":
						break;
					case "c":
						break;
					default:
						break;
				}
				break;
			case "b":
				break;
			case "c":
				break;
			default:
				break;
		}


	}


	/**
	 * Correctly documented function.
	 *
	 * @param String $a a string
	 * @param String $b another string
	 * @param Boolean $c a flag
	 * @return String a result
	 */
	private function _privateFunction($a, $b, $c = false) { // should have a underscore

		// Call the private function toto
		$this->badlyNamedPrivateFunction();

		if ($c > $a) {
			return $a + $b;
		} else {
			return $a;
		}
	}

	var $test;

	/**
	 * Function having an exception.
	 *
	 * @throws Exception
	 */
	public function functionWithException() {

		// Call the private function
		$this->_privateFunction();

		$this->test;

		// do something
		throw new Exception('Exception');
	}

	/**
	 * Error of naming, but for a good reason we decide to suppress the warning using an annotation.
	 * @SuppressWarnings privateFunctionNaming
	 */
	private function badlyNamedPrivateFunction() { // should have a underscore because it is private

		// Call the private function
		$this->_privateFunction();

		// This used to generate a false positive for "needBraces" rule with the while statement
		do {
			toto();
		} while (true);

	}

	/**
	 * False positive for docbloc detection.
	 * The final keyword was causing a problem.
	 */
	final protected function finalFunction() {

		$a = null;
	}
}