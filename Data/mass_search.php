<?php
/*
	Set of functions that implements the article
	seach based on keywords from Time API
*/

require_once('simple_html_dom.php');
// TODO: Interface for the UI client, response to HTTP GET request

function run_mass_search($keywords, $start_date, $end_date,
	$offset_initial, $offset_limit, $write_to_file = false) {
	$meta_f = NULL;
	$body_f = NULL;
	
	// open io stream
	if ($write_to_file) {
		$meta_f = fopen(META_FILE_NAME, "w"); 
		$body_f = fopen(BODY_FILE_NAME, "w");
	}
	
	for ($offset = $offset_initial; $offset < $offset_limit; ++$offset) {
		// Assume right usage and do not check sanity of search data
		$request = 
			compose_request($keywords, $start_date, $end_date, $offset);
		// issue request to Times and get results in form of JSON
		$response = file_get_contents($request);
		// process response result
		echo "Processing chunk " . ($offset + 1) . "..........\n";
		process_result($response, $write_to_file, $meta_f, $body_f);
	}
	
	// close io stream
	if ($write_to_file) {
		echo "Finish writing meta data to file" . META_FILE_NAME. "!\n";
		echo "Finish writing body data to file" . BODY_FILE_NAME. "!\n";
		fclose($meta_f);
		fclose($body_f);
	}
}

/*
	Compose request based on search keywords
*/
function compose_request($keywords,$start_date, $end_date, $offset) {
	
	$fields = "title,url,lead_paragraph"; // control the return fields
	$request = "";
	$request .= REQ_PREFIX; // set api prefix, same for all request
	$request .= '?' . 'query=' . $keywords; // set query keywords
	$request .= '&begin_date=' . $start_date; // set query start date
	$request .= '&end_date=' . $end_date; // set query end date
	$request .= '&offset=' . $offset; // set query offset
	$request .= '&fields=' . $fields; // set return result content
	$request .= '&api-key=' . API_KEY; // append api key
	return $request;
}

function process_result($response, $write_to_file, $meta_f, $body_f) {
	if ($write_to_file) {
		// called from script, write result to file
		write_to_file($response, $meta_f, $body_f);
	} else {
		// response to HTTP request and render result page for GUI
		render_result($response);
	}
}

/*
	
*/
function write_to_file($raw_result, $meta_f, $body_f) {
	try {
		// write meta data to file in form of JSON string
		fwrite($meta_f, $raw_result);

		// build hashmap with key=>value as "url=>body"
		$response = json_decode($raw_result);
		$hashmap = array();		
		foreach ($response->results as $result) {
			$record = array();
			$record['url'] = $result->url;
			$record['body'] = get_body($result->url);
			array_push($hashmap, $record);
		}
		
		fwrite($body_f, json_encode($hashmap));
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
		$result = '';
		// "articleBody" is the class name of body div
		foreach ($html->find('div.articleBody') as $body) {
			// remove html tags
			$string = strip_tags($body->innertext); 
			// convert html entities to normal chars
			$string = trim(html_entity_decode($string, ENT_QUOTES, 'UTF-8')); 
			// remove whitespaces
			$string = preg_replace( '/\s+/', ' ', $string);
			$result .= $string;
		}
		return $result;
	} catch (Exception $e) {
		echo "Error: Fail to extract body from " . $url . "\n";
		var_dump($e->getMessage());
	}
}

function render_result($response)  {
	// TODO: GUI
}

?>