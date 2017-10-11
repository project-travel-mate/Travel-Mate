<?php

/**
 * test class.
 *
 * Test with symfony coding standards.
 */
class Test {

    /**
     * Test
     *
     * @return result
     */
    function test()
    {
        switch ($a) {
            case Type::TARRAY:
                return 1;
                break;
            case BOOLEAN:
                return 2;
            default:
                return null;
        }
    }
}
