<?php
namespace PHPCheckstyle;

/**
 * Statement Item class.
 *
 * This object is stored in the "_branchingStack" array to keep track of nested statements.
 *
 * Inspired by http://www.phpcompiler.org/doc/latest/grammar.html
 *
 * @package classes
 * @SuppressWarnings checkUnusedVariables
 */
class StatementItem {

	/**
	 * Possible types of statement item.
	 */
	const TYPE_CLASS = 'CLASS';
	const TYPE_FUNCTION = 'FUNCTION';
	const TYPE_INTERFACE = 'INTERFACE';
	const TYPE_IF = 'IF';
	const TYPE_ELSE = 'ELSE';
	const TYPE_ELSEIF = 'ELSEIF';
	const TYPE_FOR = 'FOR';
	const TYPE_FOREACH = 'FOREACH';
	const TYPE_TRY = 'TRY';
	const TYPE_CATCH = 'CATCH';
	const TYPE_FINALLY = 'FINALLY';
	const TYPE_DO = 'DO';
	const TYPE_WHILE = 'WHILE';
	const TYPE_SWITCH = 'SWITCH';
	const TYPE_CASE = 'CASE';
	const TYPE_DEFAULT = 'DEFAULT';
	const TYPE_ARRAY = 'ARRAY';   // inside an array declaration

	// The statement type.
	var $type = null;

	// The statement name
	var $name = null;

	// The begin line of the statement in the file
	var $line;

	// For FUNCTION statements
	var $visibility;

	// For SWITCH / CASE statements
	var $switchHasDefault = false; // indicate that the switch instruction has a case "default" set.
	var $caseHasBreak = false; // indicate that the current case has a break instruction
	var $caseStartLine = 0; // start line of the currently processed case
	var $caseIsEmpty = true; // indicate if the case is empty

	// For DO / WHILE statements
	// indicate that we have met a DO statement (which will be described in another StatementItem, but it will be closed when we meet the WHILE).
	var $afterDoStatement = false;

	// For heredoc blocks
	var $inHeredoc = false; // used to desactivate the encapsedVariable rule inside a heredoc block

	// Flag indicating the the statement block is not sourrounded by {}
	var $noCurly = false;

	// Open parentheses count
	var $openParentheses = 0;

}
