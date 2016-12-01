package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class EvilHangmanGame implements IEvilHangmanGame {
//	Set<String> dictionaryWords;
//	Set<Character> guessedLetters;
//	StringBuilder currentWord;
	Set<String> dictionaryWords = new TreeSet<String>();
	Set<Character> guessedLetters = new TreeSet<Character>();
//	TreeSet<String> keys = new TreeSet<String>();
	StringBuilder currentWord= new StringBuilder();
	int wordlength;
	TreeSet<String> keys = new TreeSet<String>();

	
	@Override
	public void startGame(File dictionary, int wordLength) {
		wordlength = wordLength;
		dictionaryWords.clear();
		guessedLetters.clear();
		currentWord= new StringBuilder();
		
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(dictionary.getAbsolutePath()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(scanner.hasNext()){
			String word =  new String(scanner.next());
			if(word.length()==wordlength){
				dictionaryWords.add(word);
			}
		}
		
	}
	
	public boolean alreadyGuessed(char input_c){
		for(char c:guessedLetters){
			if(c==input_c){
				return true;
			}
		}
		return false;
	}

	@Override
	public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
		guess = Character.toLowerCase(guess);
		Map<String,Set<String>> partitionedDictionary = new HashMap<String,Set<String>>();
		Set<String> newDictionary = new TreeSet<String>();
		String newDictionaryKey = new String();
		
		for(String str:dictionaryWords){
			String strCopy= new String(str);
	//create new key
			for(char c: strCopy.toCharArray()){
				if(c!=guess && Character.toString(c)!="_"&&alreadyGuessed(c)==false){
					strCopy=strCopy.replace(Character.toString(c), "_");
				}
			}
	//add to partitioned map
			if(partitionedDictionary.containsKey(strCopy)){
				Set<String> values = new TreeSet<String>(partitionedDictionary.get(strCopy));
				values.add(str);
				partitionedDictionary.put(strCopy, values);
			}
			else{
				Set<String> value = new TreeSet<String>();
				value.add(str);
				partitionedDictionary.put(strCopy, value);
			}
			keys.add(strCopy);
		}
	//Select best partition
		for(Map.Entry<String,Set<String>> temp: partitionedDictionary.entrySet()){

			if(temp.getValue().size()>newDictionary.size()){
				newDictionary = temp.getValue();
				newDictionaryKey= temp.getKey();
			}
			else if(temp.getValue().size()==newDictionary.size()){
				if(guessDoesNotAppear(temp.getKey(),newDictionaryKey,guess)!=null){
				newDictionary=partitionedDictionary.get(guessDoesNotAppear(temp.getKey(),newDictionaryKey,guess));
				newDictionaryKey=guessDoesNotAppear(temp.getKey(),newDictionaryKey,guess);
				}
				else if(leastLetters(temp.getKey(),newDictionaryKey,guess)!=null){
					newDictionary = partitionedDictionary.get(leastLetters(temp.getKey(),newDictionaryKey,guess));
					newDictionaryKey = leastLetters(temp.getKey(),newDictionaryKey,guess);
				}
				else{
					newDictionary = partitionedDictionary.get(rightMost(temp.getKey(),newDictionaryKey,guess));
					newDictionaryKey = rightMost(temp.getKey(),newDictionaryKey,guess);
				}
			}
		}
		guessedLetters.add(guess);
		dictionaryWords = newDictionary;
		currentWord = new StringBuilder(newDictionaryKey);
		return newDictionary;

	}
	
	public String rightMost(String one, String two, char guess){
	String _="_";
		for(int i=one.length()-1; i>0;i--){
			if(one.charAt(i)!=_.charAt(0)&&two.charAt(i)==_.charAt(0)){
				return one;
			}
			else if (two.charAt(i)!=_.charAt(0)&&one.charAt(i)==_.charAt(0)){
				return two;
			}	
		}
return null;
	}
	
	public String getCurrentWord(){
		return new String(currentWord);
	}
	
	public String leastLetters(String one, String two, char guess){
		int countOne=0;
		int countTwo=0;
		String _="_";
		
		for(char c: one.toCharArray()){
			if(c!=_.charAt(0)){
				countOne++;
			}
		}
		
		for(char c: two.toCharArray()){
			if(c!=_.charAt(0)){
				countTwo++;
			}
		}
		
		if(countOne==countTwo){
			return null;
		}
		if(countOne>countTwo){
			return two;
		}
		return one;

	}
	
	
	
	public String guessDoesNotAppear(String one, String two, char guess){
		//if the guess appears in both return null
		if(one.contains(Character.toString(guess))&&two.contains(Character.toString(guess))){
			return null;
		}
		//if one contains the guess then return two and vice versa
		else if(one.contains(Character.toString(guess))){
			return two;
		}
		return one;
		
		
	}

}
