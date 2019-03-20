package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;

// AnchorPane


public class Controllerlib{

//	AnchorPan
	Socket socket = null;
	String host = "localhost";
	int port = 1998;
	//has to be outputstream first!!!
	ObjectOutputStream output = null;
	ObjectInputStream input = null;

	@FXML public TextField username;
	@FXML public PasswordField password;
	@FXML public Button btlogin;
	@FXML public Button btregister;
	@FXML public TextArea ta;
//	public Modellib model = null;
//	model = new Modellib();
//	public Modellib model = new Modellib();


//	public Controllerlib() throws IOException {}

	@FXML
	public void initialize() {
		try {
			socket = new Socket(host, port);
			//has to be output first then input !!!
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			System.out.println("err Controllerlib: constructor: socket setting problem\n");
			e.printStackTrace();
		}

	}

	public void LoginHandler(ActionEvent e) {
		String id = username.getText();
		String pswd = password.getText();
		ta.appendText(loggedin(id, pswd));
	}

	public String loggedin(String id, String password) {
		int type = 1;
		Message msg = new Message(id, password, type);
		try {
			output.writeObject(msg);
//			output.flush();
		} catch (IOException e) {
			System.out.println("err Controllerlib: loggedin: problem sending message to server");
			e.printStackTrace();
		}

		ServerRespond success = null;

		try {
			success = (ServerRespond) input.readObject();
		} catch (IOException e) {
			System.out.println("err Controllerlib: registered: failed to receive from server");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("err Controllerlib: registered: failed to receive from server, can't find the class");
			e.printStackTrace();
		}

		if (success.getSuccess()) {
			return id + " logged in\n";
		}
		else {
			return id + " username or password is wrong";
		}
	}

	public void RegisterHandler(ActionEvent e) {
//		model.initialization();
		String id = username.getText();
		String pswd = password.getText();
		ta.appendText(registered(id, pswd));
	}

	public String registered(String id, String password) {
		int type = 0;
		Message msg = new Message(id, password, type);
		try {
			output.writeObject(msg);
//			output.flush();
		} catch (IOException e) {
			System.out.println("err Controllerlib: registered: problem sending message to server");
			e.printStackTrace();
		}

		ServerRespond success = null;

		try {
			success = (ServerRespond) input.readObject();
		} catch (IOException e) {
			System.out.println("err Controllerlib: registered: failed to receive from server");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("err Controllerlib: registered: failed to receive from server, can't find the class");
			e.printStackTrace();
		}

		if (success.getSuccess()) {
			return id + " registered\n";

		}
		else {
			return id+" not registered \n";
		}

	}

}
