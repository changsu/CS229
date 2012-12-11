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

i: initial offset of bucket

o: offset (optional) max offset of bucket

<b>i and o together controls total number of articles fetched.
     Since there're 10 articles per bucket, there're total
     (o - i)* 10 articles fetched. By default, i = 0, o = 5. 
</b>

Example Code:
<pre>
  ~$: php mass_search_script.php -k 'economy' -s 20120829 -e 20120930
</pre>
Above script will output first 50 articles that contains keyword "economy" in
title or body from 08/29/2012 to 09/30/2012 to two files

<pre>
  ~$: php mass_search_script.php -k 'economy finance'
</pre>
Above script will output first 50 articles that contains both keyword "economy" 
and "finance" from 09/24/2011 to current time say 10/26/2012

<pre>
  ~$: php mass_search_script.php -k 'finance' -o 2
</pre>
Above script will output first 20 articles containing keyword finance

### Ouput File (all files stores json string, decodable in Java)
"body_file_name.txt": store url and body

"meta_file_name.txt": store title, url, lead paragraph, can be enriched

Look into the file, you will understand what the data looks like.
Json is an easy way of sharing data across different languages.
