<?php

/**
 * This file is an exemple of PHP file containing good naming with specific rule for function parameters.
 */

// constant naming :: PHPCHECKSTYLE_CONSTANT_NAMING
define(NAMED_CONSTANT, "A_CONSTANT_VALUE");

// constant naming :: PHPCHECKSTYLE_CONSTANT_NAMING
const CONSTANT = "CONSTANT";

// top level var naming :: PHPCHECKSTYLE_TOPLEVEL_VARIABLE_NAMING
$foo = 1;

//
/**
 * class naming :: PHPCHECKSTYLE_CLASSNAME_NAMING
 *
 * @SuppressWarnings checkUnusedVariables
 */
class NamedClass {

	// member level var naming :: PHPCHECKSTYLE_MEMBER_VARIABLE_NAMING
	var $bar = 1;

	/**
	 * constructor Naming
	 */
	function __construct() {}

	/**
	 * function naming :: PHPCHECKSTYLE_FUNCNAME_NAMING
	 *
	 * @param $_myparam a param
	 */
	function namedFunction($_myparam) {

		// local level var naming :: PHPCHECKSTYLE_LOCAL_VARIABLE_NAMING
		$fuu = 1;

		$_myparam = 0;
	}
}

/**
 * interface naming
 */
interface MyInterface {

	/**
	 * function naming :: PHPCHECKSTYLE_FUNCNAME_NAMING
	 *
	 * @param $_name a name
	 * @param $_var a var
	 */
	public function setVariable($_name, $_var);
}


