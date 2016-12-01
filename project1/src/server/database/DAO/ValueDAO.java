package server.database.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
					e1.printStackTrace();
				}
			}finally {
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
		
	}
	
//Returns the values that correspond to the given Field Key and Value string	
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
			e.printStackTrace();
		}
		Database.safeClose(stmt);
		
		return result;
	}
	

}
