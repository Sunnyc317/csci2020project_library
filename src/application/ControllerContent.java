package application;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

// This class is linked with the contentWindow.fxml file to show the book content
public class ControllerContent {
	@FXML public TextArea content;

	public void showcontent(String[] lines) {
		for (String text:lines) {
			content.appendText(text + "\n");
		}
	}
}
