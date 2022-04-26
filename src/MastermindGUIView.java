import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Observable;
import java.util.Observer;

import controller.MastermindController;
import controller.MastermindIllegalColorException;
import controller.MastermindIllegalLengthException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.MastermindModel;

/**
 * 
 * @author Aleksander Dimitrov
 * 
 * Description: This class represents the graphical user interface version
 * of the Mastermind game. It can be invoked with the arguments -window.
 * It follows the same rules as the text-based version, with some changes.
 * It uses JavaFx to display a stage with a scene of panes that hold the 
 * necessary buttons, circles, and more. There are 4 circles on the bottom 
 * that can be clicked until the desired color is chosen. When the guess
 * button is clicked, it sets up a row with the guess number, the chosen
 * colors, and the right color right place and right color wrong place pegs.
 * If the user wins, loses, or leaves pegs black, a modal alert shows up
 * displaying information about the event. When the game is over, the
 * window is disabled to prevent further guesses.
 */

public class MastermindGUIView extends javafx.application.Application implements Observer {
	
	//Instance variables (UI controls: buttons, labels, text, etc)
	private Button guessButton2;
	private int guessCount;
	private char[] guessArray;
	private String guessStr;
	private int rowNum;
	private static Circle[] pegArrayGuess;
	private static Circle[] pegArrayChecks;
	private static HashMap<Character, String> colors;
	private static char[] solution;
	private int pegNum;
	private Circle curPeg;
	private boolean gameOver;
	private GridPane checks;
	private static Stage mainStage;
		
	/**
	 * This is the constructor.
	 * It sets up the fields for the GUI view.
	 */
	public MastermindGUIView() {
		guessButton2 = new Button("Guess");
		guessCount = 1;
		guessArray = new char[4];
		guessStr = "....";
		pegArrayGuess = new Circle[4];
		pegArrayChecks = new Circle[4];
		colors = new LinkedHashMap();
		solution = new char[4];
		pegNum = 0;
		curPeg = new Circle(10,10,10);
		gameOver = false;
		checks = new GridPane();
		mainStage = null;
	}
	
	@Override
	/**
	 * This sets up the window and determines what
	 * it should like, taking button clicks into
	 * consideration. The stage holds a scene
	 * with a pane, and in that pane there is a
	 * Vbox with more panes.
	 */
	public void start(Stage stage) throws Exception {
		mainStage = stage;
		
		rowNum = 1;
		
		MastermindModel model = new MastermindModel();
		model.addObserver(this);
		
		solution = model.solution;

		MastermindController control = new MastermindController(model);
		
		Observable watch = new Observable();
		BorderPane root = new BorderPane();
	    VBox center = new VBox();
		GridPane lastCol2 = new GridPane();
		
	    // bottom pegs
	    Circle peg0 = new Circle();
		peg0.setRadius(20);
		pegArrayGuess[0] = peg0;
		Circle peg1 = new Circle();
		peg1.setRadius(20);
		pegArrayGuess[1] = peg1;
		Circle peg2 = new Circle();
		peg2.setRadius(20);
		pegArrayGuess[2] = peg2;
		Circle peg3 = new Circle();
		peg3.setRadius(20);
		pegArrayGuess[3] = peg3;
		
		makeLowerPane(root, guessButton2, pegArrayGuess);
		
		peg0.setId("0"); // allows the bottom pegs to be referred back to
		peg1.setId("1");
		peg2.setId("2");
		peg3.setId("3");
		
		colorCircles(peg0); // cycles through the colors on the bottom as the user clicks pegs
		colorCircles(peg1);
		colorCircles(peg2);
		colorCircles(peg3);

		// when the guess button is clicked
		guessButton2.setOnAction((event) -> { 
			try {
				// setup a string with a copy of the guess
				String guessStrCopy = "";
				for (int i=0; i<guessArray.length; i++) {
					guessStrCopy += guessArray[i];
				}
				
				boolean correct = control.isCorrect(guessStrCopy);
				
				model.updateGameOver(correct, guessCount, guessButton2);
				if (gameOver) {
					gameOver = false;
					root.setDisable(true);
				}
				
				// right color right place (black circs) & right color wrong place (white circs)
				int rcrp = control.getRightColorRightPlace(guessStrCopy);
				int rcwp = control.getRightColorWrongPlace(guessStrCopy);
				
				// build the 2x2 grid of feedback
				GridPane checks = new GridPane();
				GridPane newChecks1 = makeChecks(0, rcrp, Color.BLACK, control, guessStrCopy, checks);
				GridPane newChecks2 = makeChecks(rcrp, rcrp+rcwp, Color.WHITE, control, guessStrCopy, checks);
				
				// build the row of info
				makeRow(pegArrayGuess, newChecks2, center);
				
				guessCount++;
				rowNum++;
				
				// reset guess array
				guessArray = new char[4];
				
				// reset pegs to default (black)
				peg0.setFill(Color.BLACK);
				peg1.setFill(Color.BLACK);
				peg2.setFill(Color.BLACK);
				peg3.setFill(Color.BLACK);
			} catch (MastermindIllegalColorException e) { // if there are any pegs set to default (black) show alert (invalid)
				Alert a = new Alert(Alert.AlertType.WARNING);
				a.setTitle("Warning");
				a.setContentText("You must pick 4 colors (don't leave any black)");
				a.setHeaderText("Invalid Color Settings");
				a.showAndWait();
				
				// reset guess array
				guessArray = new char[4];
				
				// reset pegs to default (black)
				peg0.setFill(Color.BLACK);
				peg1.setFill(Color.BLACK);
				peg2.setFill(Color.BLACK);
				peg3.setFill(Color.BLACK);
			} catch (MastermindIllegalLengthException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		center.setSpacing(10);
		
		center.setStyle("-fx-background-color: tan");
		center.setPadding(new Insets(10)); // sets the top indent from the border
		
		center.setAlignment(Pos.TOP_CENTER);
		root.setCenter(center);

	    Scene scene = new Scene(root);
	    stage.setScene(scene);
	    stage.setTitle("Mastermind");
	    stage.setWidth(400);
	    stage.setHeight(600);
	    stage.show();
	}
	
	/**
	 * This function creates the lower pane for guessing by the user clicking pegs.
	 * @param root the pane for layout
	 * @param guessButton2 the button for guessing
	 * @param pegArrayGuess an array of pegs from the bottom
	 */
	public void makeLowerPane(BorderPane root, Button guessButton2, Circle[] pegArrayGuess) {
		GridPane bottom = new GridPane();
		Circle peg0 = new Circle();
		peg0.setRadius(20.0f);
		Circle peg1 = new Circle();
		peg1.setRadius(20.0f);
		Circle peg2 = new Circle();
		peg2.setRadius(20.0f);
		Circle peg3 = new Circle();
		peg3.setRadius(20.0f);
		GridPane lastCol2 = new GridPane();
		
		bottom.add(pegArrayGuess[0], 0, 0);
		bottom.add(pegArrayGuess[1], 1, 0);
		bottom.add(pegArrayGuess[2], 2, 0);
		lastCol2.setHgap(20);
		lastCol2.add(pegArrayGuess[3], 0, 0);
		lastCol2.add(guessButton2, 1, 0);
		bottom.add(lastCol2, 3, 0);
		
		bottom.setAlignment(Pos.CENTER);
		bottom.setPadding(new Insets(5));
		bottom.setHgap(35);
		root.setBottom(bottom);
	}
	
	/**
	 * This function cycles through to the next color
	 * of available colors each time the pegs each time the user
	 * clicks a peg.
	 * @param peg a Circle object from the bottom pane
	 */
	public void colorCircles(Circle peg) {
		peg.setOnMouseClicked((event) -> { 
			// cycle through the game's colors (r,g,b,y,p,o)
			if (peg.getFill().equals(Color.BLACK)) {
				peg.setFill(Color.RED);
				guessArray[Integer.parseInt(peg.getId())] = 'r';
			} else if (peg.getFill().equals(Color.RED)) {
				peg.setFill(Color.ORANGE);
				guessArray[Integer.parseInt(peg.getId())] = 'o';
			} else if (peg.getFill().equals(Color.ORANGE)) {
				peg.setFill(Color.YELLOW);
				guessArray[Integer.parseInt(peg.getId())] = 'y';
			} else if (peg.getFill().equals(Color.YELLOW)) {
				peg.setFill(Color.GREEN);
				guessArray[Integer.parseInt(peg.getId())] = 'g';
			} else if (peg.getFill().equals(Color.GREEN)) {
				peg.setFill(Color.BLUE);
				guessArray[Integer.parseInt(peg.getId())] = 'b';
			} else if (peg.getFill().equals(Color.BLUE)) {
				peg.setFill(Color.PURPLE);
				guessArray[Integer.parseInt(peg.getId())] = 'p';
			} else if (peg.getFill().equals(Color.PURPLE)) {
				peg.setFill(Color.RED);
				guessArray[Integer.parseInt(peg.getId())] = 'r';
			} 
		});
	}
	
	/**
	 * This function makes the 2x2 feedback grid for rcrp and rcwp.
	 * @param start the initial bound
	 * @param end the limit bound
	 * @param color the color
	 * @param control the controller
	 * @param guessStrCopy the copy of the guess
	 * @param checks the 2x2 grid
	 */
	public GridPane makeChecks(int start, int end, Color color, MastermindController control, String guessStrCopy, GridPane newChecks) { // was checks
				
		newChecks.setPadding(new Insets(5)); // seems to be how far right it's nudged/ space around
		newChecks.setHgap(5);
		newChecks.setVgap(5);
		
		Circle c1 = new Circle(10,10,5);
		Circle c2 = new Circle(10,10,5);
		Circle c3 = new Circle(10,10,5);
		Circle c4 = new Circle(10,10,5);
		
		for (int i=start; i<end; i++) {
			if (i == 0) {
				newChecks.add(c1, i, 0);
				c1.setFill(color);
			} else if (i == 1) {
				newChecks.add(c2, i, 0);
				c2.setFill(color);
			} else if (i == 2) {
				newChecks.add(c3, i-2, 1);
				c3.setFill(color);
			} else if (i == 3) {
				newChecks.add(c4, i-2, 1);
				c4.setFill(color);
			}
		}
		
		checks = new GridPane();
		
		return newChecks;
	}
	
	/**
	 * This function makes a row using the current guess number, pegs, and 2x2 grid.
	 * @param pegArrayGuess
	 * @param checks
	 * @param center
	 */
	public void makeRow(Circle[] pegArrayGuess, GridPane checks, VBox center) {
		GridPane row1 = new GridPane(); // new row from 1-10
		Circle pegW = new Circle();
		pegW.setRadius(pegArrayGuess[0].getRadius());
		pegW.setFill(pegArrayGuess[0].getFill());
		Circle pegX = new Circle();
		pegX.setRadius(pegArrayGuess[1].getRadius());
		pegX.setFill(pegArrayGuess[1].getFill());
		Circle pegY = new Circle();
		pegY.setRadius(pegArrayGuess[2].getRadius());
		pegY.setFill(pegArrayGuess[2].getFill());
		Circle pegZ = new Circle();
		pegZ.setRadius(pegArrayGuess[3].getRadius());
		pegZ.setFill(pegArrayGuess[3].getFill());
		Text num = new Text();
		num.setFont(new Font(20));
		GridPane lastCol = new GridPane();
	    GridPane centerGrid = new GridPane();
	    
	    if (guessCount < 10) {
			num.setText("     " + rowNum);
		} else {
			num.setText("   " + rowNum);
		}
		
		row1.setHgap(25);
		
		row1.add(num, 0, 0);
		row1.add(pegW, 1, 0);
		row1.add(pegX, 2, 0);
		row1.add(pegY, 3, 0);
		lastCol.setHgap(10);
		lastCol.add(pegZ, 0, 0);
		lastCol.add(checks, 1, 0);
		row1.add(lastCol, 4, 0);
		
		centerGrid.add(row1, 0, guessCount);
	    center.getChildren().add(centerGrid);
	}

	@Override
	/**
	 * This function redraws the needed graphics.
	 */
	public void update(Observable o, Object arg) { // arg is given data/ boolean, observable is model
		
		// checks if game has been won or lost
		
		if ((int) arg == 1) {
			AlertDialog a = new AlertDialog();
			a.display(1);
			gameOver = true;
		} else if ((int) arg == 2) {
			AlertDialog a = new AlertDialog();
			a.display(2);
			gameOver = true;
		}
	}
	
	/**
	 * This inner class makes an AlertDialog,
	 * which pops up upon winning the game
	 * (guessing all the right colors)
	 * or losing it (exceeding 10 guesses
	 * without finding the solution).
	 */
	private static class AlertDialog {
	     	 
	    public static void display(int result) {
	        Stage endStage = new Stage();
	        Button button = new Button("Submit");
	        button.setOnAction(e -> {
	        	endStage.close();
	        });
	     
	        Label label1 = new Label("Solution:");
	        VBox col = new VBox();
	        
	        GridPane layout = new GridPane();
	        layout.setVgap(5); 
	        layout.setHgap(5);
	        
	        Text resultText = new Text();
	        if (result == 1) {
	        	resultText.setText("   You Won!");
	        	resultText.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
	        	resultText.setFill(Color.GOLD);
		        col.setStyle("-fx-background-color: limegreen");
	        } else if (result == 2) {
	        	resultText.setText("   You Lost.");
	        	resultText.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
	        	resultText.setFill(Color.SILVER);
		        col.setStyle("-fx-background-color: cornflowerblue");
	        }
	        layout.setMaxHeight(10);
	        
	        for (int i = 0; i < solution.length; i++) {
	        	if (solution[i] == 'r') {
	        		layout.add(new Circle(10,10,15,Color.RED), i, 0);
	        	} else if (solution[i] == 'o') {
	        		layout.add(new Circle(10,10,15,Color.ORANGE), i, 0);
	        	} else if (solution[i] == 'y') {
	        		layout.add(new Circle(10,10,15,Color.YELLOW), i, 0);
	        	} else if (solution[i] == 'g') {
	        		layout.add(new Circle(10,10,15,Color.GREEN), i, 0);
	        	} else if (solution[i] == 'b') {
	        		layout.add(new Circle(10,10,15,Color.BLUE), i, 0);
	        	} else if (solution[i] == 'p') {
	        		layout.add(new Circle(10,10,15,Color.PURPLE), i, 0);
	        	}
	        }
	        
	        HBox btns = new HBox();
	        	        
	        btns.setSpacing(10);
	        col.setSpacing(20);
	       
	        Button reset = new Button("New Game");
	        reset.setMinWidth(100);
	        
	        Button exit = new Button("Exit");
	        exit.setMinWidth(50);

	        reset.setOnAction(e -> {
	        	endStage.close();
	        	restartGame();
	        });
	        
	        exit.setOnAction(e -> {  	
	        	mainStage.close();
	        	endStage.close();
	        });
	        
	        btns.setTranslateX(-12);
	        
	        btns.getChildren().add(reset);
	        btns.getChildren().add(exit);
	        
	        col.setMinWidth(100);
	        
	        col.getChildren().add(resultText);
	        col.setPadding(new Insets(40,50,70,60));
	        col.getChildren().add(layout);
	        col.getChildren().add(btns);
	        
	        Scene scene = new Scene(col, 250, 200);
	        endStage.setTitle("End");
	        endStage.setScene(scene);
	        endStage.showAndWait();
	    }
	}
	
	/**
	 * This function restarts the game upon clicking "Yes" when a game ends.
	 */
	static void restartGame() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
        		mainStage.close();
            	MastermindGUIView mainView = new MastermindGUIView();
                try {
                    mainView.start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
	
}
