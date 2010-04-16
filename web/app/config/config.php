<?php

	// title of game
	Configure::write('title', 'Brick Breaker');
	
	// number of results to be displayed for pagination
	Configure::write('pagination_limit', 25);
	
	// client key to write high scores and levels
	Configure::write('client_key', '2d4d4bb4ef2948f7974e072b0c613d97');
	
	// description of a blob that can be uploaded by user
	Configure::write('blob_description', 'Level');
	
	// whether or not to enable high scores
	Configure::write('enable_high_scores', true);
	
?>
