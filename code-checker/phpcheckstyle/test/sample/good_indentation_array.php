<?php
/**
 * Test 4 spaces indentation.
 * @SuppressWarnings localScopeVariableLength
 */
class Indentation_array {

    /**
     * Test.
     */
    public function foo() {

        $bVar = array(
            'x' => 'y',
            'z' => $this->check()
        );

        $cVar = [
            'x' => 'y',
            'z' => $this->check()
        ];

        unset($bVar, $cVar);
    }

    /**
     * Test2.
     */
    public function check() {
        return;
    }
}
