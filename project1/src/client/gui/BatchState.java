package client.gui;

import java.io.Serializable;
import java.util.ArrayList;

import client.gui.indexerWindow.tableEntry.TableEntryCell;
import shared.communicationClasses.DownloadBatchResult;
import shared.modelClasses.Field;

public class BatchState implements Serializable {

	int batchID;
	int projectID;
	String fullImgURL;
	int firstYCoord;
	int recordHeight;
	int cellWidth;
	int numberOfRecords;
	int numberOfFields;
	ArrayList<Field> fields = null;
	boolean valid;
	boolean highlighted = true;
	boolean formEntrySelected =false;
	boolean dataSaved = false;
	boolean imgInverted = false;

	int horizontalDividePos;
	int verticalDividePos;
	int imageXLoc = 0;
	int imageYLoc= 0;
	double zoom = 1;
	int windowXLoc;
	int windowYLoc;
	int windowWidth;
	int windowHeight;

	String urlPrefix;
	String user;
	String password;
	boolean hasBatch = false;
	ArrayList<ArrayList<TableEntryCell>> data;
	TableEntryCell selected =  new TableEntryCell("",0,0);
	
	public TableEntryCell getSelected() {
		return selected;
	}

	public void setSelected(TableEntryCell selected) {
		this.selected = selected;
	}
	
	transient ArrayList<BatchStateListener> downloadBatchListeners = new ArrayList<BatchStateListener>();
	public ArrayList<BatchStateListener> getDownloadBatchListeners() {
		return downloadBatchListeners;
	}

	transient ArrayList<BatchStateListener> selectFieldListeners = new ArrayList<BatchStateListener>();
	transient ArrayList<BatchStateListener> updateDataListeners = new ArrayList<BatchStateListener>();
	transient ArrayList<BatchStateListener> updateSelectedListeners = new ArrayList<BatchStateListener>();
	transient ArrayList<BatchStateListener> logoutListeners = new ArrayList<BatchStateListener>(); 
	transient ArrayList<BatchStateListener> updateImageListeners = new ArrayList<BatchStateListener>();
	
	public ArrayList<ArrayList<TableEntryCell>> getData() {
		return data;
	}

	public void setData() {
		this.data = generateData();
		updateData();
	}

	public BatchState(DownloadBatchResult currentInfo){
		projectID = currentInfo.getProjectID();
		this.user = user;
		this.password = password;
		this.firstYCoord = currentInfo.getFirstYCoord();
		this.recordHeight = currentInfo.getRecordHeight();
		this.numberOfFields = currentInfo.getNumberOfFields();
		this.numberOfRecords = currentInfo.getNumberOfRecords();
		this.fields = currentInfo.getFields();
		this.cellWidth = fields.get(0).getWidth();
		this.fullImgURL = currentInfo.getUrlPrefix()+"/"+currentInfo.getImgURL();
		this.urlPrefix = currentInfo.getUrlPrefix()+"/";
		this.data = generateData();
	}
	
	public BatchState(){

	}
	
	private ArrayList<ArrayList<TableEntryCell>> generateData() {
		int numOfRows = numberOfRecords;
		int numOfColumns = numberOfFields;
		ArrayList<ArrayList<TableEntryCell>> result = new ArrayList<ArrayList<TableEntryCell>>();
		int j = 0;
		for (int i = 0; i <= numOfRows; ++i) {
			result.add(new ArrayList<TableEntryCell>());
			for(j =0; j<= numOfColumns;++j){
				TableEntryCell record = new TableEntryCell("",i,j);
				if(j==0){
					record.setValue(Integer.toString(i+1));
				}
				result.get(i).add(record);
			}	
		}
		return result;
	}

	public void updateSelected(TableEntryCell selected){
		this.selected = selected;
		for(BatchStateListener updateSelectedListener: updateSelectedListeners){
			if(selected.getColumn()!=0){
			updateSelectedListener.updateSelected(selected);
			}
		}
	}
	
	public void logout(String function){
		for(BatchStateListener logoutListener: logoutListeners){
			logoutListener.performLogout(function);
		}
		
	}
	
	public void addLogoutListener(BatchStateListener logoutListener){
		logoutListeners.add(logoutListener);
	}
	
	public void updateImage(String function){
		for(BatchStateListener updateImageListener: updateImageListeners){
			updateImageListener.updateImage(function);
		}	
	}
	
	public void addUpdateImageListener(BatchStateListener invertImageListener){
		updateImageListeners.add(invertImageListener);
	}
	
	public void addUpdateSelectedListener(BatchStateListener updateSelectedListener){
		updateSelectedListeners.add(updateSelectedListener);
	}

	public void updateData() {
		for(BatchStateListener updateDataListener : updateDataListeners){
			updateDataListener.updateData();
		}
	}
	
	public void addUpdateDataListener(BatchStateListener updateDataListener ){
		updateDataListeners.add(updateDataListener);
	}
	
	public void addDownloadBatchListener(BatchStateListener downloadBatchListener){
		downloadBatchListeners.add(downloadBatchListener);
	}
	
	
	public void downloadBatch(){
		for(BatchStateListener batchStateListener : downloadBatchListeners){
			batchStateListener.downloadBatch();
		}
	}
	
	public void selectField(){
		for(BatchStateListener batchStateListener : selectFieldListeners){
			batchStateListener.selectField();
		}
	}
	
	public int getBatchID() {
		return batchID;
	}

	public void setBatchID(int batchID) {
		this.batchID = batchID;
	}

	public int getProjectID() {
		return projectID;
	}

	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

	public String getFullImgURL() {
		return fullImgURL;
	}

	public void setFullImgURL(String fullImgURL) {
		this.fullImgURL = fullImgURL;
	}

	public int getFirstYCoord() {
		return firstYCoord;
	}

	public void setFirstYCoord(int firstYCoord) {
		this.firstYCoord = firstYCoord;
	}

	public int getRecordHeight() {
		return recordHeight;
	}

	public void setRecordHeight(int recordHeight) {
		this.recordHeight = recordHeight;
	}

	public int getNumberOfRecords() {
		return numberOfRecords;
	}

	public void setNumberOfRecords(int numberOfRecords) {
		this.numberOfRecords = numberOfRecords;
	}

	public int getNumberOfFields() {
		return numberOfFields;
	}

	public boolean isDataSaved() {
		return dataSaved;
	}

	public void setDataSaved(boolean dataSaved) {
		this.dataSaved = dataSaved;
	}

	public void setNumberOfFields(int numberOfFields) {
		this.numberOfFields = numberOfFields;
	}

	public ArrayList<Field> getFields() {
		return fields;
	}

	public void setFields(ArrayList<Field> fields) {
		this.fields = fields;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getUrlPrefix() {
		return urlPrefix;
	}

	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isHasBatch() {
		return hasBatch;
	}

	public void setHasBatch(boolean hasBatch) {
		this.hasBatch = hasBatch;
	}
	
	public int getProject() {
		return projectID;
	}
	public void setProject(int project) {
		this.projectID = project;
	}
	public int getCellWidth(){
		return cellWidth;
	}
	public void setCellWidth(int cellWidth){
		this.cellWidth = cellWidth;
	}
	public boolean isHighlighted() {
		return highlighted;
	}

	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}
	public boolean isFormEntrySelected() {
		return formEntrySelected;
	}

	public void setFormEntrySelected(boolean formEntrySelected) {
		this.formEntrySelected = formEntrySelected;
	}

	public void setDownloadBatchListeners(
			ArrayList<BatchStateListener> downloadBatchListeners) {
		this.downloadBatchListeners = downloadBatchListeners;
	}

	public ArrayList<BatchStateListener> getSelectFieldListeners() {
		return selectFieldListeners;
	}

	public void setSelectFieldListeners(
			ArrayList<BatchStateListener> selectFieldListeners) {
		this.selectFieldListeners = selectFieldListeners;
	}

	public ArrayList<BatchStateListener> getUpdateDataListeners() {
		return updateDataListeners;
	}

	public void setUpdateDataListeners(
			ArrayList<BatchStateListener> updateDataListeners) {
		this.updateDataListeners = updateDataListeners;
	}

	public ArrayList<BatchStateListener> getUpdateSelectedListeners() {
		return updateSelectedListeners;
	}

	public void setUpdateSelectedListeners(
			ArrayList<BatchStateListener> updateSelectedListeners) {
		this.updateSelectedListeners = updateSelectedListeners;
	}

	public ArrayList<BatchStateListener> getLogoutListeners() {
		return logoutListeners;
	}

	public void setLogoutListeners(ArrayList<BatchStateListener> logoutListeners) {
		this.logoutListeners = logoutListeners;
	}

	public ArrayList<BatchStateListener> getUpdateImageListeners() {
		return updateImageListeners;
	}

	public void setUpdateImageListeners(
			ArrayList<BatchStateListener> updateImageListeners) {
		this.updateImageListeners = updateImageListeners;
	}

	public int getHorizontalDividePos() {
		return horizontalDividePos;
	}

	public void setHorizontalDividePos(int horizontalDividePos) {
		this.horizontalDividePos = horizontalDividePos;
	}

	public int getVerticalDividePos() {
		return verticalDividePos;
	}

	public void setVerticalDividePos(int verticalDividePos) {
		this.verticalDividePos = verticalDividePos;
	}

	public int getWindowXLoc() {
		return windowXLoc;
	}

	public void setWindowXLoc(int windowXLoc) {
		this.windowXLoc = windowXLoc;
	}

	public int getWindowYLoc() {
		return windowYLoc;
	}

	public void setWindowYLoc(int windowYLoc) {
		this.windowYLoc = windowYLoc;
	}

	public int getWindowWidth() {
		return windowWidth;
	}

	public void setWindowWidth(int windowWidth) {
		this.windowWidth = windowWidth;
	}

	public int getWindowHeight() {
		return windowHeight;
	}

	public void setWindowHeight(int windowHeight) {
		this.windowHeight = windowHeight;
	}

	public double getZoom() {
		return zoom;
	}

	public void setZoom(double zoom) {
		this.zoom = zoom;
	}

	public boolean isImgInverted() {
		return imgInverted;
	}

	public void setImgInverted(boolean imgInverted) {
		this.imgInverted = imgInverted;
	}
	
	public int getImageXLoc() {
		return imageXLoc;
	}

	public void setImageXLoc(int imageXLoc) {
		this.imageXLoc = imageXLoc;
	}

	public int getImageYLoc() {
		return imageYLoc;
	}

	public void setImageYLoc(int imageYLoc) {
		this.imageYLoc = imageYLoc;
	}
	
}