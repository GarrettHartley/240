package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class EvilHangmanGame implements IEvilHangmanGame {
	Set<String> currD;
	String currW;
	Set<String> guessedL;
	
 public	EvilHangmanGame(){
		currD  = new TreeSet<String>();
		currW = new String();
		guessedL = new TreeSet<String>();
	}
	
	public Set<String> getGuessed(){
		return guessedL;
	}
	
	public String getCurrW(){
		return currW;
	}
	
	@Override
	public void startGame(File dictionary, int wordLength) {
		Scanner scanner = null;
		
		try {
			scanner = new Scanner (new File(dictionary.getAbsolutePath()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(scanner.hasNext()){
			String word =  new String(scanner.next());
			if(word.length() == wordLength){
			currD.add(word);
		//	System.out.println(word);
			}
		}
		
		for(int i = 0; i < wordLength;i++){
			currW = new String(currW+"_");
		}
	//	System.out.println(currW);
	}

	@Override
	public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
		Set<String> newD = new TreeSet<String>();
		Map<String, Set<String>> parD = new TreeMap<String,Set<String>>();
		Set<String> selectD = new TreeSet<String>();

		
		guessedL.add(Character.toString(guess));
//create parD		
		for(String str: currD){
			String strKey = new String(str);
			
			for(char c: str.toCharArray()){
					if(guessedL.contains(Character.toString(c))!=true){
						strKey=strKey.replace(Character.toString(c), "_");
					}
			}
			if(parD.containsKey(strKey)){
				Set<String> newValues = new TreeSet<String>(parD.get(strKey));
				newValues.add(str);
				parD.put(strKey, newValues);
			}
			else{
				Set<String> newValues = new TreeSet<String>();
				newValues.add(str);
				parD.put(strKey, newValues);
			}
	
		}
		
//Select partition		
		for(Map.Entry<String,Set<String>> temp: parD.entrySet()){
			
			System.out.print("This is the key "+ temp.getKey());
			System.out.println(" This is the value "+ temp.getValue());
			
			if(temp.getValue().size()>selectD.size()){
				selectD= temp.getValue();
				currW = temp.getKey();
			}
			else if (temp.getValue().size()==selectD.size()){
				if(doesNotAppear(temp.getKey(),currW,guess)!=null){
					selectD= parD.get(doesNotAppear(temp.getKey(),currW,guess));
					currW = doesNotAppear(temp.getKey(),currW,guess);
				}
				else if(fewestLetters(temp.getKey(),currW)!=null){
					selectD= parD.get(fewestLetters(temp.getKey(),currW));
					currW = fewestLetters(temp.getKey(),currW);
				}
				else if(rightMost(temp.getKey(),currW)!=null){
					selectD = parD.get(rightMost(temp.getKey(),currW));
					currW = rightMost(temp.getKey(),currW);
				}
			}		
		}
		currD = selectD;
		System.out.print("This is currD: ");
		for(String stringD: currD){
			System.out.print(stringD);
		}
		return currD;
	}
	
	public String rightMost(String one, String two){
		String _="_";
		for(int i = one.length()-1; i>0;i--){
			if(one.charAt(i)!=_.charAt(0)|two.charAt(i)!=_.charAt(0)){
				if(one.charAt(i)!=_.charAt(0)&&two.charAt(i)==_.charAt(0)){
					return one;
				}
				if(two.charAt(i)!=_.charAt(0)&&one.charAt(i)==_.charAt(0)){
					return two;
				}
			}
		}
		return null;
	}
	
	public String fewestLetters(String one, String two){
		int countOne = 0;
		int countTwo = 0;
		String _="_";
		for(char c : one.toCharArray()){
			if(c!=_.charAt(0)){
				countOne++;
			}
		}
		for(char c : two.toCharArray()){
			if(c!=_.charAt(0)){
				countTwo++;
			}
		}
		if(countOne!=countTwo){
			if(countOne>countTwo){
				return two;
			}
			else{
				return one;
			}
		}
		return null;
	}
	
	public String doesNotAppear(String one, String two, char guess){
	//	System.out.println("doesNotAppear");
		if(one.contains(Character.toString(guess))==false|two.contains(Character.toString(guess))==false){
		if(one.contains(Character.toString(guess))==false){
			return one;
		}
		else{
			return two;
		}
		
		}
		return null;
	}

}
