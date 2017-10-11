<?php
use PHPUnit\Framework\TestCase;

/**
 * Prohibited functions tests.
 */
class ProhibitedTest extends TestCase {

	/**
	 * Test for prohibited functions.
	 */
	public function testProhibited() {
		$phpcheckstyle = $GLOBALS['PHPCheckstyle'];

		$phpcheckstyle->processFiles(array(
			'./test/sample/bad_prohibited.php'
		));

		$errorCounts = $phpcheckstyle->getErrorCounts();

		$this->assertEquals(0, $errorCounts['error'], 'We expect 0 errors of naming');
		$this->assertEquals(0, $errorCounts['ignore'], 'We expect 0 ignored checks');
		$this->assertEquals(0, $errorCounts['info'], 'We expect 0 info');
		$this->assertEquals(2, $errorCounts['warning'], 'We expect 2 warnings');
	}


}
?>