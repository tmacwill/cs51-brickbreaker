<?php

	// title of game
	Configure::write('title', 'Brick Breaker');
	
	// number of results to be displayed per page
	Configure::write('pagination_limit', 25);
	
	// client key to write high scores and levels
	Configure::write('client_key', '2d4d4bb4ef2948f7974e072b0c613d97');
	
	// description of a blob that can be uploaded by user
	Configure::write('blob_description', 'Level');
	
	// name of private key file
	Configure::write('private_key_file', 'cloud_cs50_net.pem');
	
	// initialization vector for AES
	Configure::write('aes_iv', 'fedcba9876543210');
	
	// whether or not to enable high scores
	Configure::write('enable_high_scores', true);
	
?>
