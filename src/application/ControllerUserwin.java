package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ControllerUserwin {

	@FXML public TextField userinput;
	@FXML public TextArea booklist;

	Socket socket = null;
	ObjectOutputStream output = null;
	ObjectInputStream input = null;
	Controllerlib conlib = null;

//	@FXML
//	public void initialize() {
//		try {
//			socket = new Socket(host, port);
//			//has to be output first then input !!!
//			output = new ObjectOutputStream(socket.getOutputStream());
//			input = new ObjectInputStream(socket.getInputStream());
//		} catch (IOException e) {
//			System.out.println("err Controllerlib: constructor: socket setting problem\n");
//			e.printStackTrace();
//		}
//
//	}

//	public void init(Socket socket) {
//		try {
//			this.socket = socket;
//			//has to be output first then input !!!
//			output = new ObjectOutputStream(socket.getOutputStream());
//			input = new ObjectInputStream(socket.getInputStream());
//		} catch (IOException e) {
//			System.out.println("err ControllerUserwin: init: socket setting problem\n");
//			e.printStackTrace();
//		}
//	}

//	public void initialize() {
//		try {
//			socket = new Socket("localhost", 1998);
//			//has to be output first then input !!!
//			output = new ObjectOutputStream(socket.getOutputStream());
//			input = new ObjectInputStream(socket.getInputStream());
//		} catch (IOException e) {
//			System.out.println("err ControllerUserwin: init: socket setting problem\n");
//			e.printStackTrace();
//		}
//	}

	public void init(Controllerlib conlib) {
		this.conlib = conlib;
	}

	public void LogoutHandler() {
		conlib.LogoutHandler();

//		Message msg = new Message("null", "null", 2);
//		try {
//			output.writeObject(msg);
////			output.flush();
//		} catch (IOException e) {
//			System.out.println("err Controllerlib: loggedin: problem sending message to server");
//			e.printStackTrace();
//		}
//
//		try {
//			output.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		System.out.println("Userwindow Controller: reached LogoutHandler");
	}

	public void SearchBook() {
		String input = userinput.getText().trim();
		conlib.SearchBook(input);
		System.out.println("Userwindow Controller: reached search button");
	}

}
