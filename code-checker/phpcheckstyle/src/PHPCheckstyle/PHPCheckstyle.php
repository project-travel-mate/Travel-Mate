<?php
namespace PHPCheckstyle;

require_once __DIR__ . "/_Constants.php";

use \Exception;
use PHPCheckstyle\Config\CheckArrayStyleConfig;
use PHPCheckstyle\Config\CheckXMLStyleConfig;
use PHPCheckstyle\Reporter\Reporters;
use PHPCheckstyle\Reporter\Reporter;
use PHPCheckstyle\Reporter\ArrayReporter;
use PHPCheckstyle\Reporter\ConsoleReporter;
use PHPCheckstyle\Reporter\HTMLConsoleFormatReporter;
use PHPCheckstyle\Reporter\HTMLFormatReporter;
use PHPCheckstyle\Reporter\PlainFormatReporter;
use PHPCheckstyle\Reporter\XmlConsoleFormatReporter;
use PHPCheckstyle\Reporter\XmlFormatReporter;
use PHPCheckstyle\Reporter\XmlNCSSReporter;
use PHPCheckstyle\Reporter\NullReporter;

/**
 * Main Class.
 * Does most of the processing.
 *
 * @author Hari Kodungallur <hkodungallur@spikesource.com>
 *         @SuppressWarnings checkUnusedPrivateFunctions functionMaxParameters
 */
class PHPCheckstyle {

	// The class used to export the result
	public $_reporter;

	/**
	 * The PHP file parser.
	 *
	 * @var Tokenizer
	 */
	private $tokenizer = null;

	/**
	 * The stack of currently opened statements.
	 *
	 * @var StatementStack
	 */
	private $statementStack = null;

	/**
	 * Debug flag.
	 *
	 * @var Boolean
	 */
	private $debug = false;

	// Error counts for error levels
	private $errorCounts = array(
		ERROR => 0,
		IGNORE => 0,
		INFO => 0,
		WARNING => 0
	);

	// Variables
	private $validExtensions = array(
		"php",
		"tpl"
	);

	// Language for messages
	private $lang;

	private $level;

	private $messages = array();

	// Files to ignore
	private $ignoredFiles = array(
		".", // Directory link
		"..", // Directory link
		".svn", // SVN directory
		".git*"
	);

	// Accounts for .git, .gitignore .gitmodules etc

	//
	// variables used while processing control structure
	//
	// Left parenthesis opened in control statement or function statement
	private $_csLeftParenthesis = 0;

	// Left parenthesis opened in function call
	private $_fcLeftParenthesis = 0;

	private $inDoWhile = false;

	private $token = false;

	private $lineNumber = 0;

	// Store the current line number
	private $_isLineStart = true;

	// Start of a line (just after a return)

	// Indicate if we are in a control statement declaration (for, if, while, ...)
	// The control statement starts just after the statement token
	// and stops at the closing of the parenthesis or the new line if no parenthesis is used
	private $_inControlStatement = false;

	// We are inside a string (only happens with T_ENCAPSED_AND_WHITESPACE)
	private $_inString = false;

	private $_beforeArrayDeclaration = false;

	private $_inClassStatement = false;

	// We are in a class statement (declaration)
	private $_inInterfaceStatement = false;

	// We are in an interface statement (declaration)
	private $_inFunctionStatement = false;

	// We are in a function statement (declaration)
	private $_inFuncCall = false;

	// We are in a function call
	private $_inFunction = false;

	// We are inside a function
	private $_inClass = false;

	// We are inside a class
	private $_inInterface = false;

	// We are inside an interface
	private $_privateFunctions = array();

	// The list of private functions in the class
	private $_privateFunctionsStartLines = array();

	private $_functionParameters = array();

	// The list of function parameters
	private $_currentFuncCall = [];

	// The stack of function calls
	private $_usedFunctions = array();

	// The list of functions that are used in the class
	private $_variables = array();

	// The variables used. Array of VariableInfo.
	private $_nbFunctionParameters = 0;

	// Count the number of parameters of the current function
	private $_justAfterFuncStmt = false;

	// We are just after a control statement (last } )
	private $_justAfterControlStmt = false;

	// We are just after a function statement (last } )
	private $_functionStartLine = 0;

	// Starting line of the current function
	private $_switchStartLine = 0;

	// Starting line of the current switch statement
	private $_functionReturns = false;

	// Does the function return a value ?
	private $_functionThrows = false;

	// Does the function throw an exception ?
	private $_functionLevel = 0;

	// Level of Nesting of the function
	private $_functionVisibility = 'PUBLIC';

	// PUBLIC, PRIVATE or PROTECTED or ANONYMOUS
	private $_functionStatic = false;

	// Is the function static
	private $_classLevel = 0;

	// Level of Nesting of the class
	private $_interfaceLevel = 0;

	// Level of Nesting of the interface
	private $_constantDef = false;

	private $_currentClassname = null;

	private $_currentInterfacename = null;

	private $_currentFilename = null;

	private $_packageName = null;

	private $_currentStatement = false;

	private $_currentFunctionName = null;

	// Number of @params in the docblock of a function
	private $_docblocNbParams = 0;

	// Number of @return in the docblock of a function
	private $_docblocNbReturns = 0;

	// Number of @throw in the docblock of a function
	private $_docblocNbThrows = 0;

	// Does the function inherits its doc
	private $_docblocInheritDoc = false;

	private $_cyclomaticComplexity = 0;

	private $_npathComplexity = 0;

	private $_fileSuppressWarnings = array();

	// List of warnings to ignore for this file
	private $_classSuppressWarnings = array();

	// List of warnings to ignore for this class
	private $_interfaceSuppressWarnings = array();

	// List of warnings to ignore for this interface
	private $_functionSuppressWarnings = array();

	// List of warnings to ignore for this function

	// For MVC frameworks
	private $_isView = false;

	private $_isModel = false;

	private $_isController = false;

	private $_isClass = false;

	/**
	 * These functions are not tested for naming.
	 */
	private $_specialFunctions = array();

	/**
	 * These functions are not allowed.
	 */
	private $_prohibitedFunctions = array();

	/**
	 * These tokens are not allowed.
	 */
	private $_prohibitedTokens = array();

	/**
	 * These keywords are not allowed.
	 */
	private $_prohibitedKeywords = array();

	/**
	 * These regex patterns are not allowed.
	 */
	private $_prohibitedKeywordsRegex = array();

	/**
	 * These functions are deprecated.
	 */
	private $_deprecatedFunctions = array();

	/**
	 * These functions are aliased.
	 */
	private $_aliasedFunctions = array();

	/**
	 * These tokens are replaced.
	 */
	private $_replacements = array();

	/**
	 * System variables ($_POST, etc.) are not tested for naming.
	 */
	private $_systemVariables = array();

	// The class used to export the count of lines
	private $_lineCountReporter;

	private $_excludeList = array();

	private $_config;

	// Informations used to count lines of code
	private $_ncssTotalClasses = 0;

	private $_ncssTotalInterfaces = 0;

	private $_ncssTotalFunctions = 0;

	private $_ncssTotalLinesOfCode = 0;

	private $_ncssTotalPhpdoc = 0;

	private $_ncssTotalLinesPhpdoc = 0;

	private $_ncssTotalSingleComment = 0;

	private $_ncssTotalMultiComment = 0;

	private $_ncssFileClasses = 0;

	private $_ncssFileInterfaces = 0;

	private $_ncssFileFunctions = 0;

	private $_ncssFileLinesOfCode = 0;

	private $_ncssFilePhpdoc = 0;

	private $_ncssFileLinesPhpdoc = 0;

	private $_ncssFileSingleComment = 0;

	private $_ncssFileMultiComment = 0;

	// Whether or not the progress display is shown.
	private $_displayProgress = false;

	/**
	 * Constructor.
	 *
	 * @param String $formats
	 *        	Array of output formats ("text", "html", "console", ...)
	 *        	Accordingly creates the formatter objects
	 * @param String $outDir
	 *        	output file where results are stored.
	 *        	Note that in case of "html" format, the output is xml and run.php transforms the xml file into html
	 * @param String $configFile
	 *        	The path of the config file
	 * @param String $linecountfile
	 *        	output file where line counts are stored
	 * @param Boolean $debug
	 *        	indicate if we print so debug info
	 * @param Boolean $progress
	 *        	indicate if we log the progress of the scan
	 * @access public
	 */
	public function __construct($formats, $outDir, $configFile, $linecountfile = null, $debug = false, $progress = false, $level = INFO) {

		// Initialise the Tokenizer
		$this->tokenizer = new Tokenizer();

		// Initialise the statement stack
		$this->statementStack = new StatementStack();

		$this->debug = $debug;

		// Reporting Level
		$this->level = $level;

		// Initialise the Reporters
		$this->_reporter = new Reporters();
		if (in_array("text", $formats)) {
			$this->_reporter->addReporter(new PlainFormatReporter($outDir));
		}
		if (in_array("html", $formats)) {
			$this->_reporter->addReporter(new HTMLFormatReporter($outDir));
		}
		if (in_array("html_console", $formats)) {
			$this->_reporter->addReporter(new HTMLConsoleFormatReporter());
		}
		if (in_array("xml", $formats)) {
			$this->_reporter->addReporter(new XmlFormatReporter($outDir));
		}
		if (in_array("xml_console", $formats)) {
			$this->_reporter->addReporter(new XmlConsoleFormatReporter());
		}
		if (in_array("console", $formats)) {
			$this->_reporter->addReporter(new ConsoleReporter($this->level));
		}
		if (in_array("array", $formats)) {
			$this->_reporter->addReporter(new ArrayReporter());
		}
		if (in_array("null", $formats)) {
			$this->_reporter->addReporter(new NullReporter());
		}
		if ($linecountfile !== null) {
			$this->_lineCountReporter = new XmlNCSSReporter($outDir, $linecountfile);
		}

		// Initialize progress reporting
		$this->_displayProgress = $progress;

		// Initialise the configuration
		if (is_array($configFile)) {
			$this->_config = new CheckArrayStyleConfig($configFile);
		} else {
			$this->_config = new CheckXMLStyleConfig($configFile);
			$this->_config->parse();
		}

		// Load the list of system variables
		$this->_systemVariables = $this->_config->getConfigItems('systemVariables');
		if (!$this->_systemVariables) {
			$this->_systemVariables = array();
		}

		// Load the list of special functions
		$this->_specialFunctions = $this->_config->getConfigItems('specialFunctions');
		if (!$this->_specialFunctions) {
			$this->_specialFunctions = array();
		}

		// Load the list of forbidden functions
		$this->_prohibitedFunctions = $this->_config->getTestItems('checkProhibitedFunctions');
		if (!$this->_prohibitedFunctions) {
			$this->_prohibitedFunctions = array();
		}

		// Load the list of forbidden tokens
		$this->_prohibitedTokens = $this->_config->getTestItems('checkProhibitedTokens');
		if (!$this->_prohibitedTokens) {
			$this->_prohibitedTokens = array();
		}

		// Load the list of deprecated functions
		$this->_deprecatedFunctions = $this->_config->getTestDeprecations('checkDeprecation');
		if (!$this->_deprecatedFunctions) {
			$this->_deprecatedFunctions = array();
		}

		// Load the list of aliased functions
		$this->_aliasedFunctions = $this->_config->getTestAliases('checkAliases');
		if (!$this->_aliasedFunctions) {
			$this->_aliasedFunctions = array();
		}

		// Load the list of replacements
		$this->_replacements = $this->_config->getTestReplacements('checkReplacements');
		if (!$this->_replacements) {
			$this->_replacements = array();
		}

		// Load the list of prohibited keywords
		$this->_prohibitedKeywords = $this->_config->getTestItems('checkProhibitedKeywords');
		if (!$this->_prohibitedKeywords) {
			$this->_prohibitedKeywords = array();
		}

		// Load the list of prohibited keyword regexs
		$this->_prohibitedKeywordsRegex = $this->_config->getTestItems('checkProhibitedKeywordsRegex');
		if (!$this->_prohibitedKeywordsRegex) {
			$this->_prohibitedKeywordsRegex = array();
		}

		// Set the default language file
		$this->setLang('en-us');
	}

	/**
	 * Set the language file to use
	 *
	 * @param String $lang
	 * @return void
	 * @throws Exception
	 */
	public function setLang($lang = 'en-us') {
		$this->lang = $lang;

		try {
			$this->messages = parse_ini_file(__DIR__ . '/Lang/' . $this->lang . '.ini');
		} catch (Exception $e) {
			throw $e;
		}
	}

	/**
	 * Custom Error Handler.
	 *
	 * @param Integer $errno
	 *        	Level of the error
	 * @param String $errstr
	 *        	Error message
	 * @return boolean
	 */
	public function customErrorHandler($errno, $errstr) {
		if ($this->_isActive('phpException')) {
			$check = 'phpException';
			$level = $this->_config->getTestLevel($check);
			if ($level === null) {
				$level = WARNING;
			}
			$message = $this->_getMessage('PHP_EXPCEPTION', $errstr);

			$this->_writeError($check, $message, $this->lineNumber, $level);
		}

		/* Don't execute PHP internal error handler */
		return false;
	}

	/**
	 * Returns the errors count as an array.
	 *
	 * @return Array
	 */
	public function getErrorCounts() {
		return $this->errorCounts;
	}

	/**
	 * Accessor to the current configuration.
	 *
	 * @return CheckStyleConfig
	 */
	public function getConfig() {
		return $this->_config;
	}

	/**
	 * Calls processFile repeatedly for each PHP file that is encountered.
	 *
	 * @param Array[String] $sources
	 *        	an array php file or a directory. in case of directory, it
	 *        	searches for all the php/tpl files within the directory
	 *        	(recursively) and each of those files are processed.
	 * @param Array[String] $excludes
	 *        	an array of directories or files that need to be
	 *        	excluded from processing.
	 */
	public function processFiles($sources, $excludes = array()) {
		// Start reporting the results
		$this->_reporter->start();

		// Define the custom error handler
		set_error_handler(array(
			$this,
			'customErrorHandler'
		), E_ALL);
		set_exception_handler(array(
			$this,
			'customErrorHandler'
		));

		$this->_excludeList = $excludes;

		$files = array();

		// Iterate over the sources to list the files to process
		foreach ($sources as $src) {
			$roots = explode(",", $src);
			foreach ($roots as $root) {
				$root = trim($root);
				$files = array_merge($files, $this->_getAllPhpFiles($root, $excludes));
			}
		}

		// Start counting the lines
		if ($this->_lineCountReporter !== null) {
			$this->_lineCountReporter->start();
		}

		// Process each file
		foreach ($files as $file) {
			if (is_array($file)) {
				continue;
			}

			if ($this->_displayProgress) {
				echo "Processing File: " . $file . "<br/>" . PHP_EOL;
			}

			$this->_reporter->currentlyProcessing($file);

			try {
				$this->_processFile($file);
			} catch (Exception $e) {
				$msg = $this->_getMessage('PHP_EXPCEPTION', $e->getMessage());
				$this->_writeError('phpException', $msg);
			}
		}

		// Stop reporting the results
		$this->_reporter->stop();

		// Write the count of lines for the complete project
		if ($this->_lineCountReporter !== null) {
			$this->_lineCountReporter->writeTotalCount(count($files), $this->_ncssTotalClasses, $this->_ncssTotalInterfaces, $this->_ncssTotalFunctions, $this->_ncssTotalLinesOfCode, $this->_ncssTotalPhpdoc, $this->_ncssTotalLinesPhpdoc, $this->_ncssTotalSingleComment, $this->_ncssTotalMultiComment);
		}

		// Stop counting the lines
		if ($this->_lineCountReporter !== null) {
			$this->_lineCountReporter->stop();
		}
	}

	/**
	 * Reset the state of the different flags.
	 */
	private function _resetValues() {
		$this->lineNumber = 1;

		// Reset the current attributes
		$this->_csLeftParenthesis = 0;
		$this->_fcLeftParenthesis = 0;
		$this->inDoWhile = false;

		$this->statementStack = new StatementStack();

		$this->_inString = false;
		$this->_inControlStatement = false;
		$this->_inFunctionStatement = false;
		$this->_beforeArrayDeclaration = false;
		$this->_inFunction = false;
		$this->_privateFunctions = array();
		$this->_usedFunctions = array();
		$this->_variables = array();
		$this->_privateFunctionsStartLines = array();
		$this->_inFuncCall = false;
		$this->_nbFunctionParameters = 0;
		$this->_justAfterFuncStmt = false;
		$this->_justAfterControlStmt = false;
		$this->_functionStartLine = 0;
		$this->_functionReturns = false;
		$this->_functionThrows = false;
		$this->_functionVisibility = 'PUBLIC';
		$this->_functionStatic = false;
		$this->_currentStatement = false;
		$this->_inClassStatement = false;
		$this->_inInterfaceStatement = false;

		$this->__constantDef = false;

		$this->_ncssFileClasses = 0;
		$this->_ncssFileInterfaces = 0;
		$this->_ncssFileFunctions = 0;
		$this->_ncssFileLinesOfCode = 0;
		$this->_ncssFilePhpdoc = 0;
		$this->_ncssFileLinesPhpdoc = 0;
		$this->_ncssFileSingleComment = 0;
		$this->_ncssFileMultiComment = 0;

		$this->_currentFunctionName = null;
		$this->_currentClassname = null;
		$this->_currentInterfacename = null;
		$this->_currentFilename = null;
		$this->_packageName = null;

		$this->_docblocNbParams = 0;
		$this->_docblocNbReturns = 0;
		$this->_docblocNbThrows = 0;
		$this->_docblocInheritDoc = false;

		$this->_isView = false;
		$this->_isModel = false;
		$this->_isController = false;
		$this->_isClass = false;

		$this->_isLineStart = true;
	}

	/**
	 * Process one php file.
	 *
	 * @param String $filename
	 *        	the file name
	 */
	private function _processFile($filename) {
		if ($this->debug) {
			echo "Processing File : " . $filename . PHP_EOL;
		}

		// Reset the tokenizer
		$this->tokenizer->reset();

		// Reset the state of the attributes
		$this->_resetValues();

		// Try to detect the type of file in a MVC framework
		if (stripos($filename, 'view') !== false || stripos($filename, 'layouts') !== false) {
			$this->_isView = true;
		} elseif (stripos($filename, 'model') !== false) {
			$this->_isModel = true;
		} elseif (stripos($filename, 'controller') !== false) {
			$this->_isController = true;
		} elseif (stripos($filename, 'class') !== false) {
			// simple simple data objects
			$this->_isClass = true;
		}

		// Store the file name
		$this->_currentFilename = $filename;

		// By defaut the package name is based on the file name
		$this->_packageName = $this->_extractPackageName($filename);

		// Tokenize the file
		$this->tokenizer->tokenize($filename);

		// Empty PHP file
		if ($this->tokenizer->getTokenNumber() === 0) {
			$this->_checkEmptyFile($filename);
			return; // end the scan
		}

		// Go to the first token
		$token = $this->tokenizer->getCurrentToken();

		// File start
		$this->_processFileStart();

		// Run through every token of the file
		while ($token !== false) {
			// Process the token
			$this->_processToken($token);

			$this->lineNumber = $token->line;

			// Go to the next token
			$token = $this->tokenizer->getNextToken();
		}

		// Test the last token of the file
		if ($this->_isActive('noFileCloseTag')) {
			if ($this->tokenizer->checkPreviousToken(T_CLOSE_TAG)) {
				// Closing tag is not recommended since PHP 5.0
				$this->_writeError('noFileCloseTag', $this->_getMessage('END_FILE_CLOSE_TAG'));
			}
		}

		// Inner HTML is OK for views but not for other classes (controllers, models, ...)
		if ($this->_isActive('noFileFinishHTML') && !$this->_isView) {
			if ($this->tokenizer->checkPreviousToken(T_INLINE_HTML)) {
				$this->_writeError('noFileFinishHTML', $this->_getMessage('END_FILE_INLINE_HTML'));
			}
		}

		// Check for unused private functions
		$this->_checkUnusedPrivateFunctions();

		// Check for unused variables
		$this->_checkUnusedVariables();

		if ($this->_ncssFileClasses > 0 || $this->_ncssFileInterfaces > 0) {
			// Test the file name, only if it contains a class or interface
			$this->_checkFileNaming();
		}

		// Write the count of lines for this file
		if ($this->_lineCountReporter !== null) {
			$this->_lineCountReporter->writeFileCount($this->_packageName, $this->_ncssFileClasses, $this->_ncssFileInterfaces, $this->_ncssFileFunctions, $this->_ncssFileLinesOfCode, $this->_ncssFilePhpdoc, $this->_ncssFileLinesPhpdoc, $this->_ncssFileSingleComment, $this->_ncssFileMultiComment);
		}

		// Reset the suppression warnings
		$this->_fileSuppressWarnings = array();
		$this->_classSuppressWarnings = array();
		$this->_interfaceSuppressWarnings = array();
	}

	/**
	 * Process the start of a file.
	 */
	private function _processFileStart() {
		// Check for the presence of a mandatory header
		$this->_checkMandatoryHeader();
	}

	/**
	 * Go through a directory recursively and get all the
	 * PHP files.
	 * Ignores files or subdirectories that are in the _excludeList
	 *
	 * @param String $src
	 *        	source directory
	 * @param Array[String] $excludes
	 *        	paths to exclude
	 * @param String $dir
	 *        	the base directory
	 * @return Array[String] an array of php files
	 */
	private function _getAllPhpFiles($src, $excludes, $dir = '') {
		$files = array();
		if (!is_dir($src)) {

			// Source is a file
			$isExcluded = false;
			foreach ($excludes as $patternExcluded) {
				if (strstr($src, $patternExcluded)) {
					$isExcluded = true;
				}
			}
			if (!$isExcluded) {
				$files[] = $src;
			}
		} else {
			// Source is a directory
			$root = opendir($src);
			if ($root) {
				while ($file = readdir($root)) {
					if ($this->_inArray($file, $this->ignoredFiles)) {
						continue;
					}

					$fullPath = $src . "/" . $file;
					$isExcluded = false;
					foreach ($excludes as $patternExcluded) {
						if (strstr($fullPath, $patternExcluded)) {
							$isExcluded = true;
						}
					}

					if (!$isExcluded) {
						if (is_dir($src . "/" . $file)) {
							$filesToMerge = $this->_getAllPhpFiles($src . "/" . $file, $excludes, $dir . '/' . $file);
							$files = array_merge($files, $filesToMerge);
						} else {
							$pathParts = pathinfo($file);
							if (array_key_exists('extension', $pathParts)) {
								if (in_array($pathParts['extension'], $this->validExtensions)) {
									$files[] = realpath($src . "/" . $file);
								}
							}
						}
					}
				}
			}
		}

		return $files;
	}

	/**
	 * Check for the presence of a prohibited token.
	 *
	 * @param TokenInfo $token
	 *        	the token to check
	 */
	private function _checkProhibitedTokens($token) {
		if ($this->_isActive('checkProhibitedTokens')) {
			if (in_array($token->getName(), $this->_prohibitedTokens)) {
				$msg = $this->_getMessage("PROHIBITED_TOKEN", $token->getName());
				$this->_writeError('checkProhibitedTokens', $msg);
			}
		}
	}

	/**
	 * Check for the presence of a prohibited keyword (the text of the token).
	 *
	 * @param TokenInfo $token
	 *        	the token to check
	 */
	private function _checkProhibitedKeywords($token) {
		if ($this->_isActive('checkProhibitedKeywords')) {
			foreach ($this->_prohibitedKeywords as $keyword) {
				if (strpos($token->text, $keyword) !== FALSE) {
					$msg = $this->_getMessage('PROHIBITED_KEYWORD', $token->text);
					$this->_writeError('checkProhibitedKeywords', $msg);
				}
			}
		}
	}

	/**
	 * Check for the presence of a text corresponding to a prohibited regexp.
	 *
	 * @param TokenInfo $token
	 *        	the token to check
	 */
	private function _checkProhibitedKeywordsRegex($token) {
		if ($this->_isActive('checkProhibitedKeywordsRegex')) {

			foreach ($this->_prohibitedKeywordsRegex as $pattern) {

				preg_match_all($pattern, $token->text, $matches);
				$matches = $matches[0];
				if (!empty($matches)) {
					foreach ($matches as $key => $value) {
						$msg = $this->_getMessage('PROHIBITED_KEYWORD_REGEX', $value);
						$this->_writeError('checkProhibitedKeywordsRegex', $msg);
					}
				}
			}
		}
	}

	/**
	 * Process a token.
	 *
	 * @see http://www.php.net/manual/en/tokens.php
	 *
	 * @param TokenInfo $token
	 *        	@SuppressWarnings functionLength cyclomaticComplexity
	 */
	private function _processToken($token) {
		// Debug
		if ($this->debug) {
			echo $this->statementStack->getStackDump() . PHP_EOL;
			echo "Level " . $this->statementStack->count() . " - " . $token->toString() . PHP_EOL;
		}

		// Check if the token is in the list of prohibited tokens
		$this->_checkProhibitedTokens($token);

		// Check if the token text is in the list of prohibited keywords
		$this->_checkProhibitedKeywords($token);

		// Check if the token text correspond to a prohibited regular expression
		$this->_checkProhibitedKeywordsRegex($token);

		// Specific tokens checks
		switch ($token->id) {

			case T_COMMENT:
			case T_ML_COMMENT:
			case T_DOC_COMMENT:
				$this->_processComment($token);
				break;

			case T_OPEN_TAG:
				$this->_processOpenTag($token);
				break;

			case T_CLOSE_TAG:
				$this->_processCloseTag($token);
				break;

			case T_DO:
			case T_WHILE:
			case T_IF:
			case T_ELSEIF:
			case T_FOR:
			case T_FOREACH:
				$this->_processControlStatement($token);
				$this->_cyclomaticComplexity ++;
				$this->_npathComplexity ++;
				break;

			case T_SWITCH:
				$this->_processSwitchStart();
				$this->_processControlStatement($token);
				$this->_cyclomaticComplexity ++;
				$this->_npathComplexity ++;
				break;

			case T_ELSE:

				// We don't increment the cyclomatic complexity for the last else
				$this->_processControlStatement($token);
				$this->_npathComplexity ++;
				break;

			case T_CASE:
				$this->_processSwitchCase();
				$this->_cyclomaticComplexity ++;
				$this->_npathComplexity ++;
				break;

			case T_DEFAULT:
				$this->_processSwitchDefault();
				$this->_npathComplexity ++;
				break;

			case T_BREAK:
				$this->_processSwitchBreak();
				break;

			case T_TRY:
				$this->_processControlStatement($token);
				$this->_npathComplexity ++;
				break;

			case T_CATCH:
				$this->_processCatch();
				$this->_processControlStatement($token);
				$this->_npathComplexity ++;
				break;

			case T_FINALLY:
				$this->_processFinally();
				$this->_processControlStatement($token);
				$this->_npathComplexity ++;
				break;

			case T_WHITESPACE:
			case T_TAB:

				if ($this->_isLineStart) {
					// If the whitespace is at the start of the line, we check for indentation
					$this->_checkIndentation($token);
				}
				break;

			case T_INLINE_HTML:
				break;

			// beginning of a function definition
			// check also for existence of docblock
			case T_FUNCTION:
				$this->_processFunctionStatement();
				break;

			// beginning of a class
			// check also for the existence of a docblock
			case T_CLASS:
				$this->_processClassStatement();
				break;

			// beginning of an interface
			// check also for the existence of a docblock
			case T_INTERFACE:
				$this->_processInterfaceStatement();
				break;

			// Namespace declaration
			case T_NAMESPACE:
				$this->_processNamespace();
				break;

			// operators, generally, need to be surrounded by whitespace
			case T_PLUS_EQUAL:
			case T_MINUS_EQUAL:
			case T_MUL_EQUAL:
			case T_DIV_EQUAL:
			case T_CONCAT_EQUAL:
			case T_MOD_EQUAL:
			case T_AND_EQUAL:
			case T_OR_EQUAL:
			case T_XOR_EQUAL:
			case T_SL_EQUAL:
			case T_SR_EQUAL:
			case T_BOOLEAN_OR:
			case T_BOOLEAN_AND:
			case T_IS_IDENTICAL:
			case T_IS_NOT_IDENTICAL:
			case T_IS_SMALLER_OR_EQUAL:
			case T_IS_GREATER_OR_EQUAL:
			case T_IS_GREATER:
			case T_IS_SMALLER:
			case T_PLUS:
			case T_MULTIPLY:
			case T_DIVIDE:
			case T_QUESTION_MARK:
			case T_COLON:
			case T_MODULO:
			case T_CONCAT:
				$this->_checkWhiteSpaceBefore($token->text);
				$this->_checkWhiteSpaceAfter($token->text);
				break;

			case T_IS_EQUAL:
			case T_IS_NOT_EQUAL:
				$this->_checkStrictCompare($token->text);
				$this->_checkWhiteSpaceBefore($token->text);
				$this->_checkWhiteSpaceAfter($token->text);
				break;

			case T_LOGICAL_AND:
				if ($this->_isActive('useBooleanOperators')) {
					$this->_writeError('useBooleanOperators', $this->_getMessage("USE_BOOLEAN_OPERATORS_AND"));
				}
				$this->_checkWhiteSpaceBefore($token->text);
				$this->_checkWhiteSpaceAfter($token->text);
				break;

			case T_LOGICAL_OR:
				if ($this->_isActive('useBooleanOperators')) {
					$this->_writeError('useBooleanOperators', $this->_getMessage("USE_BOOLEAN_OPERATORS_OR"));
				}
				$this->_checkWhiteSpaceBefore($token->text);
				$this->_checkWhiteSpaceAfter($token->text);
				break;

			// ASSUMPTION:
			// that T_STRING followed by "(" is a function call
			// Actually, I am not sure how good an assumption this is.
			case T_STRING:

				// If the word "define" have been used right before the string
				if ($this->_constantDef === true) {
					$this->_checkConstantNaming($token->text);
				}

				// Check whether this is a function call (and if "define", set the flag)
				$this->_processFunctionCall($token->text);

				break;

			case T_CONST:

				// Skip until T_STRING representing the constant name
				while (!$this->tokenizer->checkCurrentToken(T_STRING)) {
					$this->tokenizer->getNextToken();
				}
				$this->_constantDef = true;
				$token = $this->tokenizer->getCurrentToken();
				$this->_checkConstantNaming($token->text);
				break;

			case T_CONSTANT_ENCAPSED_STRING:

				// If the word "define" have been used right before the constant encapsed string
				if ($this->_constantDef === true) {
					$this->_checkConstantNaming($token->text);
				}
				break;

			case T_ENCAPSED_AND_WHITESPACE:

				// Constant part of string with variables
				$this->_checkEncapsedVariablesInsideString();
				break;

			case T_CURLY_OPEN: // for protected variables within strings "{$var}"
				$stackitem = new StatementItem();
				$stackitem->line = $token->line;
				$stackitem->type = 'curly_open';
				$stackitem->name = 'curly_open';
				$this->statementStack->push($stackitem);
				break;

			case T_DOLLAR_OPEN_CURLY_BRACES: // for extended format "${var}"
				$stackitem = new StatementItem();
				$stackitem->line = $token->line;
				$stackitem->type = 'dollar_curly_open';
				$stackitem->name = 'dollar_curly_open';
				$this->statementStack->push($stackitem);
				break;

			case T_NEW_LINE:
				$this->_countLinesOfCode();
				// Case of a control statement without parenthesis, it closes at the end of the line
				if ($this->_inControlStatement && $this->_csLeftParenthesis === 0) {
					$this->_inControlStatement = false;
				}
				// Test the length of the line, only if it's not html.
				if ($this->_isActive('lineLength')) {
					$this->_checkLargeLine();
				}
				break;

			case T_RETURN:
				$this->_processReturn();
				break;

			case T_THROW:
				$this->_processThrow();
				break;

			case T_INC:
			case T_DEC:
				$this->_checkUnaryOperator();
				break;

			case T_DOUBLE_ARROW:
				$this->_checkWhiteSpaceBefore($token->text);
				$this->_checkWhiteSpaceAfter($token);
				break;

			case T_OBJECT_OPERATOR:
				$this->_checkNoWhiteSpaceBefore($token->text);
				$this->_checkNoWhiteSpaceAfter($token);
				break;

			case T_START_HEREDOC:
				$this->_processStartHeredoc();
				break;

			case T_END_HEREDOC:
				$this->_processEndHeredoc();
				break;

			case T_VARIABLE:
				$this->_processVariable($token->text);
				break;

			case T_GOTO:
				$this->_checkGoTo();
				break;

			case T_CONTINUE:
				$this->_checkContinue();
				break;

			case T_EXIT: // exit() of die()
				$this->_checkAliases($token->text);
				break;

			case T_BRACES_OPEN: // {
				$this->_processBracesOpen($token);
				break;

			case T_BRACES_CLOSE: // }
				$this->_processBracesClose($token);
				break;

			case T_SEMICOLON: // ;
				$this->_processSemiColon($token);
				break;

			case T_MINUS:
				$this->_processMinus($token);
				break;

			case T_EQUAL:
				$this->_checkInnerAssignment();
				$this->_checkWhiteSpaceBefore($token->text);
				// whitespace after (exept for Assign By Reference)
				if (!$this->tokenizer->checkNextToken(T_AMPERSAND)) {
					$this->_checkWhiteSpaceAfter($token->text);
				}
				break;

			case T_COMMA:
				$this->_checkNoWhiteSpaceBefore($token->text);
				$this->_checkWhiteSpaceAfter($token->text);
				break;

			case T_EXCLAMATION_MARK:
				$this->_checkNoWhiteSpaceAfter($token);
				break;

			case T_PARENTHESIS_OPEN:
				$this->_processParenthesisOpen($token);
				$this->_checkNoWhiteSpaceAfter($token);
				break;

			case T_PARENTHESIS_CLOSE:
				$this->_processParenthesisClose($token);
				$this->_checkNoWhiteSpaceBefore($token->text);
				break;

			case T_AMPERSAND:

				// One of the function parameter is passed by reference
				if ($this->_isActive('avoidPassingReferences')) {
					if ($this->_inFunctionStatement) {
						$this->_writeError('avoidPassingReferences', $this->_getMessage("PASSING_REFERENCE"));
					}
				}
				break;
			case T_SQUARE_BRACKET_OPEN:
				$this->_processSquareBracketOpen($token);
				break;
			case T_SQUARE_BRACKET_CLOSE:
				$this->_processSquareBracketClose($token);
				break;
			case T_QUOTE:
				$this->_checkPreferQuotes($token);
				$this->_inString = !$this->_inString;
				break;
			case T_DOLLAR:
				$this->_checkVariableVariable($token);
				break;
			case T_ARRAY:
				$this->_processArray($token);
				break;
			default:
				break;
		}

		// If the last token is a NEW_LINE, the next token will be at the start of the line
		$this->_isLineStart = ($token->id === T_NEW_LINE);
	}

	/**
	 * Launched when a [ is encountered.
	 *
	 * @param TokenInfo $token
	 *        	the current token
	 */
	private function _processSquareBracketOpen($token) {

		// If the [ is preceded by = we are in a array declaration
		if ($this->tokenizer->checkPreviousValidToken(T_EQUAL)) {

			$stackitem = new StatementItem();
			$stackitem->line = $token->line;
			$stackitem->type = 'ARRAY';
			$stackitem->name = 'square_bracket_open';

			$this->statementStack->push($stackitem);
		}
	}

	/**
	 * Launched when a ] is encountered.
	 *
	 * @param TokenInfo $token
	 *        	the current token
	 */
	private function _processSquareBracketClose($token) {

		// We are in a array declaration, we unstack
		if ($this->statementStack->getCurrentStackItem()->type === StatementItem::TYPE_ARRAY && $this->statementStack->getCurrentStackItem()->name === 'square_bracket_open') {
			$this->statementStack->pop();
		}
	}

	/**
	 * Launched when a array token is encountered.
	 *
	 * @param TokenInfo $token
	 *        	the current token
	 */
	private function _processArray($token) {

		// If the next token is a parenthesis then we are in a array declaration
		if ($this->tokenizer->checkNextValidToken(T_PARENTHESIS_OPEN)) {

			// flag the array declaration, for use when we encounter the T_PARENTHESIS_OPEN
			$this->_beforeArrayDeclaration = true;
		}
	}

	/**
	 * Launched when a ( sign is encountered.
	 *
	 * @param TokenInfo $token
	 *        	the current token
	 */
	private function _processParenthesisOpen($token) {
		if ($this->_inFuncCall) {
			// inside a function call
			$this->_fcLeftParenthesis += 1;
		} elseif ($this->_inControlStatement || $this->_inFunctionStatement) {
			// inside a function or control statement
			$this->_csLeftParenthesis += 1;
		}

		// We are in a array declaration
		if ($this->_beforeArrayDeclaration) {
			$stackitem = new StatementItem();
			$stackitem->line = $token->line;
			$stackitem->type = 'ARRAY';
			$stackitem->name = 'ARRAY';
			$this->statementStack->push($stackitem);

			$this->_beforeArrayDeclaration = false;
		}

		$this->statementStack->getCurrentStackItem()->openParentheses += 1;
	}

	/**
	 * Launched when a ) sign is encountered.
	 *
	 * @param TokenInfo $token
	 *        	the current token
	 */
	private function _processParenthesisClose($token) {
		$this->statementStack->getCurrentStackItem()->openParentheses -= 1;

		// Decrease the number of opened brackets
		if ($this->_inFuncCall) {
			$this->_fcLeftParenthesis -= 1;
		} elseif ($this->_inControlStatement || $this->_inFunctionStatement) {
			$this->_csLeftParenthesis -= 1;
		}
		// If 0 we are not in the call anymore
		if ($this->_fcLeftParenthesis === 0) {
			$this->_inFuncCall = false;
			array_pop($this->_currentFuncCall);
		}
		// If 0 we are not in the statement anymore
		if ($this->_csLeftParenthesis === 0) {
			if ($this->_inControlStatement) {
				$this->_inControlStatement = false;
				$this->_justAfterControlStmt = true;
				$this->_checkNeedBraces();
			} elseif ($this->_inFunctionStatement && !$this->_inInterface) {
				$this->_inFunctionStatement = false;
				$this->_justAfterFuncStmt = true;
			}
		}

		// We the count arrive to 0 we probably have something to do
		if ($this->statementStack->getCurrentStackItem()->openParentheses === 0) {

			// We are in a array declaration, we unstack
			if ($this->statementStack->getCurrentStackItem()->type === StatementItem::TYPE_ARRAY and $this->statementStack->getCurrentStackItem()->name !== 'square_bracket_open') {
				$this->statementStack->pop();
			}
		}
	}

	/**
	 * Launched when a minus sign is encountered.
	 *
	 * @param TokenInfo $token
	 *        	the current token
	 */
	private function _processMinus($token) {
		if (!$this->_inFuncCall) {
			$this->_checkWhiteSpaceBefore($token->text);
		}
		// We allow some '-' signs to skip the the space afterwards for negative numbers
		// float number or integer
		if (!($this->tokenizer->checkNextToken(T_LNUMBER) || $this->tokenizer->checkNextToken(T_DNUMBER))) {
			$this->_checkWhiteSpaceAfter($token->text);
		}
	}

	/**
	 * Launched when a semicolon is encountered.
	 *
	 * @param TokenInfo $token
	 *        	the current token
	 */
	private function _processSemiColon($token) {
		// ";" should never be preceded by a whitespace
		$this->_checkNoWhiteSpaceBefore($token->text);

		// ";" should never be preceded by ;
		$this->_checkEmptyStatement();

		// If we are in a statement not surrounded by curly braces, we unstack the last line.
		if ($this->statementStack->getCurrentStackItem()->noCurly === true) {
			$this->statementStack->pop();
		}
	}

	/**
	 * Launched when an opening brace is encountered.
	 *
	 * @param TokenInfo $token
	 *        	the current token
	 */
	private function _processBracesOpen($token) {
		// "{" signifies beginning of a block. We need to look for
		// its position when it is a beginning of a control structure
		// or a function or class definition.

		// Check we have a white space before a curly opening in case of a "same line" indentation
		if ($this->_config->getTestProperty('funcDefinitionOpenCurly', 'position') === SAME_LINE) {
			$this->_checkWhiteSpaceBefore($token->text);
		}
		$stackitem = new StatementItem();
		$stackitem->line = $token->line;

		// if _justAfterFuncStmt is set, the "{" is the beginning of a function definition block
		if ($this->_justAfterFuncStmt) {
			$this->_processFunctionStart();
			$stackitem->type = "FUNCTION";
			$stackitem->name = $this->_currentFunctionName;
			$stackitem->visibility = $this->_functionVisibility;
		} else if ($this->_justAfterControlStmt) {
			// if _justAfterControlStmt is set, the "{" is the beginning of a control stratement block
			$this->_processControlStatementStart();
			$stackitem->type = strtoupper($this->_currentStatement);
		} else if ($this->_inClassStatement) {
			// if _inClassStatement is set then we are just after a class declaration
			$this->_inClassStatement = false;
			$this->_processClassStart();
			$stackitem->type = "CLASS";
			$stackitem->name = $this->_currentClassname;
		} else if ($this->_inInterfaceStatement) {
			// if _inInterfaceStatement is set then we are just after a Interface declaration
			$this->_inInterfaceStatement = false;
			$this->_processInterfaceStart();
			$stackitem->type = "INTERFACE";
			$stackitem->name = $this->_currentInterfacename;
		} else {
			$stackitem->type = "{";
		}

		// Check if the block is not empty
		$this->_checkEmptyBlock();

		$this->statementStack->push($stackitem);
	}

	/**
	 * Launched when a closing brace is encountered.
	 *
	 * @param TokenInfo $token
	 *        	the current token
	 */
	private function _processBracesClose($token) {

		// signifies the end of a block
		// currently tests whether this token resides on a new line.
		// This test is deactivated when in a view
		if ($this->_isActive('controlCloseCurly') && !($this->_isView) && !($this->_inString)) {
			$previousToken = $this->tokenizer->peekPrvsValidToken();
			if (($previousToken->line === $token->line) && ($previousToken->id !== T_BRACES_OPEN)) {
				// the last token was on the same line
				$this->_writeError('controlCloseCurly', $this->_getMessage("END_BLOCK_NEW_LINE"));
			}
		}

		$currentStackItem = $this->statementStack->getCurrentStackItem();

		// Workaround code
		if (!is_String($currentStackItem)) {

			// Test for the end of a switch bloc
			if ($currentStackItem->type === StatementItem::TYPE_SWITCH || $currentStackItem->type === StatementItem::TYPE_DEFAULT || $currentStackItem->type === StatementItem::TYPE_CASE) {
				$this->_processSwitchStop();
			}

			// Test for the end of a function
			if ($currentStackItem->type === StatementItem::TYPE_FUNCTION) {
				$this->_processFunctionStop();
			}

			// Test for the end of a class
			if ($currentStackItem->type === StatementItem::TYPE_CLASS) {
				$this->_processClassStop();
			}

			// Test for the end of an interface
			if ($currentStackItem->type === StatementItem::TYPE_INTERFACE) {
				$this->_processInterfaceStop();
			}
		}
		$this->statementStack->pop();

		// Particular case of a ELSE IF {}
		// We unstack both the IF and the ELSE
		$isElse = ($currentStackItem->type === StatementItem::TYPE_ELSE);
		$isIf = ($this->statementStack->getCurrentStackItem()->type === StatementItem::TYPE_IF);
		$isNoCurly = $this->statementStack->getCurrentStackItem()->noCurly;
		if ($isElse && $isIf && $isNoCurly) {
			$this->statementStack->pop();
		}
	}

	/**
	 * Check if the current line if a line of code and if it's the case increment the count.
	 *
	 * This function is called when we meet a T_NEW_LINE token.
	 */
	private function _countLinesOfCode() {
		// We get the previous token (T_WHITESPACE, T_COMMENT, ... ignored);
		$currentToken = $this->tokenizer->getCurrentToken();
		$previousToken = $this->tokenizer->peekPrvsValidToken();

		// If the previous token is not the new line (empty line), we suppose we have some code
		if ($previousToken !== null) {
			if (!$this->tokenizer->checkToken($previousToken, T_NEW_LINE) && ($previousToken->line === $currentToken->line)) {
				$this->_ncssTotalLinesOfCode ++;
				$this->_ncssFileLinesOfCode ++;
			}
		}
	}

	/**
	 * Checks to see if the constant follows the naming convention.
	 *
	 * @param String $text
	 *        	the string containing the constant.
	 */
	private function _checkConstantNaming($text) {
		if ($this->_isActive('constantNaming')) {
			$text = ltrim($text, "\"'"); // just in case, remove the quotes
			$text = rtrim($text, "\"'");
			$ret = preg_match($this->_config->getTestRegExp('constantNaming'), $text);
			if (!$ret) {
				$msg = $this->_getMessage('CONSTANT_NAMING', $text, $this->_config->getTestRegExp('constantNaming'));
				$this->_writeError('constantNaming', $msg);
			}
		}
		$this->_constantDef = false;
	}

	/**
	 * Checks to see if the variable follows the naming convention.
	 *
	 * @param String $text
	 *        	the string containing the variable.
	 */
	private function _checkVariableNaming($text) {
		if ($this->_inClass || $this->_inInterface) {
			if ($this->_inFunctionStatement || $this->_inInterfaceStatement) {
				$this->_checkScopedVariableNaming($text, 'functionParameterNaming', 'FUNCTION_PARAMETER_NAMING');
			} else if ($this->_inFunction) {
				if (array_key_exists($text, $this->_functionParameters)) {
					$this->_checkScopedVariableNaming($text, 'functionParameterNaming', 'FUNCTION_PARAMETER_NAMING');
				} else {
					$this->_checkScopedVariableNaming($text, 'localVariableNaming', 'LOCAL_VARIABLE_NAMING');
				}
			} else {
				$this->_checkScopedVariableNaming($text, 'memberVariableNaming', 'MEMBER_VARIABLE_NAMING');
			}
		} else {
			$this->_checkScopedVariableNaming($text, 'topLevelVariableNaming', 'TOPLEVEL_VARIABLE_NAMING');
		}
	}

	/**
	 * Utility function to check the naming of a variable
	 * given its scope rule and message.
	 *
	 * @param String $text
	 *        	the string containing the variable.
	 * @param String $ruleName
	 *        	the rule for the scope of the variable.
	 * @param String $msgName
	 *        	the message associated with the rule.
	 */
	private function _checkScopedVariableNaming($variableText, $ruleName, $msgName) {
		if ($this->_isActive($ruleName) || $this->_isActive('variableNaming')) {
			$texttoTest = ltrim($variableText, "\"'"); // remove the quotes
			$texttoTest = rtrim($texttoTest, "\"'");
			if (strpos($texttoTest, "$") === 0) {
				// remove the "&"
				$texttoTest = substr($texttoTest, 1);
			}
			// If the variable is not listed as an exception
			if (!$this->_config->isException($ruleName, $texttoTest)) {

				if ($this->_isActive($ruleName)) {
					// Scoped variable
					$ret = preg_match($this->_config->getTestRegExp($ruleName), $texttoTest);
				} else {
					// Default case
					$ret = preg_match($this->_config->getTestRegExp('variableNaming'), $texttoTest);
				}
				if (!$ret) {
					if ($this->_isActive($ruleName)) {
						$msg = $this->_getMessage($msgName, $variableText, $this->_config->getTestRegExp($ruleName));
					} else {
						$msg = $this->_getMessage('VARIABLE_NAMING', $variableText, $this->_config->getTestRegExp('variableNaming'));
					}
					$this->_writeError($ruleName, $msg);
				}
			}
		}
	}

	/**
	 * Check the naming of a function.
	 *
	 * @param String $text
	 *        	the name of the function.
	 */
	private function _checkFunctionNaming($text) {
		if ($this->_isActive('functionNaming')) {

			$ret = preg_match($this->_config->getTestRegExp('functionNaming'), $text);
			if (!$ret) {
				$msg = $this->_getMessage('FUNCNAME_NAMING', $text, $this->_config->getTestRegExp('functionNaming'));
				$this->_writeError('functionNaming', $msg);
			}
		}
	}

	/**
	 * Check the naming of a private function.
	 *
	 * @param String $text
	 *        	the name of the function.
	 */
	private function _checkPrivateFunctionNaming($text) {
		if ($this->_isActive('privateFunctionNaming')) {
			$ret = preg_match($this->_config->getTestRegExp('privateFunctionNaming'), $text);
			if (!$ret) {
				$msg = $this->_getMessage('PRIVATE_FUNCNAME_NAMING', $text, $this->_config->getTestRegExp('privateFunctionNaming'));
				$this->_writeError('privateFunctionNaming', $msg);
			}
		}
	}

	/**
	 * Check the naming of a protected function.
	 *
	 * @param String $text
	 *        	the name of the function.
	 */
	private function _checkProtectedFunctionNaming($text) {
		if ($this->_isActive('protectedFunctionNaming')) {
			$ret = preg_match($this->_config->getTestRegExp('protectedFunctionNaming'), $text);
			if (!$ret) {
				$msg = $this->_getMessage('PROTECTED_FUNCNAME_NAMING', $text, $this->_config->getTestRegExp('protectedFunctionNaming'));
				$this->_writeError('protectedFunctionNaming', $msg);
			}
		}
	}

	/**
	 * Check the naming of a class.
	 *
	 * @param String $text
	 *        	the name of the class.
	 */
	private function _checkClassNaming($text) {
		if ($this->_isActive('classNaming')) {
			$ret = preg_match($this->_config->getTestRegExp('classNaming'), $text);
			if (!$ret) {
				$msg = $this->_getMessage('CLASSNAME_NAMING', $text, $this->_config->getTestRegExp('classNaming'));
				$this->_writeError('classNaming', $msg);
			}
		}
	}

	/**
	 * Check the naming of an interface.
	 *
	 * @param String $text
	 *        	the name of the interface.
	 */
	private function _checkInterfaceNaming($text) {
		if ($this->_isActive('interfaceNaming')) {
			$ret = preg_match($this->_config->getTestRegExp('interfaceNaming'), $text);
			if (!$ret) {
				$msg = $this->_getMessage('INTERFACENAME_NAMING', $text, $this->_config->getTestRegExp('interfaceNaming'));
				$this->_writeError('interfaceNaming', $msg);
			}
		}
	}

	/**
	 * Check the naming of a file.
	 */
	private function _checkFileNaming() {
		if ($this->_isActive('fileNaming')) {
			$fileBaseName = basename($this->_currentFilename);

			$ret = preg_match($this->_config->getTestRegExp('fileNaming'), $fileBaseName);
			if (!$ret) {
				$msg = $this->_getMessage('FILENAME_NAMING', $fileBaseName, $this->_config->getTestRegExp('fileNaming'));
				$this->_writeError('fileNaming', $msg);
			}
		}
	}

	/**
	 * Check that the type name matches the file name.
	 *
	 * @param String $typeName
	 *        	the name of the type.
	 */
	private function _checkTypeNameFileNameMatch($typeName) {
		/* currentFilename holds file path - get basename */
		$fileBaseName = basename($this->_currentFilename);
		/*
		 * quick n dirty - append '.' to type name to ensure that we don't miss something like Foo1.php and Foo
		 */
		if ($this->_isActive('typeNameMatchesFileName') && !(substr($fileBaseName, 0, strlen($typeName) + 1) === $typeName . ".")) {
			$msg = $this->_getMessage('TYPE_FILE_NAME_MISMATCH', $typeName, $fileBaseName);
			$this->_writeError('typeNameMatchesFileName', $msg);
		}
	}

	/**
	 * Check the validity of a function call.
	 *
	 * @param String $text
	 *        	the name of the function.
	 */
	private function _processFunctionCall($text) {
		if (strtolower($text) === "define") {
			$this->_constantDef = true;
		}

		if ($this->tokenizer->checkNextValidToken(T_PARENTHESIS_OPEN)) {
			// ASSUMPTION:that T_STRING followed by "(" is a function call
			$this->_inFuncCall = true;

			array_push($this->_currentFuncCall, $text);

			// Add the function name to the list of used functions
			$this->_usedFunctions[$text] = $text;

			// Check if the function call is made on an object of if it's a base PHP function.
			$isObjectCall = $this->tokenizer->checkPreviousToken(T_OBJECT_OPERATOR);

			if (!$isObjectCall) {

				// Detect prohibited functions
				$this->_checkProhibitedFunctions($text);

				// Detect deprecated functions
				$this->_checkDeprecation($text);

				// Detect aliased functions
				$this->_checkAliases($text);

				// Detect replaced functions
				$this->_checkReplacements($text);
			}

			// Detect an @ before the function call
			$this->_checkSilenced($text);

			// Detect space after function name
			if ($this->_isActive('noSpaceAfterFunctionName')) {
				if (!$this->tokenizer->checkNextToken(T_PARENTHESIS_OPEN)) {
					$msg = $this->_getMessage('NO_SPACE_AFTER_TOKEN', $text);
					$this->_writeError('noSpaceAfterFunctionName', $msg);
				}
			}
		}

		// Optimisation : Avoid using count/sizeof inside a loop
		if ($this->_isActive('functionInsideLoop')) {
			if ((strtolower($text) === 'count' || strtolower($text) === 'sizeof') && $this->_inControlStatement) {
				$loops = array(
					'do',
					'while',
					'for',
					'foreach'
				);
				if (in_array($this->_currentStatement, $loops)) {
					$msg = $this->_getMessage('FUNCTION_INSIDE_LOOP', strtolower($text));
					$this->_writeError('functionInsideLoop', $msg);
				}
			}
		}
	}

	/**
	 * Process a control statement declaration (if/do/while/for/...).
	 *
	 * @param TokenInfo $token
	 *        	the control statement.
	 */
	private function _processControlStatement($token) {
		$csText = strtolower($token->text);

		$this->_inControlStatement = true;
		$this->_currentStatement = $csText;

		// first token: if not one whitespace, error
		if ($this->_isActive('spaceAfterControlStmt')) {
			if (!$this->tokenizer->checkNextToken(T_WHITESPACE)) {
				if (($token->id !== T_ELSE) && ($token->id !== T_TRY) && ($token->id !== T_DO)) {
					$msg = $this->_getMessage('SPACE_AFTER_TOKEN', $csText);
					$this->_writeError('spaceAfterControlStmt', $msg);
				}
			}
		}

		// for some control structures like "else" and "do",
		// there is no statements they will be followed directly by "{"
		if (($token->id === T_ELSE) || ($token->id === T_DO) || ($token->id === T_TRY)) {
			if ($this->tokenizer->checkNextValidToken(T_BRACES_OPEN)) {
				$this->_inControlStatement = false;
				$this->_justAfterControlStmt = true;
			}
		}

		// "else if" is different
		if ($token->id === T_ELSE) {
			if ($this->tokenizer->checkNextValidToken(T_IF)) {
				// control statement for "else" is done
				// new control statement "if" will start
				$this->_inControlStatement = false;
			}

			// ELSE just after a IF with no curly : we close the IF statement
			if ($this->statementStack->getCurrentStackItem()->type === StatementItem::TYPE_IF && $this->statementStack->getCurrentStackItem()->noCurly === true) {
				$this->statementStack->pop();
			}
		}

		if ($token->id === T_IF) {
			// IF just after a ELSE with no curly : we close the ELSE statement
			if ($this->statementStack->getCurrentStackItem()->type === StatementItem::TYPE_ELSE && $this->statementStack->getCurrentStackItem()->noCurly === true) {
				$this->statementStack->pop();
			}
		}

		// "else" and "elseif" should start in the same line as "}"
		if ($token->id === T_ELSE || $token->id === T_ELSEIF) {
			$position = $this->_config->getTestProperty('controlStructElse', 'position');
			$previousToken = $this->tokenizer->peekPrvsValidToken();
			if ($previousToken->id === T_BRACES_CLOSE) {
				if (($position === SAME_LINE) && ($previousToken->line !== $token->line)) {
					$msg = $this->_getMessage('CS_STMT_ALIGNED_WITH_CURLY', $csText);
					$this->_writeError('controlStructElse', $msg);
				}
				if (($position === NEW_LINE) && ($previousToken->line === $token->line)) {
					$msg = $this->_getMessage('CS_STMT_ON_NEW_LINE', $csText);
					$this->_writeError('controlStructElse', $msg);
				}
			}
		}

		// The checkNeedBraces rule is usually launched after a closing parenthesis
		// In the case of the "else", we need to launch it now
		if ($token->id === T_ELSE) {
			$this->_checkNeedBraces();
		}

		// By default we consider that the statement block will start after the next '{'
		// In case there is no curly opening we need to consider the next line as being the statement
		if ($this->tokenizer->checkNextValidToken(T_PARENTHESIS_OPEN)) {
			// in case the statement type is IF, FOR, FOREACH, CATCH, WHILE we skip the conditional
			$startPos = $this->tokenizer->findClosingParenthesisPosition($token->position);
		} else {
			$startPos = $token->position;
		}

		// Now we expect the '{' token
		if (!$this->tokenizer->checkNextValidToken(T_BRACES_OPEN, null, $startPos + 1)) {

			// If not the case, we store the control statement in the stack
			$stackitem = new StatementItem();
			$stackitem->line = $token->line;
			$stackitem->type = strtoupper($csText);
			$stackitem->name = $csText;
			$stackitem->noCurly = true;
			$this->statementStack->push($stackitem);
		}

		// To avoid a false positive when treating the while statement of a do/while
		// We keep track that we have met a do statement
		if ($token->id === T_DO) {
			// Note : The current stack item is not yet the control statement itself, it's the parent
			$this->statementStack->getCurrentStackItem()->afterDoStatement = true;
		}
	}

	/**
	 * Process the start of a control structure (if/do/while/for/...).
	 *
	 * Launched when we meet the { just after the statement declaration.
	 */
	private function _processControlStatementStart() {
		// check for curly braces
		if ($this->_isActive('controlStructOpenCurly')) {

			$pos = $this->_config->getTestProperty('controlStructOpenCurly', 'position');

			$currentToken = $this->tokenizer->getCurrentToken();
			$previousToken = $this->tokenizer->peekPrvsValidToken();

			if ($pos === NEW_LINE) {
				// We expect the next token after the curly to be on a new line
				$isPosOk = ($previousToken->line < $currentToken->line);
			} else {
				// We expect the next token after the curly to be on the same line
				$isPosOk = ($previousToken->line === $currentToken->line);
			}

			if (!$isPosOk) {
				$tmp = ($pos === SAME_LINE) ? "the previous line." : "a new line.";
				$msg = $this->_getMessage('LEFT_CURLY_POS', $tmp);
				$this->_writeError('controlStructOpenCurly', $msg);
			}
		}

		// End the control statement
		$this->_justAfterControlStmt = false;
	}

	/**
	 * Process the start of a interface.
	 */
	private function _processInterfaceStart() {
		$this->_inInterface = true;
		$this->_interfaceLevel = $this->statementStack->count();

		// Check the position of the open curly after the interface declaration
		if ($this->_isActive('interfaceOpenCurly')) {
			$pos = $this->_config->getTestProperty('interfaceOpenCurly', 'position');

			$currentToken = $this->tokenizer->getCurrentToken();
			$previousToken = $this->tokenizer->peekPrvsValidToken();

			if ($pos === NEW_LINE) {
				// The previous token should be on the previous line
				$isPosOk = ($previousToken->line < $currentToken->line);
			} else {
				// The previous token should be on the same line
				$isPosOk = ($previousToken->line === $currentToken->line);
			}

			if (!$isPosOk) {
				$tmp = ($pos === SAME_LINE) ? "the previous line." : "a new line.";
				$msg = $this->_getMessage('LEFT_CURLY_POS', $tmp);
				$this->_writeError('interfaceOpenCurly', $msg);
			}
		}
	}

	/**
	 * Process the end of a interface.
	 */
	private function _processInterfaceStop() {
		// We are out of the interface
		$this->_inInterface = false;
	}

	/**
	 * Process the namespace declaration.
	 */
	private function _processNamespace() {
		// We replace the package name with the namespace
		$this->_packageName = $this->tokenizer->peekNextValidToken()->text;
	}

	/**
	 * Process the start of a class.
	 */
	private function _processClassStart() {
		$this->_inClass = true;
		$this->_classLevel = $this->statementStack->count();

		// Check the position of the open curly after the class declaration
		if ($this->_isActive('classOpenCurly')) {
			$pos = $this->_config->getTestProperty('classOpenCurly', 'position');

			$currentToken = $this->tokenizer->getCurrentToken();
			$previousToken = $this->tokenizer->peekPrvsValidToken();

			if ($pos === NEW_LINE) {
				// The previous token should be on the previous line
				$isPosOk = ($previousToken->line < $currentToken->line);
			} else {
				// The previous token should be on the same line
				$isPosOk = ($previousToken->line === $currentToken->line);
			}

			if (!$isPosOk) {
				$tmp = ($pos === SAME_LINE) ? "the previous line." : "a new line.";
				$msg = $this->_getMessage('LEFT_CURLY_POS', $tmp);
				$this->_writeError('classOpenCurly', $msg);
			}
		}
	}

	/**
	 * Process the end of a class.
	 */
	private function _processClassStop() {
		// We are out of the class
		$this->_inClass = false;

		// Reset of the warnings suppression is done at the end of the file, hoping we have 1 file / class
	}

	/**
	 * Process the start of a function.
	 */
	private function _processFunctionStart() {
		$this->_inFunction = true;
		$this->_cyclomaticComplexity = 1;
		$this->_npathComplexity = 0;
		$this->_functionLevel = $this->statementStack->count();
		$this->_justAfterFuncStmt = false;

		$this->_functionStartLine = $this->lineNumber;

		// Check the position of the open curly after the function declaration
		if ($this->_isActive('funcDefinitionOpenCurly')) {
			$pos = $this->_config->getTestProperty('funcDefinitionOpenCurly', 'position');

			$currentToken = $this->tokenizer->getCurrentToken();
			$previousToken = $this->tokenizer->peekPrvsValidToken();

			if ($pos === NEW_LINE) {
				// The previous token should be on the previous line
				$isPosOk = ($previousToken->line < $currentToken->line);
			} else {
				// The previous token should be on the same line
				$isPosOk = ($previousToken->line === $currentToken->line);
			}

			if (!$isPosOk) {
				$tmp = ($pos === SAME_LINE) ? "the previous line." : "a new line.";
				$msg = $this->_getMessage('LEFT_CURLY_POS', $tmp);
				$this->_writeError('funcDefinitionOpenCurly', $msg);
			}
		}
	}

	/**
	 * Check the cyclomatic complexity of a function.
	 *
	 * Called by _processFunctionStop().
	 */
	private function _checkCyclomaticComplexity() {
		if ($this->_isActive('cyclomaticComplexity')) {
			$warningLevel = $this->_config->getTestProperty('cyclomaticComplexity', 'warningLevel');
			$errorLevel = $this->_config->getTestProperty('cyclomaticComplexity', 'errorLevel');
			$msg = $this->_getMessage('CYCLOMATIC_COMPLEXITY', $this->_currentFunctionName, $this->_cyclomaticComplexity, $warningLevel);

			// Direct call to the reporter to allow different error levels for a single test.
			if ($this->_cyclomaticComplexity > $warningLevel) {
				$this->_writeError('cyclomaticComplexity', $msg, $this->_functionStartLine, WARNING);
			} else if ($this->_cyclomaticComplexity > $errorLevel) {
				$this->_writeError('cyclomaticComplexity', $msg, $this->_functionStartLine, ERROR);
			}
		}
	}

	/**
	 * Check the NPath complexity of a function.
	 *
	 * Called by _processFunctionStop()
	 */
	private function _checkNPathComplexity() {
		if ($this->_isActive('npathComplexity')) {
			$warningLevel = $this->_config->getTestProperty('npathComplexity', 'warningLevel');
			$errorLevel = $this->_config->getTestProperty('npathComplexity', 'errorLevel');
			$msg = $this->_getMessage('NPATH_COMPLEXITY', $this->_currentFunctionName, $this->_npathComplexity, $warningLevel);

			// Direct call to the reporter to allow different error levels for a single test.
			if ($this->_npathComplexity > $warningLevel) {
				$this->_writeError('npathComplexity', $msg, $this->_functionStartLine, WARNING);
			} else if ($this->_npathComplexity > $errorLevel) {
				$this->_writeError('npathComplexity', $msg, $this->_functionStartLine, ERROR);
			}
		}
	}

	/**
	 * Check that the declared parameters in the docblock match the content of the function.
	 *
	 * Called by _processFunctionStop().
	 */
	private function _checkDocBlockParameters() {

		// For anonymous functions, we don't check the docblock
		$isAnonymous = $this->statementStack->getCurrentStackItem()->visibility === 'ANONYMOUS';

		if ($this->_isActive('docBlocks') && !$isAnonymous && !$this->_config->isException('docBlocks', $this->_currentFunctionName) && !$this->_docblocInheritDoc) {

			// If the function is not private and we check the doc
			$isPrivateExcluded = $this->_config->getTestProperty('docBlocks', 'excludePrivateMembers');
			if (!($isPrivateExcluded && $this->_functionVisibility === 'PRIVATE')) {

				// Check the docblock @return
				if (($this->_config->getTestProperty('docBlocks', 'testReturn') !== 'false')) {

					if ($this->_functionReturns && ($this->_docblocNbReturns === 0)) {
						$msg = $this->_getMessage('DOCBLOCK_RETURN', $this->_currentFunctionName);
						$this->_writeError('docBlocks', $msg);
					}
				}

				// Check the docblock @param
				if (($this->_config->getTestProperty('docBlocks', 'testParam') !== 'false')) {
					if ($this->_nbFunctionParameters !== $this->_docblocNbParams) {
						$msg = $this->_getMessage('DOCBLOCK_PARAM', $this->_currentFunctionName);
						$this->_writeError('docBlocks', $msg);
					}
				}

				// Check the docblock @throw
				if (($this->_config->getTestProperty('docBlocks', 'testThrow') !== 'false')) {

					if ($this->_functionThrows && ($this->_docblocNbThrows === 0)) {
						$msg = $this->_getMessage('DOCBLOCK_THROW', $this->_currentFunctionName);
						$this->_writeError('docBlocks', $msg);
					}
				}
			}
		}
	}

	/**
	 * Check the length of the function.
	 *
	 * Called by _processFunctionStop().
	 */
	private function _checkFunctionLength() {
		// Check the length of the function
		if ($this->_isActive('functionLength')) {

			$functionLength = $this->lineNumber - $this->_functionStartLine;
			$maxLength = $this->_config->getTestProperty('functionLength', 'maxLength');

			if ($functionLength > $maxLength) {
				$msg = $this->_getMessage('FUNCTION_LENGTH_THROW', $this->_currentFunctionName, $functionLength, $maxLength);
				$this->_writeError('docBlocks', $msg);
			}
		}
	}

	/**
	 * Process the end of a function declaration.
	 */
	private function _processFunctionStop() {
		$this->_inFunction = false; // We are out of the function

		// Check the cyclomatic complexity
		$this->_checkCyclomaticComplexity();

		// Check the NPath Complexity
		$this->_checkNPathComplexity();

		// Check the docblock content
		$this->_checkDocBlockParameters();

		// Check function length
		$this->_checkFunctionLength();

		// Check unused function parameters
		$this->_checkUnusedFunctionParameters();

		// Reset the warnings suppressed by annotation
		$this->_functionSuppressWarnings = array();

		// Reset the count of elements in the current function docblock
		$this->_docblocNbParams = 0;
		$this->_docblocNbReturns = 0;
		$this->_docblocNbThrows = 0;
		$this->_docblocInheritDoc = false;
	}

	/**
	 * Process a function parameters to detect visibility and staticity.
	 *
	 * @param TokenInfo $token
	 *        	a token
	 */
	private function _detectVisibility($token) {
		if ($this->tokenizer->checkToken($token, T_PRIVATE)) {
			$this->_functionVisibility = 'PRIVATE';
		} else if ($this->tokenizer->checkToken($token, T_PROTECTED)) {
			$this->_functionVisibility = 'PROTECTED';
		} else if ($this->tokenizer->checkToken($token, T_STATIC)) {
			$this->_functionStatic = true;
		}
	}

	/**
	 * Process a function declaration statement (the parameters).
	 */
	private function _processFunctionStatement() {
		// Increment the number of functions
		$this->_ncssTotalFunctions ++;
		$this->_ncssFileFunctions ++;

		// Reset the default values
		$this->funcArgString = "";
		$this->_nbFunctionParameters = 0;
		$this->_functionParameters = array();
		$this->_inFunctionStatement = true;
		$this->_functionReturns = false;
		$this->_functionThrows = false;
		$this->_inControlStatement = false;
		$this->_currentStatement = false;
		$this->_inClassStatement = false;
		$this->_inInterfaceStatement = false;
		$this->_functionVisibility = 'PUBLIC';
		$this->_functionStatic = false;

		// Detect the visibility (on the token just before the current one).
		$previousToken = $this->tokenizer->peekPrvsValidToken();
		$this->_detectVisibility($previousToken);
		if ($previousToken->position > 1) {
			$prepreviousToken = $this->tokenizer->peekPrvsValidToken($previousToken->position - 1);
			$this->_detectVisibility($prepreviousToken);
		}

		// Find the function name
		$currentToken = $this->tokenizer->getCurrentToken();
		$curlyOpeningDetected = $this->tokenizer->checkNextToken(T_PARENTHESIS_OPEN);
		$nameDetected = null;
		while ($currentToken !== null && !$curlyOpeningDetected) {
			// Skip the token
			$this->tokenizer->getNextToken();
			$currentToken = $this->tokenizer->getCurrentToken();

			if ($currentToken->id === T_STRING) {
				$nameDetected = $currentToken->text;
			}
			$curlyOpeningDetected = $this->tokenizer->checkNextValidToken(T_PARENTHESIS_OPEN);
		}

		// currentToken should end up being the function name token

		// Tracking the function's name.
		if ($nameDetected !== null && trim($nameDetected) !== "") {
			$functionName = $currentToken->text;

			// Check the PHPDoc presence
			$this->_checkDocExists(T_FUNCTION);
		} else {
			// We have an anonymous function or closure.
			$functionName = 'anonymous';
			$this->_functionVisibility = 'ANONYMOUS';

			// We don't check the PHPDoc for anonymous functions
		}
		$this->_currentFunctionName = $functionName;

		// If the function is private we add it to the list of function to use (and store the line number)
		if ($this->_functionVisibility === 'PRIVATE') {
			$this->_privateFunctions[$functionName] = $functionName;
			$this->_privateFunctionsStartLines[$functionName] = $currentToken->line;
		}

		// Function is a constructor
		$constructorNamingStyle = $this->_config->getTestProperty('constructorNaming', 'naming');
		$useOldConstructorNamingStyle = ($constructorNamingStyle === 'old');
		$useNewConstructorNamingStyle = ($constructorNamingStyle === 'new');
		if (strtolower($functionName) === "__construct" && $this->_isActive('constructorNaming') && $useOldConstructorNamingStyle) {
			$msg = $this->_getMessage('CONSTRUCTOR_NAMING', $this->_currentClassname);

			$this->_writeError('constructorNaming', $msg);
		}

		// Special functions are not checked
		if (!in_array($functionName, $this->_specialFunctions)) {

			// Constructors
			if ($functionName === $this->_currentClassname) {

				// Function is a constructor
				if ($this->_isActive('constructorNaming') && $useNewConstructorNamingStyle) {
					$msg = $this->_getMessage('CONSTRUCTOR_NAMING', '__construct()');
					$this->_writeError('constructorNaming', $msg);
				}
			} else {

				// Other function
				if ($this->_functionVisibility === 'PRIVATE') {
					$this->_checkPrivateFunctionNaming($this->_currentFunctionName);
				} else if ($this->_functionVisibility === 'PROTECTED') {
					$this->_checkProtectedFunctionNaming($this->_currentFunctionName);
				} else {
					$this->_checkFunctionNaming($this->_currentFunctionName);
				}
			}
		}

		// List the arguments of the currently analyzed function.
		// Check the order of the parameters of the function.
		// The parameters having a default value should be in last position.
		$foundDefaultValues = false;
		$functionTokenPosition = $this->tokenizer->getCurrentPosition();

		$functionParamsStart = $this->tokenizer->findNextStringPosition('(', $functionTokenPosition);
		$functionParamsStop = $this->tokenizer->findClosingParenthesisPosition($functionTokenPosition);

		for ($i = $functionParamsStart; $i < $functionParamsStop; $i ++) {
			$functionToken = $this->tokenizer->peekTokenAt($i);

			// Current token is a parameter
			if ($this->tokenizer->checkToken($functionToken, T_VARIABLE)) {
				$this->_nbFunctionParameters ++;
				$parameterName = $functionToken->text;
				$this->_functionParameters[$parameterName] = "unused"; // We flag the parameter as unused

				// Check is this parameter as a default value
				$nextTokenInfo = $this->tokenizer->peekNextValidToken($i + 1);
				$hasDefaultValue = $this->tokenizer->checkToken($nextTokenInfo, T_EQUAL);
				if ($hasDefaultValue) {
					$foundDefaultValues = true;
				}

				// Check if the parameter has a default value
				if ($this->_isActive('defaultValuesOrder')) {
					if ($foundDefaultValues && !$hasDefaultValue) {
						$this->_writeError('defaultValuesOrder', $this->_getMessage('FUNC_DEFAULTVALUE_ORDER'));
						break;
					}
				}
			}
		}

		// Test for the max number of parameters
		if ($this->_isActive('functionMaxParameters')) {
			$paramCount = $this->_nbFunctionParameters;
			$maxParams = $this->_config->getTestProperty('functionMaxParameters', 'maxParameters');
			if ($paramCount > $maxParams) {
				$msg = $this->_getMessage('MAX_PARAMETERS', $this->_currentFunctionName, $paramCount, $maxParams);
				$this->_writeError('functionMaxParameters', $msg);
			}
		}
	}

	/**
	 * Process the start of a switch block.
	 */
	private function _processSwitchStart() {
		$this->_switchStartLine = $this->lineNumber;
	}

	/**
	 * Process the end of a switch block.
	 */
	private function _processSwitchStop() {
		// If we already are in a "case", we remove it from the stack
		if ($this->statementStack->getCurrentStackItem()->type === StatementItem::TYPE_CASE || $this->statementStack->getCurrentStackItem()->type === StatementItem::TYPE_DEFAULT) {

			// Test if the previous case had a break
			$this->_checkSwitchCaseNeedBreak();

			$this->statementStack->pop();
		}

		$this->_checkSwitchNeedDefault();
	}

	/**
	 * Check if the default case of a switch statement is present.
	 *
	 * This function is launched at the end of switch/case.
	 */
	private function _checkSwitchNeedDefault() {
		if ($this->_isActive('switchNeedDefault')) {
			if (!$this->statementStack->getCurrentStackItem()->switchHasDefault) {
				// Direct call to reporter to include a custom line number.
				$this->_writeError('switchNeedDefault', $this->_getMessage('SWITCH_DEFAULT'), $this->_switchStartLine, $this->_config->getTestLevel('switchNeedDefault'));
			}
		}
	}

	/**
	 * Process a case statement.
	 */
	private function _processSwitchCase() {
		// If we already are in a "case", we remove it from the stack
		if ($this->statementStack->getCurrentStackItem()->type === StatementItem::TYPE_CASE || $this->statementStack->getCurrentStackItem()->type === StatementItem::TYPE_DEFAULT) {

			// Test if the previous case had a break
			$this->_checkSwitchCaseNeedBreak();

			$this->statementStack->pop();
		}

		// If the case arrives after the default
		$this->_checkSwitchDefaultOrder();

		// We store the control statement in the stack
		$stackitem = new StatementItem();
		$stackitem->line = $this->lineNumber;
		$stackitem->type = "CASE";
		$stackitem->name = "case";
		$this->statementStack->push($stackitem);

		// For this case
		$this->statementStack->getCurrentStackItem()->caseHasBreak = false;
		$this->statementStack->getCurrentStackItem()->caseStartLine = $this->lineNumber;

		// Check if the case is empty
		// We detect if the next valid token after ":" is another T_CASE
		$startPos = $this->tokenizer->findNextStringPosition(":", $this->tokenizer->getCurrentPosition());
		$this->statementStack->getCurrentStackItem()->caseIsEmpty = $this->tokenizer->checkNextValidToken(T_CASE, false, $startPos + 1) || $this->tokenizer->checkNextValidToken(T_DEFAULT, false, $startPos + 1);
	}

	/**
	 * Check if the switch/case statement default case appear at the end.
	 *
	 * This function is launched at the start of each case.
	 */
	private function _checkSwitchDefaultOrder() {
		if ($this->_isActive('switchDefaultOrder')) {
			if ($this->statementStack->getCurrentStackItem()->switchHasDefault) {
				// The default flag is already set, it means that a previous case case a default
				$this->_writeError('switchDefaultOrder', $this->_getMessage('SWITCH_DEFAULT_ORDER'));
			}
		}
	}

	/**
	 * Check if the case statement of a switch/case a a break instruction.
	 *
	 * This function is launched at the start of each case.
	 */
	private function _checkSwitchCaseNeedBreak() {
		$stackItem = $this->statementStack->getCurrentStackItem();

		// Test if the previous case had a break
		if ($this->_isActive('switchCaseNeedBreak') && ($stackItem->type === "CASE") && !$stackItem->caseHasBreak && !$stackItem->caseIsEmpty) {
			// Direct call to reporter to include a custom line number.
			$this->_writeError('switchCaseNeedBreak', $this->_getMessage('SWITCH_CASE_NEED_BREAK'), $this->statementStack->getCurrentStackItem()->caseStartLine, $this->_config->getTestLevel('switchCaseNeedBreak'));
		}
	}

	/**
	 * Process a default statement.
	 */
	private function _processSwitchDefault() {
		// If we already are in a "case", we remove it from the stack
		if ($this->statementStack->getCurrentStackItem()->type === StatementItem::TYPE_CASE || $this->statementStack->getCurrentStackItem()->type === StatementItem::TYPE_DEFAULT) {

			// Test if the previous case had a break
			$this->_checkSwitchCaseNeedBreak();

			$this->statementStack->pop();
		}

		// We flag the "SWITCH" stack item as having a default
		$this->statementStack->getCurrentStackItem()->switchHasDefault = true;

		// We store the control statement in the stack
		$stackitem = new StatementItem();
		$stackitem->line = $this->lineNumber;
		$stackitem->type = "DEFAULT";
		$stackitem->name = "default";
		$this->statementStack->push($stackitem);
	}

	/**
	 * Process a break statement.
	 */
	private function _processSwitchBreak() {
		$this->statementStack->getCurrentStackItem()->caseHasBreak = true;
	}

	/**
	 * Process an interface declaration statement.
	 */
	private function _processInterfaceStatement() {
		// Check PHPDoc Presence
		$this->_checkDocExists(T_INTERFACE);

		$this->_ncssTotalInterfaces ++;
		$this->_ncssFileInterfaces ++;

		// Test if there is more than one class per file
		if ($this->_isActive('oneInterfacePerFile') && $this->_ncssFileInterfaces > 1) {
			$msg = $this->_getMessage('ONE_INTERFACE_PER_FILE', $this->_currentFilename);
			$this->_writeError('oneInterfacePerFile', $msg);
		}

		// Reset the default values
		$this->_inFunction = false;
		$this->_nbFunctionParameters = 0;
		$this->_functionParameters = array();
		$this->_inFunctionStatement = false;
		$this->_functionReturns = false;
		$this->_functionThrows = false;
		$this->_inControlStatement = false;
		$this->_currentStatement = false;
		$this->_inInterfaceStatement = true;

		// skip until T_STRING representing the interface name
		while (!$this->tokenizer->checkCurrentToken(T_STRING)) {
			$this->tokenizer->getNextToken();
		}

		$token = $this->tokenizer->getCurrentToken();
		$interfacename = $token->text;
		$this->_currentInterfacename = $interfacename;

		// Test that the interface name matches the file name
		$this->_checkTypeNameFileNameMatch($interfacename);

		// Check interface naming
		$this->_checkInterfaceNaming($interfacename);

		$this->_checkWhiteSpaceAfter($interfacename);
	}

	/**
	 * Process a class declaration statement.
	 */
	private function _processClassStatement() {
		$isAfterScopeResolutionOperator = $this->tokenizer->checkPreviousToken(T_DOUBLE_COLON);
		if ($isAfterScopeResolutionOperator) {
			return;
		}

		// Check PHPDoc presence
		$this->_checkDocExists(T_CLASS);

		$this->_ncssTotalClasses ++;
		$this->_ncssFileClasses ++;

		// Test if there is more than one class per file
		if ($this->_isActive('oneClassPerFile') && $this->_ncssFileClasses > 1) {
			$msg = $this->_getMessage('ONE_CLASS_PER_FILE', $this->_currentFilename);
			$this->_writeError('oneClassPerFile', $msg);
		}

		// Reset the default values
		$this->_inFunction = false;
		$this->_nbFunctionParameters = 0;
		$this->_functionParameters = array();
		$this->_inFunctionStatement = false;
		$this->_functionReturns = false;
		$this->_functionThrows = false;
		$this->_inControlStatement = false;
		$this->_currentStatement = false;
		$this->_inClassStatement = true;

		// skip until T_STRING representing the class name
		while (!$this->tokenizer->checkCurrentToken(T_STRING)) {
			$this->tokenizer->getNextToken();
		}

		$token = $this->tokenizer->getCurrentToken();
		$classname = $token->text;
		$this->_currentClassname = $classname;

		// Test that the class name matches the file name
		$this->_checkTypeNameFileNameMatch($classname);

		// Check class naming
		$this->_checkClassNaming($classname);

		$this->_checkWhiteSpaceAfter($classname);
	}

	/**
	 * Check for empty block.
	 *
	 * This function is launched when the current token is {
	 */
	private function _checkEmptyBlock() {
		// If the next valid token is } then the statement is empty.
		if ($this->_isActive('checkEmptyBlock') && $this->_currentStatement) {

			if ($this->tokenizer->checkNextValidToken(T_BRACES_CLOSE)) {
				$msg = $this->_getMessage('EMPTY_BLOCK', $this->_currentStatement);
				$this->_writeError('checkEmptyBlock', $msg);
			}
		}
	}

	/**
	 * Check for encapsed variables inside string.
	 *
	 * This function is launched when the current token is T_ENCAPSED_AND_WHITESPACE.
	 */
	private function _checkEncapsedVariablesInsideString() {
		if ($this->_isActive('encapsedVariablesInsideString') and (!$this->statementStack->getCurrentStackItem()->inHeredoc or ($this->_inFuncCall and !$this->_config->isException('encapsedVariablesInsideString', end($this->_currentFuncCall))))) {
			$this->_writeError('encapsedVariablesInsideString', $this->_getMessage('VARIABLE_INSIDE_STRING'));
		}
	}

	/**
	 * Check for potential bugs when using comparison operator.
	 *
	 * This function is launched when the current token is T_IS_EQUAL or T_IS_NOT_EQUAL.
	 *
	 * @param String $text
	 *        	the comparison operator used
	 */
	private function _checkStrictCompare($text) {
		if ($this->_isActive('strictCompare')) {
			$message = $this->_getMessage('USE_STRICT_COMPARE', $text);
			$this->_writeError('strictCompare', $message);
		}
	}

	/**
	 * Check for inner assignments.
	 *
	 * This function is launched when the current token is = (assignment).
	 */
	private function _checkInnerAssignment() {
		// If the test if active and we are inside a control statement
		if ($this->_isActive('checkInnerAssignment') && $this->_inControlStatement) {

			// If the control statement is not listed as an exception
			if (!$this->_config->isException('checkInnerAssignment', $this->_currentStatement)) {
				$this->_writeError('checkInnerAssignment', $this->_getMessage('INSIDE_ASSIGNMENT'));
			}
		}
	}

	/**
	 * Check for empty statement.
	 *
	 * This function is launched when the current token is ;
	 */
	private function _checkEmptyStatement() {
		// If the next valid token is ; then the statement is empty.
		if ($this->_isActive('checkEmptyStatement')) {

			if ($this->tokenizer->checkNextValidToken(T_SEMICOLON)) {
				$this->_writeError('checkEmptyStatement', $this->_getMessage('EMPTY_STATEMENT'));
			}
		}
	}

	/**
	 * Check for unused functions.
	 *
	 * This function is launched at the end of a file.
	 */
	private function _checkUnusedPrivateFunctions() {
		if ($this->_isActive('checkUnusedPrivateFunctions')) {

			// We make a diff between the list of declared private functions and the list of called functions.
			// This is a very approximative test, we don't test that the called function is from the good class.
			// The usedFunctions array contains a lot of false positives
			$uncalledFunctions = array_diff($this->_privateFunctions, $this->_usedFunctions);

			foreach ($uncalledFunctions as $uncalledFunction) {

				$msg = $this->_getMessage('UNUSED_PRIVATE_FUNCTION', $uncalledFunction);
				// Direct call to reporter to include a custom line number.
				$this->_writeError('checkUnusedPrivateFunctions', $msg, $this->_privateFunctionsStartLines[$uncalledFunction], $this->_config->getTestLevel('checkUnusedPrivateFunctions'));
			}
		}
	}

	/**
	 * Check for unused variables in the file.
	 *
	 * This function is launched at the end of a file.
	 */
	private function _checkUnusedVariables() {
		if ($this->_isActive('checkUnusedVariables')) {

			foreach ($this->_variables as $variable) {

				if ((!$variable->isUsed) && !($this->_isClass || $this->_isView) && !$this->_config->isException('checkUnusedVariables', $variable->name)) {
					$msg = $this->_getMessage('UNUSED_VARIABLE', $variable->name);
					$this->_writeError('checkUnusedVariables', $msg, $variable->line, $this->_config->getTestLevel('checkUnusedVariables'));
				}
			}
		}
	}

	/**
	 * Check for unused code.
	 *
	 * Dead code after a return or a throw TOKEN.
	 *
	 * @param String $endToken
	 *        	The name of the end token (RETURN or THROW)
	 */
	private function _checkUnusedCode($endToken) {
		if ($this->_isActive('checkUnusedCode')) {

			// The check is done only when we are at the root level of a function
			if ($this->statementStack->getCurrentStackItem()->type === StatementItem::TYPE_FUNCTION) {

				// Find the end of the return statement
				$pos = $this->tokenizer->findNextStringPosition(';');

				// Find the next valid token after the return statement
				$nextValidToken = $this->tokenizer->peekNextValidToken($pos + 1);

				// Find the end of the function or bloc of code
				$posClose = $this->tokenizer->findNextStringPosition('}');

				// If the end of bloc if not right after the return statement, we have dead code
				if ($nextValidToken !== null && $posClose > $nextValidToken->position) {
					$msg = $this->_getMessage('UNUSED_CODE', $this->statementStack->getCurrentStackItem()->name, $endToken);
					$this->_writeError('checkUnusedCode', $msg);
				}
			}
		}
	}

	/**
	 * Check for unused function parameters.
	 *
	 * This function is launched at the end of a function
	 */
	private function _checkUnusedFunctionParameters() {
		if ($this->_isActive('checkUnusedFunctionParameters')) {

			// If the docblock is inherited, this means that the function implements a parent function
			// We may have some unused parameters in this case, we don't generate a warnings
			if (!$this->_docblocInheritDoc) {

				foreach ($this->_functionParameters as $variableName => $value) {
					if ($value !== "used") {
						$msg = $this->_getMessage('UNUSED_FUNCTION_PARAMETER', $this->_currentFunctionName, $variableName);
						$this->_writeError('checkUnusedFunctionParameters', $msg);
					}
				}
			}
		}
	}

	/**
	 * Check the variable use.
	 *
	 * This function is launched when the current token is T_VARIABLE
	 *
	 * @param String $text
	 *        	The variable name
	 */
	private function _processVariable($text) {
		// Check the variable naming convention
		if (!in_array($text, $this->_systemVariables)) {
			$this->_checkVariableNaming($text);
		}

		// Check the variable name's length
		$this->_checkVariableNameLength($text);

		// Check if the variable is not a deprecated system variable
		$this->_checkDeprecation($text);

		// Check if the variable is not replaced
		$this->_checkReplacements($text);

		// Check if the variable is a function parameter
		if (!empty($this->_functionParameters[$text]) && $this->_inFunction) {

			$this->_functionParameters[$text] = "used";
		} else if (!$this->_inFunctionStatement) {

			// Global variable
			$pos = $this->tokenizer->getCurrentPosition();
			$nextTokenInfo = $this->tokenizer->peekNextValidToken($pos + 1);

			// if the next token is an equal, we suppose that this is an affectation
			$nextTokenText = $nextTokenInfo->text;
			$affectionTokens = array(
				'=',
				'+=',
				'*=',
				'/=',
				'-=',
				'%=',
				'&=',
				'|=',
				'^=',
				'<<=',
				'>>=',
				'.='
			);
			$isAffectation = in_array($nextTokenText, $affectionTokens);

			// Check if the variable has already been met
			if (empty($this->_variables[$text]) && !in_array($text, $this->_systemVariables)) {
				// The variable is met for the first time
				$variable = new VariableInfo();
				$variable->name = $text;
				$variable->line = $this->lineNumber; // We store the first declaration of the variable
				$this->_variables[$text] = $variable;
			} else if ($isAffectation) {
				// The variable is reaffected another value, this doesn't count as a valid use.
			} else {

				// Manage the case of $this->attribute
				if ($text === '$this') {

					$nextTokenInfo2 = $this->tokenizer->peekNextValidToken($nextTokenInfo->position);

					if ($this->tokenizer->checkToken($nextTokenInfo2, T_OBJECT_OPERATOR)) {

						// Detect $this inside a static function.
						if ($this->_functionStatic) {
							if ($this->_isActive('thisInStatic')) {
								$this->_writeError('thisInStatic', $this->_getMessage('THIS_IN_STATIC_FUNCTION'));
							}
						}

						$nextTokenInfo3 = $this->tokenizer->peekNextValidToken($nextTokenInfo2->position + 1);

						// This does not look like a function call, it should be a class attribute.
						// We eliminate the $this-> part
						$text = '$' . $nextTokenInfo3->text;
					}
				}

				// The variable is met again, we suppose we have used it for something
				if (!empty($this->_variables[$text])) {
					$variable = $this->_variables[$text];
					$variable->isUsed = true;
					$this->_variables[$text] = $variable;
				}
			}
		}
	}

	/**
	 * Process the return token.
	 *
	 * This function is launched when the current token is T_RETURN
	 */
	private function _processReturn() {
		// If not an empty return (no value before the semicolon)
		if (!$this->tokenizer->checkNextValidToken(T_SEMICOLON)) {
			// Remember that the current function does return something (for PHPDoc)
			$this->_functionReturns = true;
		}

		// Search for unused code after the return
		$this->_checkUnusedCode('RETURN');
	}

	/**
	 * Process the throw token.
	 *
	 * This function is launched when the current token is T_THROW
	 */
	private function _processThrow() {
		// Remember that the current function does throw an exception
		$this->_functionThrows = true;

		// Search for unused code after the throw of an exception
		$this->_checkUnusedCode('THROW');
	}

	/**
	 * Process the catch token.
	 *
	 * This function is launched when the current token is T_CATCH
	 */
	private function _processCatch() {
		// We consider that all preview "Throws" are caught (this may be wrong)
		$this->_functionThrows = false;
	}

	/**
	 * Process the finally token.
	 *
	 * This function is launched when the current token is T_FINALLY
	 */
	private function _processFinally() {
		// We consider that all preview "Throws" are caught
		$this->_functionThrows = false;

		$this->_checkUnusedCode('FINALLY');
	}

	/**
	 * Process the start of a heredoc block.
	 *
	 * This function is launched when the current token is T_START_HEREDOC.
	 */
	private function _processStartHeredoc() {
		$this->statementStack->getCurrentStackItem()->inHeredoc = true;

		// Rule the "checkHeredoc" rule
		$this->_checkHeredoc();
	}

	/**
	 * Process the end of a heredoc block.
	 *
	 * This function is launched when the current token is T_END_HEREDOC.
	 */
	private function _processEndHeredoc() {
		$this->statementStack->getCurrentStackItem()->inHeredoc = false;
	}

	/**
	 * Check for presence of heredoc.
	 */
	private function _checkHeredoc() {
		if ($this->_isActive('checkHeredoc')) {
			$this->_writeError('checkHeredoc', $this->_getMessage('HEREDOC'));
		}
	}

	/**
	 * Check for the presence of a white space before the text.
	 *
	 * @param String $text
	 *        	The text of the token to test
	 */
	private function _checkWhiteSpaceBefore($text) {
		if ($this->_isActive('checkWhiteSpaceBefore')) {

			if (!$this->_config->isException('checkWhiteSpaceBefore', $text)) {

				if (!$this->tokenizer->checkPreviousToken(T_WHITESPACE)) {
					$msg = $this->_getMessage('SPACE_BEFORE_TOKEN', $text);
					$this->_writeError('checkWhiteSpaceBefore', $msg);
				}
			}
		}
	}

	/**
	 * Check for the absence of a white space before the text.
	 *
	 * @param String $text
	 *        	The text of the token to test
	 */
	private function _checkNoWhiteSpaceBefore($text) {
		if ($this->_isActive('noSpaceBeforeToken')) {

			if (!$this->_config->isException('noSpaceBeforeToken', $text)) {

				if ($this->tokenizer->checkPreviousToken(T_WHITESPACE)) {

					// To avoid false positives when using a space indentation system,
					// check that we are on the same line as the previous valid token
					$prevValid = $this->tokenizer->peekPrvsValidToken();
					$currentToken = $this->tokenizer->getCurrentToken();
					if ($prevValid->line === $currentToken->line) {
						$msg = $this->_getMessage('NO_SPACE_BEFORE_TOKEN', $text);
						$this->_writeError('noSpaceBeforeToken', $msg);
					}
				}
			}
		}
	}

	/**
	 * Check for the presence of a white space after the text.
	 *
	 * @param String $text
	 *        	The text of the token to test
	 */
	private function _checkWhiteSpaceAfter($text) {
		if ($this->_isActive('checkWhiteSpaceAfter')) {
			if (!$this->_config->isException('checkWhiteSpaceAfter', $text)) {

				if (!$this->tokenizer->checkNextToken(T_WHITESPACE)) {
					// In case of new line or a PHP closing tag it's OK
					if (!($this->tokenizer->checkNextToken(T_NEW_LINE) || $this->tokenizer->checkNextToken(T_CLOSE_TAG))) {
						$msg = $this->_getMessage('SPACE_AFTER_TOKEN', $text);
						$this->_writeError('checkWhiteSpaceAfter', $msg);
					}
				}
			}
		}
	}

	/**
	 * Check for the absence of a white space after the text.
	 *
	 * @param TokenInfo $token
	 *        	the token
	 */
	private function _checkNoWhiteSpaceAfter($token) {
		if ($this->_isActive('noSpaceAfterToken')) {

			$text = $token->text;

			if (!$this->_config->isException('noSpaceAfterToken', $text)) {

				// The next token is a whitespace
				if ($this->tokenizer->checkNextToken(T_WHITESPACE)) {

					// And the next valid token is on the same line
					$tokenAfter = $this->tokenizer->peekNextValidToken();
					if ($tokenAfter->line === $token->line) {
						$msg = $this->_getMessage('NO_SPACE_AFTER_TOKEN', $text);
						$this->_writeError('noSpaceAfterToken', $msg);
					}
				}
			}
		}
	}

	/**
	 * Avoid using unary operators (++ or --) inside a control statement.
	 */
	private function _checkUnaryOperator() {
		if ($this->_isActive('checkUnaryOperator')) {
			// If the control statement is not listed as an exception
			if (!$this->_config->isException('checkUnaryOperator', $this->_currentStatement) || ($this->statementStack->getCurrentStackItem()->type === StatementItem::TYPE_ARRAY)) {
				// And if we are currently in a control statement or an array statement
				if ($this->_inControlStatement || ($this->statementStack->getCurrentStackItem()->type === StatementItem::TYPE_ARRAY)) {
					$this->_writeError('checkUnaryOperator', $this->_getMessage('UNARY_OPERATOR'));
				}
			}
		}
	}

	/**
	 * Check if the current line exceeds the maxLineLength allowed.
	 *
	 * Launched at the start of a new line.
	 */
	private function _checkLargeLine() {
		$checkHTMLLines = $this->_config->getTestProperty('lineLength', 'checkHTMLLines');

		// If the current token is HTML we don't check the line size
		if ($checkHTMLLines === "true" || !$this->tokenizer->checkNextValidToken(T_INLINE_HTML)) {
			$maxLength = $this->_config->getTestProperty('lineLength', 'maxLineLength');
			$lineString = ""; // String assembled from tokens
			$currentTokenIndex = $this->tokenizer->getCurrentPosition();
			$currentToken = $this->tokenizer->getCurrentToken($currentTokenIndex);

			do {
				$currentTokenString = $currentToken->text;
				$lineString .= $currentTokenString;

				$currentTokenIndex += 1;
				$currentToken = $this->tokenizer->peekTokenAt($currentTokenIndex);

				$isNull = ($currentToken === null);
				$isNewLine = !$isNull && $this->tokenizer->checkToken($currentToken, T_NEW_LINE);
			} while (!($isNull || $isNewLine));

			$lineLength = strlen($lineString);

			// Reporting the error if the line length exceeds the defined maximum.
			if ($lineLength > $maxLength) {
				// Does not report if the line is a multiline comment - i.e. has /* in it)
				if (strpos($lineString, "/*")) {
					return;
				}
				$msg = $this->_getMessage('LONG_LINE', $lineLength, $maxLength);
				$this->_writeError('lineLength', $msg);
			}
		}
	}

	/**
	 * Checks for presence of tab in the whitespace character string.
	 *
	 * Launched when T_WHITESPACE or T_TAB is met at the beginning of a line.
	 *
	 * @param TokenInfo $token
	 *        	the token to check
	 */
	private function _checkIndentation($token) {
		$whitespaceString = $token->text;

		if ($this->_isActive('indentation')) {
			$indentationType = $this->_config->getTestProperty('indentation', 'type');

			// If indentation type is space, we look for tabs in the string
			if (strtolower($indentationType) === 'space' || strtolower($indentationType) === 'spaces') {
				$tabfound = preg_match("/\t/", $whitespaceString);
				if ($tabfound) {
					$this->_writeError('indentation', $this->_getMessage('INDENTATION_TAB'), $token->line);
				}

				// for spaces, we check the indentation level
				$this->_checkIndentationLevel($token);
			} else if (strtolower($indentationType) === 'tab' || strtolower($indentationType) === 'tabs') {
				// If indentation type is tabs, we look for whitespace in the string
				$whitespacefound = preg_match("/[ ]/", $whitespaceString);
				if ($whitespacefound) {
					$this->_writeError('indentation', $this->_getMessage('INDENTATION_WHITESPACE'), $token->line);
				}
			}
		}
	}

	/**
	 * Check the indentation level.
	 *
	 * Launched when T_WHITESPACE is met at the beginning of a line.
	 *
	 * @param TokenInfo $token
	 *        	the token to check
	 */
	private function _checkIndentationLevel($token) {
		$whitespaceString = $token->text;

		// Number of spaces used
		$indentationNumber = $this->_config->getTestProperty('indentation', 'number');
		if (empty($indentationNumber)) {
			$indentationNumber = 4;
		}

		// doesn't work if we are not in a class
		if (!$this->_inClass) {
			return;
		}

		// don't check empty lines, when we are inside a control statement, when the next token is a object opérator, ...
		if ($this->_inControlStatement || $this->_inFuncCall || !isset($this->lineNumber) || $this->tokenizer->checkNextToken(T_NEW_LINE) || $this->tokenizer->checkNextValidToken(T_PARENTHESIS_CLOSE) || $this->tokenizer->checkNextValidToken(T_BOOLEAN_AND) || $this->tokenizer->checkNextValidToken(T_BOOLEAN_OR) || $this->tokenizer->checkNextValidToken(T_OBJECT_OPERATOR)) {
			return;
		}

		$previousToken = $this->tokenizer->peekPrvsToken();

		// only check a line once
		if (!isset($this->indentationLevel['previousLine']) || $this->lineNumber !== $this->indentationLevel['previousLine']) {
			// Nesting level is the number of items in the branching stack
			$nesting = $this->statementStack->count();

			// But we must anticipate if the current line change the level
			if ($this->tokenizer->checkNextValidToken(T_BRACES_CLOSE) || $this->tokenizer->checkNextValidToken(T_SQUARE_BRACKET_CLOSE)) {
				$nesting --;
			}

			$expectedIndentation = $nesting * $indentationNumber;
			$indentation = strlen($whitespaceString);
			if ($previousToken->id !== T_NEW_LINE) {
				$indentation = 0;
			}

			// don't check when the line is a comment
			if ($this->tokenizer->checkNextToken(T_COMMENT)) {
				return;
			}

			// Control switch statement indentation
			if ($this->statementStack->getCurrentStackItem()->type === StatementItem::TYPE_SWITCH) {

				// Don't check brackets in a switch
				if ($this->tokenizer->checkNextValidToken(T_BRACES_OPEN) || $this->tokenizer->checkNextValidToken(T_BRACES_CLOSE)) {
					return;
				}
			} else if ($this->statementStack->getCurrentStackItem()->type === StatementItem::TYPE_CASE || $this->statementStack->getCurrentStackItem()->type === StatementItem::TYPE_DEFAULT) {

				// If the current line contains a new case, we decrease the expected level
				if ($this->tokenizer->checkNextValidToken(T_CASE) || $this->tokenizer->checkNextValidToken(T_DEFAULT) || $this->tokenizer->checkNextValidToken(T_BRACES_CLOSE)) {
					$expectedIndentation -= $indentationNumber;
				}
			}

			if ($expectedIndentation !== $indentation) {
				$msg = $this->_getMessage('INDENTATION_LEVEL', $expectedIndentation, $indentation);
				$this->_writeError('indentationLevel', $msg, $token->line);
			}
		}
		$this->indentationLevel['previousLine'] = $this->lineNumber;
	}

	/**
	 * Checks if the block of code need braces.
	 *
	 * This function is called we the current token is ) and we are in a control statement.
	 */
	private function _checkNeedBraces() {
		if ($this->_isActive('needBraces')) {
			$stmt = strtolower($this->_currentStatement);
			if (in_array($stmt, array(
				'if',
				'elseif',
				'do',
				'for',
				'foreach'
			)) || ($stmt === "while" && !$this->statementStack->getParentStackItem()->afterDoStatement)) {
				if (!$this->tokenizer->checkNextValidToken(T_BRACES_OPEN)) {
					$msg = $this->_getMessage('NEED_BRACES', $stmt);
					$this->_writeError('needBraces', $msg);
				}
			} else if ($stmt === "else") {
				if (!$this->tokenizer->checkNextValidToken(T_BRACES_OPEN) && !$this->tokenizer->checkNextValidToken(T_IF)) {
					$msg = $this->_getMessage('NEED_BRACES', $stmt);
					$this->_writeError('needBraces', $msg);
				}
			}

			if ($stmt === "while") {
				$this->statementStack->getCurrentStackItem()->afterDoStatement = false;
			}
		}
	}

	/**
	 * Process a TODO comment.
	 *
	 * @param Token $token
	 *        	the doc token.
	 */
	private function _processTODO($token) {
		if ($this->_isActive('showTODOs')) {
			$text = $token->text;
			$todoStr = stripos($text, 'TODO');
			if ($todoStr !== FALSE) {

				$todoMsg = substr($text, $todoStr + 4);

				// Take the first line only
				$lines = preg_split('/\r\n|\r|\n/', $todoMsg);
				$todoMsg = $lines[0];

				// Remove a ':' from the start
				$colonPos = stripos($todoMsg, ':');
				if ($todoStr !== FALSE) {
					$todoMsg = substr($todoMsg, $colonPos + 1);
				}

				// Trim
				$todoMsg = trim($todoMsg);

				$msg = $this->_getMessage('TODO', $todoMsg);
				$this->_writeError('showTODOs', $msg, $token->line);
			}
		}
	}

	/**
	 * Process a comment.
	 *
	 * @param TokenInfo $token
	 *        	the token
	 */
	private function _processComment($token) {
		// Count the lines of comment
		if ($token->id === T_COMMENT) {
			$this->_ncssTotalSingleComment ++;
			$this->_ncssFileSingleComment ++;
		} else if ($token->id === T_ML_COMMENT) {
			$this->_ncssTotalMultiComment ++;
			$this->_ncssFileMultiComment ++;
		} else if ($token->id === T_DOC_COMMENT) {
			$this->_ncssTotalPhpdoc ++;
			$this->_ncssFilePhpdoc ++;
			$this->_ncssTotalLinesPhpdoc ++;
			$this->_ncssFileLinesPhpdoc ++;
			if ($this->_isFileDocComment()) {
				$this->_processAnnotation(T_FILE, $token->text);
			}
		}

		// Count the @params, @returns and @throw
		if (stripos($token->text, '/**') !== false) {
			// Reset the count of elements if it's a new docblock
			$this->_docblocNbParams = 0;
			$this->_docblocNbReturns = 0;
			$this->_docblocNbThrows = 0;
		}
		if (stripos($token->text, '@param') !== false) {
			$this->_docblocNbParams ++;
		}
		if (stripos($token->text, '@return') !== false) {
			$this->_docblocNbReturns ++;
		}
		if (stripos($token->text, '@throw') !== false) {
			$this->_docblocNbThrows ++;
		}
		if (stripos($token->text, '@inheritdoc') !== false) {
			$this->_docblocInheritDoc = true;
		}

		// Check if the comment starts with '#'
		if ($this->_isActive('noShellComments')) {
			$todoStr = strpos($token->text, '#');
			if ($todoStr === 0) {
				$this->_writeError('noShellComments', $this->_getMessage('NO_SHELL_COMMENTS'));
			}
		}

		// Check if the comment contains a TO DO
		$this->_processTODO($token);
	}

	/**
	 * Utility function to determine whether or not a T_DOC_COMMENT
	 * is at the file level or belongs to a class, interface, method
	 * or member.<br>
	 * <b>Note:<b> This will miss file doc blocks with no new line
	 * before the next token - in this case we can never be sure
	 * what the doc block is aimed at.
	 *
	 * @return true if the next token is a T_NEW_LINE or T_DOC_COMMENT,
	 *         false otherwise.
	 */
	private function _isFileDocComment() {
		$tokenPosition = $this->tokenizer->getCurrentPosition();
		$nextToken = $this->tokenizer->peekNextValidToken($tokenPosition, true);
		return !$this->_inClassStatement && !$this->_inInterfaceStatement && ($nextToken->id === T_NEW_LINE || $nextToken->id === T_DOC_COMMENT);
	}

	/**
	 * Check for the existence of a docblock for the current token
	 * o go back and find the previous token that is not a whitespace
	 * o if it is a access specifier (private, public etc), then
	 * see if private members are excluded from comment check
	 * (input argument specified this). if we find an access
	 * specifier move on to find the next best token
	 * o if it is ABSTRACT or STATIC specifier move on to find the next best token
	 * o if the found token is a T_DOC_COMMENT, then we have a docblock
	 *
	 * This, of course, assumes that the function/class/interface has to be
	 * immediately preceded by docblock. Even regular comments are not
	 * allowed, which I think is okay.
	 *
	 * Launched when a CLASS / FUNCTION or INTERFACE statement is found.
	 *
	 * @param Integer $token
	 *        	T_CLASS, T_FUNCTION or T_INTERFACE
	 * @return true is docblock is found
	 */
	private function _checkDocExists($token) {
		// current token = the token after T_CLASS, T_FUNCTION or T_INTERFACE
		//
		// token positions:
		// . curToken - 1 = T_CLASS/T_FUNCTION/T_INTERFACE
		// . curToken - 2 = whitespace before T_CLASS/T_FUNCTION/T_INTERFACE
		// . curToken - 3 = T_ABSTRACT/T_PUBLIC/T_PROTECTED/T_PRIVATE/T_STATIC
		// or T_DOC_COMMENT, if it is present
		//
		$isPrivateExcluded = $this->_config->getTestProperty('docBlocks', 'excludePrivateMembers');

		// Locate the function, class or interface token
		$functionTokenPosition = $this->tokenizer->getCurrentPosition();
		while (true) {
			// the type - function, class or interface. (Horribly named).
			$functionToken = $this->tokenizer->peekTokenAt($functionTokenPosition);

			$isFunction = $this->tokenizer->checkToken($functionToken, T_FUNCTION);
			$isClass = $this->tokenizer->checkToken($functionToken, T_CLASS);
			$isInterface = $this->tokenizer->checkToken($functionToken, T_INTERFACE);

			if ($isFunction || $isClass || $isInterface) {
				break;
			}
			$functionTokenPosition --;
		}

		// Records the type, as well as the type name for more precise error reporting.
		// Two positions forward from declaration of type.
		$typeToken = $this->tokenizer->peekTokenAt($functionTokenPosition);
		$type = $typeToken->text;
		$nameToken = $this->tokenizer->peekTokenAt($functionTokenPosition + 2);
		$name = $nameToken->text;

		$isOldStyleConstructor = (strtolower($name) === strtolower($this->_currentClassname));
		$isNewStyleConstructor = (strtolower($name) === '__construct');
		if ($isOldStyleConstructor || $isNewStyleConstructor) {
			$type = "constructor";
		}

		$found = false;
		$isPrivate = false;
		$docTokenPosition = $functionTokenPosition - 1;

		// List of tokens to ignore when looking for the docblock
		$tokenToIgnoreList = array(
			T_STATIC,
			T_FINAL,
			T_ABSTRACT,
			T_PROTECTED,
			T_PUBLIC,
			T_WHITESPACE,
			T_TAB,
			T_COMMENT,
			T_ML_COMMENT,
			T_NEW_LINE
		);

		// Go backward and look for a T_DOC_COMMENT
		while (true) {
			$docToken = $this->tokenizer->peekTokenAt($docTokenPosition);

			// if the token is in the list above.
			if ($this->tokenizer->isTokenInList($docToken, $tokenToIgnoreList)) {
				// All these tokens are ignored
			} else if ($this->tokenizer->checkToken($docToken, T_PRIVATE)) {
				$isPrivate = true; // we are in a private function
			} else if ($this->tokenizer->checkToken($docToken, T_DOC_COMMENT)) {
				// We have found a doc comment
				$found = true;
				break;
			} else {
				// Any other token found, we stop
				$found = false;
				break;
			}

			$docTokenPosition --;

			if ($docTokenPosition === 0) {
				break; // special case for beginning of the file
			}
		}

		if ($found) {
			// Doc found, look for annotations
			$this->_processAnnotation($token, $docToken->text);
		} else {
			// No documentation found
			if ($this->_isActive('docBlocks') && !($isPrivateExcluded && $isPrivate)) {
				$msg = $this->_getMessage('MISSING_DOCBLOCK', $type, $name);
				$this->_writeError('docBlocks', $msg);
			}
		}
	}

	/**
	 * Process PHP_DOC looking for annotations.
	 *
	 * @param Integer $token
	 *        	T_CLASS or T_FUNCTION
	 * @param String $comment
	 *        	the comment to analyse
	 */
	private function _processAnnotation($token, $comment) {
		// Read the documentation line by line
		$subToken = strtok($comment, PHP_EOL);
		while ($subToken !== false) {
			// Manage annotations
			$pos = stripos($subToken, "@SuppressWarnings");
			if ($pos !== false) {
				$suppressedCheck = trim(substr($subToken, $pos + strlen("@SuppressWarnings")));
				$supprArray = explode(' ', $suppressedCheck);
				foreach ($supprArray as $supprCheck) {
					$suppressedCheck = trim($supprCheck);
					// Store the suppressed warning in the corresponding array
					if ($token === T_CLASS) {
						$this->_classSuppressWarnings[] = $suppressedCheck;
					} elseif ($token === T_INTERFACE) {
						$this->_interfaceSuppressWarnings[] = $suppressedCheck;
					} elseif ($token === T_FUNCTION) {
						$this->_functionSuppressWarnings[] = $suppressedCheck;
					} elseif ($token === T_FILE) {
						$this->_fileSuppressWarnings[] = $suppressedCheck;
					}
				}
			}

			$subToken = strtok(PHP_EOL);
		}
	}

	/**
	 * Check for silenced call to functions.
	 *
	 * @param String $text
	 *        	The text of the token to test
	 */
	private function _checkSilenced($text) {
		if ($this->_isActive('checkSilencedError')) {
			if (!$this->_config->isException('checkSilencedError', $text)) {
				$previousToken = $this->tokenizer->peekPrvsToken();
				if ($previousToken->id === T_AROBAS) {
					$this->_writeError('checkSilencedError', $this->_getMessage('SILENCED_ERROR'));
				}
			}
		}
	}

	/**
	 * Check for the presence of a mandatory header.
	 */
	private function _checkMandatoryHeader() {
		if ($this->_isActive('mandatoryHeader')) {

			$expectedHeader = $this->_config->getTestProperty('mandatoryHeader', 'header');
			$expectedHeader = trim($expectedHeader);
			$expectedHeader = preg_replace('~[\r\n\t ]+~', '', $expectedHeader);

			$filecontent = $this->tokenizer->content;
			$filecontent = preg_replace('~[\r\n\t ]+~', '', $filecontent);

			if (strpos($filecontent, $expectedHeader) === FALSE) {
				$this->_writeError('mandatoryHeader', $this->_getMessage('MANDATORY_HEADER'));
			}
		}
	}

	/**
	 * Check for deprecated functions.
	 *
	 * @param String $text
	 *        	The text of the token to test
	 */
	private function _checkDeprecation($text) {
		if ($this->_isActive('checkDeprecation')) {
			$key = strtolower($text);
			if (array_key_exists($key, $this->_deprecatedFunctions)) {
				$msg = $this->_getMessage('DEPRECATED_FUNCTION', $this->_deprecatedFunctions[$key]['old'], $this->_deprecatedFunctions[$key]['version'], $this->_deprecatedFunctions[$key]['new']);
				$this->_writeError('checkDeprecation', $msg);
			}
		}
	}

	/**
	 * Check for prohibited functions.
	 *
	 * @param String $functionName
	 *        	The function name
	 */
	private function _checkProhibitedFunctions($functionName) {
		if ($this->_isActive('checkProhibitedFunctions')) {
			if (in_array($functionName, $this->_prohibitedFunctions)) {
				$msg = $this->_getMessage('PROHIBITED_FUNCTION', $functionName);
				$this->_writeError('checkProhibitedFunctions', $msg);
			}
		}
	}

	/**
	 * Check for aliased functions.
	 *
	 * @param String $text
	 *        	The text of the token to test
	 */
	private function _checkAliases($text) {
		if ($this->_isActive('checkAliases')) {
			$key = strtolower($text);
			if (array_key_exists($key, $this->_aliasedFunctions)) {
				$msg = $this->_getMessage('ALIASED_FUNCTION', $this->_aliasedFunctions[$key]['old'], $this->_aliasedFunctions[$key]['new']);
				$this->_writeError('checkAliases', $msg);
			}
		}
	}

	/**
	 * Check for replaced functions.
	 *
	 * @param String $text
	 *        	The text of the token to test
	 */
	private function _checkReplacements($text) {
		if ($this->_isActive('checkReplacements')) {
			$key = strtolower($text);
			if (array_key_exists($key, $this->_replacements)) {
				$msg = $this->_getMessage('REPLACED', $this->_replacements[$key]['old'], $this->_replacements[$key]['new']);
				$this->_writeError('checkReplacements', $msg);
			}
		}
	}

	/**
	 * Check for goto.
	 */
	private function _checkGoTo() {
		if ($this->_isActive('checkGoTo')) {
			/* means we've encountered a goto and the rule is active, so complain */
			$this->_writeError('checkGoTo', $this->_getMessage('GOTO'));
		}
	}

	/**
	 * Check for short open tag.
	 *
	 * @param TokenInfo $token
	 */
	private function _checkShortOpenTag($token) {
		// check if shorthand code tags are allowed
		if ($this->_isActive('noShortPhpCodeTag')) {
			$shortOpenTagPos = strpos($token->text, '<?php');
			if ($shortOpenTagPos === false) {
				$this->_writeError('noShortPhpCodeTag', $this->_getMessage('WRONG_OPEN_TAG'));
			}
		}
	}

	/**
	 * Check for continue.
	 */
	private function _checkContinue() {
		if ($this->_isActive('checkContinue')) {
			/* means we've encountered a continue and the rule is active, so complain */
			$this->_writeError('checkContinue', $this->_getMessage('CONTINUE'));
		}
	}

	/**
	 * Tell is a check is active.
	 *
	 * @param String $check
	 *        	the name of the check
	 * @return a boolean
	 */
	private function _isActive($check) {
		// Check if the check is configured
		$test = $this->_config->getTest($check);
		$active = !empty($test);

		$active = $active && !(in_array($check, $this->_functionSuppressWarnings) || in_array($check, $this->_classSuppressWarnings) || in_array($check, $this->_interfaceSuppressWarnings) || in_array($check, $this->_fileSuppressWarnings));

		return $active;
	}

	/**
	 * Check for empty PHP files.
	 *
	 * @param String $fileName
	 *        	the file name
	 */
	private function _checkEmptyFile($fileName) {
		if ($this->_isActive('checkEmptyFile')) {
			$msg = $this->_getMessage('EMPTY_FILE', $fileName);
			$this->_writeError('checkEmptyFile', $msg);
		}
	}

	/**
	 * Output the error to the selected reporter.
	 *
	 * @param String $check
	 *        	the name of the check
	 * @param String $message
	 *        	the error message
	 * @param int $lineNumber
	 *        	optional line number
	 * @param String $level
	 *        	the error level
	 */
	private function _writeError($check, $message, $lineNumber = null, $level = null) {
		if ($lineNumber === null) {
			$lineNumber = $this->lineNumber;
		}

		// If level not specified, get it in the config
		if ($level === null) {
			$level = $this->_config->getTestLevel($check);
			// If level not found in the config then WARNING by default.
			if ($level === null) {
				$level = WARNING;
			}
		}

		$this->errorCounts[$level] ++;

		$this->_reporter->writeError($lineNumber, $check, $message, $level);
	}

	/**
	 * Process a PHP Open Tag.
	 *
	 * @param TokenInfo $token
	 *        	the control statement.
	 */
	private function _processOpenTag($token) {
		// Check for short open tag
		$this->_checkShortOpenTag($token);

		// Check that the tag is at the beginning of the line
		$this->_checkPhpTagsStartLine($token);
	}

	/**
	 * Process a PHP Close Tag.
	 *
	 * @param TokenInfo $token
	 *        	the control statement.
	 */
	private function _processCloseTag($token) {
		// Check that the tag is at the beginning of the line
		$this->_checkPhpTagsStartLine($token);
	}

	/**
	 * Check that the PHP Open or Close tag is at the beginning of the line..
	 *
	 * @param TokenInfo $token
	 */
	private function _checkPhpTagsStartLine($token) {
		if ($this->_isActive('phpTagsStartLine')) {
			if (!$this->_isLineStart) {
				$this->_writeError('phpTagsStartLine', $this->_getMessage('PHP_TAGS_START_LINE'));
			}
		}
	}

	/**
	 * Check the style of the quotes used.
	 *
	 * @param TokenInfo $token
	 */
	private function _checkPreferQuotes($token) {
		if ($this->_isActive('preferQuotes')) {
			$style = $this->_config->getTestProperty('preferQuotes', 'type');
			if ($style === SINGLE_QUOTE && $token->text === '"') {
				$this->_writeError('preferQuotes', $this->_getMessage("PREFER_SINGLE_QUOTES"));
			} elseif ($style === DOUBLE_QUOTE && $token->text === "'") {
				$this->_writeError('preferQuotes', $this->_getMessage("PREFER_DOUBLE_QUOTES"));
			}
		}
	}

	/**
	 * Check the use of a variable variable ($$a).
	 * Called when the current token is a single $.
	 *
	 * @param TokenInfo $token
	 */
	private function _checkVariableVariable($token) {
		if ($this->_isActive('variableVariable')) {

			if ($this->tokenizer->checkNextToken(T_VARIABLE)) {
				$msg = $this->_getMessage("VARIABLE_VARIABLE", '$' . $this->tokenizer->peekNextToken()->text);
				$this->_writeError('variableVariable', $msg);
			}
		}
	}

	/**
	 * Check the length of a local variable name.
	 *
	 * @param String $name
	 */
	private function _checkVariableNameLength($name) {
		if ($this->_isActive('localScopeVariableLength')) {

			// we remove the $ sign
			$name = substr($name, 1);

			// we check for exceptions to the rule
			if ($this->_config->isException('localScopeVariableLength', $name)) {
				return;
			}

			$length = strlen($name);
			$min = $this->_config->getTestProperty('localScopeVariableLength', 'minLength');
			$max = $this->_config->getTestProperty('localScopeVariableLength', 'maxLength');

			// we check the min length
			if (!empty($min) && ($length < $min)) {
				$msg = $this->_getMessage('VARIABLE_NAMING_LENGTH_SHORT', $name);
				$this->_writeError('localScopeVariableLength', $msg);
			}

			// we check the max length
			if (!empty($max) && ($length > $max)) {
				$msg = $this->_getMessage('VARIABLE_NAMING_LENGTH_LONG', $name);
				$this->_writeError('localScopeVariableLength', $msg);
			}
		}
	}

	/**
	 * Extract a package name from the filename.
	 *
	 * @param String $filename
	 *        	the file name
	 */
	private function _extractPackageName($filename) {
		$filename = str_replace('/', '.', $filename);
		$filename = str_replace('\\', '.', $filename);
		while (strpos($filename, '.') === 0) {
			$filename = substr($filename, 1);
		}
		if (strlen($filename) > 4) { // remove the .php at the end
			$filename = substr($filename, 0, -4);
		}

		// Identify the package name
		$packageName = substr($filename, 0, strrpos($filename, '.'));

		return $packageName;
	}

	/**
	 * Returns the message, for the language chosen.
	 *
	 * @param String $message
	 *        	the message to return
	 * @return a string
	 */
	private function _getMessage() {
		set_error_handler(array(
			$this,
			'_messageErrorHandler'
		), E_ALL);

		$args = func_get_args();
		if (isset($this->messages[$args[0]])) {
			$msg = $this->messages[$args[0]];

			try {
				array_shift($args);
				$formattedMsg = vsprintf($msg, $args);
			} catch (Exception $e) {
				$formattedMsg = $msg;
			}

			set_error_handler(array(
				$this,
				'customErrorHandler'
			), E_ALL);

			return $formattedMsg;
		}

		return FALSE;
	}

	/**
	 * Handles any errors that may be thrown by vsprintf.
	 *
	 * @param Integer $errno
	 *        	Level of the error
	 * @param String $errstr
	 *        	Error message
	 * @return boolean
	 */
	private function _messageErrorHandler($errno, $errstr) {
		if (error_reporting() === 0) {
			return false;
		}

		throw new Exception($errstr, $errno);
	}

	/**
	 * Checks if a string is in an array, but with wildcards.
	 *
	 * @param String $needle
	 *        	the string to search for
	 * @param Array $haystack
	 *        	the array to look in
	 * @return boolean
	 */
	private function _inArray($needle, $haystack) {
		foreach ($haystack as $strand) {
			if (fnmatch($strand, $needle)) {
				return true;
			}
		}
		return false;
	}
}
