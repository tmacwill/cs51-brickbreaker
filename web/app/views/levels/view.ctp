<h3>Levels</h3>
<ul>
<?php foreach ($levels as $level): ?>
<li><a href="<?php echo $level['Level']['url']; ?>"><?php echo $level['Level']['title']; ?></a>
<?php endforeach; ?>
</ul>
