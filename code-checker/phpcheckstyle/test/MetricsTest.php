<?php
use PHPUnit\Framework\TestCase;

/**
 * Metrics tests.
 */
class MetricsTest extends TestCase {

	/**
	 * Test for different metrics.
	 */
	public function testMetrics() {
		$phpcheckstyle = $GLOBALS['PHPCheckstyle'];

		$phpcheckstyle->processFiles(array(
			'./test/sample/bad_metrics.php'
		));

		$errorCounts = $phpcheckstyle->getErrorCounts();

		$this->assertEquals(0, $errorCounts['error'], 'We expect 0 errors of naming');
		$this->assertEquals(0, $errorCounts['ignore'], 'We expect 0 ignored checks');
		$this->assertEquals(1, $errorCounts['info'], 'We expect 1 info');
		$this->assertEquals(3, $errorCounts['warning'], 'We expect 3 warnings');
	}


}
?>