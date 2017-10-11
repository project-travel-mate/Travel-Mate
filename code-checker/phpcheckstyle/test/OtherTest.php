<?php
use PHPUnit\Framework\TestCase;

/**
 * Other tests.
 */
class OtherTest extends TestCase {

	/**
	 * Test others rules.
	 */
	public function testOther() {
		$phpcheckstyle = $GLOBALS['PHPCheckstyle'];

		$phpcheckstyle->processFiles(array(
			'./test/sample/bad_other.php'
		));

		$errorCounts = $phpcheckstyle->getErrorCounts();

		$this->assertEquals(0, $errorCounts['error'], 'We expect 0 errors of naming');
		$this->assertEquals(0, $errorCounts['ignore'], 'We expect 0 ignored checks');
		$this->assertEquals(0, $errorCounts['info'], 'We expect 0 info');
		$this->assertEquals(20, $errorCounts['warning'], 'We expect 20 warnings');
	}


	/**
	 * Test for PHP exceptions during parsing.
	 */
	public function testException() {
		$phpcheckstyle = $GLOBALS['PHPCheckstyle'];

		$phpcheckstyle->processFiles(array(
			'./test/sample/unterminated_comment.php'
		));

		$errorCounts = $phpcheckstyle->getErrorCounts();

		$this->assertEquals(1, $errorCounts['error'], 'We expect 1 error');
		$this->assertEquals(0, $errorCounts['ignore'], 'We expect 0 ignored checks');
		$this->assertEquals(0, $errorCounts['info'], 'We expect 0 info');
		$this->assertEquals(0, $errorCounts['warning'], 'We expect 0 warnings');
	}


	/**
	 * Test for empty PHP file.
	 */
	public function testEmpty() {
		$phpcheckstyle = $GLOBALS['PHPCheckstyle'];

		$phpcheckstyle->processFiles(array(
			'./test/sample/empty.php'
		));

		$errorCounts = $phpcheckstyle->getErrorCounts();

		$this->assertEquals(0, $errorCounts['error'], 'We expect 0 error');
		$this->assertEquals(0, $errorCounts['ignore'], 'We expect 0 ignored checks');
		$this->assertEquals(0, $errorCounts['info'], 'We expect 0 info');
		$this->assertEquals(1, $errorCounts['warning'], 'We expect 1 warnings');
	}


	/**
	 * Test for the presence of a break in a switch case.
	 */
	public function testSwitchCaseNeedBreak() {
		$phpcheckstyle = $GLOBALS['PHPCheckstyle'];

		$phpcheckstyle->processFiles(array(
			'./test/sample/switch_multi_case.php'
		));

		$errorCounts = $phpcheckstyle->getErrorCounts();

		$this->assertEquals(0, $errorCounts['error'], 'We expect 0 error');
		$this->assertEquals(0, $errorCounts['ignore'], 'We expect 0 ignored checks');
		$this->assertEquals(0, $errorCounts['info'], 'We expect 0 info');
		$this->assertEquals(1, $errorCounts['warning'], 'We expect 1 warnings');
	}

}
?>