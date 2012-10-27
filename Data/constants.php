<?php
/*
	Define set of constants used in API usage
*/

define ("DEFAULT_START_DATE", "20110924");

define ("API_KEY", 
	"99ccc4f56c777169f4f6fb4098525de8:9:66845896");
	
define ("REQ_PREFIX", 
	"http://api.nytimes.com/svc/search/v1/article");
	
// control number of result pages = OFFSET_LIMIT * 10;
define ("OFFSET_LIMIT", "5");

// output file ID and body 
define ("BODY_FILE_NAME", "body_file_name.txt");

// output file ID and file meta data(title, author, etc) 
define("META_FILE_NAME", "meta_file_name.txt");

?>