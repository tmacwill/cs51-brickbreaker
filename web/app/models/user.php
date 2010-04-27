<?php

/**
 * @file user.php
 * @author Tommy MacWilliam
 * 
 */

/**
 * Model for users.
 * 
 */
class User extends AppModel
{
    public $name = 'User';
    // user_id is foreign key found in blob model
    public $hasMany = 'Blob';
}

?>
