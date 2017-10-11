<!-- PHP tags should be in the begin of a line -->

<?php  // ok

	$a = 0;
?> // ok

	<?php // nok

		echo $a;

	?>  // nok
	
	
 <?php echo $a ?>  // nok