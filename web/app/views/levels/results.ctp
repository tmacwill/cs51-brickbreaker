<?php 
	echo $form->create('Level', array('action' => 'search'));
	echo $form->input('query', array('label' => 'Search for levels', 'default' => $query));
	echo $form->end('Search');
?>
	
<?php if (isset($levels)): ?>
	<?php if ($query != ""): ?>
		<h2>Results for "<?php echo $query?>"</h2>
	<?php endif;?>
	
	<?php if($levels == null): ?>
		<h3>No levels found</h3>
	<?php endif; ?>
		
	<table>
		<tr>
			<th><?php echo $paginator->sort('Title', 'title'); ?></th>
			<th><?php echo $paginator->sort('Username', 'username'); ?></th>
			<th><?php echo $paginator->sort('Downloads', 'downloads'); ?></th>
		</tr>
		<?php foreach ($levels as $level): ?>
			<tr>
				<td><?php echo $html->link($level['Level']['title'], array('controller' => 'levels', 'action' => 'view', $level['Level']['id'])); ?></td>
				<td><?php echo $level['User']['username']; ?></td>
				<td><?php echo $level['Level']['downloads']; ?></td>
			</tr>
		<?php endforeach; ?>
	</table>
<?php endif; ?>

<?php echo $paginator->numbers(); ?>
<br />
<?php 
	echo $paginator->prev('Previous', null, null, array('class' => 'disabled'));
	echo $paginator->next('Next', null, null, array('class' => 'disabled'));
?>

