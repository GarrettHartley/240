package server.database.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import server.database.Database;
import server.database.DatabaseException;
import shared.communicationClasses.ValidateUserParam;
import shared.communicationClasses.ValidateUserResult;
import shared.modelClasses.User;


/**
 * @author hartley9
 *
 **************************
 ********USER TABLE********
 **************************
 CREATE TABLE users (
 primaryID INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE  DEFAULT 1, 
 username VARCHAR NOT NULL  UNIQUE , 
 password VARCHAR NOT NULL , 
 firstname CHAR NOT NULL ,
 lastname CHAR NOT NULL ,
 email VARCHAR NOT NULL  UNIQUE )
 */
public class UserDAO {

	private Database db;

	public UserDAO(Database db) {
		this.db = db;
	}

	/**
	* add a user to the users table
	*/
	public void insert(User user){
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		

		try {
			String query = "insert into users (username, password, firstname, lastname, numrecords,email) values (?, ?, ?, ?, ?,?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPassword());
			stmt.setString(3, user.getFirstname());
			stmt.setString(4, user.getLastname());
			stmt.setInt(5, user.getNumRecords());
			stmt.setString(6, user.getEmail());
			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				user.setPrimaryID(id);
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
		}finally {
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}

	}
	
	// Validates that the user exists
		public ValidateUserResult validateUser(ValidateUserParam param) throws DatabaseException {
			ValidateUserResult result = new ValidateUserResult();
			PreparedStatement stmt = null;
			ResultSet rs = null;

			String query = "select firstname, lastname, numrecords "
					+ "from users where username = '"+param.getUser()+"' and password = '"+param.getPassword()+"'";
			try {
				stmt = db.getConnection().prepareStatement(query);
				rs = stmt.executeQuery();
				String firstname = new String();
				String lastname = new String();
				int numrecords = -1;

				if(rs.next()){
					firstname = rs.getString(1);
					lastname = rs.getString(2);
					numrecords = rs.getInt(3);
					result.setValid(true);
				}
				else{
					result.setOutput("FALSE\n");
				}
				result.setFirstname(firstname);
				result.setLastname(lastname);
				result.setNumberOfRecords(numrecords);	
			} 	catch (SQLException e) {
				DatabaseException serverEx = new DatabaseException(e.getMessage(), e);

				throw serverEx;
			}	finally {
				Database.safeClose(rs);
				Database.safeClose(stmt);
			}
			return result;
		}

//Checks if the user has a Batch. If the User does not a Batch is assigned to them
	public String hasBatch(String username, String password){
		String result = "false";
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String query = "SELECT hasbatch FROM users where"+
				" username = '"+username+"' and password = '"+password+"'";
		String assignBatch = "update users set hasbatch = 'true' where username ='"+username+"' and password = '"+password+"'";

		try {
			stmt = db.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			if(rs.next()){
				result = rs.getString(1);
				if(result.equals("false")){
					stmt = db.getConnection().prepareStatement(assignBatch);
					stmt.executeUpdate();
					result = "false";
				}	
			}

		} 	catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);

			try {
				throw serverEx;
			} catch (DatabaseException e1) {
				e1.printStackTrace();
			}
		}	finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}

		return result;

	}

// Returns the Primary Id of the user with the given parameters
	public int getUserID(ValidateUserParam param) throws DatabaseException{
		int result = -1;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String query = "select primaryID from users where username = '"+param.getUser()+"' and password = '"+param.getPassword()+"'";
		try {
			stmt = db.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			while(rs.next()){
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

// Unassigns the batch from the user Identified by username and password
	public void unassignBatch(String username, String password){
		PreparedStatement stmt = null;
		String query = "update users set hasbatch = 'false' where username = '"+username+"' and password = '"+password+"'";

		try {
			stmt = db.getConnection().prepareStatement(query);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		Database.safeClose(stmt);

	}

// Gives the user credit for the number of records their submited batch contained
	public void giveCreditForBatch(int pID, String username, String password, int numRecords){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int curNumRecords=0;
		String queryGetCurrent = "SELECT numrecords FROM users where username = '"+username+"' and password = '"+password+"'";

		try {
			stmt = db.getConnection().prepareStatement(queryGetCurrent);
			rs = stmt.executeQuery();
			curNumRecords = rs.getInt(1)+numRecords;
			String querySum = "update users set numrecords = '"+curNumRecords+"' where username = '"+username+"' and password = '"+password+"'";
			stmt = db.getConnection().prepareStatement(querySum);
			stmt.execute();

		} catch (SQLException e) {

			e.printStackTrace();
		}	
		Database.safeClose(stmt);
	}

	public void delete(){

	}

}