<?php
namespace PHPCheckstyle\Reporter;

/**
 * Class used to handle multiple reporters.
 */
class Reporters {

	// The list of registered reporters.
	public $reporters = array();

	/**
	 * Add a reporter to the list of registered reporters.
	 *
	 * @param Reporter $reported
	 *        	a Reporter
	 */
	public function addReporter($reported) {
		$this->reporters[] = $reported;
	}

	/**
	 * Any initialization before starting to write should be done here
	 */
	public function start() {
		foreach ($this->reporters as $reporter) {
			$reporter->start();
		}
	}

	/**
	 * Any cleanup work before closing should be done here.
	 */
	public function stop() {
		foreach ($this->reporters as $reporter) {
			$reporter->stop();
		}
	}

	/**
	 * this function called everytime a new file has been started for
	 * checkstyle processing.
	 *
	 * @param $phpFile new
	 *        	file's name
	 */
	public function currentlyProcessing($phpFile) {
		foreach ($this->reporters as $reporter) {
			$reporter->currentlyProcessing($phpFile);
		}
	}

	/**
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
	 * @param $level the
	 *        	severity level
	 */
	public function writeError($line, $check, $message, $level = WARNING) {
		foreach ($this->reporters as $reporter) {
			$reporter->writeError($line, $check, $message, $level);
		}
	}
}
