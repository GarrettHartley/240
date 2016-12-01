package client.gui.indexerWindow.tableEntry.spellChecker;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class SpellCorrector implements ISpellCorrector {

	TreeSet<String> dictionary;
	Set<String> inputWords;
	
	Set<String> goodWords;
	
	public SpellCorrector(){
		dictionary = new TreeSet<String>();
		inputWords = new HashSet<String>();
	}
	
	@Override
	public void useDictionary(String dictionaryFileName) throws IOException {
		// TODO Auto-generated method stub
		if(dictionaryFileName!=null){
		Scanner scanner = new Scanner(new File(dictionaryFileName));
		while(scanner.hasNext()){
			String[] words;
			words = scanner.next().toLowerCase().split(",");
			for(String word: words){
			dictionary.add(word);
//			dictionary.find(word);
			}
		}
		}
	}
	
	public Set<String> addToMe(Set<String> add, Set<String> me){
		for(String str: add){
			me.add(str);
		}
		return me;
	}
	
	public Set<String> deletion(Set<String> inputWords){
		Set<String> newWords = new HashSet<String>();
	for(String str: inputWords)	{

		for(int i = 0; i<str.length();i++ ){
			String newString = new String(str.substring(0,i)+str.substring(i+1));
			if(newString.length()!=0){
			newWords.add(newString);
			}
		}
	}
	return newWords;
	}
	
	public Set<String> swap(Set<String> inputWords){
		Set<String> newWords = new HashSet<String>();
	for(String str: inputWords)	{
		for(int i = 0; i<str.length()-1;i++ ){
			String newString = new String(str.substring(0,i)+str.substring(i+1,i+2)+str.substring(i,i+1)+str.substring(i+2));
			newWords.add(newString);
		}
	}
	return newWords;	
	}
	
	public Set<String> insertion(Set<String> inputWords){
		Set<String> newWords = new HashSet<String>();
	for(String str: inputWords)	{
		for(int i = 0; i<str.length();i++ ){
			for(int j = 0;j<26;j++){
				int k = j+'a';
			String newString = new String(str.substring(0,i)+Character.toString((char)k)+str.substring(i));
			newWords.add(newString);
			}
		}
		for(int j = 0;j<26;j++){
			int k = j+'a';
		String newString = new String(str+Character.toString((char)k));
		newWords.add(newString);
		}
	}
	return newWords;	
	}
	public Set<String> alter(Set<String> inputWords){
		Set<String> newWords = new HashSet<String>();
	for(String str: inputWords)	{
		for(int i = 0; i<str.length();i++ ){
			for(int j = 0;j<26;j++){
				int k = j+'a';
			String newString = new String(str.substring(0,i)+Character.toString((char)k)+str.substring(i+1));
			newWords.add(newString);
			}
		}
	}
	return newWords;	
	}
	
	public Set<String> getGoodEdits(Set<String> words){
		Set<String> goodEdits = new HashSet<String>();
		for(String str: words){
			if(str.length()!=0){
			if(dictionary.contains(str)==true){
				goodEdits.add(str);
			}
			}
		}
		
		return goodEdits;
	}
	
	
	
	public Set<String> edit(Set<String> inputWords){
		Set<String> newWords = new HashSet<String>();
//		//deletion
		newWords=addToMe(newWords,deletion(inputWords));
//		//swap
		newWords = addToMe(newWords,swap(inputWords));
//		//alteration
		newWords = addToMe(newWords,insertion(inputWords));
	//	
//	//	insertion
		newWords = addToMe(newWords,alter(inputWords));

	//	System.out.println(newWords.toString());
	//	System.out.println(newWords.size());
		return newWords;
	}

//	public String mostSimilarWord(Set<String> goodEdits){
//		String mostSimilarWord = new String();
//		mostSimilarWord = null;
//		System.out.println("This is most similar "+goodEdits.toString());
//		goodWords = goodEdits;
//		for(String str: goodEdits){
//			if(mostSimilarWord == null){
//				mostSimilarWord = str;
//			}
//			else if(dictionary.contains(str).getValue()>dictionary.find(mostSimilarWord).getValue()){
//				mostSimilarWord = str;
//			}
//			else if(dictionary.find(str).getValue()==dictionary.find(mostSimilarWord).getValue()){
//				if(str.compareTo(mostSimilarWord)<0){
//					mostSimilarWord = str;
//				}
//			}	
//		}
//		return mostSimilarWord;
//	}

	
	@Override
	public String suggestSimilarWord(String inputWord)throws NoSimilarWordFoundException {
//		Set<String> words = new HashSet<String>();
//		words.add(inputWord);
//	//	System.out.println(inputWord);
//	//	System.out.println("HJellow? "+dictionary.find(inputWord));
//		if(dictionary.find(inputWord)!=null){
//			
//			return inputWord;
//		}
//		
//		Set<String> editsOne = new HashSet<String>();
//		editsOne = edit(words);
//		
//		Set<String> editstoTwo = new HashSet<String>(editsOne);
//		editsOne=getGoodEdits(editsOne);
//	//	System.out.println(editsOne.toString());
//		if(mostSimilarWord(editsOne) != null){
//			//System.out.println("Got to edits 1");
//			 return mostSimilarWord(editsOne);
//		}
//		
//		//System.out.println("Got to edits 2");
//		Set<String> editsTwo = new HashSet<String>();
//		editsTwo = edit(editstoTwo);
//		editsTwo = getGoodEdits(editsTwo);
//		
//		if(mostSimilarWord(editsTwo) != null){
//
//			 return mostSimilarWord(editsTwo);
//		}
//		
//		
//		
//	//	System.out.println(editsOne.toString());
//		throw new NoSimilarWordFoundException();
		return null;
	}
	
	
	
	public Set<String> getGoodWords() {
		return goodWords;
	}

	public void setGoodWords(Set<String> goodWords) {
		this.goodWords = goodWords;
	}

	public TreeSet<String> getDictionary(){
		return dictionary;
	}

}
