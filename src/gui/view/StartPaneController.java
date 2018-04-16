package gui.view;

import Generator.Difficulty.Level;
import Generator.ProblemGen;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import jfoenix.controls.JFXButton;
import jfoenix.controls.JFXRadioButton;
import jfoenix.controls.JFXSpinner;

public class StartPaneController {

	@FXML private JFXRadioButton relaxButton;
	@FXML private JFXRadioButton easyButton;
	@FXML private JFXRadioButton normalButton;
	@FXML private JFXRadioButton hardButton;
	@FXML private JFXRadioButton insaneButton;
	@FXML private JFXButton startButton;
	@FXML private JFXSpinner spinner;
	private ToggleGroup levelGroup;
	private GameController gameController;
	
	public void initialize(){
		spinner.setVisible(false);
		levelGroup = new ToggleGroup();
		relaxButton.setToggleGroup(levelGroup); 
		easyButton.setToggleGroup(levelGroup);
		normalButton.setToggleGroup(levelGroup);
		hardButton.setToggleGroup(levelGroup);
		insaneButton.setToggleGroup(levelGroup);
	}
	
	public void setGameController(GameController gameController){
		this.gameController = gameController;
	}
	
	public void startGame(MouseEvent e){
		spinner.setVisible(true);
		startButton.setDisable(true);
		GenerateGame g  = new GenerateGame();
		Toggle t = levelGroup.getSelectedToggle();
		if (t == relaxButton)
			g.setLevel(Level.RELAX);
		if (t == easyButton)
			g.setLevel(Level.EASY);
		if (t == normalButton)
			g.setLevel(Level.NORMAL);
		if (t == hardButton)
			g.setLevel(Level.HARD);
		if (t == insaneButton)
			g.setLevel(Level.INSANE);
		if (t == null){
			spinner.setVisible(false);
			startButton.setDisable(false);
			return;
		}	
		Thread th = new Thread(g);
		th.setDaemon(true);
		th.start();
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5),new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                if (g.isRunning()){
                	th.stop();
                	startGame(new MouseEvent(null, 0, 0, 0, 0, null, 0, false, false, false, false, false, false, false, false, false, false, null));
                }
                
            }	
		}));
		timeline.playFromStart();
	}
	
	public class GenerateGame extends Task<Void>{
		
		Level level;

		public void setLevel(Level level) {
			this.level = level;
		}

		@Override
		protected Void call() throws Exception {
			int[][] i = ProblemGen.generateProblem(level);
			Platform.runLater(new Runnable(){
				public void run(){
					gameController.startGame(i);
					spinner.setVisible(false);
					startButton.setDisable(false);
				}
			});
			return null;
		}
		
	}
}
