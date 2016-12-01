package server.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dataImporter.DataImporter;
import server.database.DAO.BatchDAO;
import server.database.DAO.FieldDAO;
import server.database.DAO.ProjectDAO;
import server.database.DAO.UserDAO;
import server.database.DAO.ValueDAO;

public class Database {

	public static void main(String[] args) {

	}
	
	private static final String DATABASE_DIRECTORY = "database";
	private static final String DATABASE_FILE = "indexerInfo.sqlite";
	private static final String DATABASE_URL = "jdbc:sqlite:" + DATABASE_DIRECTORY +
												File.separator + DATABASE_FILE;

	public static void initialize() throws DatabaseException {
		try {
			final String driver = "org.sqlite.JDBC";
			Class.forName(driver);
		}
		catch(ClassNotFoundException e) {
			
			DatabaseException serverEx = new DatabaseException("Could not load database driver", e);
			
			throw serverEx; 
		}
	}
	
	private DataImporter dataImporter;
	private ProjectDAO pojectDAO;
	private ValueDAO valueDAO;
	private BatchDAO batchDAO;
	private FieldDAO fieldDAO;
	private UserDAO userDAO;
	private Connection connection;
	
	public Database(){
		dataImporter = new DataImporter(this);
		pojectDAO = new ProjectDAO(this);
		valueDAO = new ValueDAO(this);
		batchDAO = new BatchDAO(this);
		fieldDAO = new FieldDAO(this);
		userDAO = new UserDAO(this);
		connection = null;
		try {
			initialize();
		} catch (DatabaseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}
	
	public void startTransaction() throws DatabaseException {
		try {
			assert (connection == null);			
			connection = DriverManager.getConnection(DATABASE_URL);
			connection.setAutoCommit(false);
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not connect to database. Make sure " + 
				DATABASE_FILE + " is available in ./" + DATABASE_DIRECTORY, e);
		}
	}
	
	public void endTransaction(boolean commit) {

		if (connection != null) {		
			try {
				if (commit) {
					connection.commit();
				}
				else {
					connection.rollback();
				}
			}
			catch (SQLException e) {
				System.out.println("Could not end transaction");
				e.printStackTrace();
			}
			finally {
				safeClose(connection);
				connection = null;
			}
		}
	}
	
	public DataImporter getDataImporter() {
		return dataImporter;
	}

	public void setDataImporter(DataImporter dataImporter) {
		this.dataImporter = dataImporter;
	}

	public ProjectDAO getProjectsDAO() {
		return pojectDAO;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public static void safeClose(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			}
			catch (SQLException e) {
				// ...
			}
		}
	}
	
	public static void safeClose(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			}
			catch (SQLException e) {
				// ...
			}
		}
	}
	
	public static void safeClose(PreparedStatement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			}
			catch (SQLException e) {
				// ...
			}
		}
	}
	
	public static void safeClose(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			}
			catch (SQLException e) {
				// ...
			}
		}
	}
	

}

