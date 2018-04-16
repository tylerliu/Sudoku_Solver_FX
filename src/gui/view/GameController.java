package gui.view;

import java.awt.Toolkit;
import java.io.IOException;
import Solvers.DLXs99;
import Solvers.Solver;
import gui.SolverMain;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import jfoenix.controls.JFXButton;
import jfoenix.controls.JFXButton.ButtonType;
import jfoenix.controls.JFXDialog.DialogTransition;
import jfoenix.controls.JFXDialog;
import jfoenix.transitions.JFXFillTransition;

public class GameController {

	@FXML private AnchorPane solverPane;
	@FXML private GridPane subfield00, subfield01, subfield02, subfield10, subfield11, 
		subfield12, subfield20, subfield21, subfield22;
	private GridPane[][] subfields = new GridPane[3][3];

	@FXML
	private JFXButton timeLabel;
	@FXML
	private JFXButton restartButton;
	
	@FXML
	private JFXButton checkButton;
	@FXML
	private JFXButton clearButton;
	@FXML
	private StackPane dialogPane;
	
	//panes
	private JFXDialog gameMenu;
	private AnchorPane startPane;
	

	private JFXButton[][] grid = new JFXButton[9][9];
	private boolean[][] question = new boolean[9][9];
	private JFXFillTransition[][] selectgrid = new JFXFillTransition[9][9];

	private int focusedbutton = -1;
	private SolverMain solverMain;
	
	private Timer timer = new Timer();
	private boolean clearButtonConfirm;
	private boolean restartButtonConfirm;
	private boolean gameRunning;

	@FXML
	private void initialize() {

		//initualize the subfields
		subfields[0][0] = subfield00;subfields[0][1] = subfield01;subfields[0][2] = subfield02;subfields[1][0] = subfield10;subfields[1][1] = subfield11;
		subfields[1][2] = subfield12;subfields[2][0] = subfield20;subfields[2][1] = subfield21;subfields[2][2] = subfield22;

		// create cell buttons
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++) {
				grid[i][j] = new JFXButton("grid" + i + j);
				grid[i][j].setPrefHeight(45);
				grid[i][j].setPrefWidth(45);
				grid[i][j].setText("");
				grid[i][j].setButtonType(ButtonType.RAISED);

				grid[i][j].getStyleClass().add("grid");
				subfields[i / 3][j / 3].add(grid[i][j], j % 3, i % 3);

				// transition of color
				selectgrid[i][j] = new JFXFillTransition(new Duration(500), grid[i][j], Color.WHITE,
						Color.web("#303f9f"));
				
				selectgrid[i][j].setOnFinished(new HandleTransition());
				
				
				grid[i][j].addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
					// System.out.println("Pressed"+gridFind((JFXButton)event.getSource()));
					changeGridFocus((JFXButton) (e.getSource()));
				});

			}
		
		timer = new Timer();
		
		timer.init();
		
		// create dialog
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(SolverMain.class.getResource("view/StartPane.fxml"));
		try {
			startPane = (AnchorPane) loader.load();
			startPane.getStylesheets()
					.add(getClass().getResource("../../jfoenix/resources/css/jfoenix-design.css").toExternalForm());
		} catch (IOException e) {
			e.printStackTrace();
		}
		StartPaneController startPaneController = (StartPaneController) loader.getController();
		startPaneController.setGameController(this);

		gameMenu = new JFXDialog(dialogPane, startPane, DialogTransition.CENTER);
		endGame();

	}

	public void setSolverMain(SolverMain solverMain) {
		this.solverMain = solverMain;

	}

	public int gridFind(JFXButton b) {
		for (int i = 0; i <= 8; i++)
			for (int j = 0; j <= 8; j++)
				if (grid[i][j] == b)
					return i * 9 + j;
		return -1;
	}

	public void changeGridFocus(JFXButton b) {
		int index = gridFind(b);
		if (question[index / 9][index % 9])return;
		if (index == focusedbutton) {
			if (selectgrid[index / 9][index % 9].statusProperty().get().equals(Status.RUNNING)) {
				
				//if it is running
				selectgrid[index / 9][index % 9].setRate(-selectgrid[index / 9][index % 9].getRate());
			}
			selectgrid[index / 9][index % 9].play();
			focusedbutton = -1;
			return;
		}
		if (focusedbutton != -1) {
			if (selectgrid[focusedbutton / 9][focusedbutton % 9].statusProperty().get().equals(Status.RUNNING)) {
				
				//if it is running
				selectgrid[focusedbutton / 9][focusedbutton % 9].setRate(-selectgrid[focusedbutton / 9][focusedbutton % 9].getRate());
			}
			selectgrid[focusedbutton / 9][focusedbutton % 9].play();
		}
		if (selectgrid[index / 9][index % 9].statusProperty().get().equals(Status.RUNNING)) {
			
			//if it is running
			selectgrid[index / 9][index % 9].setRate(-selectgrid[index / 9][index % 9].getRate());
		}
		selectgrid[index / 9][index % 9].play();
		focusedbutton = index;
	}

	public void KeyHandle(KeyEvent event) {
		KeyCode key = event.getCode();
		if (focusedbutton < 0 || question[focusedbutton / 9][focusedbutton % 9])
			return;
		switch (key) {
		case RIGHT:
			if (focusedbutton < 80)
				changeGridFocus(grid[(focusedbutton + 1) / 9][(focusedbutton + 1) % 9]);
			break;
		case UP: // up key
			if (focusedbutton > 8)
				changeGridFocus(grid[(focusedbutton - 9) / 9][(focusedbutton - 9) % 9]);
			break;
		case LEFT: // left key
			if (focusedbutton > 0)
				changeGridFocus(grid[(focusedbutton - 1) / 9][(focusedbutton - 1) % 9]);
			break;
		case DOWN: // down key
			if (focusedbutton < 72)
				changeGridFocus(grid[(focusedbutton + 9) / 9][(focusedbutton + 9) % 9]);
			break;
		case BACK_SPACE:// backspace
			grid[focusedbutton / 9][focusedbutton % 9].setText("");
			break;
		case DIGIT1:
			grid[focusedbutton / 9][focusedbutton % 9].setText("1");
			break;
		case DIGIT2:
			grid[focusedbutton / 9][focusedbutton % 9].setText("2");
			break;
		case DIGIT3:
			grid[focusedbutton / 9][focusedbutton % 9].setText("3");
			break;
		case DIGIT4:
			grid[focusedbutton / 9][focusedbutton % 9].setText("4");
			break;
		case DIGIT5:
			grid[focusedbutton / 9][focusedbutton % 9].setText("5");
			break;
		case DIGIT6:
			grid[focusedbutton / 9][focusedbutton % 9].setText("6");
			break;
		case DIGIT7:
			grid[focusedbutton / 9][focusedbutton % 9].setText("7");
			break;
		case DIGIT8:
			grid[focusedbutton / 9][focusedbutton % 9].setText("8");
			break;
		case DIGIT9:
			grid[focusedbutton / 9][focusedbutton % 9].setText("9");
			break;

		default:
			Toolkit.getDefaultToolkit().beep();
			break;

		}
	}

	public void clearGrid(MouseEvent e) {

		if (clearButtonConfirm){
			confirmClear();
			clearButton.setText("Clear");
			clearButtonConfirm = false;
			clearButton.getStyleClass().set(1, null);
		}
		else{
			clearButtonConfirm = true;
			clearButton.setText("Confirm Clear");
			clearButton.getStyleClass().set(1, "confirm");
			Timeline timeline = new Timeline(new KeyFrame(
			        Duration.millis(10000),
			        ae -> {
			        	clearButton.setText("Clear");
			        	clearButtonConfirm = false;
			        	clearButton.getStyleClass().set(1, null);
			        }));
			timeline.play();
		}
	}

	public void showDialog() {
		dialogPane.toFront();
		gameMenu.show();
	}

	public void dialogEnd() {
		gameMenu.close();
		dialogPane.toBack();

	}

	public void confirmClear() {
		for (int i = 0; i <= 8; i++)
			for (int j = 0; j <= 8; j++)
				if (!question[i][j])grid[i][j].setText("");
		if (focusedbutton != -1 )
			changeGridFocus(grid[focusedbutton / 9][focusedbutton % 9]);
	}
	
	public void startGame(int[][] matrix){
		dialogEnd();
		for (int i = 0; i < 9; i ++)
			for (int j = 0; j < 9; j ++)
				if (matrix[i][j] != 0){
					grid[i][j].getStyleClass().set(1,"locked");
					question[i][j] = true;
					grid[i][j].setText(Integer.toString(matrix[i][j]));
				}
		
		gameRunning = true;
		restartButton.setDisable(false);
		resumeGame();
	}
	
	public void endGame(){
		for (int i = 0; i < 9; i++){
			for (int j = 0; j < 9; j ++){
				
				//TODO reset each grid
				grid[i][j].setText("");
				grid[i][j].getStyleClass().set(1, null);
				question[i][j] = false;
			}
		}
		
		//reset timer
		timer.resetTimer();
		showDialog();
		gameRunning = false;
	}
	
	public void restart(MouseEvent e){
		if (restartButtonConfirm){
			endGame();
			restartButton.setText("Restart");
			restartButtonConfirm = false;
			restartButton.getStyleClass().set(1, null);
			restartButton.setDisable(true);
		}
		else{
			restartButtonConfirm = true;
			restartButton.setText("Confirm Restart");
			restartButton.getStyleClass().set(1, "confirm");
			Timeline timeline = new Timeline(new KeyFrame(
			        Duration.millis(10000),
			        ae -> {
			        	restartButton.setText("Restart");
			        	restartButtonConfirm = false;
			        	restartButton.getStyleClass().set(1, null);
			        }));
			timeline.play();
		}
		
	}
	
	public void checkGrid(MouseEvent e){
		checkButton.setDisable(true);
		CheckTask t = new CheckTask();
		new Thread(t).start();
	}
	
	public void pauseGame(){
		timer.pauseTimer();
		for (int i = 0; i < 9; i ++)
			for (int j = 0; j < 9; j++)
				grid[i][j].setVisible(false);
	}
	
	public void resumeGame(){
		timer.startTimer();
		for (int i = 0; i < 9; i ++)
			for (int j = 0; j < 9; j++)
				grid[i][j].setVisible(true);
	}
	
	public boolean isRunning(){
		return gameRunning;
	}
	
	private class HandleTransition implements EventHandler<ActionEvent> {
		@Override
		public synchronized void handle(ActionEvent e){
			
			((JFXFillTransition)e.getSource()).setRate(-((JFXFillTransition)e.getSource()).getRate());
			if (focusedbutton >= 0 && ((JFXFillTransition)e.getSource()).getRegion() == grid[focusedbutton/9][focusedbutton%9]){
				((JFXFillTransition)e.getSource()).getRegion().getStyleClass().set(1, "selected");
				((JFXFillTransition)e.getSource()).getRegion().applyCss();
			}
			else {
				((JFXFillTransition)e.getSource()).getRegion().getStyleClass().set(1,null);
				((JFXFillTransition)e.getSource()).getRegion().applyCss();
			}
			
		}
	}
	
	private class CheckTask extends Task<Void> {
		
		@Override
		protected Void call() throws Exception{
			String t = getResult();
			Platform.runLater(new Runnable(){

				@Override
				public void run() {
					if(t.equals("Success"))timer.pauseTimer();
					checkButton.setText(t);
					checkButton.getStyleClass().set(1, "confirm");
					checkButton.setDisable(false);
					Timeline timeline = new Timeline(new KeyFrame(
					        Duration.millis(10000),
					        ae -> {
					        	checkButton.setText("Check");
					        	checkButton.getStyleClass().set(1, null);
					        }));
					timeline.play();
				}
				
			});
			return null;
		}
		
		protected String getResult(){
			Solver f = new DLXs99();
			for (int i = 0; i <= 8; i++)// row
				for (int j = 0; j <= 8; j++)// column
				synchronized(grid[i][j]){
					if (grid[i][j].getText().length() == 0)return "Uncompleted";
					f.WriteOV(i + 1, j + 1, Integer.parseInt(grid[i][j].getText()));
				}

			// solve and check result
			if (f.solve()) {
				return "Success";
				
			} else {
				return "Wrong";
			}
		}
	}
	
	private class Timer {
		
		private boolean isRunning;
		private int sec = 0, min = 0;
		private Timeline timeline;

		protected void init() {
			timeline = new Timeline();
	        timeline.setCycleCount(Timeline.INDEFINITE);
	        timeline.getKeyFrames().add(
	                new KeyFrame(Duration.seconds(1),
	                  new EventHandler<ActionEvent>() {
	                    public void handle(ActionEvent event) {
	                        triggerEvent();
	                    }
	                }));
		}
		
		private void triggerEvent(){
			if (isRunning){
				sec++;
				if (sec == 60){
					sec = 0;
					min++;
				}
			timeLabel.setText(timer.getTime());
			}
		}
		
		public String getTime(){
			return (min<10 ?"0":"")+min+":"+(sec<10 ?"0":"")+sec;
		}

		public boolean getIsRunning() {
			return isRunning;
		}
		
		public void startTimer(){
			isRunning = true;
			timeline.play();
		}
		
		public void pauseTimer(){
			isRunning = false;
			timeline.pause();
		}
		
		/**
		 * reset the timer to 0
		 */
		synchronized public void resetTimer(){
			pauseTimer();
			timeline.stop();
			sec = 0;
			min = 0;
			timeLabel.setText(timer.getTime());
		}
	}
}
