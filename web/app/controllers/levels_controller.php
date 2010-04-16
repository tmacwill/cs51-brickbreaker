<?php

// import sanitize helper to prevent sql attacks
App::import('Sanitize');

class LevelsController extends AppController
{
    public $name = 'Levels';

    // add a new level, either from web interface or client
    public function add($key, $user_id, $title, $data)
    {
		// allow uploading from client, key must be correct in order for upload to work
		if (!empty($user_id) && !empty($title) && !empty($data) && ($key == Configure::read('client_key')))
		{
			// sanitize database input
			$this->data['Level']['user_id'] = Sanitize::paranoid($user_id, array(' '));
			$this->data['Level']['title'] = Sanitize::paranoid($title, array(' '));
			$this->data['Level']['data'] = Sanitize::paranoid($data, array(' '));
			
			// save level to database
			$this->Level->save($this->data);
			exit();
		}
		
    	// make sure user is logged in
    	$this->requestAction('/users/check_login');

    	// uploading from web interface, so check if form was completed correctly
    	if (!empty($this->data) && is_uploaded_file($this->data['Level']['file']['tmp_name']))
    	{
    	    // read temporary uploaded file 
    	    $file_data = addslashes(fread(fopen($this->data['Level']['file']['tmp_name'], 'r'), $this->data['Level']['file']['size']));
    	    // associate uid with that of currently logged in user
    	    $this->data['Level']['user_id'] = $this->Session->read('session_uid');
    	    $this->data['Level']['data'] = $file_data;

			// sanitize database input
			$this->data['Level']['title'] = Sanitize::paranoid($this->data['Level']['title'], array(' '));

    	    // save data to database
    	    $this->Level->save($this->data);
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
    
    // browse levels
    public function browse()
    {
		// reset pagination filter
		$this->paginate = array('limit' => Configure::read('pagination_limit'), 'order' => array('Level.downloads' => 'desc'));
		// browsing is same as a blank search
		$this->redirect('/levels/results/');
	}

    // download the blob of the given id
    public function download($id)
    {
		// get level with given id
    	$level = $this->Level->findById($id);
    	// increment downlad count
    	$level['Level']['downloads']++;
    	$this->Level->save($level);
    	
    	// tell browser to download file by setting attachment header
    	header('Content-Disposition: attachment; filename="'. $level['Level']['name'] .'"');
    	echo $level['Level']['data'];
    	// need to exit to avoid 404
    	exit();
    }

	// return results for search query
	public function results($query)
	{
		$this->pageTitle = 'Brick Breaker | Search Results';

		// search level titles
		$conditions = array("Level.title LIKE" => "%" . $query . "%");
		$this->paginate = array('conditions' => $conditions, 'limit' => Configure::read('pagination_limit'), 'order' => array('Level.downloads' => 'desc'));
		
		// send variables to view
		$this->set('query', $query);
		$this->set('levels', $this->paginate('Level'));
	}
    
    // get all books matching query
	public function search()
	{
		$this->pageTitle = 'Brick Breaker | Search';
		
		// form has been submitted
		if (!empty($this->data))
		{
			// redirect to url containing query
			$url = '/levels/results/' . $this->data['Level']['query'];
			$this->redirect($url);
			exit();
		}
	}

    // get level information for the level of the given id
    public function view($id)
    {
    	$level = $this->Level->findById($id);
    	// send array to view
    	$this->set('level', $level);
    	return $level;
    }
}
