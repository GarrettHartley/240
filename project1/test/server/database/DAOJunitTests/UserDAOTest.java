package server.database.DAOJunitTests;

import static org.junit.Assert.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.database.Database;
import server.database.DatabaseException;
import server.database.DAO.UserDAO;
import shared.communicationClasses.ValidateUserParam;
import shared.communicationClasses.ValidateUserResult;
import shared.modelClasses.User;

public class UserDAOTest {


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Database.initialize();
	}

	@Before
	public void setUp() throws Exception {
		PreparedStatement stmt = null;
		db = new Database();
		db.startTransaction();
		
		String deleteUsers = "drop table if exists users;";
		String createUsers = "CREATE TABLE users ("+
				"primaryID INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE  DEFAULT 1,"+
				"username VARCHAR NOT NULL  UNIQUE ,"+ 
				"password VARCHAR NOT NULL ,"+ 
				"firstname CHAR NOT NULL ,"+
				"lastname CHAR NOT NULL ,"+
				"numrecords INTEGER NOT NULL  DEFAULT 0,"+
				"hasbatch BOOLEAN DEFAULT false,"+
				"email VARCHAR NOT NULL  UNIQUE );";
		
		stmt = db.getConnection().prepareStatement(deleteUsers);
		stmt.execute();
		stmt = db.getConnection().prepareStatement(createUsers);
		stmt.execute();
		
		try {
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() throws Exception {
		db.endTransaction(false);
		db = null;
		userDAO = null;
	}
	
	private Database db;
	private UserDAO userDAO;
	
	
	//Tests insert and validateUser
	@Test
	public void testInsert() {
		userDAO = new UserDAO(db);
		User user = new User();
	
		ValidateUserParam validParam = new ValidateUserParam();
		ValidateUserParam invalidParam = new ValidateUserParam();
		ValidateUserResult validResult = new ValidateUserResult();
		ValidateUserResult invalidResult = new ValidateUserResult();

		user.setUsername("Madlib");
		user.setPassword("aveyTare");
		user.setFirstname("jDilla");
		user.setLastname("peteRock");
		user.setNumRecords(741);
		user.setEmail("cloud@dead");
		
		userDAO.insert(user);
		
		validParam.setPassword("aveyTare");
		validParam.setUser("Madlib");
		invalidParam.setUser("Madlib");
		invalidParam.setPassword("test1");
		
		try {
			validResult = userDAO.validateUser(validParam);
			invalidResult = userDAO.validateUser(invalidParam);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		
		assertEquals(validResult.getFirstname().equals("jDilla"),true);
		assertEquals(validResult.getLastname().equals("peteRock"),true);
		assertEquals(validResult.getNumberOfRecords()==741,true);
		assertEquals(validResult.getValid(),true);
		assertEquals(invalidResult.getValid(),false);
			
	}
	
	@Test
	public void testUnassignBatch() {
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		userDAO = new UserDAO(db);

			String query = "insert into users (username, password, firstname, lastname, numrecords,email, hasbatch) values (?, ?, ?, ?, ?,?,?)";
			try {
				stmt = db.getConnection().prepareStatement(query);
				stmt.setString(1, "test");
				stmt.setString(2, "test");
				stmt.setString(3, "Todd");
				stmt.setString(4, "Terje");
				stmt.setInt(5, 741);
				stmt.setString(6, "yours");
				stmt.setString(7, "true");
				stmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			userDAO.unassignBatch("test", "test");
			
			assertEquals("hasbatch".equals("false"), false);

	}

}
