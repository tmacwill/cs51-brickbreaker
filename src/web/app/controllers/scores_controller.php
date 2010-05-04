<?php

/**
 * @file scores_controller.php
 * @author Tommy MacWilliam
 * 
 */

App::import('Core', 'Sanitize');
App::import('Core', 'Xml');

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
	 * @code
	 * <score>
	 *   <client-key>abcdef123456</client-key>
	 *   <user-id>123</user-id>
	 *   <blob-id>123abc456def</blob-id>
	 *   <score>456789</score>
	 * </score>
	 * @endcode
	 * 
	 */
	public function add()
	{
		// decrypt post request
		$key = file_get_contents(Configure::read('private_key_file'));
		$aes_key_encrypted = $_POST['key'];
		openssl_private_decrypt(base64_decode($aes_key_encrypted), $aes_key, $key);
		
		$postdata_encrypted = $_POST['postdata'];
		$iv = Configure::read('aes_iv');
		$postdata = mcrypt_decrypt(MCRYPT_RIJNDAEL_128, $aes_key, base64_decode($postdata_encrypted), MCRYPT_MODE_CBC, $iv);
		
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
	 * Test controller functionality.
	 *
	 */
	public function test()
	{
		// arrays to hold test names and respective results
		$results = array();
		$tests = array();
		
		// create test score
		array_push($tests, 'insert score');
		$this->data['Score']['id'] = -1;
		$this->data['Score']['user_id'] = 1;
		$this->data['Score']['blob_id'] = 'test';
		$this->data['Score']['score'] = 100;
		
		// insert test blob into database
		if ($this->Score->save($this->data))
			array_push($results, 'pass');
		else
			array_push($results, 'fail');
		
		// read blob data from database	
		array_push($tests, 'read score');
		$score = $this->Score->findById('-1');
		if ($score['Score']['user_id'] == $this->data['Score']['user_id'] && $score['Score']['blob_id'] == $this->data['Score']['blob_id']
				&& $score['Score']['score'] == $this->data['Score']['score'])
			array_push($results, 'pass');
		else
			array_push($results, 'fail');
			
		// delete blob data from database
		array_push($tests, 'delete score');
		if ($this->Score->delete($score))
			array_push($results, 'pass');
		else
			array_push($results, 'fail');
			
		// tests complete, send results to view
		$this->set('tests', $tests);
		$this->set('results', $results);
		
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
		
		// only view high scores for given blob if id is given
		if ($blob_id != '')
			$conditions = array('Score.blob_id' => $blob_id);
			
		$this->paginate = array('conditions' => $conditions, 'limit' => Configure::read('pagination_limit'), 
								'order' => array('Score.score' => 'desc'));
		
		// format xml request from client
		if ($this->params['url']['ext'] == 'xml')
			$this->set('scores', $this->Score->find('all', array('conditions' => $conditions,
													'order' => array('Score.score' => 'desc'))));
		// web html response
		else
			$this->set('scores', $this->paginate('Score'));
	}
}
