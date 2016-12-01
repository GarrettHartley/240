package client.gui.indexerWindow.tableEntry.spellChecker;

import java.io.IOException;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String dictionaryFile = new String(args[0]);
		String input = new String(args[1]);
		
		SpellCorrector game = new SpellCorrector();
		try {
			game.useDictionary(dictionaryFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("This is to string "+game.getDictionary().toString());
		try {
			System.out.println("Found word "+game.suggestSimilarWord(input));
		} catch (client.gui.indexerWindow.tableEntry.spellChecker.ISpellCorrector.NoSimilarWordFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
