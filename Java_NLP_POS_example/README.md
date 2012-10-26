
###Machine Learning for Recommendation System(suggestion tab) in decorya.com
####Goal: 
Develop a software system that extracts useful tags info about collections, goods or merchants from user generated content, 
builds bayesian network on these tags and export the network structure to database that is further used by php for the 
recommendation system

#### Functions of the Java Application
1. Extract structured and unstructured data by scripting home decor-related web pages
2. Part-of-Speech tags extraction from UGC and spreadsheet/graphic views
3. Bayesian Network Builder
4. Bayesian Network operator, adding/deleting/reversing edges of the network structure
5. Interface to export to databse

####Installation and Running
1.Run in Eclipse IDE: 

1) [Download Eclipse IDE for Java EE Developers](http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/heliossr1) and unzip in a folder of your choice

2) Open Eclipse

3) Import the project: select "File"->"Import". In the popup "Import" dialog, select "General"->"Existing Projects into Workspace" and click "next". In the new dialog, "Select root directory" using "Browse" button and navigate to the path where you save the project. Then click "Finish".

4) Add external .jar files into class path: Right click the project name in the project explorer, select "Properties" in the pop up menu. In the pop up dialog, choose the tab "Java Build Path", choose "Libraries" tab, click "Add External JARs..." and choose the 4 .jar files _jsoup-1.6.1jar jxl-2.6.jar stanford-postagger.jar stanford-postagger-2012-01-06.jar_ in the Lib folder in project path, then click "Open" and "Ok"

5) Now, in the project explorer, navigate to the src/groupod/application/GroupodWindow.java file, and run it as Java Application

2.Run ouside IDE:

1) Windows: Open the folder "Groupod-datamining-running version", double click "groupod.jar", then you can run the program.

2) Mac/Linux: cd into the folder "Goupod-datamining-running version", type the command line: java -jar groupod.jar

####Usage (The "Import","Export","Feature Extraction", "Start Filtering" button has deprecated) 
1. _Under IDE Running Environment_: Run src/groupod/filesystem/ExtractStructData.java and src/groupod/filesystem/ExtractUnstructData.java as Java Application and get struct and unstructured data in "output.xls" and "UnstructData.xls" files respectively.
2. "Spreadsheet View" and "Graphic View" provide basic views of the extracted unstruct data from UGC.
3. "Build Bayesian Network" button will direct to another window that generates the network. you can set up following parameters of the network builder.
<ul>
<li>category</li>
<li>style</li>
<li>support (affects number of nodes in the network)</li>
<li>confidence (affects number of edges in the network)</li>
<li>Nouns/Adjs/Nouns+Adjs (choose type of nodes)</li>
</ul>
4. Once set up support, confidence and word type, click one of the button in category or style, the builder will start building Bayesian Network.
5. Once the network will is built, a graphic view with green nodes will show up. The size of the circle represents the frequency of the word and the 
directional edge represent the cause-and-effect relationship between two nodes. At the bottom is the control panel that provide basic operation of the
network. Choose add, delete or reverse in the "Edit Edge" panel, assign head and tail names, then click "done", this record of operation has been saved. 
You can save multiple operation, namely editing multiple edges at the same time. Then, click "Re-build" button, the whole network struture and parameters 
will be regenerated embedding the "expert knowledge". 
6. "Export to Database" button will export the built Bayesian Network structure to the dababase that will further interact with php program 
to provide suggestion features to the site.
   

####Software Package and Class Documentation.
Please refer to the "doc" folder for more details.

#### Algorithm Details

1.Feature Extraction: Use Stanford NLP software to extract Part-Of-Speech tags from user generated content and count the frequency of nouns and adjectives for each picture.

2.Frequent Itemset Finding: As illustrated in the picture above. In each category, there are multiple pictures with interesting tags extracted from UGC. Each picture can be treated as "one basket" and for each category, use frequent item finding algorithm to obtain frequent item, namely, frequent tags people use for decoration of specific category.

3.Bayesian Belief Network(BN) Learning: This is the core and the hardest part of the algorithm. The learning part include parameter learning and structure learning. 
For more algorithm details about Bayesian Network Learning, please refer to [A tutorial on learning with Bayesian Network](http://research.microsoft.com/apps/pubs/default.aspx?id=69588).
<ul>
<li>Structure Learning</li>
We use the associate rules finding method to generate the cause-and-effect relationship between words(nodes/tags), namely, the edges in the Bayesian Network.
<li>Parameter Learning</li>
Given the structure of the BN, since we already obtain the frequency of each frequent itemset, we can learn the parameters using Maximum Likelihood Estimation method.
</ul>

#### Further Work
The suggestion system need to be further A/B tested by real user engagement and behavior. Following are the tentative methods:
<ul>
<li>Without network structure, make suggestion purely based on random selection of tags each time and compare the metrics</li>
<li>Test what's the percentage of time that user spend on search and suggestion tabs</li>
<li>Track user clicks on the "show more" button, which is actually a negative indicator of the suggestion system</li> 
<li>Get statistic from collections the average percentage of suggestion items each collection</li>
</ul>
