<?php

// import sanitize helper to prevent sql attacks
App::import('Sanitize');

class BlobsController extends AppController
{
    public $name = 'Blobs';

    // add a new blob, either from web interface or client
    public function add($key, $user_id, $title, $data)
    {
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
    	    $file_data = addslashes(fread(fopen($this->data['Blob']['file']['tmp_name'], 'r'), 
				$this->data['Blob']['file']['size']));
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

    // download the blob of the given id
    public function download($id)
    {
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

	// return results for search query
	public function results($query)
	{
		$this->pageTitle = 'Brick Breaker | Search Results';

		// search blob titles
		$conditions = array("Blob.title LIKE" => "%" . $query . "%");
		$this->paginate = array('conditions' => $conditions, 'limit' => Configure::read('pagination_limit'), 
			'order' => array('Blob.downloads' => 'desc'));
		
		// send variables to view
		$this->set('query', $query);
		$this->set('blobs', $this->paginate('Blob'));
		return $blobs;
	}
    
    // get all books matching query
	public function search()
	{
		$this->pageTitle = 'Brick Breaker | Search';
		
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
    	$blob = $this->Blob->findById($id);
    	// send array to view
    	$this->set('blob', $blob);
    	return $blob;
    }
}
