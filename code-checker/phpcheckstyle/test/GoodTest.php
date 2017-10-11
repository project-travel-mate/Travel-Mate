<?php
use PHPUnit\Framework\TestCase;

/**
 * Test the nominal case, these test should not return warnings.
 */
class GoodTest extends TestCase {

	/**
	 * Test for nominal case of good style according to the rules.
	 */
	public function testGood() {
		$phpcheckstyle = $GLOBALS['PHPCheckstyle'];

		$phpcheckstyle->processFiles(array(
			'./test/sample/good.php'
		));

		$errorCounts = $phpcheckstyle->getErrorCounts();

		$this->assertEquals(0, $errorCounts['error'], 'We expect 0 errors');
		$this->assertEquals(0, $errorCounts['ignore'], 'We expect 0 ignored checks');
		$this->assertEquals(0, $errorCounts['info'], 'We expect 0 info');
		$this->assertEquals(0, $errorCounts['warning'], 'We expect 0 warnings');
	}



	/**
	 * Test for good iterators.
	 */
	public function testDoWhile() {
		$phpcheckstyle = $GLOBALS['PHPCheckstyle'];

		$phpcheckstyle->processFiles(array(
			'./test/sample/good_do_while.php'
		));

		$errorCounts = $phpcheckstyle->getErrorCounts();

		$this->assertEquals(0, $errorCounts['error'], 'We expect 0 errors');
		$this->assertEquals(0, $errorCounts['ignore'], 'We expect 0 ignored checks');
		$this->assertEquals(0, $errorCounts['info'], 'We expect 0 info');
		$this->assertEquals(0, $errorCounts['warning'], 'We expect 0 warnings');
	}

	/**
	 * Test for good_anonymous_function.
	 */
	public function testAnonymousFunction() {
		$phpcheckstyle = $GLOBALS['PHPCheckstyle'];

		$phpcheckstyle->processFiles(array(
			'./test/sample/good_anonymous_function.php'
		));

		$errorCounts = $phpcheckstyle->getErrorCounts();

		$this->assertEquals(0, $errorCounts['error'], 'We expect 0 errors');
		$this->assertEquals(0, $errorCounts['ignore'], 'We expect 0 ignored checks');
		$this->assertEquals(0, $errorCounts['info'], 'We expect 0 info');
		$this->assertEquals(0, $errorCounts['warning'], 'We expect 0 warnings');
	}

	/**
	 * Test for try/catch/finally.
	 */
	public function testException() {
		$phpcheckstyle = $GLOBALS['PHPCheckstyle'];

		$phpcheckstyle->processFiles(array(
			'./test/sample/good_exception.php'
		));

		$errorCounts = $phpcheckstyle->getErrorCounts();

		$this->assertEquals(0, $errorCounts['error'], 'We expect 0 errors');
		$this->assertEquals(0, $errorCounts['ignore'], 'We expect 0 ignored checks');
		$this->assertEquals(0, $errorCounts['info'], 'We expect 0 info');
		$this->assertEquals(0, $errorCounts['warning'], 'We expect 0 warnings');
	}
}
?>