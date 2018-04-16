package gui;

import java.io.IOException;

import gui.model.SudokuModel;
import gui.view.GameController;
import gui.view.RootMenuController;
import gui.view.SolverController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import jfoenix.controls.JFXDrawersStack;
import jfoenix.resources.css.*;

public class SolverMain extends Application {

	private Stage primaryStage;
	private SudokuModel model;
	private AnchorPane solver;
	private AnchorPane game;
	private BorderPane rootMenu;
	public JFXDrawersStack RootDrawerStack;
	private AnchorPane current = null;
	private GameController gameController;

	
	@Override
	public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Sudoku!");
        this.primaryStage.getIcons().add(new Image("file:resources/Sudoku-Icon.png"));
        
        //load font
        //TODO fix loading font
        Font.loadFont(getClass().getResourceAsStream("../jfoenix/resources/font/roboto/Roboto-Regular.ttf"), 24);
        Font.loadFont(getClass().getResourceAsStream("../jfoenix/resources/font/roboto/Roboto-Regular.ttf"), 64);
        Font.loadFont(getClass().getResourceAsStream("../jfoenix/resources/font/roboto/Roboto-Regular.ttf"), 20);
        Font.loadFont(getClass().getResourceAsStream("../jfoenix/resources/font/roboto/Roboto-Regular.ttf"), 22);
        Font.loadFont(getClass().getResourceAsStream("../jfoenix/resources/font/roboto/Roboto-Bold.ttf"), 22);
        Font.loadFont(getClass().getResourceAsStream("../jfoenix/resources/font/roboto/Roboto-Medium.ttf"), 22);
        
        
        initRootMenuLayout();
        initSolverLayout();
        initGameLayout();
        changeScene();
        
    }

	private void initRootMenuLayout() {
		try{
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(SolverMain.class.getResource("view/RootMenu.fxml"));
	        rootMenu = (BorderPane) loader.load();
	        
	        rootMenu.getStylesheets().add(getClass().getResource("../jfoenix/resources/css/jfoenix-design.css").toExternalForm());
	        RootMenuController rootMenuController = loader.getController();
            rootMenuController.setSolverMain(this);
            
            //setup drawer stack for root menu
            RootDrawerStack= new JFXDrawersStack();
            RootDrawerStack.setContent(rootMenu);
	        
	        Scene scene = new Scene(RootDrawerStack);
            primaryStage.setScene(scene);
	        primaryStage.setResizable(false);
	        primaryStage.show();
	        
		} catch (IOException e) {
            e.printStackTrace();
        }
	}

	private void initSolverLayout() {
		try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SolverMain.class.getResource("view/Solver.fxml"));
            solver = (AnchorPane) loader.load();
            solver.getStylesheets().add(getClass().getResource("../jfoenix/resources/css/jfoenix-design.css").toExternalForm());
            //solver.getStylesheets().add(getClass().getResource("../jfoenix/resources/css/jfoenix-fonts.css").toExternalForm());
            solver.getStyleClass().add("-fx-background: #EEEEEE;");
            
            SolverController solverController = loader.getController();
            solverController.setSolverMain(this);

        } catch (IOException e) {
            e.printStackTrace();
        }

	}
	
	private void initGameLayout() {
		try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SolverMain.class.getResource("view/Game.fxml"));
            game = (AnchorPane) loader.load();
            game.getStylesheets().add(getClass().getResource("../jfoenix/resources/css/jfoenix-design.css").toExternalForm());
            //solver.getStylesheets().add(getClass().getResource("../jfoenix/resources/css/jfoenix-fonts.css").toExternalForm());
            game.getStyleClass().add("-fx-background: #EEEEEE;");
            
            gameController = loader.getController();
            gameController.setSolverMain(this);

        } catch (IOException e) {
            e.printStackTrace();
        }

	}
	
	/**
	 * ChangeScene between Game and Solver
	 */
	public void changeScene(){
		if (current == game){
			current = solver;
		}
		else{
			current = game;
			
		}
		rootMenu.setCenter(current);
	}
	
	public boolean isGame(){
		return current == game;
	}
	
	/**
	 * Returns the main stage.
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}


	public static void main(String[] args) {
		launch(args);
	}
	
	public SudokuModel getModel(){
		return model;
	}

	public GameController getGameController() {
		return gameController;
	}

	public BorderPane getRootMenu() {
		return rootMenu;
	}

	public AnchorPane getGame() {
		return game;
	}
	
}
