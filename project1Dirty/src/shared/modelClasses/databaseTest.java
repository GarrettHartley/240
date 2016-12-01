package shared.modelClasses;

import java.rmi.ServerException;

import server.database.Database;
import server.database.DatabaseException;
//import server.database.DAO.ProjectDAO;

public class databaseTest {

	public static void main(String[] args) {
		
		Database db = new Database();

		
	//	ProjectDAO pDAO = new ProjectDAO(db);
//		int S = 741;
//		int X = 741;
//		int F = 62;
//		int R = 417;
	//	Project project = new Project(S, "thisIStitle",X,F, R);
			
		try {
			db.startTransaction();
			db.getDataImporter().init();
			db.endTransaction(true);
		}
		catch (DatabaseException e) {
			db.endTransaction(false);
			try {
				throw new ServerException(e.getMessage(), e);
			} catch (ServerException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

}
