Code used to collect seed data

###Running Environment:
To run the script, log to any myth machines, clone the code.
Or you can have php locally installed, but it may take time.
myth alread has php installed.

###How to run the script?
Usage:
<pre> 
  ~$: php mass_search_script.php -k 'keywords' (-s 'start_date' -e 'end_date')
</pre>

Options:

k: keywords (required) muliple keywords seperated by space, say "economy
   finance"

s: start date (optional) 2011.09.24 by default

e: end date (optional) current time when you run the script

### Ouput File (all files stores json string, decodable in Java)
"body_file_name.txt": store url and body

"meta_file_name.txt": store title, url, lead paragraph, can be enriched

Look into the file, you will understand what the data looks like.
Json is an easy way of sharing data across different languages.
