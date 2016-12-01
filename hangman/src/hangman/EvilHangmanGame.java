package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class EvilHangmanGame implements IEvilHangmanGame {
	Set<String> words;
	String currW;
	Set<String> guessedL;
	Set<String> keys;
	
	public EvilHangmanGame(){
		words = new TreeSet<String>();
		currW  = new String();
		guessedL = new TreeSet<String>();
		keys= new TreeSet<String>();
	}
	
	@Override
	public void startGame(File dictionary, int wordLength) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(dictionary.getAbsolutePath()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(scanner.hasNext()){
		
			String inputWord = new String(scanner.next());
			if(inputWord.length()==wordLength){
			words.add(inputWord);
			}
		}
		
		for(int i =0;i<wordLength;i++){
			currW = currW+"_";
		}
		
		
	}
	
	public String makeKey(String str){
		String strKey = new String(str);
			for(char c:str.toCharArray()){
				if(guessedL.contains(Character.toString(c))!=true){
					strKey =strKey.replace(Character.toString(c), "_");
				}
			}
		//	System.out.println("srtKey ="+ strKey);
		return strKey;
	}

	public String notAppear(String one, String two, char guess){
		if(one.contains(Character.toString(guess))==true&&two.contains(Character.toString(guess))==false){
			return one;
		}
		if(one.contains(Character.toString(guess))==false&&two.contains(Character.toString(guess))==true){
			return two;
		}
	return null;	
	}
	
	public String fewestLetters(String one, String two, char guess){
		int countOne = 0;
		int countTwo = 0;
		String _="_";
		for(char c:one.toCharArray()){
			if(c!=_.charAt(0)){
				countOne++;
			}
		}
		for(char c:two.toCharArray()){
			if(c!=_.charAt(0)){
				countTwo++;
			}
		}
		if(countOne!=countTwo){
			if(countOne>countTwo){
				return two;
			}
			return one;
		}
		return null;
	}
	
	public String rightMost(String one,String two, char guess){		String _ = "_";
		for(int i =one.length()-1; i>0;i--){
			if(one.charAt(i)!=_.charAt(0)&&two.charAt(i)==_.charAt(0)){
				return one;
			}
			else if(one.charAt(i)==_.charAt(0)&&two.charAt(i)!=_.charAt(0)){
				return two;
			}	
		}
		
		return null;
	}
	
	@Override
	public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
		
		//create partitions
		Map<String,Set<String>> pdictionary = new TreeMap<String,Set<String>>();
		guessedL.add(Character.toString(guess));
		for(String str: words){
			String strKey = new String(makeKey(str));
		//	System.out.println(strKey);
			if(pdictionary.containsKey(strKey)==false){
				Set<String> newValues = new TreeSet<String>();
				newValues.add(str);
				pdictionary.put(strKey,newValues);
			}
			else if(pdictionary.containsKey(strKey)==true){
				Set<String> newValues = new TreeSet<String>(pdictionary.get(strKey));
				newValues.add(str);
				pdictionary.put(strKey, newValues);
			}
		}
		
	//	System.out.println(pdictionary.toString());
		//select partitions
		Set<String> newDictionary = new TreeSet<String>();
		newDictionary = null;
		for(Map.Entry<String, Set<String>> temp: pdictionary.entrySet()){
			if(newDictionary == null){
				newDictionary = temp.getValue();
				currW = temp.getKey();
			}
			if(newDictionary.size()<temp.getValue().size()){
				newDictionary = temp.getValue();
				currW = temp.getKey();
			}
			else if(newDictionary.size()==temp.getValue().size()){
				if(notAppear(currW,temp.getKey(),guess)!=null){
					newDictionary = pdictionary.get(notAppear(currW,temp.getKey(),guess));
					currW = notAppear(currW,temp.getKey(),guess);
				}
				else if(fewestLetters(currW,temp.getKey(),guess)!=null){
					newDictionary=pdictionary.get(fewestLetters(currW,temp.getKey(),guess));
					currW = fewestLetters(currW,temp.getKey(),guess);
				}
				else if(rightMost(currW,temp.getKey(),guess)!=null){
					newDictionary=pdictionary.get(rightMost(currW,temp.getKey(),guess));
					currW = rightMost(currW,temp.getKey(),guess);
				}

			}
			
			
			
		}
		
		
		
		words = newDictionary;
		
		return words;
	}
	
	public String getWord(){
		return words.iterator().next();
		
	}

}