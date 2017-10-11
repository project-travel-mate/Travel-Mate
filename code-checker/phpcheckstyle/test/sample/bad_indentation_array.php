<?php
/**
 * This file is an exemple of PHP file containing bad indentation.
 * Tabs are expected for each level of indentation.
 *
 * This file should generate 6 warnings with the default config.
 */
 class Indentation_array {

	/**
 	 * Test.
 	 */
	function test() {

        $aVar = array(
        'x' => 'y'  // same level
        );

        $bVar = array(
            'x' => 'y'  
            ); // extra level

        $cVar = [
        'x' => 'y'  // same level
        ];

        $dVar = [
            'x' => 'y'  
            ]; // extra level
    }
}