<?php

class ScoresController extends AppController
{
	public $name = 'Scores';
	public $components = array('RequestHandler');
	
	// add a high score, only from desktop client
	public function add($key, $user_id, $score)
	{
		// uploading only from client, key must be correct in order for upload to work
		if (!empty($user_id) && !empty($score)&& ($key == Configure::read('client_key')))
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
		$this->pageTitle = Configure::read('title') . ' | High Scores';
		$this->paginate = array('limit' => Configure::read('pagination_limit'), 'order' => array('Score.score' => 'desc'));
		
		// format xml request from client
		if ($this->params['url']['ext'] == 'xml')
			$this->set('scores', $this->Score->find('all'));
		// web html response
		else
			$this->set('scores', $this->paginate('Score'));
	}
}
