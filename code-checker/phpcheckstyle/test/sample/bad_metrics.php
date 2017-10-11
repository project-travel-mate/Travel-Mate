<?php
/**
 * This file is an exemple of PHP file generating warnings about the metrics.
 *
 * @SuppressWarnings localScopeVariableLength
 * @SuppressWarnings strictCompare
 */
class Metrics {

	/**
	 * Metrics about a function.
	 *
	 * @param String $a
	 * @param String $b
	 * @param String $c
	 * @param String $d
	 * @param String $e
	 * @param String $f
	 * @return String something
	 */
	function testMetrics($a, $b, $c, $d, $e, $f) {
		// 1 - functionMaxParameters

		// 2 - cyclomaticComplexity : function too complex
		if ($a == $b) {
			if ($b == $c) {
				if ($c == $c) {
					if ($d == $c) {
						if ($e == $c) {
							if ($f == $c) {
								return "a";
							}
						}
					} else if ($b == $c) {
						if ($b == $c) {
							if ($b == $c) {
								if ($b == $c) {
									return "b";
								}
							}
						}
					} else if ($b == $c) {
						if ($b == $c) {
							if ($b == $c) {
								if ($b == $c) {
									return "c";
								}
							}
						}
					}
				}
			}
		}

		// 3 - lineLength (line too long)
		echo "this is a very very very long lineeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee";
























































































































































































		// 4 - functionLength (function too long)


	}


}
