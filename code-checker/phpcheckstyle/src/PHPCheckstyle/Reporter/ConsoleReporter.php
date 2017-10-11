<?php
namespace PHPCheckstyle\Reporter;

/**
 * Writes the errors to the console
 * Format:
 * ================================
 * FileName Line X: Error Message
 * FileName Line Y: Error Message
 * ================================
 */
class ConsoleReporter extends Reporter {

    /**
     * @var String
     */
    private $reportingLevel;

    public function __construct($level = INFO) {
        $this->reportingLevel = $this->calculateLevel($level);
        parent::__construct();
    }

    /**
	 * {@inheritdoc}
	 *
	 * @param Integer $line
	 *        	the line number
	 * @param String $check
	 *        	the name of the check
	 * @param String $message
	 *        	the text to log
	 * @param String $level
	 *        	the severity level
	 * @SuppressWarnings
	 */
	public function writeError($line, $check, $message, $level = WARNING) {
	    $messageLevel = $this->calculateLevel($level);
	    if ($messageLevel >= $this->reportingLevel) {
            echo "File \"" . $this->currentPhpFile . "\"" . " " . $level . ", line " . $line . " - " . $message . "\n";
        }
	}

    /**
     * Transforms a specified level from a String to an Integer
     * so one can check if the level is above or below a
     * certain threshold.
     *
     * Unknown level will return 1 which is equal to INFO
     *
     * @param String $level
     *
     * @return int
     *          INFO = 1
     *          WARNING = 2
     *          ERROR = 3
     *          unknown = 1
     */
	private function calculateLevel($level) {
	    switch ($level) {
            case INFO:
                return 1;
            case WARNING:
                return 2;
            case ERROR:
                return 3;
            default:
                return 1;
        }
    }
}
