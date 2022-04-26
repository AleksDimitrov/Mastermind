import java.util.Scanner;

import controller.MastermindController;
import controller.MastermindIllegalColorException;
import controller.MastermindIllegalLengthException;
import javafx.application.Application;
import model.MastermindModel;

/**
 * 
 * @author Aleksander Dimitrov
 * 
 * Description: This file acts as the view
 * in the MVC setup. This means that it
 * deals with the I/O of the Mastermind
 * program. Mastermind is a game where
 * the user attempts to guess an answer
 * of 4 pegs of specific colors (out
 * of 6 possible colors), where
 * duplicates are allowed. The answer
 * is randomized.
 *
 */
public class Mastermind {
	/**
	 * The view which the user interacts with.
	 * 
	 * @param args The command arguments
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		Application.launch(MastermindGUIView.class, args);

	}
}