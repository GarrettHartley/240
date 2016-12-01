package server.database.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import server.database.Database;
import server.database.DatabaseException;
import shared.communicationClasses.Get_projects_param;
import shared.communicationClasses.Get_projects_result;
import shared.modelClasses.Project;

/**
 * @author hartley9
 *
 **************************
 ******PROJECT TABLE*******
 **************************
 CREATE TABLE projects (
 primaryID INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , 
 title VARCHAR NOT NULL , 
 recordsperimage INTEGER NOT NULL  DEFAULT 1, 
 firstycoord INTEGER NOT NULL  DEFAULT 0, 
 recordheight INTEGER NOT NULL  DEFAULT 1,
 numberfields INTEGER NOT NULL DEFAULT 0)
 */

public class ProjectDAO {
	
	ProjectDAO(){
		
	}
	
	private Database db;
	
	public ProjectDAO(Database db) {
		this.db = db;
	}
	
	public Get_projects_result getProjects(Get_projects_param param) throws DatabaseException{
		Get_projects_result result = new Get_projects_result();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String query = "SELECT primaryID, title FROM projects";
			stmt = db.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			while(rs.next()){
				Get_projects_result presult = new Get_projects_result();
				presult.setProjectID(rs.getInt(1));
				presult.setProjectTitle(rs.getString(2));
				result.getResults().add(presult);
				result.setValid(true);
			}
		} catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
						
			throw serverEx;
		}
		finally{
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
				
		return result;
	}
	
	public Project getProjectByID(int pID){
		Project result = new Project();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String query = "SELECT * FROM projects where primaryID = '"+pID+"'"; 
		
		try {
			stmt = db.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();

			while(rs.next()){
				result.setFirstYCoord(rs.getInt(4));
				result.setRecordHeight(rs.getInt(5));
				result.setRecordsPerImage(rs.getInt(3));
				result.setNumberOfFields(rs.getInt(6));
			}
			
		} 	catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
						
			try {
				throw serverEx;
			} catch (DatabaseException e1) {
				e1.printStackTrace();
			}
		}		
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		if(result.getFirstYCoord()!=-1){
		return result;
		}
		else{
			return null;
		}
		
	}
	
	/**
	 * Adds a new Project to table
	 */
	
	public void insert(Project project){
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		
		try {
			String query = "insert into projects (title, recordsperimage, firstycoord, recordheight, numberfields) values (?, ?, ?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, project.getTitle());
			stmt.setInt(2, project.getRecordsPerImage());
			stmt.setInt(3, project.getFirstYCoord());
			stmt.setInt(4, project.getRecordHeight());
			stmt.setInt(5, project.getNumberOfFields());
			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				project.setPrimaryID(id);
			}
			else {
				try {
					throw new DatabaseException("Could not insert contact");
				} catch (DatabaseException e) {
					e.printStackTrace();
				}
			}
		}
		catch (SQLException e) {
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
		
}
