<?php

require_once("constants.php");
require_once("mass_search.php");

/*
  This is the script for extracting seed data from NewYorkTimes API
 */
function get_opts() {
	$opts = "";
	$opts .= "k:"; // keywords (required)
	$opts .= "s:"; // startdate (optional)
	$opts .= "e:"; // enddate (optional)
	$opts .= "o:"; // offset limit (optional)
	$options = getopt($opts);
	return $options;
}

function propose_opts($options) {
	if (!isset($options['k'])) {
		print_err_msg("Illegal Command, no keywords found");
		print_help();
		die();
	}
	$keywords = $options['k'];
	$start_date = isset($options['s']) ? $options['s'] : DEFAULT_START_DATE;
	$end_date = isset($options['e']) ? $options['e'] : date('Ymd');
	$offset_limit = isset($options['o']) ? $options['o'] : OFFSET_LIMIT;
	run_mass_search($keywords, $start_date, $end_date, $offset_limit, true);
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