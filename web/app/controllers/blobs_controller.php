<?php

// import sanitize helper to prevent sql attacks
App::import('Sanitize');

class BlobsController extends AppController
{
    public $name = 'Blobs';
    public $components = array('RequestHandler');

    // add a new blob, either from web interface or client
    public function add($key = '', $user_id = '', $title = '', $data = '')
    {
		$this->pageTitle = Configure::read('title') . ' | Add New ' . Configure::read('blob_description');
		
		// allow uploading from client, key must be correct in order for upload to work
		if (!empty($user_id) && !empty($title) && !empty($data) && ($key == Configure::read('client_key')))
		{
			// sanitize database input
			$this->data['Blob']['user_id'] = Sanitize::paranoid($user_id, array(' '));
			$this->data['Blob']['title'] = Sanitize::paranoid($title, array(' '));
			$this->data['Blob']['data'] = Sanitize::paranoid($data, array(' '));
			
			// save blob to database
			$this->Blob->save($this->data);
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
    	    $this->data['Blob']['data'] = $file_data;

			// sanitize database input
			$this->data['Blob']['title'] = Sanitize::paranoid($this->data['Blob']['title'], array(' '));

    	    // save data to database
    	    $this->Blob->save($this->data);
    	    $this->Session->setFlash('Upload Succeeded');
    	    
    	    // redirect user to his profile
    	    $this->redirect('/users/view/' . $this->Session->read('session_uid'));
    	    exit();
    	}
    }

    // send all session variables to view
    public function beforeRender()
    {
    	$this->set('session_username', $this->Session->read('session_username'));
    	$this->set('session_uid', $this->Session->read('session_uid'));
    }
    
    // browse all blobs
    public function browse()
    {
		// reset pagination filter
		$this->paginate = array('limit' => Configure::read('pagination_limit'), 'order' => array('Blob.downloads' => 'desc'));
		// browsing is same as a blank search
		$this->redirect('/blobs/results/');
	}

	// delete blob of the given id
	// if correct key is given, blob will be deleted without checking session credentials
	public function delete($id, $key = '')
	{
		// client request with correct key, so okay to delete blob of given id
		if ($key == Configure::read('client_key'))
			$this->Blob->delete($id);
		// web request
		else
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
			$this->redirect('/users/view' . $this->Session->read('session_uid'));
		}
	}

    // download the blob of the given id
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
    
    // edit blob of the given id
	// if correct key is given, blob will be edited without checking session credentials
    public function edit($id, $key = '', $title = '', $data = '')
    {
		// client request with correct key, so okay to edit blob of given id
		if ($key == Configure::read('client_key'))
		{
			// get blob, then replace information
			$blob = $this->Blob->findById($id);
			$blob['Blob']['title'] = Sanitize::paranoid($title, array(' '));
			$blob['Blob']['data'] = Sanitize::paranoid($data, array(' '));
			$this->Blob->save($blob);
			
			// don't redirect client request
			exit();
		}
		// web request
		else
		{
			// make sure user is logged in
			$this->requestAction('/users/check_login');
			$blob = $this->Blob->findById($id);
			
			// do not edit blob if user id doesn't match id of logged in user
			if ($blob['User']['id'] != $this->Session->read('session_uid'))
			{
				$this->Session->setFlash('Cannot edit level');
				// redirect user to his blob library
				$this->redirect('/users/view/' . $this->Session->read('session_uid'));
			}
			
			// form submitted, so process data
			if (!empty($this->data))
			{
				$blob['Blob']['title'] = Sanitize::paranoid($title, array(' '));
				$blob['Blob']['data'] = Sanitize::paranoid($data, array(' '));
				$this->Blob->save($blob);
				
				// redirect user to his blob library
				$this->redirect('/users/view/' . $this->Session->read('session_uid'));
			}
			else
				// form not submitted, so send data to view
				$this->set('blob', $this->Blob->findById($id));
		}
	}
    
	// return results for search query
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
			$blobs = $this->Blob->find('all', array('conditions' => $conditions));
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
    
    // get all books matching query
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

    // get blob information for the blob of the given id
    public function view($id)
    {
		$this->pageTitle = Configure::read('title') . ' | View ' . Configure::read('blob_description');
		
    	$blob = $this->Blob->findById($id);
    	// send array to view
    	$this->set('blob', $blob);
    	return $blob;
    }
}
