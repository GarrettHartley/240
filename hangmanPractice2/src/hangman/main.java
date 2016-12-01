package hangman;

import hangman.IEvilHangmanGame.GuessAlreadyMadeException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeSet;

public class main {
	
	
	
	public static boolean validInput(String guess){
		if(guess.length()==1&&Character.isLetter(guess.charAt(0))==true){
			return true;
		}
		 return false;
	}

	public static void main(String[] args) {
	
		File dictionary = new File(args[0]);
		int wordLength=Integer.parseInt(args[1]);
		int guesses = Integer.parseInt(args[2]);
		
		//Set<String> guessL = new TreeSet<String>();
		EvilHangmanGame game = new EvilHangmanGame();
		game.startGame(dictionary,wordLength);
		
		
		
		while(guesses>0){
//Begin turn
			System.out.println("Guesses Remaining: "+guesses);
			System.out.print("Guessed so far: ");
			for(String c:game.getGuessed()){
				System.out.print(c+" ");
			}
			System.out.println();
			System.out.println(game.getCurrW());
		//	System.out.print()
			
			
			String guess = new String();
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			
			try {
				guess = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			try {
				game.makeGuess(guess.charAt(0));
			} catch (GuessAlreadyMadeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			guesses--;
		}
		
		

	}

}
