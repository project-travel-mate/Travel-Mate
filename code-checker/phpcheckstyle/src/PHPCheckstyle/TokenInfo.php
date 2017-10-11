<?php
namespace PHPCheckstyle;

/**
 * TokenInfo class.
 *
 * This object is returned by the tokenizer.
 *
 * @package classes
 *          @SuppressWarnings checkUnusedVariables
 *
 */
class TokenInfo {

	/**
	 * The token ID.
	 *
	 * @var Integer
	 */
	var $id = null;

	/**
	 * The token text.
	 *
	 * @var String
	 */
	var $text = null;

	/**
	 * The position of the token in the file.
	 *
	 * @var Integer
	 */
	var $position = null;

	/**
	 * The line number.
	 *
	 * @var Integer
	 */
	var $line;

	/**
	 * Return a string representation of the token.
	 *
	 * @return String
	 */
	public function toString() {
		$result = "";
		$result .= "line : " . $this->line;
		$result .= ", pos : " . $this->position;
		$result .= ", id : " . $this->getName($this->id);

		// Rename some special chars
		$text = str_replace("\r\n", "\\r\\n", $this->text);
		$text = str_replace("\r", "\\r", $text);
		$text = str_replace("\n", "\\n", $text);

		$result .= ", text : " . $text;
		return $result;
	}

	/**
	 * Return the name of a token, including the NEW_LINE one.
	 *
	 * @return String the name of the token
	 */
	public function getName() {
		$tagNames = array(
			T_NEW_LINE             => 'T_NEW_LINE',
			T_TAB                  => 'T_TAB',
			T_SEMICOLON            => 'T_SEMICOLON',
			T_BRACES_OPEN          => 'T_BRACES_OPEN',
			T_BRACES_CLOSE         => 'T_BRACES_CLOSE',
			T_PARENTHESIS_OPEN     => 'T_PARENTHESIS_OPEN',
			T_PARENTHESIS_CLOSE    => 'T_PARENTHESIS_CLOSE',
			T_COMMA                => 'T_COMMA',
			T_EQUAL                => 'T_EQUAL',
			T_CONCAT               => 'T_CONCAT',
			T_COLON                => 'T_COLON',
			T_MINUS                => 'T_MINUS',
			T_PLUS                 => 'T_PLUS',
			T_IS_GREATER           => 'T_IS_GREATER',
			T_IS_SMALLER           => 'T_IS_SMALLER',
			T_MULTIPLY             => 'T_MULTIPLY',
			T_DIVIDE               => 'T_DIVIDE',
			T_QUESTION_MARK        => 'T_QUESTION_MARK',
			T_MODULO               => 'T_MODULO',
			T_EXCLAMATION_MARK     => 'T_EXCLAMATION_MARK',
			T_AMPERSAND            => 'T_AMPERSAND',
			T_SQUARE_BRACKET_OPEN  => 'T_SQUARE_BRACKET_OPEN',
			T_SQUARE_BRACKET_CLOSE => 'T_SQUARE_BRACKET_CLOSE',
			T_AROBAS               => 'T_AROBAS',
			T_UNKNOWN              => 'T_UNKNOWN',
			T_DOLLAR               => 'T_DOLLAR',
		);

		if (isset($tagNames[$this->id])) {
			$result = $tagNames[$this->id];
		} else {
			$result = token_name($this->id);
		}

		return $result;
	}
}
