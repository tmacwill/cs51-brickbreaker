<?php 
	echo $form->create('Blob', array('action' => 'search'));
	echo $form->input('query', array('label' => 'Search for ' . Configure::read('blob_description') . 's', 'default' => $query));
	echo $form->end('Search');
?>
	
<?php if (isset($blobs)): ?>
	<?php if ($query != ""): ?>
		<h2>Results for "<?php echo $query?>"</h2>
	<?php endif;?>
	
	<?php if($blobs == null): ?>
		<h3>No <?php echo Configure::read('blob_description') . 's'; ?> found</h3>
	<?php endif; ?>
		
	<?php $paginator->options(array('url' => $this->passedArgs)); ?>
	<table>
		<tr>
			<th><?php echo $paginator->sort('Title', 'title'); ?></th>
			<th><?php echo $paginator->sort('Username', 'username'); ?></th>
			<th><?php echo $paginator->sort('Downloads', 'downloads'); ?></th>
		</tr>
		<?php foreach ($blobs as $blob): ?>
			<tr>
				<td><?php echo $html->link($blob['Blob']['title'], array('controller' => 'blobs', 'action' => 'view', $blob['Blob']['id'])); ?></td>
				<td><?php echo $html->link($blob['User']['username'], array('controller' => 'users', 'action' => 'view', $blob['User']['id'])); ?></td>
				<td><?php echo $blob['Blob']['downloads']; ?></td>
			</tr>
		<?php endforeach; ?>
	</table>
<?php endif; ?>

<?php echo $paginator->numbers(); ?>
<br />
<?php echo $paginator->prev('Previous', null, null, array('class' => 'disabled')); ?> 
<?php echo $paginator->next('Next', null, null, array('class' => 'disabled')); ?>

