#!/usr/bin/php
<?php

/**
 * CLI file to run the PHPCheckstyle
 *
 * @version 1.12.0
 */
function usage() {
	$options = array(
		"--src" =>
			"Root of the source directory tree or a file (can be repeated for multiple sources).",

		"--exclude" =>
			"[Optional] A directory or file that needs to be excluded (can be repeated for multiple exclusions).",

		"--format" =>
			"[Optional] Output format (html/text/xml/xml_console/console/html_console). Defaults to 'html'.",

		"--outdir" =>
			"[Optional] Report Directory. Defaults to './style-report'.",

		"--config" =>
			"[Optional] The name of the config file'.",

		"--debug" =>
			"[Optional] Add some debug logs (warning, very verbose)'.",

		"--linecount" =>
			"[Optional] Generate a report on the number of lines of code (JavaNCSS format)'.",

		"--progress" =>
			"[Optional] Prints a message noting the file and every line that is covered by PHPCheckStyle.",

		"--lang" =>
			"[Optional] Language file to use for the result (en-us by default).",

        "--level" =>
            "[Optional] Specifies the output level (info, warning, error). For console report only.",

		"--quiet" =>
			"[Optional] Quiet mode.",

		"--time" =>
			"[Optional] Display the process time in ms          .",

		"--help" =>
			"Display this usage information.",
	);

	echo "Usage: " . $_SERVER['argv'][0] . " <options>\n";
	echo "\n";
	echo "    Options: \n";
	foreach ($options as $option => $description) {
		echo "       " . str_pad($option, 16, " ") . $description . "\n";
	}
	exit();
}

// default values
$options['src'] = false;
$options['exclude'] = array();
$options['format'] = "html"; // default format
$options['outdir'] = "./style-report"; // default ouput directory
$options['config'] = "default.cfg.xml";
$options['debug'] = false;
$options['progress'] = false;
$options['lang'] = 'en-us';
$options['quiet'] = false;
$options['time'] = false;
$lineCountFile = null;

// loop through user input
for ($i = 1; $i < $_SERVER["argc"]; $i ++) {
	switch ($_SERVER["argv"][$i]) {
		case "--src":
			$i++;
			$options['src'][] = $_SERVER['argv'][$i];
			break;

		case "--outdir":
			$i++;
			$options['outdir'] = $_SERVER['argv'][$i];
			break;

		case "--exclude":
			$i++;
			$options['exclude'][] = $_SERVER['argv'][$i];
			break;

		case "--format":
			$i++;
			$options['format'] = $_SERVER['argv'][$i];
			break;

		case "--lang":
			$i++;
			$options['lang'] = $_SERVER['argv'][$i];
			break;

        case "--level":
            $i++;
            $options['level'] = $_SERVER['argv'][$i];
            break;

		case "--config":
			$i++;
			$options['config'] = $_SERVER['argv'][$i];
			break;

		case "--debug":
			$options['debug'] = true;
			break;

		case "--linecount":
			$options['linecount'] = true;
			break;

		case "--progress":
			$options['progress'] = true;
			break;

		case "--quiet":
			$options['quiet'] = true;
			break;

		case "--time":
			$options['time'] = true;
			break;

		case "--help":
			usage();
			break;

		default:
			usage();
			break;
	}
}

define("PHPCHECKSTYLE_HOME_DIR", dirname(__FILE__));
// require_once PHPCHECKSTYLE_HOME_DIR."/src/PHPCheckstyle.php";
require_once "vendor/autoload.php";

// check for valid format and set the output file name
// right now the output file name is not configurable, only
// the output directory is configurable (from command line)
$knownFormats = array(
	'html',
	'html_console',
	'console',
	'text',
	'xml',
	'xml_console',
	'array',
	'null'
);
$formats = explode(',', $options['format']);
$unknownFormats = array_diff($formats, $knownFormats);
if (!empty($unknownFormats)) {
	echo sprintf("\nUnknown format %s.\n\n", implode(', ', $unknownFormats));
	usage();
}

// check that source directory is specified and is valid
if (!$options['src']) {
	echo "\nPlease specify a source directory/file using --src option.\n\n";
	usage();
}

if (!empty($options['linecount'])) {
	$lineCountFile = "ncss.xml";
}

if ($options['time']) {
	$time_start = microtime();
}
$level = INFO;
if (isset($options['level'])) {
    switch ($options['level']) {
        case INFO:
            $level = INFO;
            break;
        case WARNING:
            $level = WARNING;
            break;
        case ERROR:
            $level = ERROR;
            break;
        default:
            echo "Unkown level '" . $options['level'] . "'. Ingnoring level'\n";
    }
}


$style = new PHPCheckstyle\PHPCheckstyle($formats, $options['outdir'], $options['config'], $lineCountFile, $options['debug'], $options['progress'], $level);

if (file_exists(__DIR__ . '/src/PHPCheckstyle/Lang/' . $options['lang'] . '.ini')) {
	$style->setLang($options['lang']);
}

$style->processFiles($options['src'], $options['exclude']);

$errorCounts = $style->getErrorCounts();

if (!$options['quiet']) {
 	echo PHP_EOL . "Summary" . PHP_EOL;
 	echo "=======" . PHP_EOL . PHP_EOL;
 	echo "Errors:   " . $errorCounts[ERROR] . PHP_EOL;
 	echo "Ignores:  " . $errorCounts[IGNORE] . PHP_EOL;
 	echo "Infos:    " . $errorCounts[INFO] . PHP_EOL;
 	echo "Warnings: " . $errorCounts[WARNING] . PHP_EOL;
 	echo "=======" . PHP_EOL . PHP_EOL;
 	echo "Reporting Completed." . PHP_EOL;

 	if ($options['time']) {
 		$time_end = microtime();
 		$time = $time_end - $time_start;

 		echo "Processing ended in " . $time . " ms" . PHP_EOL;
 	}

}




$exitCode = ($errorCounts[ERROR] > 0) ? 1 : 0;
exit($exitCode);
