package application;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerHandler implements Runnable{
	Socket socket = null;
	// ObjectInputStream fromClient = null;
	// ObjectOutputStream toClient = null;
	// DataOutputStream toClientdata = null;
	BufferedReader fromClient = null;
	PrintWriter toClient = null;
	File userinfo = null;
	File booklist = null;

	public ServerHandler(Socket socket) {
		this.socket = socket;
		userinfo = new File("userinfo.txt");
		booklist = new File("booklist.txt");
	}

	 // public Boolean loginRequest(Message msg) {
	 // 	Scanner inputf = null;
	 // 	try {
	 // 		inputf = new Scanner(userinfo);
	 // 	} catch (FileNotFoundException e) {
	 // 		System.out.println("err ServerHandler: registerRequest: scanner setting problem");
	 // 		e.printStackTrace();
	 // 	} catch (IOException e) {
	 // 		System.out.println("err ServerHandler: registerRequest: filewriter setting problem");
	 // 		e.printStackTrace();
	 // 	}
	 //
	 // 	String id = msg.getId();
	 // 	String password = msg.getPassword();
	 // 	String idNpassword = id+":"+password;
	 //
	 // 	//return true if the user and password is in file
	 // 	while (inputf.hasNext()) {
	 // 		if (inputf.nextLine().equals(idNpassword)) {
	 // 			System.out.println("User and password matched");
	 // 			inputf.close();
	 // 			return true;
	 // 		}
	 // 	}
	 // 	//otherwise false
	 // 	return false;
	 // }

	public Boolean loginRequest(String request) {
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
		String[] msg = request.split(" ");
		String idNpassword = msg[1];

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

	public Boolean registerRequest(String request) {
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

		String[] msg = request.split(" ");
		String idNpassword = msg[1];

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

	public Boolean searchbookrequest(String request) {
		String[] msg = request.split(" ");
		String bookname = msg[1];
		Scanner inputb = null;

		try {
			inputb = new Scanner(booklist);
		} catch (IOException ioe) {
			System.out.println("err ServerHandler: searchbookrequest: problem with booklist scanner");
		}

		while (inputb.hasNext()) {
			if (bookname.equals(inputb.nextLine() ) ) {
				System.out.println("book found by server");
//				toClient.writeBoolean(foundbook);
//				File indicatedbook = new File(bookname);
//				These two lines should be in run()

//				toClient.writeObject(indicatedbook);
//				next step, passing file to user
				inputb.close();
				return true;
			}
		}
		System.out.println("book not found by server");
		return false;
	}

	public String showbooklist() {
		String booknames = "";
		Scanner inputb = null;
		try {
			inputb = new Scanner(booklist);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

		if (inputb != null) {
			while (inputb.hasNext()) {
				booknames += inputb.nextLine()+ " ";
				System.out.println(booknames);
			}
		}
		return booknames;
	}

	public String showcontent(String request) {
		String[] commands = request.split(" ");
		String bookname = commands[1] + ".txt";
		File book = new File(bookname);
		Scanner inputc = null;
		try {
			inputc = new Scanner(book);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String content = "";
		if (inputc != null) {
			while (inputc.hasNext()) {
				content += inputc.nextLine() + "*";
			}
		}
		System.out.println(content);
		return content;
	}

	public Boolean listenRequest(String request) {
		if (request.startsWith("register")) {
			Boolean success = registerRequest(request);
			if(success) {
				toClient.println("registersuccess");
				toClient.flush();
				System.out.println("Responded to client (register success)");
			}
			else {
				toClient.println("registerfail");
				toClient.flush();
				System.out.println("Responded to client (register fail)");
			}
		}
		else if (request.startsWith("login")) {
			Boolean success = loginRequest(request);
			if (success) {
				toClient.println("loginsuccess");
				toClient.flush();
				String books = showbooklist();
				toClient.println(books);
				toClient.flush();
				System.out.println("Responded to client (log in success)");
			}
			else {
				toClient.println("loginfail");
				toClient.flush();
				System.out.println("Responded to client (log in fail)");
			}
		}
		else if (request.startsWith("logout")) {
			System.out.println("user logged out");
			return false;
		}
		else if (request.startsWith("search")) {
			Boolean found = searchbookrequest(request);
			if (found) {
				toClient.println("searchsuccess");
				toClient.flush();
				String content = showcontent(request);
				toClient.println(content);
				toClient.flush();
			}
			else {
				toClient.println("searchfail");
				toClient.flush();
			}
		}
		return true;
	}


	@Override
	public void run() {
		Boolean connected = true;
		try {
			fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()) );
			toClient = new PrintWriter(socket.getOutputStream());
		} catch (IOException ioe1){
			System.out.println("err ServerHandler: run: io setting problem, line65");
			ioe1.printStackTrace();
			connected = false;
		}

		while (connected) {
			System.out.println("It is connected now, listening to request. ");
			// Message msg = null;
			String request = "null";
			try {
				// msg = (Message)fromClient.readObject();
				request = fromClient.readLine();
			} catch (IOException e) {
				System.out.println("err ServerHandler: run: problem receiving message from client, line31");
				e.printStackTrace();
			}
			connected = listenRequest(request);
		}

	}


}
