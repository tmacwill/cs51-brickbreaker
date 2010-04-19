<?php

// import sanitize helper to prevent sql attacks
App::import('Sanitize'); 

class UsersController extends AppController
{
    public $name = 'Users';
    public $components = array('RequestHandler');

    // create new user
    public function add()
    {
		$this->pageTitle = Configure::read('title') . ' | Create New Account';
	
		// form has been submitted
		if (!empty($this->data))
		{
			// make sure passwords match
			if ($this->data['password'] != $this->data['confirm_password'])
			{
				$this->Session->setFlash('Passwords do not match');
				$this->redirect('/users/add');
				exit();
			}

			// sanitize database input
			$this->data['User']['username'] = Sanitize::paranoid($this->data['User']['username'], array(' '));
			$this->data['User']['password'] = md5(Sanitize::paranoid($this->data['User']['password'], array(' ')));
			
			// make sure username is unique
			$conditions = array('User.username' => $this->data['User']['username']);
			$count = $this->User->find('count', array('conditions' => $conditions));
			
			if ($count != 0)
			{
				$this->Session->setFlash('A user with that name already exists. Please choose a new username.');
				$this->redirect('/users/add');
				exit();
			}
			
			// save user to users database
			else if ($this->User->save($this->data))
			{
				$this->Session->setFlash('Account created');
				$this->redirect('/users/login');
				exit();				
			}
		}
    }
    
    // send all session variables to view
    public function beforeRender()
    {
		$this->set('session_username', $this->Session->read('session_username'));
		$this->set('session_uid', $this->Session->read('session_uid'));
    }
    
    // make sure user is logged in
    public function check_login()
    {
		if ($this->action != 'login' && $this->action != 'logout')
		{
			if (!$this->Session->check('session_uid'))
			{
				$this->Session->setFlash('Please log in first');
				$this->redirect('/users/login/');
				exit();
			}
			else
				return true;
		}
    }
    
    // log user in and create new session
    public function login()
    {
		$this->pageTitle = Configure::read('title') . ' | User Login';
		// form submitted
		if (!empty($this->data))
		{
			// sanitize database input
			$this->data['User']['username'] = Sanitize::paranoid($this->data['User']['username'], array(' '));
			$this->data['User']['password'] = Sanitize::paranoid($this->data['User']['password'], array(' '));
			
			$user = $this->User->findByUsername($this->data['User']['username']);
			
			// make sure user exists and passwords match
			if ($user != null && $user['User']['password'] == md5($this->data['User']['password']))
			{
				// save user credentials
				$this->Session->write('session_uid', $user['User']['id']);
				$this->Session->write('session_username', $user['User']['username']);
				$this->Session->setFlash('Successfully logged in');
				$this->redirect('/blobs/browse');
				exit();
			}
			else
				$this->Session->setFlash('Incorrect username or password');
		}
    }
    
    // log user out and destroy session
    public function logout()
    {
		// destory session variables
		$this->Session->destroy('session_uid');
		$this->Session->destroy('session_username');
		
		// redirect user to login page
		$this->Session->setFlash('Successfully logged out');
		$this->redirect('/blobs/browse');
		exit();
    }
    
    // view the profile of the given user
    public function view($id)
    {
		$this->pageTitle = Configure::read('title') . ' | View User';
		
		// get all blobs associated with user
		$conditions = array('User.id' => $id);
		$this->paginate = array('conditions' => $conditions, 'limit' => Configure::read('pagination_limit'));
		
		// format xml request from client
		if ($this->params['url']['ext'] == 'xml')
			$this->set('blobs', $this->User->findAllById($id));
		// web html response
		else
			$this->set('blobs', $this->paginate('Blob'));
	}
	
	// verify if the user's credentials are valid, used by client
	// returns empty <users> element if credentials are not valid
	public function verify($username, $password)
	{
		$user = $this->User->findByUsername($username);
		
		// make sure user exists and passwords match
		if ($user != null && $user['User']['password'] == md5($password))
			$this->set('user', $user);
	}
}

?>
