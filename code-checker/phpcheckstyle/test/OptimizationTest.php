<?php
use PHPUnit\Framework\TestCase;

/**
 * Optimization tests.
 */
class OptimizationTest extends TestCase {

	/**
	 * Test PHP Tags rules.
	 */
	public function testTextAfterClosingTag() {
		$phpcheckstyle = $GLOBALS['PHPCheckstyle'];

		$phpcheckstyle->processFiles(array(
			'./test/sample/bad_optimisation.php'
		));

		$errorCounts = $phpcheckstyle->getErrorCounts();

		$this->assertEquals(0, $errorCounts['error'], 'We expect 0 errors of naming');
		$this->assertEquals(0, $errorCounts['ignore'], 'We expect 0 ignored checks');
		$this->assertEquals(0, $errorCounts['info'], 'We expect 0 info');
		$this->assertEquals(2, $errorCounts['warning'], 'We expect 2 warnings');
	}


}
?>