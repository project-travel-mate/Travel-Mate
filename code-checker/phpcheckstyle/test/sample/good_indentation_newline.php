<?php
/**
 * Test same line indentation.
 * @SuppressWarnings localScopeVariableLength
 */
class Indentation {

	/**
	 * Test
	 */
	function foo()
	{
		//It wants this bracket to be indented
		$a = 0;

		//code
		if ($a === 1)
		{
			// new code
			echo "toto";
		}
		else
		{
			echo "titi";
		}

	} //And this bracket too

}