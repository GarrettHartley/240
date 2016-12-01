package dataImporter;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import server.database.Database;
import server.database.DatabaseException;
import server.database.DAO.BatchDAO;
import server.database.DAO.FieldDAO;
import server.database.DAO.ProjectDAO;
import server.database.DAO.UserDAO;
import server.database.DAO.ValueDAO;
import shared.modelClasses.Batch;
import shared.modelClasses.Field;
import shared.modelClasses.Project;
import shared.modelClasses.User;
import shared.modelClasses.Value;

/**
 * @author hartley9
 *
 */
public class DataImporter {

	private static Database db;
	
	
// 	Constructs Data Importer and establishes database with
//  the common database among all connections in the project
	public DataImporter(Database db) {
		this.db = db;
	}

	
// Replaces existing tables in the database with new tables
	public void init(){
		PreparedStatement stmt = null;
		String deleteProjects = "drop table if exists projects;";
		String deleteUsers = "drop table if exists users;";
		String deleteFields = "drop table if exists fields;";
		String deleteBatches = "drop table if exists batches;";
		String deleteValues = "drop table if exists valuesInfo;";

		String createProjects = "CREATE TABLE projects ("+
				"primaryID INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL UNIQUE ,"+ 
				"title VARCHAR NOT NULL ,"+
				"recordsperimage INTEGER NOT NULL  DEFAULT 1,"+
				"firstycoord INTEGER NOT NULL  DEFAULT 0,"+
				"recordheight INTEGER NOT NULL  DEFAULT 1,"+
				"numberfields INTEGER NOT NULL  DEFAULT 0)";

		String createUsers = "CREATE TABLE users ("+
				"primaryID INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE  DEFAULT 1,"+
				"username VARCHAR NOT NULL  UNIQUE ,"+ 
				"password VARCHAR NOT NULL ,"+ 
				"firstname CHAR NOT NULL ,"+
				"lastname CHAR NOT NULL ,"+
				"numrecords INTEGER NOT NULL  DEFAULT 0,"+
				"hasbatch BOOLEAN DEFAULT false,"+
				"email VARCHAR NOT NULL  UNIQUE );";

		String createFields = "CREATE TABLE fields ("+
				"primaryID INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ,"+
				"foreignProjectKey INTEGER NOT NULL ,"+
				"title VARCHAR NOT NULL ,"+ 
				"xcoord INTEGER NOT NULL  DEFAULT 0,"+ 
				"width INTEGER NOT NULL  DEFAULT 1,"+ 
				"helphtml VARCHAR NOT NULL ,"+
				"knowndata VARCHAR,"+
				"fieldNumber INTEGER NOT NULL);";

		String createBatches = "CREATE TABLE batches ("+
				"primaryID INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ,"+
				"foreignProjectKey INTEGER NOT NULL ,"+ 
				"foreignUserKey INTEGER NOT NULL ,"+ 
				"file VARCHAR NOT NULL);";

		String createValues ="CREATE TABLE valuesInfo ("+ 
				"foreignImageKey INTEGER NOT NULL ,"+ 
				"foreignFieldKey INTEGER NOT NULL ,"+
				"col INTEGER NOT NULL ,"+ 
				"row INTEGER NOT NULL ,"+ 
				"value VARCHAR NOT NULL );";



		try {
			stmt = db.getConnection().prepareStatement(deleteProjects);
			stmt.execute();
			stmt = db.getConnection().prepareStatement(createProjects);
			stmt.execute();
			stmt = db.getConnection().prepareStatement(deleteUsers);
			stmt.execute();
			stmt = db.getConnection().prepareStatement(createUsers);
			stmt.execute();
			stmt = db.getConnection().prepareStatement(deleteFields);
			stmt.execute();
			stmt = db.getConnection().prepareStatement(createFields);
			stmt.execute();
			stmt = db.getConnection().prepareStatement(deleteBatches);
			stmt.execute();
			stmt = db.getConnection().prepareStatement(createBatches);
			stmt.execute();
			stmt = db.getConnection().prepareStatement(deleteValues);
			stmt.execute();
			stmt = db.getConnection().prepareStatement(createValues);
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}


	public static String getValue(Element e){
		String result = "";
		Node child = e.getFirstChild();
		result = child.getNodeValue();
		return result;
	}

	/**
	 * creates an instance of indexerData, which is the root of the data model represented in Records.xml
	 */
	public static void main(String[] args) {
		Document document = null;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbBuilder = null;
		
// Copy directory containing Records.xml to my database directory
		File xmlInput = new File(args[0]);
		File xmlDestination = new File("database");
		try {
			FileUtils.copyDirectory(xmlInput.getParentFile(), xmlDestination);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

// Read in Records.xml in oder to populate database
		try {
			dbBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		if(dbBuilder!=null){
			try {
				document = dbBuilder.parse(xmlInput);
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

// Read in the tree structure of the xml Document and save the data in Model Classes
		document.getDocumentElement().normalize();
		Element root = document.getDocumentElement();

		IndexerData indexerData = new IndexerData(root);

		ArrayList<Project> projects = indexerData.getProjects();
		ArrayList<User> users = indexerData.getUsers();


		Database db = new Database();

		try {
			db.startTransaction();
			db.getDataImporter().init();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}

		UserDAO uDAO = new UserDAO(db);
		for(User u:users){
			uDAO.insert(u);
		}
		ProjectDAO pDAO = new ProjectDAO(db);
		BatchDAO bDAO = new BatchDAO(db);
		FieldDAO fDAO = new FieldDAO(db);
		ValueDAO vDAO = new ValueDAO(db);
		ArrayList<Field> fields = new ArrayList<Field>();
		ArrayList<Batch> batches = new ArrayList<Batch>();
		ArrayList<Value> values = new ArrayList<Value>();
		int col= 0;
		int row= 1;
		int fieldNumber=1;
		for(Project p:projects){
			pDAO.insert(p);
			fields= p.getFields();
			batches = p.getBatches();

			for(Field f:fields){
				f.setForeignProjectKey(p.getPrimaryID());
				f.setFieldNumber(fieldNumber);
				fDAO.insert(f);
				fieldNumber++;
				if(fieldNumber == fields.size()+1){
					fieldNumber = 1;
				}
			}

			for(Batch b: batches){
				b.setForeignProjectKey(p.getPrimaryID());
				bDAO.insert(b);
				values = b.getValues();
				row = 1;
				for(Value v: values){
					bDAO.setComplete(b.getPrimaryID());
					v.setForeignImageKey(b.getPrimaryID());
					v.setForeignFieldKey(fields.get(col).getPrimaryID());
					v.setCol(col+1);
					v.setRow(row);
					vDAO.insert(v);
					col++;
					if(col == fields.size()){
						col=0;
						row++;
					}
				}	
			}
		}
		
		db.endTransaction(true);

	}

}
