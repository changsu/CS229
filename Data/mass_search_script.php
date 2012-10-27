<?php
require_once("mass_search.php");
function get_opts() {
	$opts = "";
	$opts .= "k:"; // keywords (required)
	$opts .= "s:"; // startdate (optional)
	$opts .= "e:"; // enddate(optional)
	$options = getopt($opts);
	var_dump($options);
	return $options;
}

function propose_opts($options) {
	if (!isset($options['k'])) {
		print_err_msg("Illegal Command, no keywords found");
		print_help();
		die();
	}
	
}

function print_help() {
	echo "Usage: php mass_search_script.php ".
		"-k 'keywords' (-s 'start_date' -e 'end_date')\n";
}

function print_err_msg($msg) {
	echo "Error: " . $msg ."\n";
}

function main() {
	$options = get_opts();
	propose_opts($options);
}

main();

?>