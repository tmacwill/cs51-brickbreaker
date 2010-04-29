<?php

/**
 * @file score.php
 * @author Tommy MacWilliam
 * 
 */

/**
 * Model for scores.
 * 
 */
class Score extends AppModel
{
	public $name = 'Score';
	// user_id and blob_id are foreign keys found in this model
	public $belongsTo = array('User' => array('fields' => array('id', 'username')), 
								'Blob' => array('fields' => array('id', 'user_id', 'title', 'downloads')));
}

