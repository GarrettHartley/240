package client;

import static org.junit.Assert.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.database.Database;
import server.database.DAO.BatchDAO;

public class formEntryTest {
	
	private Database db;
	private BatchDAO batchDAO;
	
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

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
