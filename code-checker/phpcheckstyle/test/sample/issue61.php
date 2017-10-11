<?php
 
require_once( "Libs/autoload.php" ) ;
 
/**
 * Model Base class
 */
abstract class ModelBase {
 
    /**#@+
     * @returns string
     */
    /** Drop Table SQL (DDL) */
    abstract static public function dropTableSQL() ;
    /** Create Table SQL (DDL) */
    abstract static public function createTableSQL() ;
    /**#@-*/
    /**#@+
     * @returns boolean
     */
    /** Row addition validator */
    abstract public function validateForAdd() ;
    /** Row update validator */
    abstract public function validateForUpdate() ;
    /** Row removal validator */
    abstract public function validateForDelete() ;
 
    /**
     * Validate a numeric ID
     *
     * @param string
     */
    public function validateId( $id ) {
        return ( 1 === preg_match( '/^[1-9]([0-9]+)$/', $id ) ) ;
    }
 
    /**
     * Validate a date
     *
     * @param string
     */
    public function validateDate( $date ) {
        return ( 1 === preg_match( '/^20[123][0-9]-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[01])$/', $date ) ) ;
    }
 
    /**#@-*/
 
}