<?php

/**
 * @file blobs_controller.php
 * @author Tommy MacWilliam
 * 
 */

App::import('Core', 'Sanitize');
App::import('Core', 'Xml');

/**
 * Controller for scores.
 * 
 */
class BlobsController extends AppController
{
    public $name = 'Blobs';
    public $components = array('RequestHandler');

    /**
     * Add a new blob.
     * Used by both web interface and client.
     * Format of request POSTDATA:
     * @code
     * <blob>
     *   <client-key>abcdef123456</client-key>
     *   <user-id>123</user-id>
     *   <title>A Random Title</title>
     *   <data>Some base64 encoded data</data>
     * </blob>
     * @endcode
     * 
     */
    public function add()
    {
		$this->pageTitle = Configure::read('title') . ' | Add New ' . Configure::read('blob_description');
	
		// POST request from client
		if (!empty($_POST['postdata']))
		{
			$postdata = $_POST['postdata'];
			// parse XML request
			$xml = new Xml($postdata);
			$xml_array = $xml->toArray();
			
			// make sure keys match
			if ($xml_array['Blob']['client-key'] == Configure::read('client_key'))
			{
				// sanitize database input from POST request
				$this->data['Blob']['user_id'] = Sanitize::paranoid($xml_array['Blob']['user-id'], array(' '));
				$this->data['Blob']['title'] = Sanitize::paranoid($xml_array['Blob']['title'], array(' '));
				$this->data['Blob']['data'] = base64_decode($xml_array['Blob']['data']);
				$this->data['Blob']['id'] = md5($xml_array['Blob']['data']);
			
				// make sure blob with given id does not exist
				$blob = $this->Blob->findById($this->data['Blob']['id']);
				if (!$blob)
				{
					// save blob to database
					$this->Blob->save($this->data);
					exit();
				}
			}
			// wrong key, so do nothing
			else
				exit();
		}
		
    	// make sure user is logged in
    	$this->requestAction('/users/check_login');

    	// uploading from web interface, so check if form was completed correctly
    	if (!empty($this->data) && is_uploaded_file($this->data['Blob']['file']['tmp_name']))
    	{
    	    // read temporary uploaded file 
    	    $file_data = fread(fopen($this->data['Blob']['file']['tmp_name'], 'r'), 
							$this->data['Blob']['file']['size']);
							
    	    // associate uid with that of currently logged in user
    	    $this->data['Blob']['user_id'] = $this->Session->read('session_uid');
			// sanitize database input
			$this->data['Blob']['title'] = Sanitize::paranoid($this->data['Blob']['title'], array(' '));
    	    $this->data['Blob']['data'] = $file_data;
    	    // generate a unique id for a blob by hashing its contents
    	    $this->data['Blob']['id'] = md5(base64_decode($file_data));

			// make sure blob with given id does not exist
			$blob = $this->Blob->findById($this->data['Blob']['id']);
			if (!$blob)
			{
				// save blob to database
				$this->Blob->save($this->data);
	    	    $this->Session->setFlash('Upload Succeeded');
			}
			// do not save new blob if already existing
			else
				$this->Session->setFlash('Blob already exists');
    	    
    	    // redirect user to his profile
			$this->redirect('/users/view/' . $this->Session->read('session_uid'));
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
     * Browse all blobs.
     * Used by both web interface and client.
     * 
     */
    public function browse()
    {
		// reset pagination filter
		$this->paginate = array('limit' => Configure::read('pagination_limit'), 'order' => array('Blob.downloads' => 'desc'));
		// browsing is same as a blank search
		$this->redirect('/blobs/results/');
	}

	/**
	 * Delete a blob.
	 * Used by web interface.
	 * @param $id The id of the blob to delete.
	 *
	 */
	public function delete($id)
	{
		// make sure user is logged in
		$this->requestAction('/users/check_login');
		
		// make sure user id of blob matches the logged in user before deleting
		$blob = $this->Blob->findById($id);
		if ($blob['User']['id'] == $this->Session->read('session_uid'))
			$this->Blob->delete($id);
		else
			$this->Session->setFlash('Cannot delete level');
			
		// redirect user to his blob library
		$this->redirect('/users/view/' . $this->Session->read('session_uid'));
	}
	
	/**
	 * Download blob data.
	 * Used by both web interface and client.
	 * @param $id The id of the blob to download.
	 * 
	 */
    public function download($id)
    {
		$this->pageTitle = Configure::read('title') . ' | Download ' . Configure::read('blob_description');
		
		// get blob with given id
    	$blob = $this->Blob->findById($id);
    	// increment downlad count
    	$blob['Blob']['downloads']++;
    	$this->Blob->save($blob);
    	
    	// tell browser to download file by setting attachment header
    	header('Content-Disposition: attachment; filename="'. $blob['Blob']['name'] .'"');
    	echo $blob['Blob']['data'];
    	// need to exit to avoid 404
    	exit();
    }
    
    /**
     * Edit blob data.
     * Used by web interface.
     * @param $id The id of the blob to edit
     * 
     */
	public function edit($id = '')
    {
		// make sure user is logged in
		$this->requestAction('/users/check_login');
		$blob = $this->Blob->findById($id);
						
		// do not edit blob if user id doesn't match id of logged in user
		if (empty($this->data) && $blob['Blob']['user_id'] != $this->Session->read('session_uid'))
		{
			$this->Session->setFlash('Cannot edit ' . Configure::read('blob_description'));
			// redirect user to his blob library
			$this->redirect('/users/view/' . $this->Session->read('session_uid'));
			exit();
		}
		
		// form submitted, so process data
		if (!empty($this->data))
		{		
			// sanitize database input and save new data
			$blob['Blob']['title'] = Sanitize::paranoid($this->data['Blob']['title'], array(' '));
			
			// change file data if user uploaded new file
			if (is_uploaded_file($this->data['Blob']['file']['tmp_name']))
			{
				// read temporary uploaded file 
				$file_data = fread(fopen($this->data['Blob']['file']['tmp_name'], 'r'), 
								$this->data['Blob']['file']['size']);
				$blob['Blob']['data'] = $file_data;
			}
			$this->Blob->save($blob);
			
			// redirect user to his blob library
			$this->redirect('/users/view/' . $this->Session->read('session_uid'));
		}
		// form not submitted, so send data to view
		else
			$this->set('blob', $this->Blob->findById($id));
	}
    
	/**
	 * Show results for search query.
	 * Used by both web interface and client.
	 * @param $query Search query. If none given, data for all blobs will be fetched.
	 * 
	 */ 
	public function results($query = '')
	{
		$this->pageTitle = Configure::read('title') . ' | Search Results';
		// send query to view
		$this->set('query', $query);

		// search blob titles
		$conditions = array("Blob.title LIKE" => "%" . $query . "%");
		
		// format xml request from client
		if ($this->params['url']['ext'] == 'xml')
		{
			$blobs = $this->Blob->find('all', array('conditions' => $conditions, 
										'fields' => array('Blob.id', 'Blob.user_id', 'Blob.title', 'Blob.downloads')));
			$this->set('blobs', $blobs);
		}
		// web html response
		else
		{
			$this->paginate = array('conditions' => $conditions, 'limit' => Configure::read('pagination_limit'), 
				'order' => array('Blob.downloads' => 'desc'));
			$this->set('blobs', $this->paginate('Blob'));
		}
	}
    
    /**
     * Search for blobs.
     * Used by web interface. Will redirect user to results view with query.
     * 
     */
	public function search()
	{
		$this->pageTitle = Configure::read('title') . ' | Search ' . Configure::read('blob_description') . 's';
		
		// form has been submitted
		if (!empty($this->data))
		{
			// redirect to url containing query
			$url = '/blobs/results/' . $this->data['Blob']['query'];
			$this->redirect($url);
			exit();
		}
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
		
		// create test blob
		array_push($tests, 'insert blob');
		$this->data['Blob']['id'] = 'test';
		$this->data['Blob']['user_id'] = 1;
		$this->data['Blob']['title'] = 'test title';
		$this->data['Blob']['data'] = 'test data';
		
		// insert test blob into database
		if ($this->Blob->save($this->data))
			array_push($results, 'pass');
		else
			array_push($results, 'fail');
		
		// read blob data from database	
		array_push($tests, 'read blob');
		$blob = $this->Blob->findById('test');
		if ($blob['Blob']['user_id'] == $this->data['Blob']['user_id'] && $blob['Blob']['title'] == $this->data['Blob']['title']
				&& $blob['Blob']['data'] == $this->data['Blob']['data'])
			array_push($results, 'pass');
		else
			array_push($results, 'fail');
			
		// delete blob data from database
		array_push($tests, 'delete blob');
		if ($this->Blob->delete($blob))
			array_push($results, 'pass');
		else
			array_push($results, 'fail');
			
		// tests complete, send results to view
		$this->set('tests', $tests);
		$this->set('results', $results);
		
	}

    /**
     * View page for blob, with creator and download link.
     * Used by web interface.
     * @param $id The id of the blob to view
     * 
     */
    public function view($id)
    {
		$this->pageTitle = Configure::read('title') . ' | View ' . Configure::read('blob_description');
		
    	$blob = $this->Blob->findById($id);
    	// send array to view
    	$this->set('blob', $blob);
    }
}
