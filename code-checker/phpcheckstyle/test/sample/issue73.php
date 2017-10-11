<?php 

/**
 * Test class.
 */
class Toto {

	/**
	 * Test.
	 */
	public function test() {
		
		// Should raise a warning
		delete();
		
		// Should not raise a warning
		$this->_utilisateur->delete();
		
	}
	
}