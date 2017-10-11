<?php

/**
 * This file is an exemple of PHP file containing bad naming.
 * This test file should generate 6 warnings and 6 errors with the default config.
 */



// 1 : constant naming :: PHPCHECKSTYLE_CONSTANT_NAMING
define(_badly_named_constant, "A_CONSTANT_VALUE");

// 2 : constant naming :: PHPCHECKSTYLE_CONSTANT_NAMING
const bad_CONST = "goo";


// 3 : top level var naming :: PHPCHECKSTYLE_TOPLEVEL_VARIABLE_NAMING
$XXX = 1;

// 4 : localScopeVariableLength : Variable x name length is too short.
$x = 1;

//
/**
 * 5 : class naming :: PHPCHECKSTYLE_CLASSNAME_NAMING
 *
 * @SuppressWarnings checkUnusedVariables
 */
class 9badlynamedclass {

	//6 : member level var naming :: PHPCHECKSTYLE_MEMBER_VARIABLE_NAMING
	$YYY = 1;

	/**
	 * 7 :constructor Naming :: Should be new style
	 */
	function 9badlynamedclass() {
	}


	/**
	 * 8 : function naming :: PHPCHECKSTYLE_FUNCNAME_NAMING
	 */
	function Badlynamedfunction() {

		//9 : local level var naming :: PHPCHECKSTYLE_LOCAL_VARIABLE_NAMING
		$ZZZ = 1;

	}

	/**
	 * 10 : protected function naming :: PHPCHECKSTYLE_PROTECTED_FUNCNAME_NAMING
	 */
	protected function Badlynamedfunction2() {
		badlynamedfunction3();
	}

	/**
	 * 11 : private function naming :: PHPCHECKSTYLE_PRIVATE_FUNCNAME_NAMING
	 */
	private function badlynamedfunction3() {

	}

}


/**
* 12 : interface naming
*/
interface _badlynamedinterface {
}


// 13 File Naming