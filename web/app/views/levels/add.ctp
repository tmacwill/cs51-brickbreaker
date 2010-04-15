<h3>Upload new level</h3>
<?php
	echo $form->create('Level', array('action' => 'add', 'enctype' => 'multipart/form-data'));	
	echo $form->input('title', array('label' => 'Level title'));
	echo $form->file('file', array('label' => 'Level'));
	echo $form->end('Upload');
?>
