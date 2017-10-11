<?php 

/**
 * Test class.
 */
class Toto {

	/**
	 * Test.
	 *
	 * @param Integer $param
	 *        	the param (optional)
	 */
	public function test($param = null) {
		log_message('debug', __METHOD__);
		
		$sql = " DELETE FROM toto WHERE 1 = 1 ";
				
		if ($param != null) {			
			$sql .= "AND param = ?";
			$valeurs[] = (int) $param;
		}
		
		// Execute la requête
		$this->db->query($sql, $valeurs);
		
	}
	
}