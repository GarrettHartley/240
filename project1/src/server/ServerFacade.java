package server;

import java.util.ArrayList;

import server.database.Database;
import server.database.DatabaseException;
import server.database.DAO.BatchDAO;
import server.database.DAO.FieldDAO;
import server.database.DAO.ProjectDAO;
import server.database.DAO.UserDAO;
import server.database.DAO.ValueDAO;
import server.handlers.ServerException;
import shared.communicationClasses.DownloadBatchParam;
import shared.communicationClasses.DownloadBatchResult;
import shared.communicationClasses.GetFieldsParam;
import shared.communicationClasses.GetFieldsResult;
import shared.communicationClasses.GetProjectsParam;
import shared.communicationClasses.GetProjectsResult;
import shared.communicationClasses.GetSampleBatchParam;
import shared.communicationClasses.GetSampleBatchResult;
import shared.communicationClasses.SearchParam;
import shared.communicationClasses.SearchResult;
import shared.communicationClasses.SubmitBatchParam;
import shared.communicationClasses.SubmitBatchResult;
import shared.communicationClasses.ValidateUserParam;
import shared.communicationClasses.ValidateUserResult;
import shared.modelClasses.Batch;
import shared.modelClasses.Project;
import shared.modelClasses.Value;

public class ServerFacade {

	public ServerFacade(){

	}

	public static void initialize() throws ServerException{
		try {
			Database.initialize();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}

// Makes User DAO function call to determine if the user exists	
	public static ValidateUserResult validateUser( ValidateUserParam param) throws ServerException{

		Database db = new Database();
		UserDAO uDAO = new UserDAO(db);
		ValidateUserResult result = new ValidateUserResult();

		try {
			db.startTransaction();
			result = uDAO.validateUser(param);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}

		db.endTransaction(true);
		return result;
	}

// Makes Project DAO function call and returns the relevant projects from the database
	public static GetProjectsResult getProjects(GetProjectsParam param) throws ServerException{

		Database db = new Database();
		ProjectDAO pDAO = new ProjectDAO(db);
		UserDAO uDAO = new UserDAO(db);
		ValidateUserParam uparam = new ValidateUserParam(param.getUser(),param.getPassword());
		ValidateUserResult resultU = new ValidateUserResult();

		GetProjectsResult result = new GetProjectsResult();
		try {
			db.startTransaction();
			resultU = uDAO.validateUser(uparam);

			if(resultU.getValid() == true){
				result = pDAO.getProjects(param);
				result.setValid(true);
			}

		} catch (DatabaseException e) {
			e.printStackTrace();
		}

		db.endTransaction(true);

		return result;
	}
// Makes Batch DAO function call and returns a sample Batch feom the Database
// corresponding with with project ID
	public static GetSampleBatchResult getSampleImage(GetSampleBatchParam param) throws ServerException{
		GetSampleBatchResult result = new GetSampleBatchResult();
		Database db = new Database();
		BatchDAO bDAO = new BatchDAO(db);
		UserDAO uDAO = new UserDAO(db);
		ProjectDAO pDAO = new ProjectDAO(db);
		ValidateUserResult resultU = new ValidateUserResult();
		ValidateUserParam uparam = new ValidateUserParam(param.getUser(),param.getPassword());

		try {
			db.startTransaction();
			resultU = uDAO.validateUser(uparam);
			if(resultU.getValid() == true){
				if(pDAO.getProjectByID(param.getProjectID())!=null){
					result = bDAO.getSampleBatch(param);
					result.setValid(true);
				}
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
		}

		db.endTransaction(true);
		result.setUrlPrefix(param.getUrlPrefix());
		return result;
	}

// Makes DAO function calls to return an active URL to desired batch
	public static DownloadBatchResult downloadBatch(DownloadBatchParam param) throws ServerException{
		
		System.out.println("got into downloadbatch");
		DownloadBatchResult result = new DownloadBatchResult();
		int uID = -1;
		Database db = new Database();
		BatchDAO bDAO = new BatchDAO(db);
		UserDAO uDAO = new UserDAO(db);
		ProjectDAO pDAO = new ProjectDAO(db);
		FieldDAO fDAO = new FieldDAO(db);
		ValidateUserResult resultU = new ValidateUserResult();
		ValidateUserParam uparam = new ValidateUserParam(param.getUser(),param.getPassword());

		try {
			db.startTransaction();
			resultU = uDAO.validateUser(uparam);
			uID = uDAO.getUserID(uparam);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}

		Batch batchResult = new Batch();
		Project project_result = new Project();
		if(resultU.getValid() == true){
			if(uDAO.hasBatch(param.getUser(),param.getPassword())=="false"){
				if(pDAO.getProjectByID(param.getProjectID())!=null){
					batchResult = bDAO.getSingleBatch(param.getProjectID(), uID);
					result.setBatchID(batchResult.getPrimaryID());
					result.setProjectID(batchResult.getForeignProjectKey());
					System.out.println("!!!!!!!!!!!!!!!!!!!!!!"+batchResult.getImgURL());
					result.setImgURL(batchResult.getImgURL());
					project_result = pDAO.getProjectByID(param.getProjectID());
					result.setFirstYCoord(project_result.getFirstYCoord());
					result.setRecordHeight(project_result.getRecordHeight());
					result.setNumberOfRecords(project_result.getRecordsPerImage());
					result.setNumberOfFields(project_result.getNumberOfFields());
					result.setValid(true);
					if(batchResult.getImgURL().equals("")){
						result.setValid(false);
					}

				}
				try {
					result.setFields(fDAO.getFields(param.getProjectID()));
				} catch (DatabaseException e) {
					e.printStackTrace();
				}
			}

		}

		db.endTransaction(true);
		result.setUrlPrefix(param.getUrlPrefix());
		return result;
	}

// Makes DAO function calls to return the fields for a given project
	public static GetFieldsResult getFields(GetFieldsParam param) throws ServerException{
		GetFieldsResult result = new GetFieldsResult();
		ValidateUserResult resultU = new ValidateUserResult();
		ValidateUserParam uparam = new ValidateUserParam(param.getUser(),param.getPassword());
		Database db = new Database();
		FieldDAO fDAO = new FieldDAO(db);
		UserDAO uDAO = new UserDAO(db);

		try {
			db.startTransaction();
			resultU = uDAO.validateUser(uparam);
			if(resultU.getValid()==true){
				result.setFields(fDAO.getFields(param.getProjectID()));
				if(result.getFields().size()>0){
					result.setValid(true);
				}
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
		}

		db.endTransaction(true);
		return result;

	}

// Makes DAO Function calls to find the desired values and their fields
	public static SearchResult Search(SearchParam param) throws ServerException{
		SearchResult result = new SearchResult();
		if(param.getSearchValues().size()>0){
			ValidateUserResult resultU = new ValidateUserResult();
			ValidateUserParam uparam = new ValidateUserParam(param.getUser(),param.getPassword());
			Database db = new Database();
			UserDAO uDAO = new UserDAO(db);
			ValueDAO vDAO = new ValueDAO(db);
			BatchDAO bDAO = new BatchDAO(db);
			FieldDAO fDAO = new FieldDAO(db);

			try {
				db.startTransaction();
				resultU = uDAO.validateUser(uparam);
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
			ArrayList<Value> found = new ArrayList<Value>();
			if(resultU.getValid()==true){
				for(int i :param.getFieldsToSearch()){
					try {
						if(i>0 && i <= fDAO.getNumberOfFields()){
							result.setValid(true);
							for(String str: param.getSearchValues()){
								found = vDAO.findValue(i,str);
								for(Value v: found){
									if(v.getRow()>0){
										SearchResult foundResult = new SearchResult();
										foundResult.setRecordNum(v.getRow());
										foundResult.setBatchID(v.getForeignImageKey());
										foundResult.setImgURL(bDAO.getImgURLByID(v.getForeignImageKey()));
										foundResult.setFieldID(i);
										result.getResults().add(foundResult);
									}
								}
							}
						}
					} catch (DatabaseException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return result;
	}

// Makes DAO function calls to submit a batch and update the user's account
	public static SubmitBatchResult submitBatch(SubmitBatchParam param) throws ServerException{
		SubmitBatchResult result = new SubmitBatchResult();
		ValidateUserResult resultU = new ValidateUserResult();
		ValidateUserParam uparam = new ValidateUserParam(param.getUser(),param.getPassword());
		int uID = -1;
		Database db = new Database();
		BatchDAO bDAO = new BatchDAO(db);
		UserDAO uDAO = new UserDAO(db);
		ValueDAO vDAO = new ValueDAO(db);
		FieldDAO fDAO = new FieldDAO(db);
		ProjectDAO pDAO = new ProjectDAO(db);
		try {
			db.startTransaction();
			resultU = uDAO.validateUser(uparam);
			uID = uDAO.getUserID(uparam);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		if(resultU.getValid()==true){	
			int pID = bDAO.getProjectIDByBatchID(param.getBatchID(),uID);	
			if(pID!=-1){
				Project project = pDAO.getProjectByID(pID);
				int numberOfValues=(project.getNumberOfFields()*project.getRecordsPerImage());	
				if(checkNumberOfValues(param,numberOfValues)==true){
					int firstFieldId = fDAO.getFirstFieldID(pID);
					bDAO.SubmitBatch(param,vDAO,firstFieldId);		
					uDAO.giveCreditForBatch(pID, param.getUser(), param.getPassword(), project.getRecordsPerImage());
					uDAO.unassignBatch(param.getUser(), param.getPassword());
					result.setValid(true);
				}
			}
		}
		db.endTransaction(true);
		return result;
	}
	
// Verifies that the user submited the correct number of values for the Submit Batch function
	public static boolean checkNumberOfValues(SubmitBatchParam param, int numberOfValues){
		String allValues = param.getFieldValues();
		String[] valueGroups = allValues.split(";");
		int countValues = 0;
		for(int i=0;i<valueGroups.length;i++){
			String[] values = valueGroups[i].split(",",-1);
			for(int j=0;j< values.length;j++){
				countValues++;
			}
		}
		if(countValues == numberOfValues){
			return true;
		}
		return false;
	}

}
