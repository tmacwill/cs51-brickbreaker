<h3><?php echo $blob['Blob']['title']; ?></h3>
<h5>Creator: <?php echo $blob['User']['username']; ?></h5>
<h5>Downloads: <?php echo $blob['Blob']['downloads']; ?></h5>
<h4><?php echo $html->link('Download', array('controller' => 'blobs', 'action' => 'download', $blob['Blob']['id'])); ?></h4>
