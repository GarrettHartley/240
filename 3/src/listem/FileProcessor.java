package listem;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class FileProcessor {

	public List<File> getFiles(boolean recusive,File f){
	
		List<File> files = new ArrayList<File>();
		if(recusive == true){	
			addFilesR(f,files);
		}

		for(File file:f.listFiles()){
			if(file.isFile()){
				files.add(file);
			}
		}
		return files;	
	}
	
	public List<File> addFilesR(File f, List<File> files){
		for(File file:f.listFiles()){
			if(file.isDirectory()){
				addFilesR(file,files);
			}
			else{
				files.add(file);
			}
		}
		return files;
	}
	
	public boolean checkFile(File file, String fileSelectionPattern ){
		Pattern p = Pattern.compile(fileSelectionPattern);
		Matcher m = p.matcher(file.getName());
		boolean b = m.matches();
		return b;
	}
	
}
