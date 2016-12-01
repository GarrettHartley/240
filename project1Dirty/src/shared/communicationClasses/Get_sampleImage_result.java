package shared.communicationClasses;
/**
this object is used to pass parameters of the server<br>
****************************************************<br>
				Database Key Help<br>
User: primaryID<br>
Project: primaryID<br>
Batch: primaryID, foreignProjectKey, foreignUserKey<br>
Field: primaryKey, foreignProjectKey<br>
Value: foreignBatchKey, foreignFieldKey<br>
*/
public class Get_sampleImage_result {

	String imgURL;
	boolean valid;
	String urlPrefix;
	
	public Get_sampleImage_result(){
		valid = false;
		imgURL = new String();
		urlPrefix = new String();
	}

	public String getImgURL() {
		return imgURL;
	}

	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	@Override
	public String toString() {
		if(valid==true){
		return this.urlPrefix+"/"+imgURL+"\n";
		}
		else{
			return "FAILED\n";
		}
	}

	public String getUrlPrefix() {
		return urlPrefix;
	}

	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}
	

}
