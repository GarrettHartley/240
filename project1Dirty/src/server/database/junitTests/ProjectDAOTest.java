package server.database.junitTests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.database.Database;
import server.database.DAO.ProjectDAO;
import shared.modelClasses.Project;

public class ProjectDAOTest {

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
		db.endTransaction(true);
		db = null;
		projectDAO = null;
	}
	
	private Database db;
	private ProjectDAO projectDAO;

	@Test
	public void testGetProjects() {
		fail("Not yet implemented");
	}

	
//Test insert and get ProjectByID
	@Test	
	public void testInsert() {
		projectDAO = new ProjectDAO(db);
		Project project = new Project();
		Project getProjectFromTable = new Project();
		
		project.setTitle("testing");
		project.setRecordsPerImage(8);
		project.setFirstYCoord(199);
		project.setRecordHeight(60);
		project.setNumberOfFields(4);
	
		projectDAO.insert(project);
		getProjectFromTable = projectDAO.getProjectByID(4);
		assertEquals(getProjectFromTable.getRecordsPerImage()==8,true);
		assertEquals(getProjectFromTable.getFirstYCoord()==199,true);
		assertEquals(getProjectFromTable.getRecordHeight()==60,true);
		assertEquals(getProjectFromTable.getNumberOfFields()==4,true);

	}

}
