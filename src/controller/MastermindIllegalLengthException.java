package controller;

/**
 * 
 * @author Aleksander Dimitrov
 * 
 * Description: This file creates a custom exception
 * which handles the case that the guess has an invalid
 * length (one not equal to 4).
 *
 */
public class MastermindIllegalLengthException extends Exception {

	private static final long serialVersionUID = 1L;
	
	/**
	 * This passes in an error message to the general Exception class.
	 * 
	 * @param errorMsg
	 */
	public MastermindIllegalLengthException(String errorMsg) {
		super(errorMsg);
	}
}
