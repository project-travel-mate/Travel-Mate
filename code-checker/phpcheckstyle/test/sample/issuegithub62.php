<?php

/**
 * customized Exception class for CouchDB errors
 */
class CouchException extends Exception {

    // CouchDB response codes we handle specialized exceptions
    protected static $codeSubtypes = [
        401 => 'CouchUnauthorizedException',
        403 => 'CouchForbiddenException',
        404 => 'CouchNotFoundException',
        417 => 'CouchExpectationException'
    ];


    /**
     * Test
     *
     * @return result
     */
    function test()
    {
        $classes = array(
            $this->em->getClassMetadata(self::ITEM_GROUP_CLASS),
            $this->em->getClassMetadata(self::SINGLE_IDENT_CLASS),
            $this->em->getClassMetadata(self::SINGLE_IDENT_NO_TO_STRING_CLASS)
        );
    }

}
