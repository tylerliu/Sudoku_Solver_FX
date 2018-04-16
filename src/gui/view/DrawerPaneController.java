package gui.view;

import java.io.IOException;

import gui.SolverMain;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import jfoenix.controls.JFXButton;
import jfoenix.controls.JFXDrawer;
import jfoenix.controls.JFXHamburger;
import jfoenix.controls.JFXToolbar;

public class DrawerPaneController {

	private StackPane drawerPane;
	@FXML private HBox barHBox;
	@FXML private JFXButton menuGameButton;
	@FXML private JFXButton menuSolverButton;
	private SolverMain solverMain;
	private BorderPane rootMenu;
	private RootMenuController rootMenuController;
	
	@FXML
	private void initialize(){
		
		menuSolverButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e)->{
			menuButtonClicked(e);
		});
		menuGameButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e)->{
			menuButtonClicked(e);
		});
	}
	
	public void setSolverMain(StackPane drawerPane, RootMenuController rootMenuController,BorderPane rootMenu){
		this.drawerPane = drawerPane;
		this.rootMenuController = rootMenuController;
		this.rootMenu = rootMenu;
	}
	
	public void setSolverMain(SolverMain solverMain){
		this.solverMain = solverMain;
	}
	
	public void menuButtonClicked(MouseEvent e){
		if (e.getSource() == menuSolverButton){
			rootMenuController.setTitleText("Sudoku Solver");
			if (rootMenuController.isGameRunning())
				solverMain.getGameController().endGame();
			if (solverMain.isGame())solverMain.changeScene();
		}
		if (e.getSource() == menuGameButton){
			rootMenuController.setTitleText("Sudoku Game");
			if (rootMenuController.isGameRunning())
				solverMain.getGameController().resumeGame();
			if (!solverMain.isGame())solverMain.changeScene();
			
		}
		solverMain.RootDrawerStack.toggle(rootMenuController.drawer);
	}
}
