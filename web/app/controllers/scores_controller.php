<?php

/**
 * @file scores_controller.php
 * @author Tommy MacWilliam
 * 
 */

App::import('Sanitize');
App::import('Xml');

/**
 * Controller for scores.
 * 
 */
class ScoresController extends AppController
{
	public $name = 'Scores';
	public $components = array('RequestHandler');
	
	/**
	 * Add a new high score.
	 * Used by client.
	 * Format of request POSTDATA:
	 * <score>
	 *   <client-key>abcdef123456</client-key>
	 *   <user-id>123</user-id>
	 *   <blob-id>123abc456def</blob-id>
	 *   <score>456789</score>
	 * </score>
	 * 
	 */
	public function add()
	{
		// decrypt post request
		$postdata_encrypted = $_POST['postdata'];
		$key = file_get_contents(Configure::read('private_key_file'));
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
	
	/**
     * Called before rendering of each view.
     * Used by web interface.
     * 
     */
	public function beforeRender()
    {
    	$this->set('session_username', $this->Session->read('session_username'));
    	$this->set('session_uid', $this->Session->read('session_uid'));
    }
	
	/**
	 * View the high scores table.
	 * Used by both web interface and client.
	 * @param $blob_id Optional. If specified, only view high scores for that blob.
	 * If not, view all high scores.
	 * 
	 */
	public function view($blob_id = '')
	{
		$this->pageTitle = Configure::read('title') . ' | High Scores';
		
		// if id given, only list high scores for that level
		if ($blob_id != '')
			$conditions = array('Score.blob_id' => $blob_id);
			
		$this->paginate = array('conditions' => $conditions, 'limit' => Configure::read('pagination_limit'), 
								'order' => array('Score.score' => 'desc'));
		
		// format xml request from client
		if ($this->params['url']['ext'] == 'xml')
			$this->set('scores', $this->Score->find('all', array('conditions' => $conditions)));
		// web html response
		else
			$this->set('scores', $this->paginate('Score'));
	}
}
