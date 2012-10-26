package groupod.filesystem;
import groupod.application.*;
import groupod.algorithms.*;
import java.sql.*;
import java.util.*;

/**
 * The class is used to export the generated BN to DB
 * @author changsu
 *
 */
public class ExportToDB {
	private static String account = "ccs108changsu"; // replace with your account
	private static String password = "gopeicei"; // replace with your password
	private static String server = "mysql-user.stanford.edu";
	private static String database = "c_cs108_changsu"; // replace with your db
	private static Statement stmt;
	private static String graphName;
	
	public void connect(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection
			( "jdbc:mysql://" + server, account ,password);
			stmt = con.createStatement();
			stmt.executeQuery("USE " + database);
			createIndexTable();
//			ResultSet rs = stmt.executeQuery("SELECT * FROM users");
//			while(rs.next()) {
//				String name = rs.getString("name");
//				System.out.println(name+ "\n");
//			}
		} catch (SQLException e) {
//			TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
//			TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * The method is used to create a index table for all the graphs
	 * Schema |graph_id|category|support|confidence|word_type|tablename
	 * @param stmt
	 */
	private void createIndexTable(){
		String query = "CREATE TABLE IF NOT EXISTS `graph_index`(" +
		"`graph_id` BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,"+
		"`category` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci," +
		"`support` INT UNSIGNED," + "`confidence` FLOAT UNSIGNED," + 
		"`word_type` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci," +
		"`tablename` TEXT CHARACTER SET utf8 COLLATE utf8_general_ci" + 
		") ENGINE = MYISAM;";
		executeQuery(query);
	}
	
	/**
	 * The method firstly dump the table attribute into graph_index table,
	 * @param category
	 * @param support
	 * @param confidence
	 * @param wordtype
	 */
	public void dumpIntoIndexTable(String category, String support, String confidence, String wordtype){
		// TODO check whether the table has been exist before
		// The 4 attributes is unique, but seems bug in table name, so use only 2, to be fixed in the furture
		// Need to reconstruct to meaningful table name
		graphName = "";
		String rawName = category.toLowerCase();
		String[] pieces = rawName.split(" ");
		for(int i = 0; i < pieces.length; i++){
			graphName += pieces[i];
		}
		graphName = graphName + "_" + wordtype;
		String query = "INSERT INTO graph_index (category, support, confidence, word_type, tablename) VALUES(\"" + category + "\",\""
		+ support + "\",\"" + confidence + "\",\"" + wordtype + "\",\"" + graphName + "\");";
		executeQuery(query);
	}
	
	/**
	 * The method is used to dump graph data into table
	 */
	public void dumpIntoTable(){
		buildTableSchema();
		// Test first, check whether all the information are stored in the node	
		Set<String> keys = Category.frequentSingleItem.keySet();
		for(String key : keys){
			Node node = Category.frequentSingleItem.get(key);
			String nodeName = node.getName();
			int support = node.getSupport();
			String parents = transSet(node.getParents());
			String children = transSet(node.getChildren());
			String localTable = transTable(node.getLocalMap());
			// Insert into Table
			String query = "INSERT INTO " + graphName + " (node_name, support, parents, children, local_table) VALUES(\"" + nodeName + "\",\""
			+ support + "\",\"" + parents + "\",\"" + children + "\",\"" + localTable + "\");";
			executeQuery(query);
		}
	}
	
	/**
	 * Transform the node set into storable db content with format
	 * node1, node2, node3...
	 * @param nodes
	 * @return
	 */
	private String transSet(Set<Node> nodes){
		String result = "";
		for(Node node : nodes){
			result += node.getName() + ",";
		}
		if(result.length() > 1){
			return result.substring(0, result.length() - 1);
		}else{
			return result;
		}
	}
	
	private String transTable(Map<Set<String>, Double> map){
		String result = "";
		Set<Set<String>> keys = map.keySet();
		for(Set<String> key : keys){
			result += transStrs(key) + " " + map.get(key) + "|";
		}
		if(result.length() > 1){
			return result.substring(0, result.length() - 1);
		}else{
			return result;
		}
	}
	
	private String transStrs(Set<String> strs){
		String result = "";
		for(String str : strs){
			result += str + ",";
		}
		if(result.length() > 1){
			return result.substring(0, result.length() - 1);
		}else{
			return result;
		}
	}
	
	/**
	 * Build the table schema for each graph to be exported into db
	 */
	private void buildTableSchema(){
		String query = "CREATE TABLE IF NOT EXISTS `" + graphName + "`(" +
		"`node_id` BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,"+
		"`node_name` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci," +
		"`support` INT UNSIGNED," +
		"`parents` TEXT CHARACTER SET utf8 COLLATE utf8_general_ci," + 
		"`children` TEXT CHARACTER SET utf8 COLLATE utf8_general_ci," +
		"`local_table` TEXT CHARACTER SET utf8 COLLATE utf8_general_ci" + 
		") ENGINE = MYISAM;";
		executeQuery(query);
	}
	
	/**
	 * The method is used to execute query and return success or failure
	 * @param stmt
	 * @param query
	 * @return
	 */
	private static boolean executeQuery(String query){
		try{
			stmt.execute(query);
			return true;
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}