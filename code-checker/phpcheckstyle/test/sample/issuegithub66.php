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
        $classes = array(
            $this->em->getClassMetadata(self::ITEM_GROUP_CLASS),
            $this->em->getClassMetadata(self::SINGLE_IDENT_CLASS),
            $this->em->getClassMetadata(self::SINGLE_IDENT_NO_TO_STRING_CLASS),
            $this->em->getClassMetadata(self::SINGLE_STRING_IDENT_CLASS),
            $this->em->getClassMetadata(self::SINGLE_ASSOC_IDENT_CLASS),
            $this->em->getClassMetadata(self::SINGLE_STRING_CASTABLE_IDENT_CLASS),
            $this->em->getClassMetadata(self::COMPOSITE_IDENT_CLASS),
            $this->em->getClassMetadata(self::COMPOSITE_STRING_IDENT_CLASS),
        );
    }
}
