<?php
namespace PHPCheckstyle;

/**
 * Info about a variable in the code.
 *
 * This object is stored in the "_variables" array.
 *
 * @package classes
 * @SuppressWarnings checkUnusedVariables
 */
class VariableInfo {

	// The variable name
	var $name = null;

	// The line of first appareason of the variable
	var $line;

	// Indicate the the variable have been used at least once.
	var $isUsed = false;
}
