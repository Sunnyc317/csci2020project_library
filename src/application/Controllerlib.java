package application;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
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
	// ObjectOutputStream output = null;
	// ObjectInputStream input = null;
	// DataInputStream inputdata = null;
	BufferedReader input = null;
	PrintWriter output = null;

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
	ControllerUserwin scene2Controller;


//	public Modellib model = null;
//	model = new Modellib();
//	public Modellib model = new Modellib();


//	public Controllerlib() throws IOException {}

	@FXML
	public void initialize() {
		try {
			socket = new Socket(host, port);
			//has to be output first then input !!!
			// output = new ObjectOutputStream(socket.getOutputStream());
			// input = new ObjectInputStream(socket.getInputStream());
			// inputdata = new DataInputStream(socket.getInputStream());
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()) );
			output = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			System.out.println("err Controllerlib: constructor: socket setting problem\n");
			e.printStackTrace();
		}

	}

	public void LogoutHandler() {
		String request = "logout";
		output.println(request);
		output.flush();
		output.close();
	}

	public void SearchBook(String userinput) throws IOException {

		output.println("search " + userinput);
		output.flush();
		output.close();

		String content = input.readLine();
		String[] lines = content.split("*");
		for (String text:lines) {
			ta.appendText(text + "\n");
		}
		// set the third scene for book content

//		ServerRespond success = null;
// 		String found = "null";
//
// 		try {
// //			success = (ServerRespond)input.readObject();
// 			found = input.readLine();
// 		} catch (IOException e) {
// 			System.out.println("err Controllerlib: searchBook: failed to receive from server");
// 			e.printStackTrace();
// 		}
//
// 		if (found.equals("found")) {
// 			System.out.println("Book found");
// 		}
// 		else {
// 			System.out.println("Book no found");
// 		}

	}

	public void LoginHandler(ActionEvent action_e) {
		String id = username.getText().trim();
		String pswd = password.getText().trim();
		// int type = 1;
		String type = "login";

		output.println(type+" "+id+":"+pswd);
		output.flush();

		// ServerRespond success = null;
		String success = "null";
		try {
			success = input.readLine();
		} catch (IOException e) {
			System.out.println("err Controllerlib: registered: failed to receive from server");
			e.printStackTrace();
		}

		if (success.equals("loginsuccess")) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("UserWindow.fxml"));
	            Parent root1 = loader.load();

	            scene2Controller = loader.getController();
	            scene2Controller.init(this);
				String booknames = "null";
				try {
//					System.out.println("problems when reading booknames");
					booknames = input.readLine();
				} catch (IOException ioe) {
					System.out.println("problem reading booklist");
				}
				scene2Controller.showbooklist(booknames);

				Stage stage = new Stage();
				stage.setScene(new Scene(root1));
				stage.show();
			} catch(Exception err) {
				System.out.println("Controllerlib: LoginHandler: problem opening the second window");

			}

			Platform.runLater(() -> {
				ta.appendText(id + " log in successfully\n");
			});
		}
		else {
			Platform.runLater(() -> {
				ta.appendText(id + " log in fail\n");
			});
		}


	}

	public void RegisterHandler(ActionEvent e) {
//		model.initialization();
		String id = username.getText().trim();
		String pswd = password.getText().trim();
		// ta.appendText(registered(id, pswd));
		// int type = 0;
		String type = "register";
		output.println(type+" "+id+":"+pswd);
		output.flush();

		String success = "null";

		try {
			success = input.readLine();
		} catch (IOException ioe) {
			System.out.println("err Controllerlib: registered: failed to receive from server");
			ioe.printStackTrace();
		}

		if (success.equals("registersuccess")) {
			Platform.runLater(() -> {
				ta.appendText(id+" register success\n");
			});

		}
		else {
			Platform.runLater(() -> {
				ta.appendText(id+" register fail\n");
			});
		}

	}

}
