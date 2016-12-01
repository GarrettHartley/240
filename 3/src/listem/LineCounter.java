package listem;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;


public class LineCounter extends FileProcessor implements ILineCounter {

	public Map<File,Integer> countLines(File directory,String fileSelectionPattern,boolean recursive){
		int lineCount;
		Map<File,Integer> lineCountForFile = new TreeMap<File, Integer>();
		lineCount=0;
///////// Create files recurse through and add them, iterate through files and make calls
		List<File> files = getFiles(recursive,directory);
////////////////////////////////	
		for(File file:files){
///////////	take a file return a scanner //// return null if invalid file
		lineCount = 0;
		boolean b = checkFile(file,fileSelectionPattern);
			if(b==true){
				Scanner scanner = null;	
					try {
						scanner = new Scanner(new File(file.getAbsolutePath()));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				while(scanner.hasNextLine()==true){
					System.out.println("Scanner is full");
					scanner.nextLine();
				lineCount++;
				}
				lineCountForFile.put(file, lineCount);
			}
			
		}
		
		return lineCountForFile;
		
	}
	

	
	
		
	
}
