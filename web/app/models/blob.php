<?php

/**
 * @file blob.php
 * @author Tommy MacWilliam
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
    public $belongsTo = 'User';
}

?>
