<?php 

App::error(function() {
	if (Config::get('app.debug') === FALSE) {
		echo "Not empty";
	}
});