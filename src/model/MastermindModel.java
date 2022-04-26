package model;

import java.util.Observable;
import java.util.Random;

import javafx.scene.control.Button;

/**
 * 
 * @author Aleksander Dimitrov
 * 
 * Description: This file is the model
 * of the MVC setup. This means it deals
 * with the internal data structures
 * for MasterMind.
 *
 */
public class MastermindModel extends Observable {
	// Instance fields
	public char[] solution;
	private char[] copy;
	public boolean winState;
	public boolean loseState;
	public int gameOverVersion;
	
	/**
	 * This is the main constructor for the model.
	 * 
	 * It takes in no parameters. It stores a
	 * solution as a character array of 
	 * 4 random colors out of 6 available ones 
	 * (allowing duplicates).
	 */
    public MastermindModel() { 
    	solution = new char[4];
    	
		String colorChoices = "rgbypo";
		Random rand1 = new Random();
		
		for(int i = 0; i<4; i++) {
			// selects a random char out of the 6
			char ansChar = colorChoices.charAt(rand1.nextInt(6));
			solution[i] = ansChar;
		}
    	
		winState = false;
		loseState = false;
		gameOverVersion = 0;
    }
    
    /**
     * This method is a special constructor to allow us to use JUnit to test our model.
     * 
     * Instead of creating a random solution, it allows us to set the solution from a 
     * String parameter.
     * 
     * 
     * @param answer A string that represents the four color solution
     */
    public MastermindModel(String answer) {
    	solution = answer.toCharArray();
    }
    
    /**
     * This constructor is intended for testing a
     * predefined input. It takes in a MastermindModel
     * model and creates a deep copy of it to avoid
     * overwriting it when doing the RCWP function.
     * 
     * 
     * @param model A MastermindModel model with the solution
     */
    public MastermindModel(MastermindModel model) { 
    	this.setCopy(new char[4]);
    	
    	for (int i=0; i<model.solution.length; i++) {
    		this.getCopy()[i] = model.solution[i];
    	}
    }

    /**
     * Get the color at the position index as a character.
     * 
     * Takes in an integer index parameter. Returns
     * the character at that position in the answer.
     * 
     *
     * @param index An index at which to retrieve a character
     * @return The character in the model's solution
     */
    public char getColorAt(int index) {	
    	return solution[index];
    }
    
    /**
     * Get the copy solution.
     * 
     * @return A copy of the model
     */
	public char[] getCopy() {
		return copy;
	}
	
	/**
	 * Set the copy solution.
	 * 
	 * @param copy A char[] solution to set the copy to
	 */
	public void setCopy(char[] copy) {
		this.copy = copy;
	}
	
	public String toString() {
		String ansStr = "";
		for (int i = 0; i < 4; i++) {
			ansStr += this.solution[i];
		}
		return ansStr;
	}
	
	public void updateGameOver(boolean correct, int guessCount, Button guessButton2) {		
	    if (correct) {
	    	winState = true;
	    	gameOverVersion = 1;
			setChanged();
			notifyObservers(gameOverVersion); // calls update
    	} else if (!correct && guessCount >= 10) {
    		gameOverVersion = 2;
    		setChanged();
			notifyObservers(gameOverVersion); // calls update
    	}
	}
	
    
}