<?php
namespace PHPCheckstyle\Config;

/**
 * Abstract class model for the configuration checkers.
 *
 * @author James Brooks <jbrooksuk@me.com>
 *         @SuppressWarnings docBlocks
 */
abstract class CheckStyleConfig {

	/**
	 * Stores the check configuration.
	 *
	 * @var array
	 */
	public $config = array();

	/**
	 * Return a true if the test exist, false otherwise.
	 *
	 * @param String $test
	 *        	name of the test
	 * @return Boolean true if test exists.
	 */
	public function getTest($test) {
		return (array_key_exists(strtolower($test), $this->config));
	}

	/**
	 * Return a list of items associed with a test.
	 *
	 * @param String $test
	 *        	name of the test
	 * @return array the list of items for this test.
	 */
	public function getTestItems($test) {
		$test = strtolower($test);
		return isset($this->config[$test]['item']) ? $this->config[$test]['item'] : false;
	}

	/**
	 * Return a list of exception for a test.
	 *
	 * @param String $test
	 *        	name of the test
	 * @return array the list of exceptions for this test.
	 */
	public function getTestExceptions($test) {
		$test = strtolower($test);
		return isset($this->config[$test]['exception']) ? $this->config[$test]['exception'] : false;
	}

	/**
	 * Return a list of items associed with a configuration.
	 *
	 * @param String $config
	 *        	name of the config
	 * @return array the list of items for this config.
	 */
	public function getConfigItems($config) {
		$config = strtolower($config);
		return isset($this->config[$config]) ? $this->config[$config] : array();
	}

	/**
	 * Return the level of severity of a test.
	 *
	 * @param String $test
	 *        	name of the test
	 * @return the level of severity.
	 */
	public function getTestLevel($test) {
		$ret = WARNING;

		$test = strtolower($test);

		if (array_key_exists($test, $this->config) && array_key_exists('level', $this->config[$test])) {
			$ret = strtolower($this->config[$test]['level']);
		}

		$invalidLevels = array(
			ERROR,
			IGNORE,
			INFO,
			WARNING
		);

		if (!in_array($ret, $invalidLevels)) {
			echo "Invalid level for test " . $test . " : " . $ret;
			$ret = WARNING;
		}

		return $ret;
	}

	/**
	 * Return the regular expression linked to the test.
	 *
	 * @param String $test
	 *        	name of the test
	 * @return the regular expression.
	 */
	public function getTestRegExp($test) {
		$test = strtolower($test);
		$ret = "";
		if (array_key_exists($test, $this->config) && array_key_exists('regexp', $this->config[$test])) {
			$ret = $this->config[$test]['regexp'];
		}

		return $ret;
	}

	/**
	 * Return the list of deprecated method and their replacement.
	 *
	 * @param String $test
	 *        	name of the test
	 * @return the list of depecated values.
	 */
	public function getTestDeprecations($test) {
		$test = strtolower($test);
		$ret = "";
		if (array_key_exists($test, $this->config)) {
			$ret = $this->config[$test];
		}

		return $ret;
	}

	/**
	 * Return the list of aliases and their replacement.
	 *
	 * @param String $test
	 *        	name of the test
	 * @return the list of replaced values.
	 */
	public function getTestAliases($test) {
		$test = strtolower($test);
		$ret = "";
		if (array_key_exists($test, $this->config)) {
			$ret = $this->config[$test];
		}

		return $ret;
	}

	/**
	 * Return the list of replacements.
	 *
	 * @param String $test
	 *        	name of the test
	 * @return the list of replaced values.
	 */
	public function getTestReplacements($test) {
		$test = strtolower($test);
		$ret = "";
		if (array_key_exists($test, $this->config)) {
			$ret = $this->config[$test];
		}

		return $ret;
	}

	/**
	 * Return the value of a property
	 *
	 * @param String $test
	 *        	name of the test
	 * @param String $property
	 *        	name of the property
	 * @return the value.
	 */
	public function getTestProperty($test, $property) {
		$test = strtolower($test);
		$property = strtolower($property);
		if (array_key_exists($test, $this->config) && array_key_exists($property, $this->config[$test])) {
			return $this->config[$test][$property];
		} else {
			return false;
		}
	}

	/**
	 * Change the value of a property
	 *
	 * @param String $test
	 *        	name of the test
	 * @param String $property
	 *        	name of the property
	 * @param String $value
	 *        	the value.
	 */
	public function setTestProperty($test, $property, $value) {
		$test = strtolower($test);
		$property = strtolower($property);
		if (array_key_exists($test, $this->config) && array_key_exists($property, $this->config[$test])) {
			$this->config[$test][$property] = $value;
		}
	}

	/**
	 * Indicate if a value is an exception for the test.
	 *
	 * @param String $test
	 *        	name of the test
	 * @param String $value
	 *        	the value
	 * @return Boolean true is the value is listed as an exception.
	 */
	public function isException($test, $value) {
		$exceptions = $this->getTestExceptions($test);
		return (!empty($exceptions) && in_array($value, $exceptions));
	}
}
