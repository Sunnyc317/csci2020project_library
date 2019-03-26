package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

// How I solve the start error: something to do with resource path,
// create another javafx project and copy everything in, also put fx:controller to the main pane

// This class would launch the user interface,
// but the server class (Serverlib.java) has to be launched first for the program to work
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("Log in");
			primaryStage.resizableProperty().setValue(Boolean.FALSE);
			Parent root = FXMLLoader.load(getClass().getResource("uilib.fxml"));
			primaryStage.setScene(new Scene(root));

			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
