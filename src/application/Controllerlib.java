package application;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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
	DataInputStream inputdata = null;

//	uilib.fxml attributes
	@FXML public TextField username;
	@FXML public PasswordField password;
	@FXML public Button btlogin;
	@FXML public Button btregister;
	@FXML public TextArea ta;

//	UserWindow.fxml attributes
	@FXML public TextField userinput;
	@FXML public TextArea booklist;
//	@FXML public Button search;



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
			inputdata = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			System.out.println("err Controllerlib: constructor: socket setting problem\n");
			e.printStackTrace();
		}

	}

	public void LogoutHandler() {
		Message msg = new Message("null", "null", 2);
		try {
			output.writeObject(msg);
			output.flush();
		} catch (IOException e) {
			System.out.println("err Controllerlib: loggedin: problem sending message to server");
			e.printStackTrace();
		}

		try {
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void SearchBook(String userinput) {
//		Message msg = new Message("","", 3, userinput);
//		try {
//			output.writeObject(msg);
//			output.flush();
//		} catch (IOException e) {
//			System.out.println("err Controllerlib: searchBook: problem sending message to server");
//			e.printStackTrace();
//		}
//
//		try {
//			output.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
////		ServerRespond success = null;
//		Boolean found = false;
//
//		try {
////			success = (ServerRespond)input.readObject();
//			found = inputdata.readBoolean();
//		} catch (IOException e) {
//			System.out.println("err Controllerlib: searchBook: failed to receive from server");
//			e.printStackTrace();
//		}
//
//		if (found) {
//			System.out.println("Book found");
//		}
//		else {
//			System.out.println("Book no found");
//		}

	}

//	public void LoginHandler(ActionEvent e) {
//	}

	public void LoginHandler(ActionEvent action_e) {
		String id = username.getText();
		String pswd = password.getText();
		int type = 1;

		Message msg = new Message(id, pswd, type);
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
			Platform.runLater(() -> {
				ta.appendText(id + " logged in successfully");
			});

			try {
//			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UserWindow.fxml"));
//			Parent root1 = (Parent) fxmlLoader.load();

//				Parent root1 = FXMLLoader.load(getClass().getResource("UserWindow.fxml"));
			FXMLLoader loader = new FXMLLoader(getClass().getResource("UserWindow.fxml"));
            Parent root1 = loader.load();

            ControllerUserwin scene2Controller = loader.getController();
            scene2Controller.init(this);

				Stage stage = new Stage();
				stage.setScene(new Scene(root1));
				stage.show();
			} catch(Exception err) {
				System.out.println("Controllerlib: LoginHandler: problem opening the second window");

			}

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
