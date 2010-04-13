<?php

class LevelsController extends AppController
{
    var $name = 'Levels';

    // add a new level to the user with the given ID
    function add($uid)
    {
    }

    // get a list of the levels associated with the given ID
    function list($uid)
    {
	// get all levels with the uid
	$conditions = array('Level.uid' => $uid);
	$levels = $this->Level->find('all', array('conditions' => $conditions));
	// send array to view
	$this->set('levels', $levels);
	return $levels;
    }
}