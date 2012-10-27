<?php
/*
	Set of functions that implements the article
	seach based on keywords from Time API
*/

// TODO: Interface for the UI client, response to HTTP GET request

function run_mass_search($keywords, $start_date, $end_date,
	$write_to_file = false) {
	// Assume right usage and do not check sanity of search data
	$request = compose_request($keywords, $start_date, $end_date);
	// issue request to Times and get results in form of JSON
	$response = file_get_contents($request);
	process_result($response, $write_to_file);
}

/*
	Compose request based on search keywords
*/
function compose_request($keywords,$start_date, $end_date) {
	
	$fields = "title,url,lead_paragraph"; // control the return fields
	$request = "";
	$request .= REQ_PREFIX;
	$request .= '?' . 'query=' . $keywords; // set query keywords
	$request .= '&begin_date=' . $start_date; // set query start date
	$request .= '&end_date=' . $end_date; // set query end date
	$request .= '&fields=' . $fields; // set return result content
	$request .= '&api-key=' . API_KEY; // append api key
	return $request;
}

function process_result($response, $write_to_file) {
	if ($write_to_file) {
		// called from script, write result to file
		write_to_file($response);
	} else {
		// response to HTTP request and render result page for GUI
		render_result($response);
	}
}

function write_to_file($response) {
	
}

function render_result($response)  {
	
}

?>