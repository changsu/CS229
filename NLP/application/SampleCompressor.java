package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * This class is used to "compress" sample dataset and generate
 * equal number of positive and negative samples.
 * Since the dimension of the dataset is 21000~, we need to generate
 * enough samples to enable the model to recover the data pattern
 * as well as efficient data for computation. That's how we make decisions.
 * @author changsu
 */
public class SampleCompressor {
	// constants
	private final static int NUM_POS_SAMPLE= 5000;
	private final static int NUM_NEG_SAMPLE = 60000;
	private final static int NEG_INTERVAL = 10;
	private final static String FILENAME_BEF_COMPRESS = "dataset.txt";
	private final static String FILENAME_AFT_COMPRESS = "dataset_compressed.txt";
	
	public SampleCompressor() {};
	
	public void process() {
		File inf = Processor.readFile("corpus/" + FILENAME_BEF_COMPRESS);
		File outf = new File("files/corpus/" + FILENAME_AFT_COMPRESS);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(inf));
			Writer writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outf), "UTF-8"));

			String line = null;
			int counter_pos = 0;
			int counter_neg = 0;
			// in order for uniformly sampling negative samples
			
			while((line = reader.readLine()) != null) {
				// extract the first element in line (label)
				String[] tokens = line.split(" ");
				if (Integer.parseInt(tokens[0]) == 1 && counter_pos++ <= NUM_POS_SAMPLE) {
					writer.write(line + "\n");
				} else if (Integer.parseInt(tokens[0]) == 0 && 
							counter_neg++ < NUM_NEG_SAMPLE && counter_neg % NEG_INTERVAL == 0) {
					writer.write(line + "\n");
					
				}
				// if we get enough pos and neg samples both, stop collecting samples
				if (counter_pos > NUM_POS_SAMPLE && counter_neg > NUM_POS_SAMPLE * NEG_INTERVAL)
					break;
			}
			reader.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		SampleCompressor sc = new SampleCompressor();
		sc.process();
	}
}
