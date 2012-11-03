
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.io.StringReader;

import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.ling.CoreLabel;  
import edu.stanford.nlp.ling.HasWord;  
import edu.stanford.nlp.ling.Sentence;  
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

public class testV1 {

	 public static void main(String[] args) {
		    LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
		    if (args.length > 0) {
		      parseF(lp, args[0]);

		      
		    } else {
		      System.out.print("please input the file name:");
		    }
		  }
	 
	 private ArrayList<tSentence> samples;//TODO haven't been used
	 
	 public void readFile(String fileName) {
			// for training 
			// TODO: 
	}
	//public void readSentence(tSentence ts) {//TODO???
	//	// for future steps
	//}
	 //TODO: haven't been done not sure what it is
	 //XWJ: I think it is implemented in parseF
	 
	 
	 
	  public static void parseF(LexicalizedParser lp, String filename) {
		    // This option shows loading and sentence-segment and tokenizing
		    // a file using DocumentPreprocessor
		    TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		    // You could also create a tokenier here (as below) and pass it
		    // to DocumentPreprocessor
		    for (List<HasWord> sentence : new DocumentPreprocessor(filename)) {
		      Tree parse = lp.apply(sentence);
		      parse.pennPrint();
		      System.out.println();
		      
		      GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
		      Collection tdl = gs.typedDependenciesCCprocessed(true);
		      System.out.println(tdl);
		      System.out.println();
		    }
		  }

	  
	
}
