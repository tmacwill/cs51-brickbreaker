<h3><?php echo $level['Level']['title']; ?></h3>
<h5>Creator: <?php echo $level['Level']['username']; ?></h5>
<h5>Downloads: <?php echo $level['Level']['downloads']; ?></h5>
<h4><?php echo $html->link('Download', array('controller' => 'levels', 'action' => 'download', $level['Level']['id'])); ?></h4>
