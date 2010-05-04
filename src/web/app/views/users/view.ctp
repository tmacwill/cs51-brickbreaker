<?php $paginator->options(array('url' => $this->passedArgs)); ?>
<h3>
	<?php
		if (empty($blobs))
			echo 'No ' . Configure::read('blob_description') . 's Found';
		else
			echo $blobs[0]['User']['username'] . '\'s ' . Configure::read('blob_description') . 's';
	?>
</h3>
<table>
	<tr>
		<th><?php echo $paginator->sort('Title', 'title'); ?></th>
		<th><?php echo $paginator->sort('Downloads', 'downloads'); ?></th>
		<?php if (!empty($blobs) && $blobs[0]['User']['id'] == $session_uid): ?>
			<th>Actions</th>
		<?php endif; ?>
	</tr>
	<?php foreach ($blobs as $blob): ?>
		<tr>
			<td><?php echo $html->link($blob['Blob']['title'], array('controller' => 'blobs', 'action' => 'view', $blob['Blob']['id'])); ?></td>
			<td><?php echo $blob['Blob']['downloads']; ?></td>
			<?php if (!empty($blobs) && $blobs[0]['User']['id'] == $session_uid): ?>
				<td>
					<?php echo $html->link('Edit', array('controller' => 'blobs', 'action' => 'edit', $blob['Blob']['id'])); ?> |
					<?php echo $html->link('Delete', array('controller' => 'blobs', 'action' => 'delete', $blob['Blob']['id'])); ?>
				</td>
			<?php endif; ?>
		</tr>
	<?php endforeach; ?>
</table>

<?php echo $paginator->numbers(); ?>
<br />
<?php echo $paginator->prev('Previous', null, null, array('class' => 'disabled')); ?> 
<?php echo $paginator->next('Next', null, null, array('class' => 'disabled')); ?>
