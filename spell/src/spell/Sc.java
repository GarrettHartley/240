package spell;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Sc implements ISpellCorrector {
	private static final Exception NoSimilarWordFoundException = null;
	Set<String> inDictionary;
	Trie dictionary;

	public Sc() {
		inDictionary = new HashSet<String>();
		dictionary = new Trie();
	}

	public Trie getTrie() {
		return dictionary;
	}

	@Override
	public void useDictionary(String dictionaryFileName) throws IOException {
		Scanner scanner = null;
		scanner = new Scanner(new FileReader(new File(dictionaryFileName)));

		while (scanner.hasNext()) {
			String inputWord = new String(scanner.next().toLowerCase());
			inDictionary.add(inputWord);
			dictionary.add(inputWord);
			dictionary.find(inputWord);
			// System.out.println(inputWord);
		}

	}

	public Set<String> addToMe(Set<String> add, Set<String> me) {
		for (String str : add) {
			me.add(str);
		}
		return me;
	}

	public Set<String> deletion(Set<String> words) {
		Set<String> newWords = new HashSet<String>();
		for (String str : words) {
			for (int i = 0; i < str.length(); i++) {
				String newStr = new String(str);
				newStr = newStr.substring(0, i) + newStr.substring(i + 1);
				newWords.add(newStr);
			}
		}

		return newWords;
	}

	public Set<String> swap(Set<String> words) {
		Set<String> newWords = new HashSet<String>();
		for (String str : words) {
			for (int i = 0; i < str.length()-1; i++) {
				String newStr = new String(str);
				newStr = newStr.substring(0, i)
						+ newStr.substring(i + 1, i + 2)
						+ newStr.substring(i, i + 1) + newStr.substring(i + 2);
				newWords.add(newStr);
			}
		}
		return newWords;
	}

	public Set<String> insertion(Set<String> words) {
		Set<String> newWords = new HashSet<String>();
		for (String str : words) {
			for (int i = 0; i < str.length(); i++) {
				for (int j = 0; j < 26; j++) {
					int k = j + 'a';
					String newStr = new String(str);
					newStr = newStr.substring(0, i)
							+ Character.toString((char) k)
							+ newStr.substring(i);
					newWords.add(newStr);
				}
			}
			for (int j = 0; j < 26; j++) {
			int k = j+'a';
			String newStr = new String(str);
			newStr = newStr.substring(0)+Character.toString((char) k);
			newWords.add(newStr);
			}
			
		}

		return newWords;
	}

	public Set<String> alteration(Set<String> words) {
		Set<String> newWords = new HashSet<String>();
		for (String str : words) {
			for (int i = 0; i < str.length(); i++) {
				for (int j = 0; j < 26; j++) {
					int k = j + 'a';
					String newStr = new String(str);
					newStr = newStr.substring(0, i)
							+ Character.toString((char) k)
							+ newStr.substring(i + 1);
					newWords.add(newStr);
				}
			}
		}

		return newWords;
	}

	public Set<String> edit(Set<String> edit) {
		Set<String> newedits = new HashSet<String>();
		// deletion
		newedits = addToMe(deletion(edit),newedits);
		//System.out.println(newedits.toString()+"haey");

		// swap
		newedits = addToMe(swap(edit),newedits);
		// alteration
		newedits = addToMe(alteration(edit),newedits);
		// insertion
		newedits = addToMe(insertion(edit),newedits);

		//System.out.println(newedits.size());
		return newedits;
	}
	
	public Set<String> getGoodEdits(Set<String> checkme){
		Set<String> goodEdits = new HashSet<String>();
		for(String str: checkme){
			if(dictionary.find(str)!=null){
				//System.out.println("found " + str);
				goodEdits.add(str);
			}
		}
		return goodEdits;
	}
	
	public String MostSimilarWord(Set<String> words){
		String mostsimilar = new String();
		mostsimilar = null;
		for(String str: words){
			if(mostsimilar==null){
				mostsimilar = str;
			}
			if(dictionary.find(str).getValue()>dictionary.find(mostsimilar).getValue()){
				mostsimilar = str;
			}
			else if(dictionary.find(str).getValue()==dictionary.find(mostsimilar).getValue()){
				if(str.compareTo(mostsimilar)<0){
					mostsimilar = str;
				}
			}
		}
		return mostsimilar;
	}

	@Override
	public String suggestSimilarWord(String inputWord)throws NoSimilarWordFoundException {

		inputWord= inputWord.toLowerCase();
		Set<String> editOne = new HashSet<String>();
		editOne.add(inputWord);
		
		editOne = edit(editOne);
		Set<String> goodEditsOne = new HashSet<String>();
		goodEditsOne=getGoodEdits(editOne);
		
		if(MostSimilarWord(goodEditsOne)!=null){
			return MostSimilarWord(goodEditsOne);
		}
		
		Set<String> editTwo = new HashSet<String>();
		
		editTwo = edit(editOne);
		editTwo = getGoodEdits(editTwo);
		
		if(MostSimilarWord(editTwo)!=null){
			return MostSimilarWord(editTwo);
		}
		System.out.println("Word not found threw exception");
		throw new NoSimilarWordFoundException();
	}

}
