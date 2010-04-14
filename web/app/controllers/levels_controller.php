<?php

class LevelsController extends AppController
{
    var $name = 'Levels';

    // add a new level to the user with the given ID
    function add()
    {
	// make sure user is logged in
	$this->requestAction('/users/check_login');

	// check if form was completed correctly
	if (!empty($this->data) && is_uploaded_file($this->data['Level']['file']['tmp_name']))
	{
	    // read temporary uploaded file 
	    $file_data = addslashes(fread(fopen($this->data['Level']['file']['tmp_name'], 'r'), $this->data['Level']['file']['size']));
	    // associate uid with that of currently logged in user
	    $this->data['Level']['uid'] = $this->Session->read('session_uid');
	    $this->data['Level']['data'] = $file_data;

	    // save data to database
	    $this->Level->save($this->data);
	    $this->Session->setFlash('Upload Succeeded');
	}
    }

    // send all session variables to view
    function beforeRender()
    {
	$this->set('session_username', $this->Session->read('session_username'));
	$this->set('session_uid', $this->Session->read('session_uid'));
    }

    // download the blob of the given id
    function download($id)
    {
	$file = $this->Level->findById($id);
	// tell browser to download file by setting attachment header
	header('Content-Disposition: attachment; filename="'. $file['Level']['name'] .'"');
	echo $file['Level']['data'];
	// need to exit to avoid 404
	exit();
    }

    // get level information for the level of the given id
    function view($id)
    {
	$level = $this->Level->findById($id);
	// send array to view
	$this->set('level', $level);
	return $level;
    }
}