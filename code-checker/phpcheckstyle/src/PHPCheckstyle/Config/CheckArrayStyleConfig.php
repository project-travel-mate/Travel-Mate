<?php
namespace PHPCheckstyle\Config;

/**
 * Loads the test configuration.
 *
 * @author James Brooks <jbrooksuk@me.com>
 */
class CheckArrayStyleConfig extends CheckStyleConfig {

	/**
	 * Constructor.
	 *
	 * @param String $configArray
	 *        	The path of the config file
	 */
	public function __construct($configArray) {

		// If the path is a valid file we use it as is
		if (is_array($configArray)) {
			$this->config = $configArray;
		} else {
			echo "Config must be an array";
			exit(0);
		}
	}
}
