<h2>Don't have an account? <?php echo $html->link('Create one!', '/users/add'); ?></h2>
<h3>Login</h3>
<?php 
echo $form->create('User', array('action' => 'login'));
echo $form->input('username');
echo $form->input('password');
echo $form->end('Login');
?>
