package controller;
import java.util.HashSet;

import model.MastermindModel;

/**
 * 
 * @author Aleksander Dimitrov
 * 
 * Description: This file is the 
 * controller of the MVC setup.
 * This means it deals with the 
 * functional data handling.
 */
public class MastermindController {
	// Instance fields
    private MastermindModel modelCopy;
    public MastermindModel model;
	/**
	 * This is the constructor for the controller.
	 * 
	 * It takes in a MastermindModel model. It creates 
	 * a controller which has the primary functions
	 * to run the Mastermind game and uses the model.
	 * 
	 * 
	 * @param model A MastermindModel object with the solution
	 */
	public MastermindController(MastermindModel model) {
		this.model = model;
	}
 
	/**
	 * Checks if the guess matches the secret answer
	 * stored in the model.
	 * 
	 * Takes in a String guess parameter. Checks if
	 * all the color characters in the guess are correct 
     * and in the right position and returns true or false. 
     * It will throw a MastermindIllegalColorException 
     * if it includes any letter that isn't one of:
	 * 'r', 'g', 'b', 'y', 'o', or 'p'. 
	 * It will throw a MastermindIllegalLengthException 
	 * if it doesn't have a length of exactly 4.
	 * 
	 * @param guess A String representing an attempt input by the user
	 * @return Whether the guess matches the solution in the model or not
	 * @throws MastermindIllegalColorException When the guess has an invalid color
	 * @throws MastermindIllegalLengthException When the guess is an invalid length
	 */
    public boolean isCorrect(String guess) throws MastermindIllegalColorException, MastermindIllegalLengthException {
    	checkExceptions(guess);
    	
    	if (getRightColorRightPlace(guess) == 4) {
    		return true;
    	} return false;
    }

    /**
     * Finds out how many colors from the guess
     * match the model's solution in both
     * color and position.
     * 
     * Takes in a String guess parameter. Checks if
     * each of the color characters in the guess are
     * correct and in the right position and returns
     * an integer describing how many are.
     * It will throw a MastermindIllegalColorException 
     * if it includes any letter that isn't one of:
	 * 'r', 'g', 'b', 'y', 'o', or 'p'. 
	 * It will throw a MastermindIllegalLengthException
	 * if it doesn't have a length of exactly 4.
     * 
     * @param guess A String representing an attempt input by the user
	 * @return Whether the guess matches the solution in the model or not
	 * @throws MastermindIllegalColorException When the guess has an invalid color
	 * @throws MastermindIllegalLengthException When the guess is an invalid length
     */
    public int getRightColorRightPlace(String guess) throws MastermindIllegalColorException, MastermindIllegalLengthException { 
    	checkExceptions(guess);
    	
    	int counter = 0;
    	for (int i = 0; i < 4; i++) {
    		if (guess.toCharArray()[i] == model.getColorAt(i)) {
    			counter++;
    		}
    	}
    	return counter;
    }
    
    /**
     * Finds out how many colors from the guess
     * match the model's solution in color but
     * not in position.
     * 
     * Takes in a String guess parameter. Returns an integer
     * describing how many color characters are the right 
     * color but in the wrong position by subtracting those 
     * of the right color and right place from those 
     * contained in the model.
     * It will throw a MastermindIllegalColorException 
     * if it includes any letter that isn't one of:
	 * 'r', 'g', 'b', 'y', 'o', or 'p'. 
	 * It will throw a MastermindIllegalLengthException
	 * if it doesn't have a length of exactly 4.
     * 
     * @param guess A String representing an attempt input by the user
	 * @return Whether the guess matches the solution in the model or not
	 * @throws MastermindIllegalColorException When the guess has an invalid color
	 * @throws MastermindIllegalLengthException When the guess is an invalid length
     */
    public int getRightColorWrongPlace(String guess) throws MastermindIllegalColorException, MastermindIllegalLengthException {
    	checkExceptions(guess);
    	
    	int rcrp = getRightColorRightPlace(guess);
    	int counter = 0;
    	String guess1 = "";
    	String guess2 = "";
    	
    	// makes a copy of the model to avoid overwriting the original
    	modelCopy = new MastermindModel(model);

    	for (int i = 0; i < 4; i++) {
    		for (int j = 0; j < 4; j++) {
    			// checks for the right color right place for a column
    			// and replaces the seen colors with '.' characters
    			// to avoid double counting, incrementing the counter
    			if (modelCopy.getCopy()[i] == guess.toCharArray()[i] && guess.toCharArray()[i] != '.') {
    				modelCopy.getCopy()[i] = '.';
    				guess1 = guess.substring(0, i);
    				guess2 = guess.substring(i+1);
    				guess = guess1 + "." + guess2;
    				counter++;
        		}
    			
    			// checks for the right color wrong place for the
    			// current positions by seeing if any characters
    			// in the guess are also in the model and replaces
    			// the seen colors with '.' characters to avoid 
    			// double counting, incrementing the counter
				if (modelCopy.getCopy()[i] == guess.toCharArray()[j] && guess.toCharArray()[j] != '.') {
					modelCopy.getCopy()[i] = '.';
					guess1 = guess.substring(0, j);
    				guess2 = guess.substring(j+1);
    				guess = guess1 + "." + guess2;
	    			counter++;
	    		}
    		}
    	}
    	// subtracts the right color right place count from the total count (rcrp + rcwp)
    	return counter-rcrp; 	
    }
    
    /**
     * Checks for exceptions in the colors and lengths allowed.
     * 
     * Takes in a String guess. Returns nothing.
     * It will throw a MastermindIllegalColorException 
     * if it includes any letter that isn't one of:
	 * 'r', 'g', 'b', 'y', 'o', or 'p'. 
	 * It will throw a MastermindIllegalLengthException
	 * if it doesn't have a length of exactly 4.
     * 
     * @param guess A String representing an attempt input by the user
	 * @throws MastermindIllegalColorException When the guess has an invalid color
	 * @throws MastermindIllegalLengthException When the guess is an invalid length
     */
    public void checkExceptions(String guess) throws MastermindIllegalColorException, MastermindIllegalLengthException {
    	if (guess.length() != 4) {
    		throw new MastermindIllegalLengthException("Guess had an invalid length\n"
    				+ "(Your guess must be 4 characters long)");
    	}
    	
    	// creates a set of the legal colors
    	HashSet<Character> colorChoices = new HashSet<>();
		colorChoices.add('r');
		colorChoices.add('g');
		colorChoices.add('b');
		colorChoices.add('p');
		colorChoices.add('y');
		colorChoices.add('o');
		
		// checks if any of the characters in guess aren't in the set of legal colors
    	for (int i = 0; i<4; i++) {			
			if (!colorChoices.contains(guess.toCharArray()[i])) {
				throw new MastermindIllegalColorException("One or more guess colors were invalid.\n"
						+ "(The valid colors are: 'r', 'g', 'b', 'y', 'p', 'o').");
			}
		}
    }
}