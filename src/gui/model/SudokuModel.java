package gui.model;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SudokuModel {

	private IntegerProperty[][] field = new IntegerProperty[9][9];
	private StringProperty statusLabel;

	public SudokuModel(){
		for (int i = 0; i < 9; i ++){
			for (int j = 0; j < 9; j ++){
				field[i][j] = new SimpleIntegerProperty(0);
			}
		}
		
		statusLabel = new SimpleStringProperty("Please Input Sudoku:");
	}
	
	public int getPostalCode(int rowIndex, int columnIndex) {
		return field[rowIndex][columnIndex].get();
	}

	public void setPostalCode(int rowIndex, int columnIndex, int num) {
		this.field[rowIndex][columnIndex].set(num);
	}

	public String getStatus(){
		return statusLabel.get();
	}
	
	/**
	 * sets the status Label
	 * @param status
	 */
	public void setStatus(String status){
		statusLabel.set(status);
	}
}
