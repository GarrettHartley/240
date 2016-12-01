package listem;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Grep extends FileProcessor implements IGrep {

	
public Map<File,List<String>> grep(File directory, String fileSelectionPattern,
		String substringSelectionPattern, boolean recursive){
		System.out.println("is running");
		Map<File,List<String>> fileStrings = new TreeMap<File, List<String>>();
		
		List<File> files = getFiles(recursive,directory);
		
		for(File file:files){
		////////// take a file return a scanner///	returns null if File does not match		
		boolean b = checkFile(file,fileSelectionPattern);
		/////////////////	
			if(b==true){
				Scanner scanner = null;	
				try {
					scanner = new Scanner(new File(file.getAbsolutePath()));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				List<String> strings = new ArrayList<String>();
				while(scanner.hasNextLine()!=false){
					String line = scanner.nextLine();
					System.out.println(line);
					Pattern p = Pattern.compile(substringSelectionPattern);
					Matcher m = p.matcher(line);
						if(m.find()){
								strings.add(line);
						}
				}
				if(strings.isEmpty()==false){
				fileStrings.put(file, strings);
				}
				scanner.close();
			}	
		}
		return fileStrings;
}

	
	
	
	
}
