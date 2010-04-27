<?php

class Score extends AppModel
{
	public $name = 'Score';
	public $belongsTo = array('User', 'Blob');
}
