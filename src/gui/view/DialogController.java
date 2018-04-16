package gui.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import jfoenix.controls.JFXButton;

public class DialogController {

	@FXML private AnchorPane clearDialog;
	@FXML private JFXButton cancelButton;
	@FXML private JFXButton clearButton;
	@FXML private Label message;
	
	private SolverController solverController;
	
	public void setSolverController(SolverController solverController) {
		this.solverController = solverController;
	}
	
	public void styleUp(){
		cancelButton.getStyleClass().add("dialogButton");
		clearButton.getStyleClass().add("dialogButton");
		message.getStyleClass().add("messageLabel");
		
	}

	@FXML
	public void initialize(){
		
	}
 
	public void cancel(MouseEvent e){
		solverController.dialogEnd();
	}
	
	public void clear (MouseEvent e){
		solverController.confirmClear();
	}
}
