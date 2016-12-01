package hangman;

import hangman.IEvilHangmanGame.GuessAlreadyMadeException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class main {
	
	
	public static boolean validGuess(String guess){
		if(guess.length()>1){
			return false;
		}
		if(Character.isLetter(guess.charAt(0))==false){
			return false;
		}
		
		return true;
	}

	public static void main(String[] args) {
		
		File dictionary = new File(args[0]);
		int wordLength = Integer.parseInt(args[1]);
		int guesses = Integer.parseInt(args[2]);
		Set<String> guessed = new TreeSet<String>();
		String guess= new String();
		String _ = "_";
		EvilHangmanGame game = new EvilHangmanGame();
		game.startGame(dictionary,wordLength);
		
		
		while(guesses>0){
	//begin turn
			if(game.currW.contains(_)==false){
				System.out.println("You Won! the word is"+game.getWord());
			}
			
			
		System.out.println("Guesses remaining:"+guesses);
		System.out.print("Guessed so far:");
		System.out.println(guessed.toString());
		System.out.println("Guess a letter");
		System.out.println(game.currW);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			guess = br.readLine().toLowerCase();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		while(false == validGuess(guess)|guessed.contains(guess)==true){
			
			
			if(guessed.contains(guess)==true){
				System.out.println("Already guessed. Guess again");
				BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
				try {
					guess = br1.readLine().toLowerCase();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			else if(validGuess(guess)==false){
				System.out.println("Invalid guess. Guess again");
				BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
				try {
					guess = br2.readLine().toLowerCase();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
		}
		
		guessed.add(guess);
		try {
			System.out.println(game.makeGuess(guess.charAt(0)).toString());
		} catch (GuessAlreadyMadeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		guesses--;
		}
		
		System.out.println("you lose");
		

	}

}
