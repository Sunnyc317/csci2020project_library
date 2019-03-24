package application;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Serverlib{
//	String host = "localhost";
	int port = 1998;
	ServerSocket sSocket = null;

	public Serverlib() {
		try {
			sSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("err Serverlib: ServerSocket setting problem");
			e.printStackTrace();
		}
	}

	public void start() throws IOException {

	    while(true) {
	    	Socket socket = sSocket.accept();
	    	System.out.println("Serverlib: run: reached here, socket accepted");
	    	ServerHandler handle = new ServerHandler(socket);
//	    	handle.run();
	    	new Thread(handle).start();
	    }

	}

	public static void main(String[] args) {
		try {
			Serverlib server = new Serverlib();
			server.start();
		} catch (IOException e) {
			System.out.println("err Serverlib: main: server.start failed");
			e.printStackTrace();
		}
//		launch(args);
	}


}
