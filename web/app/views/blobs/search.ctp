<?php 
	echo $form->create('Blob', array('action' => 'search'));
	echo $form->input('query', array('label' => 'Search for ' . Configure::read('blob_description') . 's'));
	echo $form->end('Search');
?>
