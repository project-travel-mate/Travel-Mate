<?php
use PHPUnit\Framework\TestCase;

/**
 * Unused functions and parameters tests.
 */
class UnusedTest extends TestCase {

	/**
	 * Nominal case.
	 */
	public function testGoodUnused() {
		$phpcheckstyle = $GLOBALS['PHPCheckstyle'];

		$phpcheckstyle->processFiles(array(
			'./test/sample/good_unused.php'
		));

		$errorCounts = $phpcheckstyle->getErrorCounts();

		$this->assertEquals(0, $errorCounts['error'], 'We expect 0 errors of naming');
		$this->assertEquals(0, $errorCounts['ignore'], 'We expect 0 ignored checks');
		$this->assertEquals(0, $errorCounts['info'], 'We expect 0 info');
		$this->assertEquals(0, $errorCounts['warning'], 'We expect 0 warnings');
	}

	/**
	 * Test for unused functions or variables.
	 */
	public function testBadUnused() {
		$phpcheckstyle = $GLOBALS['PHPCheckstyle'];

		$phpcheckstyle->processFiles(array(
			'./test/sample/bad_unused.php'
		));

		$errorCounts = $phpcheckstyle->getErrorCounts();

		$this->assertEquals(0, $errorCounts['error'], 'We expect 0 errors of naming');
		$this->assertEquals(0, $errorCounts['ignore'], 'We expect 0 ignored checks');
		$this->assertEquals(0, $errorCounts['info'], 'We expect 0 info');
		$this->assertEquals(4, $errorCounts['warning'], 'We expect 4 warnings');
	}
}
?>