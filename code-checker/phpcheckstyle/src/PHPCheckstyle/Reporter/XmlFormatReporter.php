<?php
namespace PHPCheckstyle\Reporter;
use DomDocument;

/**
 * Writes the errors into an xml file.
 *
 * Format:
 * ================================
 * <checkstyle>
 * <file name="file1">
 * <error line="M" column="1" severity="error" message="error message"/>
 * </file>
 * <file name="file2">
 * <error line="X" message="error message"/>
 * <error line="Y" message="error message"/>
 * </file>
 * <file name="file3"/>
 * </checkstyle>
 * ================================
 *
 * @author Hari Kodungallur <hkodungallur@spikesource.com>
 */
class XmlFormatReporter extends Reporter {

	private $document = false;

	private $root = false;

	private $currentElement = false;

	private $ofile = "/style-report.xml"; // The output file name

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
	 * @see Reporter::start create the document root (<phpcheckstyle>)
	 *
	 */
	public function start() {
		$this->initXml();
	}

	/**
	 *
	 * @see Reporter::start add the last element to the tree and save the DOM tree to the
	 *      xml file
	 *
	 */
	public function stop() {
		$this->endCurrentElement();
		$this->document->save($this->outputFile);
	}

	/**
	 *
	 * @see Reporter::currentlyProcessing add the previous element to the tree and start a new element
	 *      for the new file
	 *
	 * @param $phpFile the
	 *        	file currently processed
	 */
	public function currentlyProcessing($phpFile) {
		parent::currentlyProcessing($phpFile);
		$this->endCurrentElement();
		$this->startNewElement($phpFile);
	}

	/**
	 * {@inheritdoc}
	 *
	 * @param Integer $line
	 *        	the line number
	 * @param String $check
	 *        	the name of the check
	 * @param String $message
	 *        	error message
	 * @param String $level
	 *        	the severity level
	 */
	public function writeError($line, $check, $message, $level = WARNING) {
		$errEl = $this->document->createElement("error");
		$errEl->setAttribute("line", $line);
		$errEl->setAttribute("column", "1");
		$errEl->setAttribute("severity", $level);
		$errEl->setAttribute("message", $message);
		$errEl->setAttribute("source", $check);

		if (empty($this->currentElement)) {
			$this->startNewElement("");
		}
		$this->currentElement->appendChild($errEl);
	}

	/**
	 * XML header.
	 */
	protected function initXml() {
		$this->document = new DomDocument("1.0");
		$this->root = $this->document->createElement('checkstyle');
		$this->root->setAttribute("version", "1.0.0");
		$this->document->appendChild($this->root);
	}

	/**
	 * Creates a new file element.
	 *
	 * @param string file
	 */
	protected function startNewElement($fileEl) {
		$this->currentElement = $this->document->createElement("file");

		// remove the "./" at the beginning ot the path in case of relative path
		if (substr($fileEl, 0, 2) === './') {
			$fileEl = substr($fileEl, 2);
		}
		$this->currentElement->setAttribute("name", $fileEl);
	}

	/**
	 * Returns the document.
	 *
	 * @return DomDocument object
	 */
	protected function getDocument() {
		return $this->document;
	}

	/**
	 * Close the current element.
	 */
	protected function endCurrentElement() {
		if ($this->currentElement) {
			$this->root->appendChild($this->currentElement);
		}
	}
}

