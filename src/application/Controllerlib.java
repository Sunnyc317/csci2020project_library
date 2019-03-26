package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
//import java.net.URL;
//import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
//import javafx.fxml.Initializable;


// This class is linked with the uilib.fxml file to recieve user's input and interact with users
public class Controllerlib{

	Socket socket = null;
	String host = "localhost";
	int port = 1998;
	BufferedReader input = null;
	PrintWriter output = null;

	//	uilib.fxml attributes
	@FXML public TextField username;
	@FXML public PasswordField password;
	@FXML public Button btlogin;
	@FXML public Button btregister;
	@FXML public Label notification1;
	@FXML public Label notification2;

	ControllerUserwin scene2Controller;
	ControllerContent contentController;

	@FXML
	public void initialize() {
		try {
			socket = new Socket(host, port);
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()) );
			output = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			System.out.println("err Controllerlib: constructor: socket setting problem\n");
			e.printStackTrace();
		}

	}

	// The method would be called when button "log out" is hit
	// sent log out request to server and close all windows in the end
	public void LogoutHandler() throws IOException {
		String request = "logout " + username.getText().trim();
		output.println(request);
		output.flush();
		input.close();
		//need to be tested
		output.close();

		Platform.exit();
	}

	// This method would be called by the SearchBook method in ControllerUserwin class
	// (when search button is hit in the second window)
	// send search for book request and receive book content in String array,
	// then send the String array to showcontent method in contentController class
	// the third window would pop up and show the book content
	public void SearchBook(String userinput) throws IOException {
		//sending book searching request
		output.println("search " + userinput);
		output.flush();

		String found = input.readLine();
		if (found.equals("searchsuccess")) {
			// get the length of the string array
			int length = input.read();
			String[] content = new String[length];
			for (int i = 0; i < length; i ++) {
				content[i] = input.readLine();
			}

			// The third window would pop up once the searching book is found
			FXMLLoader loader = new FXMLLoader(getClass().getResource("contentWindow.fxml"));
			Parent root2 = loader.load();

			contentController = loader.getController();
			// showcontent method in contentController is called
			contentController.showcontent(content);

			Stage stage = new Stage();
			stage.setScene(new Scene(root2));
			stage.show();

		}
		else {
			System.out.println(found);
		}

	}

	// This method would be called when "Log in" button is hit
	// It would take id from username textfield and
	// password from password passwordfield
	// It would detect if the password or id field is empty and send alert
	// It would send log in request if the textfields are not empty
	// It would open a new window if log in is successful or alert user what's wrong
	// if user id / password is not correct
	public int LoginHandler(ActionEvent action_e) {
		String id = username.getText().trim();
		String pswd = password.getText().trim();
		String type = "login";

		if (id.isEmpty()) {
			notification1.setText("username cannot be empty");
			notification2.setText("");
			return -1;
		}
		if (pswd.isEmpty()) {
			notification1.setText("");
			notification2.setText("password cannot be empty");
			return -1;
		}

		output.println(type+" "+id+" "+pswd);
		output.flush();

		String success = "null";
		try {
			success = input.readLine();
		} catch (IOException e) {
			System.out.println("err Controllerlib: registered: failed to receive from server");
			e.printStackTrace();
		}

		if (success.equals("success")) {
			// The second window would pop up once log in has succeed
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("UserWindow.fxml"));
				Parent root1 = loader.load();

				scene2Controller = loader.getController();
				scene2Controller.init(this);
				String booknames = "null";
				booknames = input.readLine();
				scene2Controller.showbooklist(booknames);

				Stage stage = new Stage();
				stage.setScene(new Scene(root1));
				stage.show();
			} catch(Exception err) {
				System.out.println("Controllerlib: LoginHandler: problem opening the second window");
			}

		}
		else if (success.equals("alreadyloggedin")) {
			notification1.setText("Account already logged in");
			notification2.setText("");
		}
		else if (success.equals("password wrong")) {
			notification1.setText("");
			notification2.setText("password wrong");
		}
		else {
			notification1.setText("User doesn't exist");
			notification2.setText("");
		}
		return 0;
	}

	// This method would be called when "register" button is hit
	// the method would send register request with user id & password
	// and inform the user if the registration has succeed
	public int RegisterHandler(ActionEvent e) {
		String id = username.getText().trim();
		String pswd = password.getText().trim();

		if (id.isEmpty()) {
			notification1.setText("username cannot be empty");
			notification2.setText("");
			return -1;
		}
		if (pswd.isEmpty()) {
			notification2.setText("password cannot be empty");
			notification1.setText("");
			return -1;
		}

		String type = "register";
		output.println(type+" "+id+" "+pswd);
		output.flush();

		String success = "null";
		try {
			success = input.readLine();
		} catch (IOException ioe) {
			System.out.println("err Controllerlib: registered: failed to receive from server");
			ioe.printStackTrace();
		}

		if (success.equals("success")) {
			notification1.setText("registration success");
			notification2.setText("");
		}
		else {
			notification1.setText("User name already used");
			notification2.setText("");
		}
		return 0;
	}

	// when enter key is hit, login button action would be triggered
	public void isEnterHit(KeyEvent keyEvent) {
		if (keyEvent.getCode() == KeyCode.ENTER) {
			btlogin.fire();
		}
	}

}
