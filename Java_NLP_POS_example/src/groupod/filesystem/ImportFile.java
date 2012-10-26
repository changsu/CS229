package groupod.filesystem;
import java.io.*;

// TODO: Auto-generated Javadoc
/**
 * The ImportFile class imports seed data from existing dataset/spreadsheet.
 *
 * @author suchang
 */
public class ImportFile {
	
	/** The filename. */
	private String filename;
	
	/** The content. */
	private String content = "";
	
	/**
	 * Default constructor.
	 */
	public ImportFile(){
		filename = "test.txt";
	}
	
	/**
	 * Constructor with argument of file name.
	 *
	 * @param filename the filename
	 */
	public ImportFile(String filename){
		this.filename = filename;
	}
	
	/**
	 * Start reading file.
	 *
	 * @return String format of file content
	 */
	public String readFile(){
		try{
			// Open the file that is the first 
			// command line parameter
			FileInputStream fstream = new FileInputStream(filename);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {
				// Print the content on the console
				content += strLine;
			}
			//Close the input stream
			in.close();
			return content;
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
			return "";
		}
	}
}
