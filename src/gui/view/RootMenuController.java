package gui.view;

import java.io.IOException;
import gui.SolverMain;
import javafx.event.Event;
import javafx.event.EventHandler;
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

public class RootMenuController {

	@FXML
	private BorderPane rootMenu;
	@FXML
	private JFXToolbar toolBar;
	@FXML
	private JFXHamburger menuButton;
	@FXML
	private Label titleLabel;
	public JFXDrawer drawer;
	private SolverMain solverMain;

	// DrawerPane
	private DrawerPaneController drawerPaneController;
	private StackPane drawerPane;
	@FXML
	private HBox barHBox;
	@FXML
	private JFXButton menuGameButton;
	@FXML
	private JFXButton menuSolverButton;

	@FXML
	private void initialize() {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(SolverMain.class.getResource("view/drawerPane.fxml"));
		try {
			drawerPane = (StackPane) loader.load();
			drawer = new JFXDrawer();
			drawer.setSidePane(drawerPane);
			drawer.setDefaultDrawerSize(200);
			drawer.setLayoutX(60);
			drawer.setId("LEFT");
			drawer.setOnDrawerClosed(new EventHandler<Event>(){

				@Override
				public void handle(Event event) {
					if (isGameRunning())
						solverMain.getGameController().resumeGame();
				}
				
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		menuButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
			solverMain.RootDrawerStack.toggle(drawer);
			
			if (isGameRunning())
					solverMain.getGameController().pauseGame();
		});

		drawerPaneController = loader.getController();
		drawerPaneController.setSolverMain(drawerPane, this, rootMenu);
	}

	public void setSolverMain(SolverMain solverMain) {
		this.solverMain = solverMain;
		drawerPaneController.setSolverMain(solverMain);
	}

	public void setTitleText(String s) {
		this.titleLabel.setText(s);
	}

	public boolean isGameRunning(){
		return solverMain.isGame() && solverMain.getGameController().isRunning();
	}
}
