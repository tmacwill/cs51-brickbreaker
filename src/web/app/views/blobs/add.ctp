<h3>Upload new <?php echo Configure::read('blob_description'); ?></h3>
<?php
	echo $form->create('Blob', array('action' => 'add', 'enctype' => 'multipart/form-data'));	
	echo $form->input('title', array('label' => 'Title'));
	echo $form->file('file', array('label' => Configure::read('blob_description')));
	echo $form->end('Upload');
?>
