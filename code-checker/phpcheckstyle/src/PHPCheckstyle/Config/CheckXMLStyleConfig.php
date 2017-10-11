<?php
namespace PHPCheckstyle\Config;

/**
 * Loads the test configuration.
 *
 * @author Hari Kodungallur <hkodungallur@spikesource.com>
 * @SuppressWarnings checkUnusedPrivateFunctions
 */
class CheckXMLStyleConfig extends CheckStyleConfig {

	// The configuration file
	private $file;
	private $currentTest = false;
	private $currentConfig = false;
	private $xmlParser;

	/**
	 * Constructor.
	 *
	 * @param String $configFile
	 *        	The path of the config file
	 */
	public function __construct($configFile) {

		// If the path is a valid file we use it as is
		if (is_file($configFile)) {
			$this->file = $configFile;
		} else {
			// Otherwise we look in the config directory
			$this->file = PHPCHECKSTYLE_HOME_DIR . "/config/" . $configFile;

			if (!is_file($this->file)) {
				echo "Config file not found : " . $configFile;
				exit(0);
			}
		}

		$this->xmlParser = xml_parser_create();
		xml_set_object($this->xmlParser, $this);
		xml_set_element_handler($this->xmlParser, "_startElement", "_endElement");
		xml_set_character_data_handler($this->xmlParser, "_gotCdata");
		xml_set_default_handler($this->xmlParser, "_gotCdata");
	}

	/**
	 * Destructor.
	 */
	public function __destruct() {
		if ($this->xmlParser !== null && is_resource($this->xmlParser)) {
			xml_parser_free($this->xmlParser);
		}
	}

	/**
	 * Parses the configuration file and stores the values.
	 *
	 * @throws Exception if cannot access file
	 */
	public function parse() {
		$fp = fopen($this->file, "r");
		if (!$fp) {
			throw new Exception("Could not open XML input file");
		}

		$data = fread($fp, 4096);
		while ($data) {
			if (!xml_parse($this->xmlParser, $data, feof($fp))) {
				$errorString = xml_error_string(xml_get_error_code($this->xmlParser));
				$errorLineNo = xml_get_current_line_number($this->xmlParser);
				$msg = sprintf("Warning: XML error: %s at line %d", $errorString, $errorLineNo);
				echo $msg;
				$this->config = array();
			}

			$data = fread($fp, 4096);
		}
	}

	/**
	 * SAX function indicating start of an element
	 * Store the TEST and PROPERTY values in an array
	 *
	 * @param Parser $parser
	 *        	the parser
	 * @param Elem $elem
	 *        	name of element
	 * @param Attributes $attrs
	 *        	list of attributes of the element
	 * @SuppressWarnings cyclomaticComplexity checkUnusedFunctionParameters
	 */
	private function _startElement($parser, $elem, $attrs) {
		switch ($elem) {

			// Case of a configuration property
			case 'CONFIG':
				$this->currentConfig = strtolower($attrs['NAME']);
				$this->config[$this->currentConfig] = array();
				break;

			// Case of a configuration property item
			case 'CONFIGITEM':
				$this->config[$this->currentConfig][] = $attrs['VALUE'];
				break;

			// Case of a test rule
			case 'TEST':
				$this->currentTest = strtolower($attrs['NAME']);
				$this->config[$this->currentTest] = array();

				if (isset($attrs['LEVEL'])) {
					$this->config[$this->currentTest]['level'] = $attrs['LEVEL'];
				}

				if (isset($attrs['REGEXP'])) {
					$this->config[$this->currentTest]['regexp'] = $attrs['REGEXP'];
				}
				break;

			// Case of a propertie of a rule (name / value)
			case 'PROPERTY':
				$pname = $attrs['NAME'];
				$pval = true;
				if (array_key_exists('VALUE', $attrs)) {
					$pval = $attrs['VALUE'];
				}
				$this->config[$this->currentTest][strtolower($pname)] = $pval;
				break;

			// Case of a item of a list of values of a rule
			case 'ITEM':
				if (isset($attrs['VALUE'])) {
					$this->config[$this->currentTest]['item'][] = $attrs['VALUE'];
				}
				break;

			// Case of an exception to a rule
			case 'EXCEPTION':
				if (isset($attrs['VALUE'])) {
					$this->config[$this->currentTest]['exception'][] = $attrs['VALUE'];
				}
				break;

			// Case of a deprecated function
			case 'DEPRECATED':
				if (isset($attrs['OLD'])) {
					$this->config[$this->currentTest][strtolower($attrs['OLD'])]['old'] = $attrs['OLD'];
				}
				if (isset($attrs['NEW'])) {
					$this->config[$this->currentTest][strtolower($attrs['OLD'])]['new'] = $attrs['NEW'];
				}
				if (isset($attrs['VERSION'])) {
					$this->config[$this->currentTest][strtolower($attrs['OLD'])]['version'] = $attrs['VERSION'];
				}
				break;

			// Case of an alias function
			case 'ALIAS':
				if (isset($attrs['OLD'])) {
					$this->config[$this->currentTest][strtolower($attrs['OLD'])]['old'] = $attrs['OLD'];
				}
				if (isset($attrs['NEW'])) {
					$this->config[$this->currentTest][strtolower($attrs['OLD'])]['new'] = $attrs['NEW'];
				}
				break;

			// Case of a replacement
			case 'REPLACEMENT':
				if (isset($attrs['OLD'])) {
					$this->config[$this->currentTest][strtolower($attrs['OLD'])]['old'] = $attrs['OLD'];
				}
				if (isset($attrs['NEW'])) {
					$this->config[$this->currentTest][strtolower($attrs['OLD'])]['new'] = $attrs['NEW'];
				}
				break;

			default:
				break;
		}
	}

	/**
	 * SAX function indicating end of element
	 * Currently we dont need to do anything here
	 *
	 * @param Parser $parser
	 * @param String $name
	 */
	private function _endElement($parser, $name) {}

	/**
	 * SAX function for processing CDATA
	 * Currently we dont need to do anything here
	 *
	 * @param Parser $parser
	 * @param String $name
	 */
	private function _gotCdata($parser, $name) {}
}
