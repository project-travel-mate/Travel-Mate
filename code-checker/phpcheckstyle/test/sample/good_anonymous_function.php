<?php

// No docbloc needed, the function is anonymous
$var = function ($value = true) {
	if (in_array($value, array('+', '-'))) {
		return true;
	}
};

echo $var('+');