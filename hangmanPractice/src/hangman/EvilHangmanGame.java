package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class EvilHangmanGame implements IEvilHangmanGame {
	Set<String> curD;
	int wordL;
	Set guessedL;
	String currentWord = new String();
	
	EvilHangmanGame(){
		wordL = 0;
		curD = new TreeSet<String>();
		guessedL = new TreeSet<>();
	}
	
	
	public String getCurW(){
		return currentWord;
	}
	public void startGame(File dictionary, int wordLength) {
	
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(dictionary.getAbsolutePath()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(scanner.hasNext()){
			String newWord = new String(scanner.next());
			if(newWord.length()==wordLength){
			curD.add(newWord);
		//	System.out.println(newWord);
			}
		}
		wordL = wordLength;
		for(int i = 0; i<wordLength;i++){
			currentWord = new String(currentWord+"_");
		}
		
	}
	
	public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
		Set<String> newDictionary = new TreeSet<String>();
		String newDictionaryKey = new String();
		guessedL.add(guess);
		Map<String,Set<String>> parD = new TreeMap<String,Set<String>>();

		for(String str: curD){
			String strKey = new String(str);
			for(char c:strKey.toCharArray()){
				if(guessedL.contains(c)==false){
//					System.out.print("got here");
					strKey = strKey.replace(Character.toString(c),"_");
				}
			}
			if(parD.containsKey(strKey)==false){
				Set<String> values = new TreeSet<String>();
				values.add(str);
				parD.put(strKey, values);
//				System.out.println("Key doesn't exist: "+str+"-"+strKey);
			}
			else{
				Set<String> values = new TreeSet<String>(parD.get(strKey));
				values.add(str);
				parD.put(strKey, values);
//				System.out.println("Key already exist: "+str+"-"+strKey);
//				System.out.println(parD.get(strKey));
			}		
		}
//Select curD
		for(Map.Entry<String,Set<String>> temp: parD.entrySet()){
//Pick larger	
			if(newDictionary.size()<temp.getValue().size()){
			//	System.out.println("got here");
				newDictionary = temp.getValue();
				newDictionaryKey=temp.getKey();
			}
			else if(newDictionary.size()==temp.getValue().size()){
//Guess Does not appear
				//System.out.println("size is ==");
				if(doesNotAppear(newDictionaryKey,temp.getKey(),guess)!=null){
					newDictionary = parD.get(doesNotAppear(newDictionaryKey,temp.getKey(),guess));
					newDictionaryKey = doesNotAppear(newDictionaryKey,temp.getKey(),guess);
				}
//fewest letters
				else if(fewestLetters(newDictionaryKey,temp.getKey(),guess)!=null){
					newDictionary = parD.get(fewestLetters(newDictionaryKey,temp.getKey(),guess));
					newDictionaryKey = fewestLetters(newDictionaryKey,temp.getKey(),guess);
				}
//rightmost
				else if(fewestLetters(newDictionaryKey,temp.getKey(),guess)!=null){
					newDictionary = parD.get(fewestLetters(newDictionaryKey,temp.getKey(),guess));
					newDictionaryKey = fewestLetters(newDictionaryKey,temp.getKey(),guess);
				}

			}

		}
		

		curD = newDictionary;
		currentWord = newDictionaryKey;
		return newDictionary;
	}
	
	public String rightMost(String one,String two, char c){
		String _="_";
		for(int i = one.length();i>0;i++){
			if(one.charAt(i)!=_.charAt(0)){
				return one;
			}
			if(two.charAt(i)!=_.charAt(0)){
				return two;
			}
		}
		return null;
	}
	
	
	public String fewestLetters(String one, String two, char c){
		int oneC=0;
		int twoC=0;
		String _ = "_";
		for(char c2:one.toCharArray()){
			if(c2!=_.charAt(0)){
				oneC++;
			}
		}
		for(char c2:two.toCharArray()){
			if(c2!=_.charAt(0)){
				twoC++;
			}
		}
		if(oneC!=twoC){
			if(oneC>twoC){
				return two;
			}
			else{
				return one;
			}
		}
		return null;
	}
	public String doesNotAppear(String one, String two, char c){
		if(one.contains(Character.toString(c))==true&&two.contains(Character.toString(c))==false){
			return two;
		}
		if(two.contains(Character.toString(c))==true&&one.contains(Character.toString(c))==false){
		return one;
		}
		return null;
	}
}
