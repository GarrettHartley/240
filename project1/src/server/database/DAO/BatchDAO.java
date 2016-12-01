package server.database.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import server.database.Database;
import server.database.DatabaseException;
import shared.communicationClasses.GetSampleBatchParam;
import shared.communicationClasses.GetSampleBatchResult;
import shared.communicationClasses.SubmitBatchParam;
import shared.modelClasses.Batch;
import shared.modelClasses.Value;


/**
 * @author hartley9
 *
 **************************
 *******BATCH TABLE********
 **************************
 *CREATE TABLE batches (
 *primaryID INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , 
 *foreignProjectKey INTEGER NOT NULL , 
 *foreignUserKey INTEGER NOT NULL , 
 *imgURL VARCHAR NOT NULL )
 */
public class BatchDAO {
	
	private Database db;
	
	public BatchDAO(Database db) {
		this.db = db;
	}
	
	
	/**
	 * Adds a batch to the batches table
	 */
	public void insert(Batch batch){
		
		PreparedStatement stmt = null;
		ResultSet keyRS = null;
		
			String query = "insert into batches (foreignProjectKey, foreignUserKey, file)"+
					" values (?, ?, ?)";
			
			try {
				stmt = db.getConnection().prepareStatement(query);
				stmt.setInt(1, batch.getForeignProjectKey());
				stmt.setInt(2, batch.getForeignUserKey());
				stmt.setString(3, batch.getImgURL());
				
// If the query is successful story the primary ID in the Model Class representation of the Batch		
				if (stmt.executeUpdate() == 1) {
					Statement keyStmt = db.getConnection().createStatement();
					keyRS = keyStmt.executeQuery("select last_insert_rowid()");
					keyRS.next();
					int id = keyRS.getInt(1);
					batch.setPrimaryID(id);	
				}
			}catch (SQLException e) {
					try {
						throw new DatabaseException("Could not insert contact", e);
					} catch (DatabaseException e1) {
						e1.printStackTrace();
					}
				}
			finally {
				Database.safeClose(stmt);
				Database.safeClose(keyRS);
			}

	}
	
	/**
	 * Identify the batch using the keys (int foreignProjectKey, int foreignUserKey, int primaryID)
	 * use the map to identify which elements of the table to update
	 * key = element
	 * value = new element value
	 */
	public void SubmitBatch(SubmitBatchParam param, ValueDAO vDAO, int firstFieldID){

		String allValues = param.getFieldValues();
		String[] valueGroups = allValues.split(";");
		int col = 0;
		int row = 0;
		int fieldID = firstFieldID;
		Value value = new Value();
		PreparedStatement stmt = null;
		String update = "update batches set foreignUserKey = '-1' where primaryID ='"+param.getBatchID()+"'";
		try {
			stmt = db.getConnection().prepareStatement(update);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for(int i=0;i<valueGroups.length;i++){
			fieldID = firstFieldID;
			String[] values = valueGroups[i].split(",",-1);
			row = i+1;
			for(int j=0;j< values.length;j++){
			col = j+1;
			value.setValue(param.getBatchID(),fieldID,col,row,values[j]);
			vDAO.insert(value);
			fieldID++;
			}
		}
		
		
		
	}

// Return the Image URL of the batch with the given ID
	public String getImgURLByID(int batchID){
		String result = new String();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String query = "SELECT file FROM batches where "+
				"primaryID = '"+batchID+"'";
	
		try {
			stmt = db.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			if(rs.next()){
				result = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		return result;
		
	}
	
	
// 	Returns the primary ID of project which owns the given batch and userID
	public int getProjectIDByBatchID(int batchID, int userID){
		int result = -1;
		int foreignUserKey=-1;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String query = "SELECT foreignProjectKey, foreignUserKey FROM batches where "+
				"primaryID = '"+batchID+"'";
	
		try {
			stmt = db.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			if(rs.next()){
				result = rs.getInt(1);
				foreignUserKey = rs.getInt(2);	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		if(foreignUserKey!=userID){
			return -1;
		}
		return result;
	}

// Returns a batch from a given project that is not already assigned to a user
	public Batch getSingleBatch(int pID,int uID){
		Batch result = new Batch();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String query = "SELECT * FROM batches where "+
				"foreignProjectKey = '"+pID+"' and foreignUserKey = '0' limit 1";

		try {
			stmt = db.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			if(rs.next()){
			result.setPrimaryID(rs.getInt(1));
			result.setImgURL(rs.getString(4));
			result.setForeignProjectKey(pID);
			String updateForeignProjectKey ="update batches set foreignUserKey = '"+uID+"' where primaryID = '"+rs.getString(1)+"'";
			stmt = db.getConnection().prepareStatement(updateForeignProjectKey);
			stmt.executeUpdate();
			}
		} catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
						
			try {
				throw serverEx;
			} catch (DatabaseException e1) {
				e1.printStackTrace();
			}
		}
		finally{
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}

		return result;	
	}

// Marks the batch with the given primary ID as complete
	public void setComplete(int pID){
		PreparedStatement stmt = null;
		String update = "update batches set foreignUserKey = '-1' where primaryID = '"+pID+"'";
		
		try {
			stmt = db.getConnection().prepareStatement(update);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}	finally{
			Database.safeClose(stmt);
		}
	}
	
	/**
	 * Return batches that can be checked out for a given project
	 */
	public GetSampleBatchResult getSampleBatch(GetSampleBatchParam param){
		GetSampleBatchResult result = new GetSampleBatchResult();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String query = "SELECT file FROM batches where foreignUserKey != '"+1+"' and foreignProjectKey = '"+param.getProjectID()+"'";
		
		try {
			stmt = db.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			if(rs.next()){
			result.setImgURL(rs.getString(1));
			result.setValid(true);
			}
		} catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);

			try {
				throw serverEx;
			} catch (DatabaseException e1) {
				e1.printStackTrace();
			}
		}finally{
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}

		return result;
	}
	
	public void delete(){
		
	}
	
}
