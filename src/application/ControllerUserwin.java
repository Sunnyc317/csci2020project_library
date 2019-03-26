package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

// This class is linked with UserWindow.fxml to interact with the users (for searching books and log out)
public class ControllerUserwin {

	@FXML public TextField userinput;
	@FXML public TextArea booklist;

	Socket socket = null;
	ObjectOutputStream output = null;
	ObjectInputStream input = null;
	Controllerlib conlib = null;

	public void init(Controllerlib conlib) {
		this.conlib = conlib;
	}

	public void LogoutHandler() throws IOException {
		conlib.LogoutHandler();
		System.out.println("Userwindow Controller: reached LogoutHandler");
	}

	public void SearchBook() throws IOException {
		String input = userinput.getText().trim();
		conlib.SearchBook(input);
		System.out.println("Userwindow Controller: reached search button");
	}

	public void showbooklist(String booknames) {
		String[] books = booknames.split(" ");
		for (String name:books) {
			booklist.appendText(name + "\n");
		}
	}

}
