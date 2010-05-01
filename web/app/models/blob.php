<?php

/**
 * @file blob.php
 * @author Tommy MacWilliam
 * 
 */

/**
 * @mainpage Documentation for Brick Breaker. <br>
 * Harvard CS 51 Final Project. <br>
 * Abraham Lin, Tommy MacWilliam, Robert Nishihara, and Jacob Pritt. <br>
 *
 */


/**
 * Model for blobs.
 * 
 */
class Blob extends AppModel
{
    public $name = 'Blob';
    // user_id is foreign key found in this model 
    public $belongsTo = array('User' => array('fields' => array('id', 'username')));
}

?>
