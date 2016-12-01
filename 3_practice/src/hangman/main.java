package hangman;

import hangman.IEvilHangmanGame.GuessAlreadyMadeException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.TreeSet;

class main {

	public static boolean validInput(String input, Set<String> guessedLetters) {

		if (input.length() == 1 && Character.isLetter(input.charAt(0)) != false
				&& guessedLetters.contains(input) == false) {
			return true;
		}
		return false;

	}

	public static void main(String[] args) {
		EvilHangmanGame game = new EvilHangmanGame();
		File dictionary = new File(args[0]);
		int wordLength = Integer.parseInt(args[1]);
		int guesses = Integer.parseInt(args[2]);
		game.startGame(dictionary, wordLength);
		Set<String> guessedLetters = new TreeSet<String>();
// Start new turn
		while (guesses != 0) {
			System.out.println("Remaining guesses: " + guesses);
			System.out.print("Guessed letters: ");
			for (String s : guessedLetters) {
				System.out.print(s + " ");
			}
			System.out.println("");

	// Get Valid Guess
			System.out.print("Please guess a letter ");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String input = new String();
			try {
				input = br.readLine();
				input = input.toLowerCase();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			while (validInput(input, guessedLetters) == false) {
				if (guessedLetters.contains(input) == true) {
					System.out
							.println("You already used that letter. Please guess again ");
					try {
						input = br.readLine();
						input = input.toLowerCase();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					System.out.print("Invalid input. Please guess again ");
					try {
						input = br.readLine();
						input = input.toLowerCase();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
	// end get Valid Guess		
			guessedLetters.add(input);
			
			Set<String> newDictionary = new TreeSet<String>();
			try {
				newDictionary = game.makeGuess(input.charAt(0));
			} catch (GuessAlreadyMadeException e) {
				e.printStackTrace();
			}

	//get count of guess in current word		
			int countGuess = 0;
			for (char c : game.getCurrentWord().toCharArray()) {
				if (c == input.charAt(0)) {
					countGuess++;
				}
			}
	// done get count	
			
			if (game.getCurrentWord().contains("_") == false) {
				System.out.println("You won! The word is " + newDictionary.iterator().next());
				break;
			}

			if (countGuess != 0) {
				System.out.println("yes, there are " + countGuess + " "+ input.charAt(0) + "s");
				System.out.println(game.getCurrentWord());
			}
			else {
				guesses--;
				System.out.println(game.getCurrentWord());
				System.out.println("Sorry there were no " + input.charAt(0)
						+ " Please guess again");
			}

	// End Turn
		}
	}

}