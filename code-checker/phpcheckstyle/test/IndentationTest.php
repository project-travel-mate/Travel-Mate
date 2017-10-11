<?php
use PHPUnit\Framework\TestCase;

/**
 * Indentation tests.
 */
class IndentationTest extends TestCase {

	/**
	 * Test tabs indentation.
	 */
	public function testTabIndentation() {
		$phpcheckstyle = $GLOBALS['PHPCheckstyle'];

		$phpcheckstyle->processFiles(array(
			'./test/sample/bad_indentation.php'
		));

		$errorCounts = $phpcheckstyle->getErrorCounts();

		$this->assertEquals(0, $errorCounts['error'], 'We expect 0 errors');
		$this->assertEquals(0, $errorCounts['ignore'], 'We expect 0 ignored checks');
		$this->assertEquals(0, $errorCounts['info'], 'We expect 0 info');
		$this->assertEquals(6, $errorCounts['warning'], 'We expect 6 warnings');
	}

	/**
	 * Test tabs indentation.
	 */
	public function testSpaceIndentation() {
		$phpcheckstyle = $GLOBALS['PHPCheckstyle'];

		// Change the configuration to check for spaces instead of tabs
		$phpcheckstyle->getConfig()->setTestProperty('indentation', 'type', 'spaces');

		$phpcheckstyle->processFiles(array(
			'./test/sample/bad_indentation.php'
		));

		$errorCounts = $phpcheckstyle->getErrorCounts();

		$this->assertEquals(0, $errorCounts['error'], 'We expect 0 errors');
		$this->assertEquals(0, $errorCounts['ignore'], 'We expect 0 ignored checks');
		$this->assertEquals(0, $errorCounts['info'], 'We expect 0 info');
		$this->assertEquals(11, $errorCounts['warning'], 'We expect 11 warnings');
	}

	/**
	 * Test tabs indentation.
	 */
	public function testSpaceIndentationArray() {
		$phpcheckstyle = $GLOBALS['PHPCheckstyle'];

		// Change the configuration to check for spaces instead of tabs
		$phpcheckstyle->getConfig()->setTestProperty('indentation', 'type', 'spaces');

		$phpcheckstyle->processFiles(array(
			'./test/sample/bad_indentation_array.php'
		));

		$errorCounts = $phpcheckstyle->getErrorCounts();

		$this->assertEquals(0, $errorCounts['error'], 'We expect 0 errors');
		$this->assertEquals(0, $errorCounts['ignore'], 'We expect 0 ignored checks');
		$this->assertEquals(0, $errorCounts['info'], 'We expect 0 info');
		$this->assertEquals(11, $errorCounts['warning'], 'We expect 11 warnings');
	}

	/**
	 * Test tabs indentation.
	 */
	public function testGoodSpaceIndentationArray() {
		$phpcheckstyle = $GLOBALS['PHPCheckstyle'];

		// Change the configuration to check for spaces instead of tabs
		$phpcheckstyle->getConfig()->setTestProperty('indentation', 'type', 'spaces');

		$phpcheckstyle->processFiles(array(
			'./test/sample/good_indentation_array.php'
		));

		$errorCounts = $phpcheckstyle->getErrorCounts();

		$this->assertEquals(0, $errorCounts['error'], 'We expect 0 errors');
		$this->assertEquals(0, $errorCounts['ignore'], 'We expect 0 ignored checks');
		$this->assertEquals(0, $errorCounts['info'], 'We expect 0 info');
		$this->assertEquals(0, $errorCounts['warning'], 'We expect 0 warnings');
	}

	/**
	 * Test for indentation with new line indentation.
	 */
	public function testGoodIndentationNewLine() {
		$phpcheckstyle = $GLOBALS['PHPCheckstyle'];

		// Change the configuration to check for spaces instead of tabs
		$phpcheckstyle->getConfig()->setTestProperty('controlStructOpenCurly', 'position', 'nl');
		$phpcheckstyle->getConfig()->setTestProperty('funcDefinitionOpenCurly', 'position', 'nl');
		$phpcheckstyle->getConfig()->setTestProperty('controlStructElse', 'position', 'nl');


		$phpcheckstyle->processFiles(array(
			'./test/sample/good_indentation_newline.php'
		));

		$errorCounts = $phpcheckstyle->getErrorCounts();

		$this->assertEquals(0, $errorCounts['error'], 'We expect 0 errors');
		$this->assertEquals(0, $errorCounts['ignore'], 'We expect 0 ignored checks');
		$this->assertEquals(0, $errorCounts['info'], 'We expect 0 info');
		$this->assertEquals(0, $errorCounts['warning'], 'We expect 0 warnings');
	}

	/**
	 * Test for indentation with spaces.
	 */
	public function testGoodIndentationSpaces() {
		$phpcheckstyle = $GLOBALS['PHPCheckstyle'];

		// Change the configuration to check for spaces instead of tabs
		$phpcheckstyle->getConfig()->setTestProperty('indentation', 'type', 'spaces');

		$phpcheckstyle->processFiles(array(
			'./test/sample/good_indentation_space.php'
		));

		$errorCounts = $phpcheckstyle->getErrorCounts();

		$this->assertEquals(0, $errorCounts['error'], 'We expect 0 errors');
		$this->assertEquals(0, $errorCounts['ignore'], 'We expect 0 ignored checks');
		$this->assertEquals(0, $errorCounts['info'], 'We expect 0 info');
		$this->assertEquals(0, $errorCounts['warning'], 'We expect 0 warnings');
	}


	/**
	 * Test for for spaces missing or in excedent.
	 */
	public function testBadSpaces() {
		$phpcheckstyle = $GLOBALS['PHPCheckstyle'];

		$phpcheckstyle->processFiles(array(
			'./test/sample/bad_spaces.php'
		));

		$errorCounts = $phpcheckstyle->getErrorCounts();

		$this->assertEquals(0, $errorCounts['error'], 'We expect 0 errors of naming');
		$this->assertEquals(0, $errorCounts['ignore'], 'We expect 0 ignored checks');
		$this->assertEquals(2, $errorCounts['info'], 'We expect 2 info');
		$this->assertEquals(7, $errorCounts['warning'], 'We expect 7 warnings');
	}
}
?>