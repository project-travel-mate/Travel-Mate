<?php
use PHPUnit\Framework\TestCase;

/**
 * PHP tags tests.
 */
class PHPTagsTest extends TestCase {

	/**
	 * Test PHP Tags rules.
	 */
	public function testTextAfterClosingTag() {
		$phpcheckstyle = $GLOBALS['PHPCheckstyle'];

		$phpcheckstyle->processFiles(array(
			'./test/sample/bad_php_tags_text_after_end.php'
		));

		$errorCounts = $phpcheckstyle->getErrorCounts();

		$this->assertEquals(0, $errorCounts['error'], 'We expect 0 errors of naming');
		$this->assertEquals(0, $errorCounts['ignore'], 'We expect 0 ignored checks');
		$this->assertEquals(0, $errorCounts['info'], 'We expect 0 info');
		$this->assertEquals(1, $errorCounts['warning'], 'We expect 1 warnings');
	}

	/**
	 * Test PHP Tags rules.
	 */
	public function testClosingTagNotNeeded() {
		$phpcheckstyle = $GLOBALS['PHPCheckstyle'];

		$phpcheckstyle->processFiles(array(
			'./test/sample/bad_php_tags_end_not_needed.php'
		));

		$errorCounts = $phpcheckstyle->getErrorCounts();

		$this->assertEquals(0, $errorCounts['error'], 'We expect 0 errors of naming');
		$this->assertEquals(0, $errorCounts['ignore'], 'We expect 0 ignored checks');
		$this->assertEquals(0, $errorCounts['info'], 'We expect 0 info');
		$this->assertEquals(1, $errorCounts['warning'], 'We expect 1 warnings');
	}
}
?>