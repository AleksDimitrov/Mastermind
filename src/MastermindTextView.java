import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import controller.MastermindController;
import controller.MastermindIllegalColorException;
import controller.MastermindIllegalLengthException;
import model.MastermindModel;

public class MastermindTextView implements Observer {
	public void run() {
		System.out.println("Welcome to Mastermind!");
		System.out.println("Type play to start, or quit to end.");
		boolean quit = false;
		boolean countUp = false;
		boolean firstGame = true;
		boolean newControl = true;
		int counter = 1;
		
		Scanner stream = new Scanner(System.in);
		String input = stream.nextLine();
		
		MastermindController control = new MastermindController(new MastermindModel());
		
		while (!quit) {
			if (!firstGame && newControl && input.equals("yes")) {
				control = new MastermindController(new MastermindModel());
				newControl = false;
			}
			if ((firstGame && input.equals("play")) || (!firstGame && input.equals("yes"))) {
				System.out.println("\nInput a guess:");
				String guess = stream.nextLine();
				System.out.println();
				// this does a try/ catch block to check if isCorrect
				// throws an exception due to the guess having an invalid 
				// length or color, to determine guess validity overall
				try {
					boolean correct = control.isCorrect(guess);
					if (correct) {
						System.out.println("You Win!\n");
						System.out.println("Play again?");
						firstGame = false;
						newControl = true;
						counter = 1;
						input = stream.nextLine();
					} else if (!correct && counter == 10) {
						System.out.println("You Lose.");
						System.out.println("Solution: " + control.model);
						System.out.println("\nPlay again?");
						firstGame = false;
						newControl = true;
						counter = 1;
						input = stream.nextLine();						
					} else {
						System.out.println("Try again");
						System.out.println("Pegs that are placed correctly: " + 
								control.getRightColorRightPlace(guess));
						System.out.println("Pegs of the right color but in the wrong spot: " + 
								control.getRightColorWrongPlace(guess));
						counter++;
					}
				} catch (MastermindIllegalColorException errorColor) {
					System.out.println(errorColor.getMessage());
					System.out.println("Try again");
					continue;
				} catch (MastermindIllegalLengthException errorLength) {
					System.out.println(errorLength.getMessage());
					System.out.println("Try again");
					continue;
				}
			} else if ((firstGame && input.equals("quit")) || (!firstGame && input.equals("no"))){
				System.out.println("Thanks for playing!");
				System.exit(0);
			} else {
				System.out.println("I didn't quite get that.");
				input = stream.nextLine();
			}
	
	}
		stream.close();
	}

	@Override
	public void update(Observable o, Object arg) {
	}

}
