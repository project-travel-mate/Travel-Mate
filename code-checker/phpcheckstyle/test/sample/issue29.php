<?php 

$a = "foo"; // Variable name too short

$foo = "foo"; // Variable name OK

$veryveryveryverylongvariablenale = "foo"; // Variable name too long

for ($i = 0; $i < 10; $i++) {  // OK, exception to the rule
	$a = $i; 
}