package controller;

/**
 * 
 * @author Aleksander Dimitrov
 * 
 * Description: This file creates a custom exception
 * which handles the case that the guess has any invalid
 * characters (any that are not 'r', 'g', 'b', 'y', 'o', or 'p').
 *
 */
public class MastermindIllegalColorException extends Exception {

	private static final long serialVersionUID = 1L;
	
	/**
	 * This passes in an error message to the general Exception class.
	 * 
	 * @param errorMsg
	 */
	public MastermindIllegalColorException(String errorMsg) {
		super(errorMsg);
	}
}
