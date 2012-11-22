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
