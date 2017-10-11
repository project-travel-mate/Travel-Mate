<?php
namespace PHPCheckstyle\Reporter;

/**
 * Writes the errors into in plain text to the output file
 * Format:
 * ================================
 * File1:
 * Line X: Error Message
 * Line Y: Error Message
 *
 * File 2:
 * Line X: Error Message
 * Line Y: Error Message
 * ================================
 *
 * @author Hari Kodungallur <hkodungallur@spikesource.com>
 */
class PlainFormatReporter extends Reporter {

	private $ofile = "/style-report.txt"; // The output file name

	/**
	 * Constructor; calls parent's constructor
	 *
	 * @param $ofolder the
	 *        	folder name
	 */
	public function __construct($ofolder = false) {
		parent::__construct($ofolder, $this->ofile);
	}

	/**
	 *
	 * @see Reporter::start make sure that the file is opened
	 *
	 */
	public function start() {
		$this->_ensureFileOpen();
	}

	/**
	 *
	 * @see Reporter::stop make sure that the file is closed
	 */
	public function stop() {
		$this->_ensureFileClosed();
	}

	/**
	 *
	 * @see Reporter::stop Add a new line with the new file name
	 *
	 * @param $phpFile the
	 *        	file currently processed
	 */
	public function currentlyProcessing($phpFile) {
		parent::currentlyProcessing($phpFile);
		$this->_write("\nFile: " . $this->currentPhpFile . PHP_EOL);
	}

	/**
	 * {@inheritdoc}
	 *
	 * @param Integer $line
	 *        	the line number
	 * @param String $check
	 *        	the name of the check
	 * @param String $message
	 *        	the text
	 * @param String $level
	 *        	the severity level
	 * @SuppressWarnings checkUnusedFunctionParameters
	 */
	public function writeError($line, $check, $message, $level = WARNING) {
		$msg = "\t" . $level . " Line:" . $line . ": " . $message . PHP_EOL;
		$this->_write($msg);
	}

	private function _write($message) {
		if ($this->_ensureFileOpen()) {
			fwrite($this->fileHandle, $message);
		}
	}

	private function _ensureFileOpen() {
		if ($this->fileHandle === false) {
			$this->fileHandle = fopen($this->outputFile, "w");
		}
		return $this->fileHandle;
	}

	private function _ensureFileClosed() {
		if ($this->fileHandle) {
			fclose($this->fileHandle);
			$this->outputFile = false;
		}
	}
}
