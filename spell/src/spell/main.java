package spell;

import java.io.File;
import java.io.IOException;

import spell.ISpellCorrector.NoSimilarWordFoundException;

public class main {

	public static void main(String[] args) {
		String dictionary = new String(args[0]);
		String inputword = new String(args[1]);
		Sc game = new Sc();
		
		try {
			game.useDictionary(dictionary);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		System.out.println("This is toSTring");
//		System.out.print(game.getTrie().toString());
//		System.out.print(game.getTrie().getNodeCount());
		try {
			String output = game.suggestSimilarWord(inputword);
			System.out.println("found "+output);
		} catch (NoSimilarWordFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
