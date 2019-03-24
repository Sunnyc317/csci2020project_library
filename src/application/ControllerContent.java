package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ControllerContent {
	@FXML public TextArea content;


	public void showcontent(String[] lines) {
		for (String text:lines) {
			content.appendText(text + "\n");
		}
	}
}
