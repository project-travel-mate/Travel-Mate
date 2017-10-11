<?php
use PHPUnit\Framework\TestCase;

/**
 * Alias tests.
 */
class AliasTest extends TestCase {

	/**
	 * Test aliases for depecated methodes rule.
	 */
	public function testAlias() {
		$phpcheckstyle = $GLOBALS['PHPCheckstyle'];

		$phpcheckstyle->processFiles(array(
			'./test/sample/bad_alias.php'
		));

		$errorCounts = $phpcheckstyle->getErrorCounts();

		$this->assertEquals(0, $errorCounts['error'], 'We expect 0 errors');
		$this->assertEquals(0, $errorCounts['ignore'], 'We expect 0 ignored checks');
		$this->assertEquals(0, $errorCounts['info'], 'We expect 0 info');
		$this->assertEquals(3, $errorCounts['warning'], 'We expect 5 warnings');
	}
}
?>