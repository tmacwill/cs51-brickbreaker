<?php $paginator->options(array('url' => $this->passedArgs)); ?>
<h3><?php echo $levels[0]['User']['username']; ?>'s levels</h3>
<table>
	<tr>
		<th><?php echo $paginator->sort('Title', 'title'); ?></th>
		<th><?php echo $paginator->sort('Downloads', 'downloads'); ?></th>
	</tr>
	<?php foreach ($levels as $level): ?>
		<tr>
			<td><?php echo $html->link($level['Level']['title'], array('controller' => 'levels', 'action' => 'view', $level['Level']['id'])); ?></td>
			<td><?php echo $level['Level']['downloads']; ?></td>
		</tr>
	<?php endforeach; ?>
</table>

<?php echo $paginator->numbers(); ?>
<br />
<?php 
	echo $paginator->prev('Previous', null, null, array('class' => 'disabled'));
	echo $paginator->next('Next', null, null, array('class' => 'disabled'));
?>
