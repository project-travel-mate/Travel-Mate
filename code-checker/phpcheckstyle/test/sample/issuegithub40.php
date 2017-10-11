<?php

class Test {

	public function test($params) {

		return $this->commit(function () use ($params) {
			$whatever = 5;
			return $whatever;
		});
	}

	private function commit($callback) {
		// complex logic using $callback can go here
		$callback();
	}
}