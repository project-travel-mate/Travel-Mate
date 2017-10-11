<?php
namespace PHPCheckstyle\Reporter;

/**
 * Abstract base class for any type of report generators
 * writeError function is abstract, which will need to be implemented
 * by the deriving class. Also implement start and stop functions
 *
 * @author Hari Kodungallur <hkodungallur@spikesource.com>
 */
abstract class Reporter {

	public $outputFile;

	protected $currentPhpFile;

	/**
	 * Constructor
	 * Intializes variables.
	 * Note that the output file is initialized
	 * to stdout if not provided
	 *
	 * @param $ofolder the
	 *        	output folder
	 * @param $ofile the
	 *        	output filename
	 */
	public function __construct($ofolder = false, $ofile = "error.txt") {

		// creating the folder if it does not already exist.
		if ($ofolder !== false && !file_exists($ofolder)) {
			mkdir($ofolder, 0755, true);
		}

		// setting the output file to default.
		$this->outputFile = $ofolder . $ofile;

		if (!($this->outputFile)) {
			$this->outputFile = "php://output";
		}
	}

	/**
	 * Any initialization before starting to write should be done here
	 */
	public function start() {}

	/**
	 * Any cleanup work before closing should be done here.
	 */
	public function stop() {}

	/**
	 * this function called everytime a new file has been started for
	 * checkstyle processing.
	 *
	 * @param $phpFile new
	 *        	file's name
	 */
	public function currentlyProcessing($phpFile) {
		$this->currentPhpFile = $phpFile;
	}

	/**
	 * abstract function.
	 * For every error, this function is called once with the line where
	 * the error occurred and the actual error message
	 * It is the responsibility of the derived class to appropriately
	 * format it and write it into the output file
	 *
	 * @param $line line
	 *        	number of the error
	 * @param String $check
	 *        	the name of the check
	 * @param $message error
	 *        	message
	 * @param $regex the
	 *        	regular expression (if applicable)
	 * @param $level the
	 *        	severity level
	 */
	public abstract function writeError($line, $check, $message, $level = WARNING);
}
