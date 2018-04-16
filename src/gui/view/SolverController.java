package gui.view;

import java.awt.Toolkit;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

import Solvers.DLXs99;
import Solvers.Solver;
import gui.SolverMain;
import javafx.animation.Animation.Status;
import javafx.animation.FillTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import jfoenix.controls.JFXButton;
import jfoenix.controls.JFXButton.ButtonType;
import jfoenix.controls.JFXDialog.DialogTransition;
import jfoenix.controls.JFXSpinner;
import jfoenix.controls.JFXDialog;
import jfoenix.transitions.JFXFillTransition;

public class SolverController {

	@FXML private AnchorPane solverPane;
	@FXML private GridPane subfield00, subfield01, subfield02, subfield10, subfield11, 
		subfield12, subfield20, subfield21, subfield22;
	private GridPane[][] subfields = new GridPane[3][3];

	@FXML
	private Label statusLabel;
	@FXML
	private JFXButton solveButton;
	@FXML
	private JFXButton clearButton;
	@FXML
	private StackPane dialogPane;
	private AnchorPane dialog;
	private JFXDialog jfxDialog;

	private JFXButton[][] grid = new JFXButton[9][9];
	private JFXFillTransition[][] selectgrid = new JFXFillTransition[9][9];

	private int focusedbutton = -1;
	private SolverMain solverMain;

	@FXML
	private void initialize() {

		// {{subfield00,subfield01,subfield02},{subfield10,subfield11,subfield12},{subfield20,subfield21,subfield22}};
		subfields[0][0] = subfield00;
		subfields[0][1] = subfield01;
		subfields[0][2] = subfield02;
		subfields[1][0] = subfield10;
		subfields[1][1] = subfield11;
		subfields[1][2] = subfield12;
		subfields[2][0] = subfield20;
		subfields[2][1] = subfield21;
		subfields[2][2] = subfield22;

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

		// create dialog
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(SolverMain.class.getResource("view/ClearDialog.fxml"));
		try {
			dialog = (AnchorPane) loader.load();
			dialog.getStylesheets()
					.add(getClass().getResource("../../jfoenix/resources/css/jfoenix-design.css").toExternalForm());
		} catch (IOException e) {
			e.printStackTrace();
		}
		DialogController dialogController = (DialogController) loader.getController();
		dialogController.setSolverController(this);
		dialogController.styleUp();

		jfxDialog = new JFXDialog(dialogPane, dialog, DialogTransition.CENTER);

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
		if (focusedbutton < 0)
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

	public void solveSudoku(MouseEvent e) {
		statusLabel.setText("Solving...");
		solveButton.setDisable(true);
		Task<Solver> t = new SolveTask();
		t.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent arg0) {
				statusLabel.setText("Solved in " + (double)((SolveTask) t).getNanoTime()/ 1000000 + "ms!");
				solveButton.setDisable(false);
				Solver f = ((Solver)arg0.getSource().getValue());
				if (f != null){
					for (int i = 0; i <= 8; i++)
						for (int j = 0; j <= 8; j++) {
								grid[i][j].setText(Integer.toString(f.getAns(i + 1, j + 1)));
						}
				} else {
					statusLabel.setText("No solve!");
				}
			}
			
		});
		new Thread(t).start();
	}

	public void clearGrid(MouseEvent e) {
		showDialog();

	}

	public void showDialog() {
		dialogPane.toFront();
		jfxDialog.show();
	}

	public void dialogEnd() {
		jfxDialog.close();
		dialogPane.toBack();

	}

	public void confirmClear() {
		for (int i = 0; i <= 8; i++)
			for (int j = 0; j <= 8; j++)
				grid[i][j].setText("");
		statusLabel.setText("Intert your sudoku:");
		if (focusedbutton != -1 )
			changeGridFocus(grid[focusedbutton / 9][focusedbutton % 9]);
		dialogEnd();
	}

	
	private class HandleTransition implements EventHandler<ActionEvent>{
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
	
	private class SolveTask extends Task<Solver>{
		
		private long nanoTime = 0;

		@Override
		protected Solver call() throws Exception {
			 nanoTime = System.nanoTime();
			Solver f = new DLXs99();
			for (int i = 0; i <= 8; i++)// row
				for (int j = 0; j <= 8; j++)// column
				synchronized(grid[i][j]){
					f.WriteOV(i + 1, j + 1, grid[i][j].getText().length() == 0? 0 : Integer.parseInt(grid[i][j].getText()));
				}

			// solve and check result
			if (f.solve()) {
				nanoTime = System.nanoTime() - nanoTime;
				return f;
				
			} else {
				nanoTime = System.nanoTime() - nanoTime;
				return null;
			}
		}
		
		public long getNanoTime(){
			return nanoTime;
		}
	}
}
