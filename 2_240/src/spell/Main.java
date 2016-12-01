package spell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import spell.ISpellCorrector.NoSimilarWordFoundException;

/**
 * A simple main class for running the spelling corrector. This class is not
 * used by the passoff program.
 */
public class Main {

	/**
	 * Give the dictionary file name as the first argument and the word to
	 * correct as the second argument.
	 */
	public static void main(String[] args) throws NoSimilarWordFoundException,
			IOException {

		String dictionaryFileName = args[0];
		//String dictionaryFileName2 = args[0];
		SpellCorrector spC1 = new SpellCorrector();
		//SpellCorrector spC2 = new SpellCorrector();
		System.out.println(dictionaryFileName);
		spC1.useDictionary(dictionaryFileName);
		//spC2.useDictionary(dictionaryFileName2);

//		if (spC1.getTrie().equals(spC2.getTrie()) == true) {
//			System.out.println("Tries are equal");
//		} else {
//			System.out.println("Tries are not equal");
//		}
		 String inputWord = args[1];
		String foundword = new String();
		foundword = spC1.suggestSimilarWord(inputWord);
		System.out.println("found word " + foundword);
		System.out.println("Node count "+spC1.getTrie().getNodeCount());
		System.out.println("Word count "+spC1.getTrie().getWordCount());

	
		// Scanner scanner = null;
		// try {
		// scanner = new Scanner(new File(args[0]));
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }
		// Trie trie = new Trie();
		// while(scanner.hasNext()){
		// String word = scanner.next();
		// trie.add(word);
		// //String dictionary = trie.toString();
		// System.out.println("This is toString "+trie.toString());
		// System.out.println("Did I find it? "+trie.find("abcd"));
		// System.out.println("Node count "+trie.getNodeCount());
		// System.out.println("Word count "+trie.getWordCount());
		// //trie.find("the");
		// //System.out.println("This is toString "+trie.toString());
		// }

		/**
		 * Create an instance of your corrector here
		 */
		// ISpellCorrector corrector = null;
		//
		// corrector.useDictionary(dictionaryFileName);
		// String suggestion = corrector.suggestSimilarWord(inputWord);
		//
		// System.out.println("Suggestion is: " + suggestion);
	}

}
