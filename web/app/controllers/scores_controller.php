<?php

class ScoresController extends AppController
{
	public $name = 'Scores';
	// number of results displayed per page
    private $PAGINATE_LIMIT = 25;
	
	// add a high score, only from desktop client
	public function add($user_id, $score)
	{
		// uploading only from client, key must be correct in order for upload to work
		if (!empty($user_id) && !empty($score)&& ($key == '2d4d4bb4ef2948f7974e072b0c613d97'))
		{
			// sanitize database input
			$this->data['Score']['user_id'] = Sanitize::paranoid($user_id, array(' '));
			$this->data['Score']['score'] = Sanitize::paranoid($title, array(' '));
			
			// save score to database
			$this->Score->save($this->data);
			exit();
		}
	}
	
	// send all session variables to view
    public function beforeRender()
    {
    	$this->set('session_username', $this->Session->read('session_username'));
    	$this->set('session_uid', $this->Session->read('session_uid'));
    }
	
	// view the high scores table
	public function view()
	{
		$this->paginate = array('limit' => $this->PAGINATE_LIMIT, 'order' => array('Score.score' => 'desc'));
		$this->set('scores', $this->paginate('Score'));
	}
}
