<?php 
	echo $form->create('Level', array('action' => 'search'));
	echo $form->input('query', array('label' => 'Search for levels', 'default' => $query));
	echo $form->end('Search');
?>
