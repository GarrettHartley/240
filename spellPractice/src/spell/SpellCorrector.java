package spell;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class SpellCorrector implements ISpellCorrector {
	Trie dictionary;
	Set<String> suggestWords = new HashSet<String>();
	
	public SpellCorrector(){
		dictionary = new Trie();
	}
	
	
	@Override
	public void useDictionary(String dictionaryFileName) throws IOException {
		Scanner scanner = null;
		scanner = new Scanner(new File(dictionaryFileName));
		while(scanner.hasNext()){
			String str = new String(scanner.next().toLowerCase());
			dictionary.add(str);
			suggestWords.add(str);
		}

		
	}
	
	public Trie getDictionary(){
		return dictionary;
	}
	
	public Set<String> addToMe(Set<String> add,Set<String> me){
		for(String str: add){
			me.add(str);
		}
		return me;
	}
	
	public Set<String> deletion(Set<String> tempWords){
		Set<String> newSet = new HashSet<String>();
		for(String str:tempWords){
			for(int i=0;i<str.length();i++){
			String newStr = new String(str);
			newStr = new String( newStr.substring(0,i)+newStr.substring(i+1));
			newSet.add(newStr);
			}
		}
		return newSet;
	}
	
	public Set<String> swap(Set<String> tempWords){
		Set<String> newSet = new HashSet<String>();
		for(String str:tempWords){
			for(int i = 0; i<str.length()-1;i++){
				String newStr = new String(str);
				newStr = newStr.substring(0,i)+newStr.substring(i+1,i+2)+newStr.substring(i,i+1)+newStr.substring(i+2);
			newSet.add(newStr);
			}
		}
		
		
		return newSet;
	}
	
	public Set<String> alteration(Set<String> tempWords){
		
		Set<String> newSet = new HashSet<String>();
		for(String str:tempWords){
			for(int i = 0; i<str.length();i++){
				for(int j = 0;j<26;j++){
					int k = j+'a';
					String newStr = new String(str);
					newStr= newStr.substring(0,i)+Character.toString((char)k)+newStr.substring(i+1);
					newSet.add(newStr);
				}
			}
		}
		return newSet;
	}
	
	public Set<String> insertion(Set<String> tempWords){
		
		Set<String> newSet = new HashSet<String>();
		for(String str:tempWords){
			for(int i = 0; i<str.length();i++){
				for(int j = 0;j<26;j++){
					int k = j+'a';
					String newStr = new String(str);
					newStr= newStr.substring(0,i)+Character.toString((char)k)+newStr.substring(i);
					newSet.add(newStr);
				}
			}
		}
		for(int i = 0;i<26;i++){
			int k = i+'a';	
				for(String str:tempWords){
					String newStr = new String(str);
					newStr = newStr.substring(0)+Character.toString((char)k);
					newSet.add(newStr);
			}
		}
		
		return newSet;
	}
	
	public Set<String> edit(Set<String> newSwords){
		Set<String> temp = new HashSet<String>();
		//deletion
		temp=addToMe(deletion(newSwords),temp);	
		//swap
		temp= addToMe(swap(newSwords),temp);
		//alteration
		temp = addToMe(alteration(newSwords),temp);
		//insert
		temp = addToMe(insertion(newSwords),temp);
		return temp;
	}
	

	@Override
	public String suggestSimilarWord(String inputWord) throws NoSimilarWordFoundException {
		//System.out.println(dictionary.find(inputWord));
		System.out.print(dictionary.toString());
		Set<String> newSwords= new HashSet<String>();
		newSwords.add(inputWord);
		Set<String> editDistanceOne = new HashSet<String>();
		editDistanceOne = edit(newSwords);
		
		
		
		
		System.out.print("This is in editDistance one ");
		for(String str:editDistanceOne){
		System.out.print(" "+str);
		}
			
		
		
		
		return null;
	}

}
