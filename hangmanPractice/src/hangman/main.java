package hangman;

import hangman.IEvilHangmanGame.GuessAlreadyMadeException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.TreeSet;

public class main {

	public static boolean validGuess(String guess){
		if(guess.length()==1&&Character.isLetter(guess.charAt(0))){
			return true;
		}
		return false;
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		File dictionary = new File(args[0]);
		int wordLength = Integer.parseInt(args[1]);
		int guesses = Integer.parseInt(args[2]);
		Set<String> alreadyGuessed= new TreeSet<String>();
		EvilHangmanGame game = new EvilHangmanGame();
		game.startGame(dictionary, wordLength);
		Set<String> currentDictionary = new TreeSet<String>();
		//String currentKey = new String();
		
		
		while(guesses>0){

			BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
			String guess= null;
			System.out.println("Guesses remaining: "+guesses);
			System.out.println("Guesses so far: ");
			for(String guesStr: alreadyGuessed){
				System.out.print(guesStr+" ");
			}
			System.out.println(game.getCurW());
	
			try {
				guess = new String(br.readLine().toLowerCase());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				game.makeGuess(guess.charAt(0));
			} catch (GuessAlreadyMadeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
			
			while(validGuess(guess)==false|alreadyGuessed.contains(guess)==true){
			
				if(validGuess(guess)==false){
				System.out.println("Invalid Input. Guess again ");
				try {
					guess = new String(br.readLine().toLowerCase());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
				else{
					System.out.println("You already guessed that letter. Guess again ");
					try {
						guess = new String(br.readLine());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			try {
			currentDictionary =	game.makeGuess(guess.charAt(0));
			} catch (GuessAlreadyMadeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(game.getCurW().contains("_")==false){
				break;
			}

			alreadyGuessed.add(guess);
			if(game.getCurW().contains(guess)==false){
			guesses--;
			}
		}
		
		if(game.getCurW().contains("_")==false){
			System.out.println("You won! the word is:"+currentDictionary.iterator().next());
		}
		else{
			System.out.println("Sorry you lost");
		}
		
		
		
	}

}