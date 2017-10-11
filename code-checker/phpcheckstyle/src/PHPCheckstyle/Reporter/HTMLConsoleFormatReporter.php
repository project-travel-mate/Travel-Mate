<?php
namespace PHPCheckstyle\Reporter;

/**
 * Writes the errors to the console in HTML format.
 */
class HTMLConsoleFormatReporter extends HTMLFormatReporter {

	/**
	 * Constructor; calls parent's constructor.
	 */
	public function __construct() {
		parent::__construct();
	}

	/**
	 * Writes an HTML fragment to stdout.
	 *
	 * @param $fragment string
	 *        	The HTML fragment to write.
	 */
	protected function writeFragment($fragment) {
		echo $fragment;
	}
}
