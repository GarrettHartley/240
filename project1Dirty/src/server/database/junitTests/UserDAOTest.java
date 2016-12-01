package server.database.junitTests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.database.Database;
import server.database.DatabaseException;
import server.database.DAO.UserDAO;
import shared.communicationClasses.Validate_user_param;
import shared.communicationClasses.Validate_user_result;
import shared.modelClasses.User;

public class UserDAOTest {


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Database.initialize();
	}

	@Before
	public void setUp() throws Exception {
		db = new Database();
		db.startTransaction();
	}

	@After
	public void tearDown() throws Exception {
		db.endTransaction(false);
		db = null;
		userDAO = null;
	}
	
	private Database db;
	private UserDAO userDAO;
	

	@Test  //Tests insert and validateUser
	public void testInsert() {
		userDAO = new UserDAO(db);
		User user = new User();
		Validate_user_param validParam = new Validate_user_param();
		Validate_user_param invalidParam = new Validate_user_param();
		Validate_user_result validResult = new Validate_user_result();
		Validate_user_result invalidResult = new Validate_user_result();

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
	public void testHasBatch() {
		fail("Not yet implemented");
	}


	@Test
	public void testUnassignBatch() {
		fail("Not yet implemented");
	}

	@Test
	public void testGiveCreditForBatch() {
		fail("Not yet implemented");
	}

}
