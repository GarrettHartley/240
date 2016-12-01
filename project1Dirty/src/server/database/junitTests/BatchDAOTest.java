package server.database.junitTests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.database.Database;
import server.database.DAO.BatchDAO;
import shared.modelClasses.Batch;

public class BatchDAOTest {



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
			batch = null;
		}
		
		private Database db;
		private BatchDAO batch;
		

		@Test
		public void testGet_single_batch(){

			batch = new BatchDAO(db);
			Batch result = new Batch();
		//	result = batch.Get_single_batch(1);

		//	Batch correctResult = new Batch(1,1,"images/1890_image0.png");
			assertEquals((result.getForeignProjectKey()==1),true);
			assertEquals((result.getPrimaryID()==1),true);
			assertEquals(result.getImgURL().equals("images/1890_image0.png"),true);

		}

	@Test
	public void testInsert() {
		fail("Not yet implemented");
	}

	@Test
	public void testSubmitBatch() {
		fail("Not yet implemented");
	}

	@Test
	public void testGet_ImgURL_by_ID() {
		fail("Not yet implemented");
	}

	@Test
	public void testGet_projectID_by_batchID() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSampleBatch() {
		fail("Not yet implemented");
	}

}
