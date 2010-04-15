<?php

class ScoresController extends AppController
{
	public $name = 'Scores';
	// number of results displayed per page
    private $PAGINATE_LIMIT = 25;
	
	// send all session variables to view
    public function beforeRender()
    {
    	$this->set('session_username', $this->Session->read('session_username'));
    	$this->set('session_uid', $this->Session->read('session_uid'));
    }
	
	public function view()
	{
		$this->paginate = array('limit' => $this->PAGINATE_LIMIT, 'order' => array('Score.score' => 'desc'));
		$this->set('scores', $this->paginate('Score'));
	}
}
