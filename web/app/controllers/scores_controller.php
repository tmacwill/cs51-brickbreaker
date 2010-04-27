<?php

App::import('Sanitize');
App::import('Xml');

class ScoresController extends AppController
{
	public $name = 'Scores';
	public $components = array('RequestHandler');
	
	// add a high score, only from desktop client
	public function add()
	{
		// decrypt post request
		$postdata_encrypted = $_POST['postdata'];
		$key = file_get_contents('brickbreaker_x10hosting_com_private.pem');
		openssl_private_decrypt(base64_decode($postdata_encrypted), $postdata, $key);
		
		// parse XML request
		$xml = new Xml($postdata);
		$xml_array = $xml->toArray();
		
		// make sure keys match
		if ($xml_array['Score']['client-key'] == Configure::read('client_key'))
		{	
			// sanitize database input from POST request
			$this->data['Score']['user_id'] = Sanitize::paranoid($xml_array['Score']['user-id'], array(' '));
			$this->data['Score']['blob_id'] = $xml_array['Score']['blob-id'];
			$this->data['Score']['score'] = Sanitize::paranoid($xml_array['Score']['score'], array(' '));
			
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
