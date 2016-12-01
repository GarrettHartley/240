package server.database.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import server.database.Database;
import server.database.DatabaseException;
import shared.modelClasses.Field;

/**
 * @author hartley9
 *
 **************************
 *******FIELD TABLE********
 **************************
	CREATE TABLE fields (
	primaryID INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ,
	foreignProjectKey INTEGER NOT NULL ,
	fieldNumber INTEGER NOT NULL,
	title VARCHAR NOT NULL , 
	xcoord INTEGER NOT NULL  DEFAULT 0, 
	width INTEGER NOT NULL  DEFAULT 1, 
	helphtml VARCHAR NOT NULL , 
	knowndata VARCHAR)
 */
public class FieldDAO {
	
	private Database db;
	
	public FieldDAO(Database db) {
		this.db = db;
	}
	
	/**
	 * Add Field table 
	 * I'm not sure if this will simply initialize a table
	 * or take in all of the parameters to create and populate one
	 */
	public void insert(Field field){
		PreparedStatement stmt = null;
		ResultSet keyRS = null;
		
		String query = "insert into fields (title, xcoord, width, helphtml,knowndata,foreignProjectKey,fieldnumber) "+
				"values (?,?,?,?,?,?,?)";
		try {
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, field.getTitle());
			stmt.setInt(2, field.getXcoord());
			stmt.setInt(3, field.getWidth());
			stmt.setString(4, field.getHelpHTML());
			stmt.setString(5,field.getKnownData());
			stmt.setInt(6,field.getForeignProjectKey());
			stmt.setInt(7, field.getFieldNumber());
			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				field.setPrimaryID(id);
			}
			else {
				try {
					throw new DatabaseException("Could not insert contact");
				} catch (DatabaseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
	
	public int get_first_fieldID(int pID){
		int result = -1;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String query = "SELECT primaryID FROM fields where "+
				"foreignProjectKey = '"+pID+"' limit 1";
	
		try {
			stmt = db.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			if(rs.next()){
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}

		return result;
	}
	
	/**
	 * Identify the field by id and update
	 * use the map to identify which elements of the table to update
	 * key = element
	 * value = new element value
	 */
	public void update(int primaryID,Map<String,String> updateVariables){
		
	}
	
	public int getNumberOfFields() throws DatabaseException{
		int result = 0;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String query = "SELECT primaryID FROM fields order by primaryID desc limit 1";
		try {
			stmt = db.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			if(rs.next()){
				result = rs.getInt(1);
			}
			
		} 	catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
						
			throw serverEx;
		}		
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		return result;
	}
	
	/**
	 * Identify the field by id and read values from returned objects
	 */
	public ArrayList<Field> getFields(int foreignProjectKey) throws DatabaseException {
		ArrayList<Field> result = new ArrayList<Field>();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String query = new String();
		if(foreignProjectKey == -5){
		query ="SELECT * FROM fields";
		}
		else{
		 query = "SELECT * FROM fields where foreignProjectKey = '"+foreignProjectKey+"'";
		}
		
		try {
			stmt = db.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			while(rs.next()){
				Field field = new Field();
				field.setPrimaryID(rs.getInt(1));
				field.setFieldNumber(rs.getInt(8));
				field.setTitle(rs.getString(3));
				field.setXcoord(rs.getInt(4));
				field.setWidth(rs.getInt(5));
				field.setHelpHTML(rs.getString(6));
				field.setKnownData(rs.getString(7));
				field.setForeignProjectKey(rs.getInt(2));
				result.add(field);
				
			}
			
		} 	catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
						
			throw serverEx;
		}		
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}

		return result;
	}
	
	public void delete(){
		
	}

}
