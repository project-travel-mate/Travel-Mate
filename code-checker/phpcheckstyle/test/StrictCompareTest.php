<?php
use PHPUnit\Framework\TestCase;

/**
 * Strict Compare tests.
 */
class StrictCompareTest extends TestCase {

	/**
	 * Test for for spaces missing or in excedent.
	 */
	public function testStrictCompare() {
		$phpcheckstyle = $GLOBALS['PHPCheckstyle'];

		$phpcheckstyle->processFiles(array(
			'./test/sample/bad_strictcompare.php'
		));

		$errorCounts = $phpcheckstyle->getErrorCounts();

		$this->assertEquals(0, $errorCounts['error'], 'We expect 0 errors of naming');
		$this->assertEquals(0, $errorCounts['ignore'], 'We expect 0 ignored checks');
		$this->assertEquals(0, $errorCounts['info'], 'We expect 0 info');
		$this->assertEquals(4, $errorCounts['warning'], 'We expect 4 warnings');
	}


}
?>