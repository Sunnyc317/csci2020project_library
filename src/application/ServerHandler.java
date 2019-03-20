package application;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerHandler implements Runnable{
	Socket socket = null;
	ObjectInputStream fromClient = null;
	ObjectOutputStream toClient = null;
	// DataOutputStream toClientdata = null;
	// BufferedReader fromClient = null;
	// PrintWriter toClient = null;
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

//	public Boolean loginRequest(String msg) {
//		Scanner inputf = null;
//		try {
//			inputf = new Scanner(userinfo);
//		} catch (FileNotFoundException e) {
//			System.out.println("err ServerHandler: registerRequest: scanner setting problem");
//			e.printStackTrace();
//		} catch (IOException e) {
//			System.out.println("err ServerHandler: registerRequest: filewriter setting problem");
//			e.printStackTrace();
//		}
//
//		String id = msg.getId();
//		String password = msg.getPassword();
//		String idNpassword = id+":"+password;
//
//		//return true if the user and password is in file
//		while (inputf.hasNext()) {
//			if (inputf.nextLine().equals(idNpassword)) {
//				System.out.println("User and password matched");
//				inputf.close();
//				return true;
//			}
//		}
//		//otherwise false
//		return false;
//	}

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

	public Boolean searchbookrequest(Message msg) {
		String bookname = msg.getText();
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

//	public void listenRequest(String request) {
//
//		if (request.startsWith("register")) {
//			Boolean success = registerRequest(msg);
//			ServerRespond respond = new ServerRespond(success);
//			try {
//				toClient.writeObject(respond);
////					toClient.flush();
//				System.out.println("Responded to client (register)");
//			} catch (IOException e) {
//				System.out.println("err ServerHandler: run: sending Boolean to Client fail (register), line80");
//				e.printStackTrace();
//			}
//		}
//		else if (msg.getType() == 1) {
//			Boolean success = loginRequest(msg);
//			ServerRespond respond = new ServerRespond(success);
//			try {
//				toClient.writeObject(respond);
////					toClient.flush();
//				System.out.println("Responded to client (log in)");
//			} catch (IOException e) {
//				System.out.println("err ServerHandler: run: sending Boolean to Client fail (log in), line129");
//				e.printStackTrace();
//			}
//		}
//		else if (msg.getType() == 2) {
//			connected = false;
//			System.out.println("user logged out");
//		}
//		else if (msg.getType() == 3) {
//			Boolean found = searchbookrequest(msg);
//			ServerRespond foundbook = new ServerRespond(found);
//			try {
//				toClientdata.writeBoolean(found);
//			} catch (IOException e) {
//				System.out.println("err ServerHandler: msgtype(3): Fail to respond to Client");
//				e.printStackTrace();
//			}
//	}


	@Override
	public void run() {
		Boolean connected = true;
		try {
			fromClient = new ObjectInputStream(socket.getInputStream());
			toClient = new ObjectOutputStream(socket.getOutputStream());
			// toClientdata = new DataOutputStream(socket.getOutputStream());
			// fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()) );
			// toClient = new PrintWriter(socket.getOutputStream());
		} catch (IOException ioe1){
			System.out.println("err ServerHandler: run: io setting problem, line65");
			ioe1.printStackTrace();
			connected = false;
		}

		while (connected) {
			System.out.println("It is connected now, listening to request. ");
			Message msg = null;
			String request;
			try {
				msg = (Message)fromClient.readObject();
				// request = fromClient.readLine();
			} catch (ClassNotFoundException | IOException e) {
				System.out.println("err ServerHandler: run: problem receiving message from client, line31");
				e.printStackTrace();
			}

			// listenRequest(request);



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
				System.out.println("user logged out");
			}
			else if (msg.getType() == 3) {
				Boolean found = searchbookrequest(msg);
				ServerRespond foundbook = new ServerRespond(found);
//				try {
//					toClientdata.writeBoolean(found);
//				} catch (IOException e) {
//					System.out.println("err ServerHandler: msgtype(3): Fail to respond to Client");
//					e.printStackTrace();
//				}
//				String bookname = msg.getText();
//				Boolean foundbook = false;
//				while (inputb.hasNext()) {
//					if (bookname.getmessage().equals(inputb.nextLine() ) ) {
//						foundbook = true;
//					}
//					toClient.writeBoolean(foundbook);
//					if (foundbook) {
//						File indicatedbook = new File(filename.getmessage());
//						toClient.writeObject(indicatedbook);
//					}
//				}
			}
		}
	}


}
