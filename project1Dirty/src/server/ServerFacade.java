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
import shared.communicationClasses.Download_batch_param;
import shared.communicationClasses.Download_batch_result;
import shared.communicationClasses.Get_fields_param;
import shared.communicationClasses.Get_fields_result;
import shared.communicationClasses.Get_projects_param;
import shared.communicationClasses.Get_projects_result;
import shared.communicationClasses.Get_sampleImage_param;
import shared.communicationClasses.Get_sampleImage_result;
import shared.communicationClasses.Search_param;
import shared.communicationClasses.Search_result;
import shared.communicationClasses.Submit_batch_param;
import shared.communicationClasses.Submit_batch_result;
import shared.communicationClasses.Validate_user_param;
import shared.communicationClasses.Validate_user_result;
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

	public static Validate_user_result validateUser( Validate_user_param param) throws ServerException{

		Database db = new Database();
		UserDAO uDAO = new UserDAO(db);
		Validate_user_result result = new Validate_user_result();

		try {
			db.startTransaction();
			result = uDAO.validateUser(param);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}

		db.endTransaction(true);
		return result;
	}

	public static Get_projects_result getProjects(Get_projects_param param) throws ServerException{

		Database db = new Database();
		ProjectDAO pDAO = new ProjectDAO(db);
		UserDAO uDAO = new UserDAO(db);
		Validate_user_param uparam = new Validate_user_param(param.getUser(),param.getPassword());
		Validate_user_result resultU = new Validate_user_result();

		Get_projects_result result = new Get_projects_result();
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

	public static Get_sampleImage_result getSampleImage(Get_sampleImage_param param) throws ServerException{
		Get_sampleImage_result result = new Get_sampleImage_result();
		Database db = new Database();
		BatchDAO bDAO = new BatchDAO(db);
		UserDAO uDAO = new UserDAO(db);
		ProjectDAO pDAO = new ProjectDAO(db);
		Validate_user_result resultU = new Validate_user_result();
		Validate_user_param uparam = new Validate_user_param(param.getUser(),param.getPassword());

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

	public static Download_batch_result downloadBatch(Download_batch_param param) throws ServerException{
		Download_batch_result result = new Download_batch_result();
		int uID = -1;
		Database db = new Database();
		BatchDAO bDAO = new BatchDAO(db);
		UserDAO uDAO = new UserDAO(db);
		ProjectDAO pDAO = new ProjectDAO(db);
		FieldDAO fDAO = new FieldDAO(db);
		Validate_user_result resultU = new Validate_user_result();
		Validate_user_param uparam = new Validate_user_param(param.getUser(),param.getPassword());

		try {
			db.startTransaction();
			resultU = uDAO.validateUser(uparam);
			uID = uDAO.get_userID(uparam);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}

		Batch batch_result = new Batch();
		Project project_result = new Project();
		if(resultU.getValid() == true){
			if(uDAO.hasBatch(param.getUser(),param.getPassword())=="false"){
				if(pDAO.getProjectByID(param.getProjectID())!=null){
					batch_result = bDAO.Get_single_batch(param.getProjectID(), uID);
					result.setBatchID(batch_result.getPrimaryID());
					result.setProjectID(batch_result.getForeignProjectKey());
					result.setImgURL(batch_result.getImgURL());
					project_result = pDAO.getProjectByID(param.getProjectID());
					result.setFirstYCoord(project_result.getFirstYCoord());
					result.setRecordHeight(project_result.getRecordHeight());
					result.setNumberOfRecords(project_result.getRecordsPerImage());
					result.setNumberOfFields(project_result.getNumberOfFields());
					result.setValid(true);
					if(batch_result.getImgURL().equals("")){
						result.setValid(false);
					}
//					System.out.println("This is the batch id"+result.getBatchID());
//					System.out.println("");
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

	public static Get_fields_result getFields(Get_fields_param param) throws ServerException{
		Get_fields_result result = new Get_fields_result();
		Validate_user_result resultU = new Validate_user_result();
		Validate_user_param uparam = new Validate_user_param(param.getUser(),param.getPassword());

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

	public static Search_result Search(Search_param param) throws ServerException{
		Search_result result = new Search_result();
		if(param.getSearchValues().size()>0){
			Validate_user_result resultU = new Validate_user_result();
			Validate_user_param uparam = new Validate_user_param(param.getUser(),param.getPassword());
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
										Search_result foundResult = new Search_result();
										foundResult.setRecordNum(v.getRow());
										foundResult.setBatchID(v.getForeignImageKey());
										foundResult.setImgURL(bDAO.get_ImgURL_by_ID(v.getForeignImageKey()));
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
		result.setUrlPrefix(param.getUrlPrefix());
		return result;
	}

	public static Submit_batch_result submitBatch(Submit_batch_param param) throws ServerException{
		Submit_batch_result result = new Submit_batch_result();
		Validate_user_result resultU = new Validate_user_result();
		Validate_user_param uparam = new Validate_user_param(param.getUser(),param.getPassword());
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
			uID = uDAO.get_userID(uparam);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		if(resultU.getValid()==true){	
			// get the project id pass
			int pID = bDAO.get_projectID_by_batchID(param.getBatchID(),uID);	
			if(pID!=-1){
				Project project = pDAO.getProjectByID(pID);
				int numberOfValues=(project.getNumberOfFields()*project.getRecordsPerImage());	
				if(checkNumberOfValues(param,numberOfValues)==true){
					//get number of records for this batch
					int firstFieldId = fDAO.get_first_fieldID(pID);
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

	public static boolean checkNumberOfValues(Submit_batch_param param, int numberOfValues){
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
