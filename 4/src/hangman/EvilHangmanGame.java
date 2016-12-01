package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class EvilHangmanGame implements IEvilHangmanGame {
	
	Set<String> currentDictionary = new TreeSet<String>();
	Set<Character> guessedLetters = new TreeSet<Character>();
	//TreeSet<String> keys = new TreeSet<String>();
	StringBuilder currentWord= new StringBuilder();
	@Override
	
	public void startGame(File dictionary, int wordLength) {
	//reset class variables	
		currentDictionary.clear();
		guessedLetters.clear();
		//keys.clear();
	//create an initical current word of all blanks	
		for(int i = 0; i<wordLength;i++){
			currentWord.append("_");
		}
	//create scanner using the input File dictionary	
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(dictionary.getAbsolutePath()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//Read in the dictionary and add to currentDictionary	
		while(scanner.hasNextLine()!=false){
			String line=scanner.nextLine();
			if(line.length()==wordLength){
				currentDictionary.add(line.toLowerCase());
			}
		}
	}
	
	public String getCurrentWord(){
		return new String(currentWord);
	}
	
	public boolean alreadyGuessed(char guess){
		for(char c:guessedLetters){
			if (c==guess){
				return true;
			}
		}		
		return false;
	}

	
public String guessDoesNotAppear(String one, String two, char guess){
	//if the guess appears in both return null
	if(one.contains(Character.toString(guess))&&two.contains(Character.toString(guess))){
		return null;
	}
	//if one contains the guess then we return two and vice versa
	else if(one.contains(Character.toString(guess))){
		return two;
	}
	return one;
}


public String leastLetters(String one, String two){
//loop through each string and count letters
	int oneLetterCount = 0;
	int twoLetterCount = 0;
	String _ = "_";

	for(char c:one.toCharArray()){
		if(c!= _.charAt(0)){
			oneLetterCount++;
		}
	}

	for(char c:two.toCharArray()){
		if(c!= _.charAt(0)){
			twoLetterCount++;
		}
	}

	if(oneLetterCount == twoLetterCount){
		return null;
	}
	else if(oneLetterCount>twoLetterCount){
		return two;
	}
	return one;
	
}

public String rightMost(String one, String two){
	
	String _ = "_";
	for(int i = one.length()-1; i>0 ; i--){
		//
		if(one.charAt(i)==_.charAt(0)&&two.charAt(i)==_.charAt(0)|one.charAt(i)!=_.charAt(0)&&two.charAt(i)!=_.charAt(0)){
		}
		else{
			if(one.charAt(i)!=_.charAt(0)){
				return one;
			}
			return two;
		}
	}
	return null;
}

@Override
public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {	
	guess = Character.toLowerCase(guess);
	Set<String> newDictionary = new TreeSet<String>();

	String newDictionaryKey= new String();
	Map<String,Set<String>> partitionedDictionary = new HashMap<String,Set<String>>();
	
	for(String str:currentDictionary){
		String strCopy = new String(str);
//create new keys
			for(char c: strCopy.toCharArray()){
				if(c!=guess && alreadyGuessed(c)==false){
					strCopy=strCopy.replace(Character.toString(c),"_");
				}	
			}
// add to partitioned map				
				if(partitionedDictionary.containsKey(strCopy)){
				Set<String> values=new TreeSet<String>(partitionedDictionary.get(strCopy));
				values.add(str);
				partitionedDictionary.put(strCopy, values);
				}
				else{
					Set<String> value=new TreeSet<String>();
					value.add(str);
					partitionedDictionary.put(strCopy,value);	
				}
			//	keys.add(strCopy);		
	}
	
//Select best partition	
		for(Map.Entry<String, Set<String>> temp: partitionedDictionary.entrySet()){
			//System.out.println(newDictionary);
			System.out.println(newDictionary.size()+"Dictionary size");
			if(temp.getValue().size()>newDictionary.size()){
				newDictionary = temp.getValue();
				newDictionaryKey=temp.getKey();
			}
			else if(temp.getValue().size()==newDictionary.size()){
		// Case 1 Pick the one where the guess does not appear
				if(guessDoesNotAppear(temp.getKey(),newDictionaryKey, guess)!=null){
					newDictionary=partitionedDictionary.get(guessDoesNotAppear(temp.getKey(),newDictionaryKey,guess));
					newDictionaryKey=guessDoesNotAppear(temp.getKey(),newDictionaryKey,guess);
				}
				else if(leastLetters(temp.getKey(),newDictionaryKey)!=null){
					
					newDictionary=partitionedDictionary.get(leastLetters(temp.getKey(),newDictionaryKey));
					newDictionaryKey=leastLetters(temp.getKey(),newDictionaryKey);
					
				}
				else if(rightMost(temp.getKey(),newDictionaryKey)!=null){
					newDictionary=partitionedDictionary.get(rightMost(temp.getKey(),newDictionaryKey));
					newDictionaryKey=rightMost(temp.getKey(),newDictionaryKey);

				}	
			}	
		}
		
// Return best partition	
		guessedLetters.add(guess);	
		currentDictionary = newDictionary;
		currentWord = new StringBuilder(newDictionaryKey);
		return newDictionary;

	}
}
