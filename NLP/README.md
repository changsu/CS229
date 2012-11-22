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

2)  [POS JAR files] (http://nlp.stanford.edu/software/tagger.shtml#Download)
used to extract part-of-speech tag for each word in a sentence, and finally used in pos feature
extraction of a relation

3) [NER JAR files] (http://nlp.stanford.edu/software/CRF-NER.shtml#Download)
used to generate the entity of each word in a sentence if applicable, and finally used in entity
type feature extraction of a relation

### How to Run
After setting up the environment, we're ready to go!
The main functionality of the Java application is to read and parse input file with articles and generate listed of relations(samples), which are labelled and also transformed into feature vector for training use.

To start the program, right click on "Processor.java"->Run as->Run configurations
In the pop up dialog and right panel, click the Arguments tab, under "Program arguments:" enter
input file name and output file name respectively seperated by space. For instance
<pre>body_file_name.txt output.txt</pre>
in above case, we read in "body_file_name.txt" file and output the result to "output.txt" file.
<b>It should be noted that both the input file and outputfile are in "files" folder</b>

### Output File Format

### FAQ
<ul>
<li>What if Eclipse through "Java Out of Memory" Error</li>
Since the parse, pos taggers or ner will take a large space of JVM memory, we need to improve
that. Right click on "Processor.java"->Run as->Run configurations
In the pop up dialog and right panel, click the "Arguments" tab, under "VM arguments:" 
set the maximum JVM memory, for instance
<pre>-Xmx1024M<pre>, then JVM will be allocated 1024M memory to run the "Processor" application.
You can set any number you want, but be efficient.
</ul>