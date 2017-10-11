<?php
/**
 * This file is an exemple of PHP file containing bad indentation.
 * Tabs are expected for each level of indentation.
 *
 * This file should generate 6 warnings with the default config.
 */
 class Indentation {

	/**
 	 * Test.
 	 */
	function test() {

    $aVar = $aVar; // 4 spaces and a token
	$bVar = $bVar; // 1 tab and a token
   $cVar = $cVar; // 3 spaces and a token
     $dVar = $dVar; // 5 spaces and a token
      $eVar = $eVar; // 1 tab, 1 space spaces and a token
 }
}