<?php
/*
	Set of functions that implements the article
	seach based on keywords from Time API
*/

require_once('simple_html_dom.php');
// TODO: Interface for the UI client, response to HTTP GET request

function run_mass_search($keywords, $start_date, $end_date,
	$write_to_file = false) {
	// Assume right usage and do not check sanity of search data
	$request = compose_request($keywords, $start_date, $end_date);
	// issue request to Times and get results in form of JSON
	$response = file_get_contents($request);
	// process response result
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

function write_to_file($raw_result) {
	try {
		// write meta data to file in form of JSON string
		echo "Writing meta data to file" . META_FILE_NAME.
			"..........\n";
		$f = fopen(META_FILE_NAME, "w"); 
		fwrite($f, $raw_result);
		fclose($f);
		echo "Success!\n";
		
		// build hashmap with key=>value as "url=>body"
		echo "Writing body to file " . BODY_FILE_NAME . 
			"..........\n";
		$response = json_decode($raw_result);
		$hashmap = array();
		
		$test_url = $response->results[0]->url;
		get_body($test_url);
		// foreach ($response->results as $result) {
		// 	get_body($result->url);
		// }
		
		$f = fopen(BODY_FILE_NAME, "w"); 
		fwrite($f, json_encode($hashmap));
		fclose($f);
		
		echo "Success!\n";
	} catch (Exception $e){
		echo "Fail!!";
		var_dump($e->getMessage());
	}	
}

/*
	Because the API only provides part of the body,
	we crawl a little bit here for the whole body
*/
function get_body($url) {
	try {
		$html = file_get_html($url);
		foreach ($html->find('div.articleBody') as $body) {
			// remove html tags and convert html entity to chars
			$string = strip_tags($body->innertext); 
			var_dump($string);
		}
	} catch (Exception $e) {
		echo "Error: Fail to extract body from " . $url . "\n";
		var_dump($e->getMessage());
	}
}

function render_result($response)  {
	// TODO: GUI
}

?>