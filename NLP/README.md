### Environment Setting Up

We are using Stanford Parser Lib from [Stanford NLP] (http://nlp.stanford.edu/software/index.shtml) for labelling and feature extraction.
In order to develop,

1. Import the project through following steps:
   File->Import->General->Existing Projects into Workspace->Next->Select root directory->Browse and then select the java project where you save. Basically, just locate the NLP folder ( we have the java project) in your local git repo.
2. Download the software (jar files) if you have not done yet and add in external jars in to java build path of your current project through following steps:
   Right click on your project imported->Properties->Java Build Path->Libraries(in right panel)->Add External Jars->Locate where you save the downloaded software.

Jar Files needed to be added into Referenced Libraries:

1) [Parser JAR files] (http://nlp.stanford.edu/software/lex-parser.shtml#Download)
used to parse the sentence, get penn Treebank structure and also dependencies, and finally 
used for labelling each relation based on several rules depending on the parsed result
You need to add external JAR files `stanford-parser.jar` and `stanford-parser-2012-07-09-models.jar` here

2)  [POS JAR files] (http://nlp.stanford.edu/software/tagger.shtml#Download)
used to extract part-of-speech tag for each word in a sentence, and finally used in pos feature
extraction of a relation
You need to add external JAR file `stanford-postagger.jar`

3) [NER JAR files] (http://nlp.stanford.edu/software/CRF-NER.shtml#Download)
used to generate the entity of each word in a sentence if applicable, and finally used in entity
type feature extraction of a relation
You need to add external JAR file `stanford-ner-2012-11-11.jar`

### How to Run
After setting up the environment, we're ready to go!
The main functionality of the Java application reads and parses input file with articles and generate listed of relations(samples), which are labelled and also transformed into feature vector for training use.

To start the program, right click on "Processor.java"->Run as->Run configurations
In the pop up dialog and right panel, click the Arguments tab, under "Program arguments:" enter
input file name and output file name respectively seperated by space. For instance
<pre>body_file_name.txt output.txt</pre>
in above case, we read in "body_file_name.txt" file and output the result to "output.txt" file.

<b>It should be noted that both the input file and outputfile are in "files" folder</b>

### Features
In order to realize relation-independent model, we extracte features that do not depend on semantic or syntactic roles, namely, we do not depend on <b>parser</b> to genearte feature. 
Currently, we generate features as below given e1 and e2 (Tree node)
<ul>
<li>words between last word of e1 and first word of e2 (including)</li>
<li>number of words between last word of e1 and first word of e2</li>
<li>number of stop words between last word of e1 and first word of e2</li>
<li>number of captalized(first char) words between last word of e1 and first word of e2</li>
<li>number of punctuation between last word of e1 and first word of e2</li>
<li>number of noun-phrases between last word of e1 and first word of e2</li>
<li>entity type of head of e1</li>
<li>entity type of head of e2</li>
<li>pos of the word left to leftmost word of e1</li>
<li>pos of the word right to rightmost word of e2</li>
<li>pos sequence between last word of e1 and first word of e2</li>
</ul>

### Output File Format
You can open the "output.txt" in files folder to have a glipse of the format. Basically, it's quite similar to the file format required by [libsvm](http://www.csie.ntu.edu.tw/~cjlin/libsvm/) (at least in my mind)

Each line represents one samples with format:
<pre>label f1:v1 f2:v2 f3:v3 ...</pre>
where label represents whether its positive or negative relation(-1/+1) and fi:vi represents
the ith feature and its corresponding value.

Concrete example:
<pre>-1 10471:1 420:1 12362:1 21878:3 21879:1 21880:0 21881:1 21882:0 21960:1 21969:1</pre>

### Dictionary Used in Building Features
In files folder:
<ul>
<li>`3esl.txt` Common English words dictionary</li>
<li>`stopword.txt` Common English stop words dictionary</li>
<li>`pos.txt` Common part-of-speech tags</li>
<li>`ner.txt` Common Name Entities</li>
</ul>
<b>It should also be noted that, we do not build "pos sequence dictionary" as above three in ahead. Instead, we build the dictionary along with each time we see a new pos sequence while extracting features for each relation</b>
### Logging
For debugging, I also output some log in Java console so that we can manually test the correctness of the feature extraction. For each relation being processed, it will log some key info like this:
<pre>
original sentence: Europe was mentioned once, but the reference had nothing to do with economics. 
tagged sentence: Europe_NNP was_VBD mentioned_VBN once_RB ,_, but_CC the_DT reference_NN had_VBD nothing_NN to_TO do_VB with_IN economics_NNS ._. 
ner sentence: Europe/LOCATION was/O mentioned/O once/O,/O but/O the/O reference/O had/O nothing/O to/O do/O with/O economics/O./O 
e1: (NP (DT the) (NN reference))
e2: (NP (NNS economics))
words between: {5455=1, 21594=1, 5872=1, 19800=1, 12996=1, 15873=1, 8568=1}
number of words: 7
number of stop words: 5
number of cap words: 0
number of punctuations: 0
number of phrases: 1
Entity type e1: 0
Entity type e2: 0
POS left e1: 1
POS right e2: 0
POS sequence content: VBD-NN-TO-VB-IN-
POS sequence: 17
</pre>

### FAQ
<ul>
<li>What if Eclipse throws "Java Out of Memory" Execption</li>
Since the parse, pos taggers or ner will take a large space of JVM memory, we need to improve
that. Right click on "Processor.java"->Run as->Run configurations
In the pop up dialog and right panel, click the "Arguments" tab, under "VM arguments:" 
set the maximum JVM memory, for instance
<pre>-Xmx1024M</pre> 
then JVM will be allocated 1024M memory to run the "Processor" application.
You can set any number you want, but be efficient.
</ul>