<<<<<<< HEAD:web/app/views/levels/view.ctp
<h3><?php echo $level['Level']['title']; ?></h3>
<h5>Creator: <?php echo $level['Level']['username']; ?></h5>
<h5>Downloads: <?php echo $level['Level']['downloads']; ?></h5>
<h4><?php echo $html->link('Download', array('controller' => 'levels', 'action' => 'download', $level['Level']['id'])); ?></h4>
=======
<h3><?php echo $level['Level']['title']; ?></h3>
<h4><?php echo $level['Level']['uid']; ?></h4>
>>>>>>> 3ddf17c1bfc1c0fe8087409589c31d511a563201:web/app/views/levels/view.ctp
