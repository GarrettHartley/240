package server.database.DAOJunitTests;

import static org.junit.Assert.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.database.Database;
import server.database.DAO.ValueDAO;
import shared.modelClasses.Value;

public class ValueDAOTest {


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Database.initialize();
	}

	@Before
	public void setUp() throws Exception {
		db = new Database();
		db.startTransaction();
		PreparedStatement stmt = null;

		String deleteValues = "drop table if exists valuesInfo;";
		String createValues ="CREATE TABLE valuesInfo ("+ 
				"foreignImageKey INTEGER NOT NULL ,"+ 
				"foreignFieldKey INTEGER NOT NULL ,"+
				"col INTEGER NOT NULL ,"+ 
				"row INTEGER NOT NULL ,"+ 
				"value VARCHAR NOT NULL );";
		
		stmt = db.getConnection().prepareStatement(deleteValues);
		stmt.execute();
		stmt = db.getConnection().prepareStatement(createValues);
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
		valueDAO = null;
	}
	
	private Database db;
	private ValueDAO valueDAO;
	@Test
	public void testInsert() {
		valueDAO = new ValueDAO(db);
		Value value = new Value();
		value.setForeignImageKey(1);
		value.setForeignFieldKey(1);
		value.setCol(1);
		value.setRow(1);
		value.setValue("findme");
		
		valueDAO.insert(value);
		
		ArrayList<Value> result = new ArrayList<Value>();
		
		result = valueDAO.findValue(1, "findme");
		
		System.out.println(result.get(0).getValue());
		assertEquals(result.get(0).getValue().equals("findme"),true);
		
		
	}


}
