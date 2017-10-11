<?php
// This file is an exemple of PHP file containing bad comments.
// This file should generate 6 warnings with the default config.

# 1 - C style comment : noShellComments :: PHPCHECKSTYLE_NO_SHELL_COMMENTS

class Comments {  // 2 - Class docBlocks

	// 3 - docBlocks : No docblock :: PHPCHECKSTYLE_MISSING_DOCBLOCK
	function testComment($aVariable) { // 4 - docBlocks : testParam ::  PHPCHECKSTYLE_DOCBLOCK_PARAM

		if ($aVariable === null) {
			throw new exception('$aVariable is null');  // 5 - docBlocks : testThrow :: PHPCHECKSTYLE_DOCBLOCK_THROW
		}

		return $aVariable;  // 6 - docBlocks : testThrow :: PHPCHECKSTYLE_DOCBLOCK_RETURN

	}

}
