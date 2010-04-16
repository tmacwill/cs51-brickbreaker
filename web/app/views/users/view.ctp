<?php $paginator->options(array('url' => $this->passedArgs)); ?>
<h3><?php echo $blobs[0]['User']['username']; ?>'s <?php echo Configure::read('blob_description') . 's'; ?></h3>
<table>
	<tr>
		<th><?php echo $paginator->sort('Title', 'title'); ?></th>
		<th><?php echo $paginator->sort('Downloads', 'downloads'); ?></th>
	</tr>
	<?php foreach ($blobs as $blob): ?>
		<tr>
			<td><?php echo $html->link($blob['Blob']['title'], array('controller' => 'blobs', 'action' => 'view', $blob['Blob']['id'])); ?></td>
			<td><?php echo $blob['Blob']['downloads']; ?></td>
		</tr>
	<?php endforeach; ?>
</table>

<?php echo $paginator->numbers(); ?>
<br />
<?php 
	echo $paginator->prev('Previous', null, null, array('class' => 'disabled'));
	echo $paginator->next('Next', null, null, array('class' => 'disabled'));
?>
