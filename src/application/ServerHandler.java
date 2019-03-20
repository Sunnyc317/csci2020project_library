package application;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerHandler implements Runnable{
	Socket socket = null;
	ObjectInputStream fromClient = null;
	ObjectOutputStream toClient = null;
	File userinfo = null;
	File booklist = null;

	public ServerHandler(Socket socket) {
		this.socket = socket;
		userinfo = new File("userinfo.txt");
		booklist = new File("booklist.txt");
	}

	public Boolean loginRequest(Message msg) {
		Scanner inputf = null;
		try {
			inputf = new Scanner(userinfo);
		} catch (FileNotFoundException e) {
			System.out.println("err ServerHandler: registerRequest: scanner setting problem");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("err ServerHandler: registerRequest: filewriter setting problem");
			e.printStackTrace();
		}

		String id = msg.getId();
		String password = msg.getPassword();
		String idNpassword = id+":"+password;

		//return true if the user and password is in file
		while (inputf.hasNext()) {
			if (inputf.nextLine().equals(idNpassword)) {
				System.out.println("User and password matched");
				inputf.close();
				return true;
			}
		}
		//otherwise false
		return false;
	}

	public Boolean registerRequest(Message msg) {
		Scanner inputf = null;
		FileWriter outputf = null;
		try {
			inputf = new Scanner(userinfo);
			outputf = new FileWriter(userinfo, true);
		} catch (FileNotFoundException e) {
			System.out.println("err ServerHandler: registerRequest: scanner setting problem");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("err ServerHandler: registerRequest: filewriter setting problem");
			e.printStackTrace();
		}

		String id = msg.getId();
		String password = msg.getPassword();
		String idNpassword = id+":"+password;

		while (inputf.hasNext()) {
			if (inputf.nextLine().equals(idNpassword)) {
				System.out.println("User already registered");
				inputf.close();
				return false;
			}
		}

		try {
			outputf.write(idNpassword + "\n");
			outputf.close();
		} catch (IOException e) {
			System.out.println("err ServerHandler: registerRequest: FileWriter writing problem");
			e.printStackTrace();
		}

		return true;

	}

	@Override
	public void run() {
		Boolean connected = true;
		try {
			fromClient = new ObjectInputStream(socket.getInputStream());
			toClient = new ObjectOutputStream(socket.getOutputStream());

		} catch (IOException ioe1){
			System.out.println("err ServerHandler: run: io setting problem, line65");
			ioe1.printStackTrace();
			connected = false;
		}

		while (connected) {
			System.out.println("It is connected now, listening to request. ");
			Message msg = null;
			try {
				msg = (Message)fromClient.readObject();
			} catch (ClassNotFoundException | IOException e) {
				System.out.println("err ServerHandler: run: problem receiving message from client, line31");
				e.printStackTrace();
			}

			if (msg.getType() == 0) {
				Boolean success = registerRequest(msg);
				ServerRespond respond = new ServerRespond(success);
				try {
					toClient.writeObject(respond);
//					toClient.flush();
					System.out.println("Responded to client (register)");
				} catch (IOException e) {
					System.out.println("err ServerHandler: run: sending Boolean to Client fail (register), line80");
					e.printStackTrace();
				}
			}
			else if (msg.getType() == 1) {
				Boolean success = loginRequest(msg);
				ServerRespond respond = new ServerRespond(success);
				try {
					toClient.writeObject(respond);
//					toClient.flush();
					System.out.println("Responded to client (log in)");
				} catch (IOException e) {
					System.out.println("err ServerHandler: run: sending Boolean to Client fail (log in), line129");
					e.printStackTrace();
				}
			}
			else if (msg.getType() == 2) {
				connected = false;
			}
		}
	}


}
