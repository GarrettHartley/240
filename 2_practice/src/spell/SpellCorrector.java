package spell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class SpellCorrector implements ISpellCorrector  {
	Trie dictionary = new Trie();
	Set<String> originalWords= new HashSet<String>();
	public SpellCorrector(){
	}
	public Trie getDictionary(){
		return dictionary;

	}
	@Override
	public void useDictionary(String dictionaryFileName) throws IOException {
		Scanner scanner = null;
		try{
		scanner = new Scanner(new File(dictionaryFileName));
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}
		while(scanner.hasNext()){
			String word = scanner.next();
			dictionary.add(word);
		}
		
		try{
			scanner = new Scanner(new File(dictionaryFileName));
		} catch(FileNotFoundException e){
			e.printStackTrace();
		}
		
//Build dictionary Hashset		
while(scanner.hasNext()){
	String word = new String(scanner.next().toLowerCase());
	originalWords.add(word);
}

	}
	public Set<String> delete(Set<String> possibleWords){
		Set<String> newPossibleWords = new HashSet<String>();
		for(String word:possibleWords){
			System.out.println(word);
			for(int i =0;i<word.length();i++){
				String newWord = new String(word.substring(0,i)+word.substring(i+1,word.length()));
				newPossibleWords.add(newWord);
			}
		}
		return newPossibleWords;
	}
	
	public Set<String> swap(Set<String> possibleWords){
		Set<String> newPossibleWords = new HashSet<String>();
		for(String word:possibleWords){
			for(int i = 0;i<word.length()-1; i++){
				String newWord = new String(word.substring(0,i)+word.substring(i+1,i+2)+word.substring(i,i+1)+word.substring(i+2,word.length()));
				newPossibleWords.add(newWord);
			}
		}
		return newPossibleWords;
	}
	
	public Set<String> insert(Set<String> possibleWords){
		Set<String> newPossibleWords = new HashSet<String>();
		for(String word:possibleWords){
			for(int i = 0;i<word.length(); i++){
				for(int j = 0;j<26;j++){
					int k = j+'a';
					String newWord = new String(word.substring(0,i)+Character.toString((char)k)+word.substring(i,word.length()));
					newPossibleWords.add(newWord);		
				}
		}
		for(int j = 0;j<26;j++){
		int k = j+'a';
		String newWord = new String(word.substring(0,word.length())+Character.toString((char)k));
		newPossibleWords.add(newWord);
	}
	}
		
		return newPossibleWords;
	}
	
	public Set<String> alteration(Set<String> possibleWords){
		Set<String> newPossibleWords = new HashSet<String>();		
		for(String word:possibleWords){
			for(int i = 0;i<word.length(); i++){
				for(int j = 0;j<26;j++){
					int k = j+'a';
					String newWord = new String(word.substring(0,i)+Character.toString((char)k)+word.substring(i+1,word.length()));
					newPossibleWords.add(newWord);
				}				
			}
		}
		return newPossibleWords;
	}
	
	public void addContents(Set<String> me, Set<String> giveTo){
		for(String str:giveTo){
			me.add(str);
		}
	}
	
	public Set<String> editPossibleWords(Set<String> possibleWords){
		Set<String> returnEditPossibleWords= new HashSet<String>();
		//deletion
		addContents(returnEditPossibleWords,delete(possibleWords));
		//swap
		addContents(returnEditPossibleWords,swap(possibleWords));
		//alteration
		addContents(returnEditPossibleWords,alteration(possibleWords));
		//insertion
		addContents(returnEditPossibleWords,insert(possibleWords));
		return returnEditPossibleWords;
	}
	
	public String mostSimilarWord(Set<String> possibleWords,String mostSimilarWord){
		for(String str:possibleWords){
			if(mostSimilarWord==null){
				//System.out.println("got here 1");
				mostSimilarWord = new String(str);
			}
			if(dictionary.find(str)==null|dictionary.find(mostSimilarWord)==null){
				//System.out.println("got here 2");
				return mostSimilarWord;
			}
			if(dictionary.find(str).getValue()>dictionary.find(mostSimilarWord).getValue()){
				//System.out.println("got here 3");
				mostSimilarWord = new String(str);
			}
			if(dictionary.find(str).getValue()==dictionary.find(mostSimilarWord).getValue()){
				//System.out.println("got here 4"+str);
				if(str.compareTo(mostSimilarWord)<0){
					mostSimilarWord = new String(str);
				}
			}
		}
		return mostSimilarWord;
	}
	
	public Set<String> getGoodEdits(Set<String> alledits){
		Set<String> goodEdits = new HashSet<String>();
		for(String str:alledits){
			for(String dictionaryStr:originalWords){
			if(str.equals(dictionaryStr)){
				goodEdits.add(str);
			}
		}
		}
		return goodEdits;
	}

	@Override
	public String suggestSimilarWord(String inputWord)throws NoSimilarWordFoundException {
		System.out.println("input word "+inputWord);
		String mostSimilarWord=new String();
		mostSimilarWord = null;
//Round one
		Set<String> possibleWords = new HashSet<String>();
		possibleWords.add(inputWord.toLowerCase());
		possibleWords=editPossibleWords(possibleWords);
//	Set<String> editDistanceOne = new HashSet(possibleWords);
		Set<String> goodEditsOne = new HashSet(getGoodEdits(possibleWords));
		mostSimilarWord = mostSimilarWord(goodEditsOne,mostSimilarWord);
		System.out.println("Mostsimilar word "+mostSimilarWord);
		if(mostSimilarWord!=null){
			return mostSimilarWord;
		}
//Round Two
		System.out.println("This is in editPossibleWords");
		int count = 0;
		for(String str:possibleWords){
			count++;
			System.out.println(str+" "+count);
		}
		possibleWords = editPossibleWords(possibleWords);
		Set<String> goodEditsTwo = new HashSet(getGoodEdits(possibleWords));
		mostSimilarWord = mostSimilarWord(goodEditsTwo,mostSimilarWord);
		return mostSimilarWord;
	}

}
