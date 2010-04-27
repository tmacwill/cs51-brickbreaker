<table>
	<tr>
		<th><?php echo $paginator->sort(Configure::read('blob_description'), 'blob'); ?></th>
		<th><?php echo $paginator->sort('User', 'username'); ?></th>
		<th><?php echo $paginator->sort('Score', 'score'); ?></th>
	</tr>
	<?php foreach ($scores as $score): ?>
		<tr>
			<td><?php echo $html->link($score['Blob']['title'], array('controller' => 'scores', 'action' => 'view', $score['Blob']['id'])); ?></td>
			<td><?php echo $html->link($score['User']['username'], array('controller' => 'users', 'action' => 'view', $score['User']['id'])); ?></td>
			<td><?php echo $score['Score']['score']; ?></td>
		</tr>
	<?php endforeach; ?>
</table>
<?php echo $paginator->numbers(); ?>
<br />
<?php echo $paginator->prev('Previous', null, null, array('class' => 'disabled')); ?> 
<?php echo $paginator->next('Next', null, null, array('class' => 'disabled'));
