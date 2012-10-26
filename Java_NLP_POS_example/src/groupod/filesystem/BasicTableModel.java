package groupod.filesystem;
// BasicTableModel.java
// Source code from CS108@stanford
/*
 Demonstrate a basic table model implementation
 using ArrayList of rows, where each row is itself an ArrayList
 of the data in that row.
 This code is free for any purpose -- Nick Parlante.
 
 A row may be shorter than the number of columns
 which complicates the data handling a bit.
*/
import javax.swing.table.*;
import java.util.*;
import java.io.*;

/**
 * The BasicTable Model is a model that provides basic functions of 
 * a table manipulation
 * @author changsu
 */
public class BasicTableModel extends AbstractTableModel {
	private List<String> colNames;	// defines the number of cols
	private List<List> data;	// one List for each row
	
	/**
	 * Default constsructor
	 */
	public BasicTableModel() {
		colNames = new ArrayList<String>();
		data = new ArrayList<List>();
	}


	/*
	 Basic getXXX methods required by an class implementing TableModel
	*/
	
	/**
	 * Returns the name of each col, numbered 0..columns-1
	 * @param column index
	 * @return column name
	 */
	public String getColumnName(int col) {
		return colNames.get(col);
	}
	
	/**
	 * @return number of columns
	 */
	public int getColumnCount() {
		return(colNames.size());
	}
	
	/**
	 * @return number of rows
	 */
	public int getRowCount() {
		return(data.size());
	}
	
	/**
	 * Returns the data for each cell, identified by its row, col index
	 * @param row
	 * @param col
	 * @result data Object
	 */
	public Object getValueAt(int row, int col) {
		List rowList = data.get(row);
		Object result = null;
		if (col<rowList.size()) {
			result = rowList.get(col);
		}
		
		// _apparently_ it's ok to return null for a "blank" cell
		return(result);
	}
	
	
	/**
	 * @param row
	 * @param col
	 * @return true if a cell should be editable in the table
	 */
	public boolean isCellEditable(int row, int col) {
		return true;
	}
	
	/**
	 * Change the value of a cell
	 * @param object value
	 * @param row
	 * @param col
	 */
	public void setValueAt(Object value, int row, int col) {
		List rowList = data.get(row);
		
		// make this row long enough
		if (col>=rowList.size()) {
			while (col>=rowList.size()) rowList.add(null);
		}

		// install the data
		rowList.set(col, value);
		
		// notify model listeners of cell change
		fireTableCellUpdated(row, col);
	}
	
	
	/*
	 Convenience methods of BasicTable
	*/
	
	/**
	 * Adds the given column to the right hand side of the model
	 * @param column name
	 */
	public void addColumn(String name) {
		colNames.add(name);
		fireTableStructureChanged();
		/*
		 At present, TableModelListener does not have a more specific
		 notification for changing the number of columns.
		*/
	}

	/**
	 * Adds the given row
	 * @param row
	 * @return new row index
	 */
	public int addRow(List row) {
		data.add(row);
		fireTableRowsInserted(data.size()-1, data.size()-1);
		return(data.size() -1);
	}
	
	/** 
	 * Adds an empty row
	 * @return new row index
	 */
	public int addRow() {
		// Create a new row with nothing in it
		List row = new ArrayList();
		return(addRow(row));
	}


	/** 
	 * Deletes the given row
	 * @param row
	 */
	public void deleteRow(int row) {
		if (row == -1) return;
		
		data.remove(row);
		fireTableRowsDeleted(row, row);
	}
	
	
	/*
	 Utility.
	 Given a text line of tab-delimited strings, build
	 an ArrayList of the strings.
	*/
	private static List<String> stringToList(String string) {
	    // Create a tokenize that uses \t as the delim, and reports
	    // both the words and the delimeters.
		StringTokenizer tokenizer = new StringTokenizer(string, "\t", true);
		List<String> row = new ArrayList<String>();
		String elem = null;
		String last = null;
		while(tokenizer.hasMoreTokens()) {
			last = elem;
			elem = tokenizer.nextToken();
			if (!elem.equals("\t")) row.add(elem);
			else if (last.equals("\t")) row.add("");
			// We need to track the 'last' state so we can treat
			// two tabs in a row as an empty string column.
		}
		if (elem.equals("\t")) row.add(""); // tricky: notice final element
		
		return(row);
	}
	
	/*
	 Utility
	 Given a collection of strings, writes them out as a line of text, separated by tabs.
	 Null strings are interpreted as a zero-length strings.
	*/
	private static void writeStrings(BufferedWriter out, Collection strings) throws IOException {
		Iterator it = strings.iterator();
		
		while (it.hasNext()) {
			String string = (String)it.next();
			if (string!=null) out.write(string);
			if (it.hasNext()) out.write('\t');
		}
		out.newLine();
	}
	
	


	/**
	 * Loads the whole model from a file of tab-delimited text.
	 * @param file
	 */
	public void loadFile(File file) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			
			// read the column names
			List<String> first = stringToList(in.readLine());
			colNames = first;
			
			// each line makes a row in the data model
			String line;
			data = new ArrayList<List>();
			while ((line = in.readLine()) != null) {
				data.add(stringToList(line));
			}
			
			in.close();
			
			// Send notifications that the whole table is now different
			fireTableStructureChanged();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Saves the model to the given file as tab-delimited text.
	 * @param file
	 */
	public void saveToFile(File file) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
		 	
		 	// write the column names
		 	writeStrings(out, colNames);
		 	
		 	// write all the data
		 	for (int i=0; i<data.size(); i++) {
		 		writeStrings(out, data.get(i));
		 	}
		 	
		 	out.close();
		 }
		 catch (IOException e) {
		 	e.printStackTrace();
		 }
	}
	
	public void clear(){
		colNames.clear();
		data.clear();
	}
}

