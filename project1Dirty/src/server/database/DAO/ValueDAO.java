package server.database.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import server.database.Database;
import server.database.DatabaseException;
import shared.modelClasses.Value;

/**
 * @author hartley9
 *
 **************************
 ********USER TABLE********
 **************************
 *CREATE TABLE users (
 *primary_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE  DEFAULT 1, 
 *username VARCHAR NOT NULL  UNIQUE , 
 *password VARCHAR NOT NULL , 
 *firstname CHAR NOT NULL ,
 *lastname CHAR NOT NULL ,
 *email VARCHAR NOT NULL  UNIQUE )
 */
public class ValueDAO {

	private Database db;
	
	public ValueDAO(Database db) {
		this.db = db;
	}
	
	/**
	 * Adds value to value table with related Batch and Field Keys
	 * at row determined by batch coordinate values
	 * @param value 
	 */
	public void insert(Value value){
		PreparedStatement stmt = null;
		ResultSet keyRS = null;
		String query = "insert into valuesInfo (foreignImageKey,foreignFieldKey, col, row, value) "+
				"values (?,?,?,?,?)";
		try {
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, value.getForeignImageKey());
			stmt.setInt(2, value.getForeignFieldKey());
			stmt.setInt(3, value.getCol());
			stmt.setInt(4, value.getRow());
			stmt.setString(5, value.getValue());
			stmt.execute();
		}catch (SQLException e) {
				try {
					throw new DatabaseException("Could not insert contact", e);
				} catch (DatabaseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		finally {
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
		
	}
	
	public ArrayList<Value> findValue(int foreignFieldKey, String value){
		ArrayList<Value> result = new ArrayList<Value>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String findRow = "SELECT row, foreignImageKey  FROM valuesInfo where "+
				 "foreignFieldKey = '"+foreignFieldKey+"' and value = '"+value+"'";
		try {
			stmt = db.getConnection().prepareStatement(findRow);
			rs = stmt.executeQuery();
			while(rs.next()){
			Value valueResult = new Value();
			valueResult.setRow(rs.getInt(1));
			valueResult.setForeignImageKey(rs.getInt(2));
			result.add(valueResult);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Database.safeClose(stmt);
		
		
		return result;
	}
	
	
	/**
	 * Update a value in the table
	 * The parameters will depend on the type of update 
	 * use the map to identify which elements of the table to update
	 * key = element
	 * value = new element value
	 */
	public void update(){
		
	}
	
	/**
	 * This will query a specific Value table found using its IDs
	 */
	public Set<Value> read(int IDs[]){
		Set<Value> returnValues = new TreeSet<Value>();

		return returnValues;
	}
	
	public void delete(){
		
	}

}
