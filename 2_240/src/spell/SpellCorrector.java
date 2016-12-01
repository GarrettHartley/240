package spell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class SpellCorrector implements ISpellCorrector {
	
	private Set<String> dictionary = new HashSet<String>();
	private Trie trie = new Trie(); 
	
	public Trie getTrie(){
		return trie;
	}
		
	@Override
	public void useDictionary(String dictionaryFileName) throws IOException {
		Scanner scanner = null;
		
				try {
					scanner = new Scanner(new File(dictionaryFileName));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
//Build trie		
		while(scanner.hasNext()){
			String word = scanner.next();
			word=word.toLowerCase();
			trie.add(word);
		}
		
				try{
					scanner = new Scanner(new File(dictionaryFileName));
				} catch(FileNotFoundException e){
					e.printStackTrace();
				}
				
//Build dictionary Hashset		
		while(scanner.hasNext()){
			String word = new String(scanner.next().toLowerCase());
			dictionary.add(word);
		}
	}

public Set<String> getGoodEdits(Set<String> alledits){
	Set<String> goodEdits = new HashSet<String>();
	for(String str:alledits){
		for(String dictionaryStr:dictionary){
		if(str.equals(dictionaryStr)){
			goodEdits.add(str);
		}
	}
	}
	return goodEdits;
}
	
@Override
public String suggestSimilarWord(String inputWord) throws NoSimilarWordFoundException {
	inputWord=inputWord.toLowerCase();
	String mostSimilarWord = new String();
	mostSimilarWord=null;
// Round One
		Set<String> toeditsDistanceOne = new HashSet<String>();
		toeditsDistanceOne.add(inputWord);
		Set<String> editsDistanceOne = new HashSet<String>();
		Set<String> goodEditsDictionaryOne = new HashSet<String>();
		editsDistanceOne=editDistanceX(toeditsDistanceOne);
		goodEditsDictionaryOne=getGoodEdits(editsDistanceOne);
		mostSimilarWord = mostSimilarWord(goodEditsDictionaryOne, mostSimilarWord);

		if(mostSimilarWord!=null){
			return mostSimilarWord;
		}

//Round Two	
		Set<String> editsDistanceTwo= new HashSet<String>();
		Set<String> goodEditsDictionaryTwo= new HashSet<String>();
		
		editsDistanceTwo=editDistanceX(editsDistanceOne);
		
		goodEditsDictionaryTwo=getGoodEdits(editsDistanceTwo);
		mostSimilarWord = mostSimilarWord(goodEditsDictionaryTwo, mostSimilarWord);

		if(mostSimilarWord == null){
			System.out.println("Word not found throw exception");
			throw new NoSimilarWordFoundException();
		}
		
		return mostSimilarWord;
	}

public String mostSimilarWord(Set<String> goodEdits, String mostSimilarWord){
	System.out.println("This is most similar "+goodEdits.toString());
	for(String str:goodEdits){
		if(mostSimilarWord==null){
			mostSimilarWord = str.substring(0,str.length());
		}
//		if(trie.find(mostSimilarWord)==null){
//			return mostSimilarWord;
//		}
		if(trie.find(str).getValue()>trie.find(mostSimilarWord).getValue()){
			//System.out.println("String before "+ str);
			mostSimilarWord = str.substring(0);
			//System.out.println("String after "+ mostSimilarWord);
		}
		if(trie.find(str).getValue()==trie.find(mostSimilarWord).getValue()){
			if(str.compareTo(mostSimilarWord)<0){
				mostSimilarWord = str.substring(0,str.length());
			}
		}
	}

	return mostSimilarWord;
}
	

	public Set<String> copyDictionary(Set<String> dictionary){
		Set<String> copy = new HashSet<String>();
		for(String str:dictionary){
			copy.add(str);
		}
		return copy;
	}
	
public Set<String> editDistanceX(Set<String> wordsToEdit){
		Set<String> tempsuggestdictionary = new HashSet<String>();
		Set<String> originaldictionary= new HashSet<String>();
		Set<String> tempdictionary = new HashSet<String>();
		originaldictionary=copyDictionary(wordsToEdit);
//deletion	
		tempdictionary=deletion(originaldictionary);
		for(String str:tempdictionary){
			tempsuggestdictionary.add(str);
			}
//swap		
	tempdictionary=swap(originaldictionary);
		for(String str:tempdictionary){
			tempsuggestdictionary.add(str);
			}
//alteration		
	tempdictionary=alteration(originaldictionary);
		for(String str:tempdictionary){
			tempsuggestdictionary.add(str);
		}
//insertion
	tempdictionary=insertion(originaldictionary);	
		for(String str:tempdictionary){
			tempsuggestdictionary.add(str);
		}
		
		return tempsuggestdictionary;
	}
	
	public Set<String> deletion(Set<String> inputWords){
		Set<String> newdictionary = new HashSet<String>();
		
		for(String str:inputWords){
			for(int i = 0;i<str.length();i++){
				String newstring = new String(str.substring(0,i)+str.substring(i+1, str.length()));
				newdictionary.add(newstring);
			}
		}
		return newdictionary;
	}
	
	public Set<String> swap(Set<String> inputWords){

		Set<String> newdictionary = new HashSet<String>();
		for(String str:inputWords){
			for(int i=0;i<str.length()-1;i++){
				String newstring = new String(str.substring(0,i)+str.substring(i+1,i+2)+str.substring(i,i+1)+str.substring(i+2, str.length()));
				newdictionary.add(newstring);
				}
		}
		return newdictionary;
	}
	
	public Set<String> alteration(Set<String> inputWords){
		Set<String> newdictionary = new HashSet<String>();
		for(String str:inputWords){
			for(int i=0; i<str.length();i++){
				for(int j=0; j<26;j++){
					int k = j+'a';
					String newstring= new String(str.substring(0,i)+Character.toString((char)k)+str.substring(i+1, str.length()));
					newdictionary.add(newstring);
				}
			}
		}
		return newdictionary;
	}
	
	public Set<String> insertion(Set<String> inputWords){
		Set<String> newdictionary = new HashSet<String>();
		for(String str:inputWords){
			for(int i=0; i<str.length();i++){
				for(int j=0; j<26;j++){
					int k = j+'a';
					String newstring= new String(str.substring(0,i)+Character.toString((char)k)+str.substring(i, str.length()));
					newdictionary.add(newstring);
				}
			}
					for(int j=0; j<26;j++){
						int k = j+'a';
						String newstring= new String(str+Character.toString((char)k));
						newdictionary.add(newstring);
					}	
		}
		return newdictionary;
		}
		
}
