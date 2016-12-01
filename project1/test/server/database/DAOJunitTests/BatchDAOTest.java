package server.database.DAOJunitTests;

import static org.junit.Assert.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.database.Database;
import server.database.DAO.BatchDAO;
import shared.communicationClasses.GetSampleBatchParam;
import shared.communicationClasses.GetSampleBatchResult;
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
		PreparedStatement stmt = null;
		String deleteBatches = "drop table if exists batches;";
		String createBatches = "CREATE TABLE batches ("+
				"primaryID INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ,"+
				"foreignProjectKey INTEGER NOT NULL ,"+ 
				"foreignUserKey INTEGER NOT NULL ,"+ 
				"file VARCHAR NOT NULL);";
		stmt = db.getConnection().prepareStatement(deleteBatches);
		stmt.execute();
		stmt = db.getConnection().prepareStatement(createBatches);
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
		batchDAO = null;
	}
	
	private Database db;
	private BatchDAO batchDAO;

	@Test
	public void testInsertBatch() {
		Batch param = new Batch();
		batchDAO = new BatchDAO(db);
		
		param.setForeignProjectKey(1);
		param.setForeignUserKey(1);
		param.setImgURL("imgURL");

		batchDAO.insert(param);
// checks to see if the batch was inserted correctly		
		String result = new String(batchDAO.getImgURLByID(1));
		assertEquals("imgURL".equals(result), true);
	}

	@Test
	public void testGetSampleBatch() {
		Batch param = new Batch();
		batchDAO = new BatchDAO(db);
		GetSampleBatchParam getSampleBatchParam = new GetSampleBatchParam();
		GetSampleBatchResult getSampleBatchResult = new GetSampleBatchResult();
		
		getSampleBatchParam.setProjectID(1);
		param.setForeignProjectKey(1);
		param.setForeignUserKey(2);
		param.setImgURL("imgURL");

		batchDAO.insert(param);
		getSampleBatchResult=batchDAO.getSampleBatch(getSampleBatchParam);
		
		assertEquals(getSampleBatchResult.isValid()==true,true);
		
	}

}
