<?php
use PHPUnit\Framework\TestCase;

/**
 * Deprecation tests.
 */
class DeprecationTest extends TestCase {

	/**
	 * Test for deprecated php methods rules.
	 */
	public function testDeprecations() {
		$phpcheckstyle = $GLOBALS['PHPCheckstyle'];

		$phpcheckstyle->processFiles(array(
			'./test/sample/bad_deprecation.php'
		));

		$errorCounts = $phpcheckstyle->getErrorCounts();

		$this->assertEquals(0, $errorCounts['error'], 'We expect 0 errors');
		$this->assertEquals(0, $errorCounts['ignore'], 'We expect 0 ignored checks');
		$this->assertEquals(0, $errorCounts['info'], 'We expect 0 info');
		$this->assertEquals(5, $errorCounts['warning'], 'We expect 5 warnings');
	}
}
?>