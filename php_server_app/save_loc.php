<?php 
$myFile = "loc_data.txt";
$fh = fopen($myFile, 'a') or die("can't open file");
$stringData = $_GET['loc'];
$callback= $_GET['callback'];
fwrite($fh, $stringData."\n");
fclose($fh);
echo $callback.'("'.$stringData.'");';
