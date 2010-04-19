<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
   "http://www.w3.org/TR/html4/strict.dtd">
   
<!-- Tommy MacWilliam, 2010 -->

<html>
	<head>
		<title><?php echo $title_for_layout; ?></title>
		<?php echo $scripts_for_layout; ?>
		
		<?php echo $html->css('cake.generic'); ?>
		<?php echo $html->css('style'); ?>
		
	</head>
	
	<body>
		<div id="container">
			<div id="header">
				<span style="font-size: 2.0em; margin-right: 40px;"><?php echo Configure::read('title'); ?></span>
				
				<div id="navbar">
					<ul>
						<!-- links to browse or search levels -->
						<li><?php echo $html->link('Search ' . Configure::read('blob_description') . 's', 
										array('controller' => 'blobs', 'action' => 'search')); ?></li>
						<li><?php echo $html->link('Browse ' . Configure::read('blob_description') . 's', 
										array('controller' => 'blobs', 'action' => 'browse')); ?></li>
										
						<!-- only show link for login if user is not logged in -->
						<?php if(!isset($session_username)): ?>
							<li><?php echo $html->link('Log in', array('controller' => 'users', 'action' => 'login')); ?></li>
							
						<!-- display level upload, etc. actions if user is logged in
						<?php else: ?>				
							<li><?php echo $html->link('My ' . Configure::read('blob_description') . 's', 
													array('controller' => 'users', 'action' => 'view', $session_uid)); ?></li>
							<li><?php echo $html->link('Upload ' . Configure::read('blob_description'), 
											array('controller' => 'blobs', 'action' => 'add')); ?></li>
							<?php if(Configure::read('enable_high_scores')): ?>
								<li><?php echo $html->link('High Scores', array('controller' => 'scores', 'action' => 'view')); ?></li>
							<?php endif; ?>
							<li>Logged in as <?php echo $session_username; ?> | 
								<?php echo $html->link('Log out', array('controller' => 'users', 'action' => 'logout')); ?></li>
						<?php endif; ?>
					</ul>
				</div>
				
			</div>
			
			<div id="content">
				<?php $session->flash(); ?>
				<?php echo $content_for_layout; ?>
			</div>
		
			<div id="footer">
			</div>
		
		</div>
	</body>
	
</html>
