<?php
if( isset($_GET['loc']) && isset($_GET['callback']) ) {
	$myFile = "loc_data.txt";
	$fh = fopen($myFile, 'a') or die("can't open file");
	$today = date("F j, Y, g:i a");
	$stringData = $_GET['loc']."@".$today."\n";
	$callback= $_GET['callback'];
	fwrite($fh, $stringData);
	fclose($fh);
	echo $callback.'("'.$stringData.'");';
} else {
	echo 'alert("Error: param loc and callback should be set! ");';
}
