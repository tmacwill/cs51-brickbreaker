<?php

/**
 * @file users_controller.php
 * @author Tommy MacWilliam
 * 
 */

App::import('Core', 'Sanitize'); 
App::import('Core', 'Xml');

/**
 * Controller for users.
 * 
 */
class UsersController extends AppController
{
    public $name = 'Users';
    public $components = array('RequestHandler');

    /**
     * Create a new user.
     * Used by web interface.
     * 
     */
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
    
    /**
     * Called before rendering of each view.
     * Used by web interface.
     * 
     */
    public function beforeRender()
    {
		// send session variables
		$this->set('session_username', $this->Session->read('session_username'));
		$this->set('session_uid', $this->Session->read('session_uid'));
    }
    
    /**
     * Make sure user is logged in.
     * Used by web interface.
     * @return True if user is logged in, redirect if not.
     */
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
    
    /**
     * Log user in and create new session.
     * Used by web interface.
     * 
     */
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
    
    /**
     * Log user out and destroy session.
     * Used by web interface.
     * 
     */
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
		array_push($tests, 'insert user');
		$this->data['User']['id'] = -1;
		$this->data['User']['username'] = 'testuser';
		$this->data['User']['password'] = 'testpass';
		$this->data['User']['email'] = 'test@example.com';
		
		// insert test blob into database
		if ($this->User->save($this->data))
			array_push($results, 'pass');
		else
			array_push($results, 'fail');
		
		// read blob data from database	
		array_push($tests, 'read user');
		$user = $this->User->findById('-1');
		if ($user['User']['username'] == $this->data['User']['username'] && $user['User']['password'] == $this->data['User']['password']
				&& $user['User']['email'] == $this->data['User']['email'])
			array_push($results, 'pass');
		else
			array_push($results, 'fail');
			
		// delete blob data from database
		array_push($tests, 'delete user');
		if ($this->User->delete($user))
			array_push($results, 'pass');
		else
			array_push($results, 'fail');
			
		// tests complete, send results to view
		$this->set('tests', $tests);
		$this->set('results', $results);
		
	}
    
    /**
     * View the profile of a user.
     * Used by both web interface and client.
     * @param $id The id of the user to view.
     * 
     */
    public function view($id)
    {
		$this->pageTitle = Configure::read('title') . ' | View User';
		
		// get all blobs associated with user
		$conditions = array('User.id' => $id);
		$this->paginate = array('conditions' => $conditions, 'limit' => Configure::read('pagination_limit'));
		
		// xml response to client
		if ($this->params['url']['ext'] == 'xml')
		{
			$conditions = array('User.id' => $id);
			$blobs = $this->User->find('all', array('conditions' => $conditions, 'fields' => array('User.id', 'User.username')));
			$this->set('blobs', $blobs);
		}
		// web html response
		else
			$this->set('blobs', $this->paginate('Blob'));
	}
	
	/**
	 * Verify if the user's credentials are valid.
	 * Used by client.
	 * Format of request POSTDATA:
	 * @code
	 * <user>
	 *   <username>user</username>
	 *   <password>pass</password>
	 * </user>
	 * @endcode
	 * 
	 */
	public function verify()
	{
		$postdata = $_POST['postdata'];
		// parse xml request
		$xml = new Xml($postdata);
		$xml_array = $xml->toArray();

		$user = $this->User->findByUsername($xml_array['User']['username']);
		
		// make sure user exists and passwords match
		if ($user != null && $user['User']['password'] == md5($xml_array['User']['password']))
			// send user information or null if credentials are invalid
			$this->set('user', $user['User']);
	}
}

?>
