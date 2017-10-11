<?php
namespace PHPCheckstyle;
use \Exception;

/**
 * Lexical Analysis.
 *
 * Class that stores the tokens for a particular class and provide
 * utility functions like getting the next/previous token,
 * checking whether the token is of particular type etc.
 *
 * Based on the internal PHP tokenizer but separate the NEW_LINE and the TAB tokens.
 *
 * @see http://www.php.net/manual/en/tokens.php
 * @author Hari Kodungallur <hkodungallur@spikesource.com>
 *
 */
class Tokenizer {

	/**
	 * Indicate if the "short_open_tag" is off.
	 *
	 * @var Boolean
	 */
	private $shortOpenTagOff = false;

	/**
	 * The file content.
	 *
	 * @var String
	 */
	public $content = null;

	/**
	 * The array of tokens in a file.
	 *
	 * @var Array[TokenInfo]
	 */
	private $tokens;

	/**
	 * The array of "new" tokens.
	 *
	 * @var Array
	 */
	private $newTokens = array();

	/**
	 * Position of the index in the current file.
	 *
	 * @var Integer
	 */
	private $index = 0;

	/**
	 * Number of lines in the file.
	 *
	 * @var Integer
	 */
	private $lineNumber = 1;

	/**
	 * Number of tokens in the file.
	 *
	 * @var Integer
	 */
	private $tokenNumber = 0;

	/**
	 * List of tokens that should be ignored by the Tokenizer.
	 *
	 * @var Array
	 */
	private $ignoreTokens;

	/**
	 * Constructor
	 */
	public function __construct() {
		// Detect the php.ini settings
		$this->shortOpenTagOff = (ini_get('short_open_tag') === false);

		/*if ($this->shortOpenTagOff) {
			echo "Warning : The short_open_tag value of your php.ini setting is off, you may want to activate it for correct code analysis";
		}*/

		$this->ignoreTokens = array(
			T_WHITESPACE  => true,
			T_TAB         => true,
			T_COMMENT     => true,
			T_ML_COMMENT  => true,
			T_DOC_COMMENT => true,
		);

		$this->reset();
	}

	/**
	 * Tokenizes the input php file and stores all the tokens in the
	 * $this->tokens variable.
	 *
	 * @param String $filename
	 *        	the line where the token is found
	 */
	public function tokenize($filename) {

		$this->tokens = array();

		// Read the file
		if (filesize($filename)) {
			$fp = fopen($filename, "rb");
			$this->content = fread($fp, filesize($filename));
			$this->tokens = $this->_getAllTokens($this->content);
			fclose($fp);
		}

	}

	/**
	 * Dump the tokens of the file.
	 *
	 * @return String
	 */
	public function dumpTokens() {
		$result = "";
		foreach ($this->tokens as $token) {
			$result .= $token->toString() . PHP_EOL;
		}
		return $result;
	}

	/**
	 * Gets the next token.
	 *
	 * In the process moves the index to the next position.
	 *
	 * @return TokenInfo
	 */
	public function getNextToken() {
		if ($this->index < (count($this->tokens) - 1)) {

			// Increment the index
			$this->index ++;

			// Return the new token
			return $this->tokens[$this->index];
		} else {
			return false;
		}
	}

	/**
	 * Return the number of tokens in the file.
	 *
	 * @return Integer
	 */
	public function getTokenNumber() {
		return $this->tokenNumber;
	}

	/**
	 * Gets the current token.
	 *
	 * @return TokenInfo
	 */
	public function getCurrentToken() {
		return $this->tokens[$this->index];
	}

	/**
	 * Get the token at a given position.
	 *
	 * @param Integer $position
	 *        	the position of the token
	 * @return TokenInfo the token found
	 */
	public function peekTokenAt($position) {
		if ($position < count($this->tokens)) {
			return $this->tokens[$position];
		} else {
			return null;
		}
	}

	/**
	 * Gives the current position in the tokenizer.
	 *
	 * @return current position of the Tokenizer
	 */
	public function getCurrentPosition() {
		return $this->index;
	}

	/**
	 * Returns the next token without moving
	 * the index.
	 *
	 * @return TokenInfo if the token is found (and update the line value)
	 */
	public function peekNextToken() {
		if ($this->index < (count($this->tokens) - 1)) {
			return $this->tokens[$this->index + 1];
		} else {
			return null;
		}
	}

	/**
	 * Peeks at the previous token.
	 * That is it returns the previous token
	 * without moving the index.
	 *
	 * @return TokenInfo
	 */
	public function peekPrvsToken() {
		if ($this->index > 0) {
			return $this->tokens[$this->index - 1];
		} else {
			return null;
		}
	}

	/**
	 * Peeks at the next valid token.
	 * A valid token is one that is neither a whitespace or a comment
	 *
	 * @param Integer $startPos
	 *        	the start position for the search (if the item on this position is valid, it will be returned)
	 * @param Boolean $stopOnNewLine
	 *        	Indicate if we need to stop when we meet a new line
	 * @return TokenInfo the info about the token found
	 */
	public function peekNextValidToken($startPos = null, $stopOnNewLine = false) {
		// define the start position
		$pos = $this->getCurrentPosition() + 1; // default position for the search
		if ($startPos !== null) {
			$pos = $startPos; // if defined, set the start position
		}

		// search for the next valid token
		$token = null;
		$nbTokens = count($this->tokens);

		while ($pos < $nbTokens) {
			$token = $this->tokens[$pos];
			$pos ++;

			if (isset($this->ignoreTokens[$token->id])) {
				continue;
			} else if ($token->id === T_NEW_LINE) {
				if ($stopOnNewLine) {
					break;
				} else {
					continue;
				}
			} else {
				break;
			}
		}

		return $token;
	}

	/**
	 * Peeks at the previous valid token.
	 * A valid token is one that is neither a whitespace or a comment
	 *
	 * @param Integer $startPos a token position (optional).
	 * @return TokenInfo the info about the token found
	 */
	public function peekPrvsValidToken($startPos = null) {

		if ($startPos === null) {
			// default position for the search
			$pos = $this->getCurrentPosition() - 1;
		} else {
			// if defined, set the start position
			$pos = $startPos;
		}

		$token = null;
		while ($pos > 0) {

			$token = $this->tokens[$pos];
			$pos --;

			if (isset($this->ignoreTokens[$token->id])) {
				continue;
			} else if ($token->id === T_NEW_LINE) {
				continue;
			} else {
				break;
			}
		}

		return $token;
	}

	/**
	 * Resets all local variables
	 *
	 * @return
	 *
	 * @access public
	 */
	public function reset() {
		$this->index = 0;
		$this->tokens = array();
		$this->newTokens = array();
		$this->tokenNumber = 0;
		$this->lineNumber = 1;
	}

	/**
	 * Check if a token is equal to a given token ID
	 *
	 * @param TokenInfo $token
	 *        	the token to test
	 * @param Integer $id
	 *        	the token ID we're looking for
	 * @param String $text
	 *        	(optional) the text we're looking for
	 * @return Boolean true if the token correspond
	 */
	public function checkToken($token, $id, $text = false) {
		$result = false;
		if ($token->id === $id) {
			if ($text) {
				$result = $token->text === $text;
			} else {
				$result = true;
			}
		}
		return $result;
	}

	/**
	 * Check if the previous valid token (ignoring whitespace) correspond to the specified token.
	 *
	 * @param Integer $id
	 *        	the token ID we're looking for
	 * @param String $text
	 *        	(optional) the text we're looking for
	 * @return Boolean true if the token is found
	 */
	public function checkPreviousValidToken($id, $text = false) {
		$token = $this->peekPrvsValidToken();
		return $this->checkToken($token, $id, $text);
	}

	/**
	 * Check if the previous valid token (ignoring whitespace) correspond to the specified token.
	 *
	 * @param Integer $id
	 *        	the token ID we're looking for
	 * @param String $text
	 *        	(optional) the text we're looking for
	 * @return Boolean true if the token is found
	 */
	public function checkPreviousToken($id, $text = false) {
		$token = $this->peekPrvsToken();
		return $this->checkToken($token, $id, $text);
	}

	/**
	 * Check if a the next token exists (and if its value correspond to what is expected).
	 *
	 * @param Integer $id
	 *        	the token we're looking for
	 * @param String $text
	 *        	(optional) the text we're looking for
	 * @return Boolean true if the token is found
	 */
	public function checkNextToken($id, $text = false) {
		$token = $this->peekNextToken();
		return $this->checkToken($token, $id, $text);
	}

	/**
	 * Check if a the next token exists (and if its value correspond to what is expected).
	 *
	 * @param Integer $id
	 *        	the token we're looking for
	 * @param String $text
	 *        	(optional) the text we're looking for
	 * @return Boolean true if the token is found
	 */
	public function checkCurrentToken($id, $text = false) {
		$token = $this->getCurrentToken();
		return $this->checkToken($token, $id, $text);
	}

	/**
	 * Find the next position of the string.
	 *
	 * @param String $text
	 *        	the text we're looking for
	 * @param Integer $apos
	 *        	the position to start from (by defaut will use current position)
	 * @return Integer the position, null if not found
	 */
	public function findNextStringPosition($text, $apos = null) {
		if ($apos === null) {
			$pos = $this->getCurrentPosition();
		} else {
			$pos = $apos;
		}
		$pos += 1; // Start from the token following the current position

		$nbTokens = count($this->tokens);
		while ($pos < $nbTokens) {
			$token = $this->tokens[$pos];

			if ($text === $token->text) {
				return $pos;
			}

			$pos ++;
		}

		return null;
	}

	/**
	 * Check if the next valid token (ignoring whitespaces) correspond to the specified token.
	 *
	 * @param Integer $id
	 *        	the id of the token we're looking for
	 * @param String $text
	 *        	the text we're looking for
	 * @param Integer $startPos
	 *        	the start position
	 * @return true if the token is found
	 */
	public function checkNextValidToken($id, $text = false, $startPos = null) {
		$tokenInfo = $this->peekNextValidToken($startPos);

		if ($tokenInfo !== null) {
			return $this->checkToken($tokenInfo, $id, $text);
		} else {
			return false;
		}
	}

	/**
	 * Identify the token and enventually split the new lines.
	 *
	 * @param String $tokenText
	 *        	The token text
	 * @param Integer $tokenText
	 *        	The token text
	 * @return an array of tokens
	 */
	private function _identifyTokens($tokenText, $tokenID) {
		// Split the data up by newlines
		// To correctly handle T_NEW_LINE inside comments and HTML
		$splitData = preg_split('#(\r\n|\n|\r)#', $tokenText, null, PREG_SPLIT_DELIM_CAPTURE | PREG_SPLIT_NO_EMPTY);
		foreach ($splitData as $data) {

			$tokenInfo = new TokenInfo();
			$tokenInfo->text = $data;
			$tokenInfo->position = $this->tokenNumber;
			$tokenInfo->line = $this->lineNumber;

			if ($data === "\r\n" || $data === "\n" || $data === "\r") {
				// This is a new line token
				$tokenInfo->id = T_NEW_LINE;
				$this->lineNumber ++;
			} else if ($data === "\t") {
				// This is a tab token
				$tokenInfo->id = T_TAB;
			} else {
				// Any other token
				$tokenInfo->id = $tokenID;
			}

			$this->tokenNumber ++;

			// Added detections
			if ($tokenInfo->id === T_UNKNOWN) {
				switch ($tokenInfo->text) {
					case ";":
						$tokenInfo->id = T_SEMICOLON;
						break;
					case "{":
						$tokenInfo->id = T_BRACES_OPEN;
						break;
					case "}":
						$tokenInfo->id = T_BRACES_CLOSE;
						break;
					case "(":
						$tokenInfo->id = T_PARENTHESIS_OPEN;
						break;
					case ")":
						$tokenInfo->id = T_PARENTHESIS_CLOSE;
						break;
					case ",":
						$tokenInfo->id = T_COMMA;
						break;
					case "=":
						$tokenInfo->id = T_EQUAL;
						break;
					case ".":
						$tokenInfo->id = T_CONCAT;
						break;
					case ":":
						$tokenInfo->id = T_COLON;
						break;
					case "-":
						$tokenInfo->id = T_MINUS;
						break;
					case "+":
						$tokenInfo->id = T_PLUS;
						break;
					case ">":
						$tokenInfo->id = T_IS_GREATER;
						break;
					case "<":
						$tokenInfo->id = T_IS_SMALLER;
						break;
					case "*":
						$tokenInfo->id = T_MULTIPLY;
						break;
					case "/":
						$tokenInfo->id = T_DIVIDE;
						break;
					case "?":
						$tokenInfo->id = T_QUESTION_MARK;
						break;
					case "%":
						$tokenInfo->id = T_MODULO;
						break;
					case "!":
						$tokenInfo->id = T_EXCLAMATION_MARK;
						break;
					case "&":
						$tokenInfo->id = T_AMPERSAND;
						break;
					case "[":
						$tokenInfo->id = T_SQUARE_BRACKET_OPEN;
						break;
					case "]":
						$tokenInfo->id = T_SQUARE_BRACKET_CLOSE;
						break;
					case "@":
						$tokenInfo->id = T_AROBAS;
						break;
					case '"':
						$tokenInfo->id = T_QUOTE;
						break;
					case '$':
						$tokenInfo->id = T_DOLLAR;
						break;
					default:
				}
			}

			$this->newTokens[] = $tokenInfo;
		}

		return $this->newTokens;
	}

	/**
	 * Tokenize a string and separate the newline token.
	 *
	 * Found here : http://php.net/manual/function.token-get-all.php
	 *
	 * @param String $source
	 *        	The source code to analyse
	 * @return array
	 * @SuppressWarnings checkUnusedVariables
	 */
	private function _getAllTokens($source) {
		$newTokens = array();

		// Ugly trick
		// Reset the error array by calling an undefined variable
		set_error_handler('var_dump', 0);
		@$errLastResetUndefinedVar;
		restore_error_handler();

		// Get the tokens
		$tokens = @token_get_all($source);

		// Check for parsing errors
		$parsingErrors = error_get_last();
		if (!empty($parsingErrors) && $parsingErrors["type"] === 128) {
			throw new Exception($parsingErrors["message"]);
		}

		// Check each token and transform into an Object
		foreach ($tokens as $token) {
			$isTokenArray = is_array($token);

			$tokenID = $isTokenArray ? $token[0] : T_UNKNOWN;
			$tokenText = $isTokenArray ? $token[1] : $token;

			// Manage T_OPEN_TAG when php.ini setting short_open_tag is Off.
			if ($this->shortOpenTagOff && $tokenID === T_INLINE_HTML) {

				$startPos = strpos($tokenText, SHORT_OPEN_TAG);
				$endPos = strpos($tokenText, CLOSE_TAG, $startPos + strlen(SHORT_OPEN_TAG));

				// Extract the content of the short_open_tag
				while (strlen($tokenText) > 2 && $startPos !== false && $endPos !== false) {

					// Parse the beginning of the text
					$beforeText = substr($tokenText, 0, $startPos);
					$this->_identifyTokens($beforeText, $tokenID);

					// The open tag
					$openTag = new TokenInfo();
					$openTag->id = T_OPEN_TAG;
					$openTag->text = SHORT_OPEN_TAG;
					$this->tokenNumber++;
					$openTag->position = $this->tokenNumber;
					$openTag->line = $this->lineNumber;
					$this->newTokens[] = $openTag;

					// Tokenize the content
					$inlineText = substr($tokenText, $startPos + strlen(SHORT_OPEN_TAG), $endPos - $startPos);
					$inlineText = substr($inlineText, 0, -strlen(CLOSE_TAG));

					$inline = $this->_getAllTokens(OPEN_TAG . " " . $inlineText);

					array_shift($inline); // remove <?php
					$this->_identifyTokens($newTokens, $inline);

					// Add the close tag
					$closeTag = new TokenInfo();
					$closeTag->id = T_CLOSE_TAG;
					$closeTag->text = CLOSE_TAG;
					$this->tokenNumber++;
					$closeTag->position = $this->tokenNumber;
					$closeTag->line = $this->lineNumber;
					$this->newTokens[] = $closeTag;

					// text = the remaining text
					$tokenText = substr($tokenText, $endPos + strlen(SHORT_OPEN_TAG));

					$startPos = strpos($tokenText, SHORT_OPEN_TAG);
					$endPos = strpos($tokenText, CLOSE_TAG, $startPos + strlen(SHORT_OPEN_TAG));
				}
			}

			// Identify the tokens
			$this->_identifyTokens($tokenText, $tokenID);
		}

		return $this->newTokens;
	}

	/**
	 * Find the position of the closing parenthesis corresponding to the current position opening parenthesis.
	 *
	 * @param Integer $startPos
	 * @return Integer $closing position
	 */
	public function findClosingParenthesisPosition($startPos) {
		// Find the opening parenthesis after current position
		$pos = $this->findNextStringPosition('(', $startPos);
		$parenthesisCount = 1;

		$pos += 1; // Skip the opening parenthesis

		$nbTokens = count($this->tokens);
		while ($parenthesisCount > 0 && $pos < $nbTokens) {
			// Look for the next token
			$token = $this->peekTokenAt($pos);

			// Increment or decrement the parenthesis count
			if ($token->id === T_PARENTHESIS_OPEN) {
				$parenthesisCount += 1;
			} else if ($token->id === T_PARENTHESIS_CLOSE) {
				$parenthesisCount -= 1;
			}

			// Increment the position
			$pos += 1;
		}

		return $pos - 1;
	}

	/**
	 * Checks if a token is in the type of token list.
	 *
	 * @param TokenInfo $tokenToCheck
	 *        	the token to check.
	 * @param Array[Integer] $tokenList
	 *        	an array of token ids, e.g. T_NEW_LINE, T_DOC_COMMENT, etc.
	 * @return Boolean true if the token is found, false if it is not.
	 */
	public function isTokenInList($tokenToCheck, $tokenList) {
		foreach ($tokenList as $tokenInList) {
			if ($this->checkToken($tokenToCheck, $tokenInList)) {
				return true;
			}
		}
		return false;
	}

}
