package application;

import java.io.*;
import java.net.*;
import java.util.*;

// This is a runnable object that handles one client
public class ServerHandler implements Runnable{
	Socket socket = null;
	BufferedReader fromClient = null;
	PrintWriter toClient = null;
	File userinfo = null;
	File booklist = null;

	// The constructor received the socket that is generated when accepting a client
	// from Serverlib.java and assign it to the socket instance in the class
	// The constructor also creates two File object,
	// userinfo to store user id and password and
	// booklist to store the list of booknames
	public ServerHandler(Socket socket) {
		this.socket = socket;
		userinfo = new File("userinfo.txt");
		booklist = new File("booklist.txt");
	}

	// This method would be called by listenRequest method
	// This method execute the user's request of logging in and
	// takes in user's id and password (using parameter request)
	// and decide if the user can sign in (cannot if the account has been signed in /
	// password is wrong/ user does not exist)
	// The user id and password are the second and third element of the String array
	// that is made by splitting the String variable request by blank space
	public int loginRequest(String request) {
		Scanner inputf = null;
		try {
			inputf = new Scanner(userinfo);
		} catch (FileNotFoundException e) {
			System.out.println("err ServerHandler: registerRequest: scanner setting problem");
			e.printStackTrace();
		}
		String[] msg = request.split(" ");
		String id = msg[1];
		String password = msg[2];

		String[] info;
		Boolean found = false;
		while (inputf.hasNext()) {
			System.out.println("Checked user list");
			info = inputf.nextLine().split(" ");
			System.out.println(info[0]);
			System.out.println(id);


			if (id.equals(info[0])) {
				if (info[2].equals("1")) {
					toClient.println("alreadyloggedin");
					toClient.flush();
					return -1;
				}
				found = true;
				System.out.println(found);
				if (password.equals(info[1])) {
					toClient.println("success");
					setuserstate(id, 1);
					System.out.println("User and password matched");
					break;
				}
				else {
					toClient.println("password wrong");
					System.out.println("password wrong");
					break;
				}
			}
		}

		if (!found) {
			toClient.println("user doesn't exist");
			System.out.println("user doesn't exist");
		}
		toClient.flush();

		if (found) {
			showbooklist();
		}
		return 0;
	}

	// This method would be called by listenRequest method
	// This method receives user's request of registering and
	// takes in user id & password (by taking parameter request)
	// and decide if the user can register (cannot if user id is used)
	// The user id and password are the second and third element of the String array
	// that is made by splitting the String variable request by blank space
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

		// splitting the String variable request
		// the format of request is "register user_id user_password"
		String[] msg = request.split(" ");
		String id = msg[1];
		String password = msg[2];

		// String array info is used for splitting user id & password in userinfo file
		// user information is stored in userinfo file in the format of
		// "user_id user_password user_state"
		String[] info;
		while (inputf.hasNext()) {
			info = inputf.nextLine().split(" ");
			//check if user id and the register user id is the same
			if (id.equals(info[0])) {
				System.out.println("User already registered");
				inputf.close();
				toClient.println("fail");
				toClient.flush();
				// the method is finished after executing this if statement
				// since this user id is used
				return false;
			}
		}

		// if the user id is checked and is never used,
		// write the user id and password to userinfo file and set user's state
		// to 0, meaning the user is not logged in
		try {
			inputf.close();
			outputf.write(id+" "+password + " 0\n");
			outputf.close();
			toClient.println("success");
			toClient.flush();
		} catch (IOException e) {
			System.out.println("err ServerHandler: registerRequest: FileWriter writing problem");
			e.printStackTrace();
		}
		return true;

	}

	// This method would be called by listenRequest method
	// This method receives user's request of searching for books and
	// takes in the book name that is entered by user (by taking parameter request)
	// and decide if the book can be found (cannot if the book name is wrong / book doesn't exist)
	// the user input book name is the second element of the String array
	// that is made by splitting the String variable request by blank space
	public Boolean searchbookrequest(String request) {
		// the String request would be "search userinput_bookname"
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
				inputb.close();
				toClient.println("searchsuccess");
				toClient.flush();
				return true;
			}
		}

		toClient.println("searchfail");
		toClient.flush();
		System.out.println("book not found by server");
		return false;
	}

	// This method would be called by loginRequest method,
	// it would show the list of books in the second window
	// it would send a String of list of book names that are separated by
	// blank space
	public void showbooklist() {
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
		toClient.println(booknames);
		toClient.flush();
	}

	// This method would be called by listenRequest method
	// This method would show the book content that is requested by user
	// the book name would take the String request and withdraw the book name from it
	// request would be in formate "search bookname"
	// ArrayList<String>
	public void showcontent(String request) {
		String[] commands = request.split(" ");
		// get the absolute file name of the file by adding .txt
		String bookname = commands[1] + ".txt";
		File book = new File(bookname);
		Scanner inputc = null;
		try {
			inputc = new Scanner(book);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		ArrayList<String> content = new ArrayList<String>();
		if (inputc != null) {
			while (inputc.hasNext()) {
				content.add(inputc.nextLine() );
			}
		}

		int length = content.size();
		toClient.print(length);
		toClient.flush();
		for (int i = 0; i < length; i++) {
			toClient.println(content.get(i));
			toClient.flush();
		}
	}

	// This method would be called by loginRequest & listenRequest method
	// This method is to set the state of user to 1 (logged in) when one user
	// logged in to their account and to set the state of user to 0 (not logged in)
	// when they log out
	// It takes in user id & state(0 or 1) as parameter
	public void setuserstate(String username, int state){
		Scanner inputf = null;
		File transfer = new File("alter.txt");
		FileWriter outputf = null;
		try {
			inputf = new Scanner(userinfo);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// changing the state of the user when copying user information
		// to another file alter.txt, and then put it back to userinfo file
		try {
			outputf = new FileWriter(transfer);
			while (inputf.hasNext()) {
				String[] temp = inputf.nextLine().split(" ");
				outputf.write(temp[0]+" "+temp[1]+" ");
				if (temp[0].equals(username)) {
					outputf.write(state+"\n");
				}
				else {
					outputf.write(temp[2] + "\n");
				}
			}
			outputf.close();
			inputf.close();

			FileWriter outputrewrite = new FileWriter(userinfo);
			Scanner inputalter = new Scanner(transfer);
			while (inputalter.hasNext()) {
				outputrewrite.write(inputalter.nextLine()+"\n");
			}
			outputrewrite.close();
			inputalter.close();

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	// This method would be called by run method
	// This method takes in user's request and check what user's request is
	// by checking the first word in the String request.
	// According to different request, different method would be called
	// This method also returns false if the user want to log out
	public Boolean listenRequest(String request) {
		if (request.startsWith("register")) {
			registerRequest(request);
		}
		else if (request.startsWith("login")) {
			loginRequest(request);
		}
		else if (request.startsWith("logout")) {
			System.out.println("user logged out");
			String[] info = request.split(" ");
			setuserstate(info[1], 0);
			return false;
		}
		else if (request.startsWith("search")) {
			Boolean found = searchbookrequest(request);
			if (found) {
				showcontent(request);
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

		// listening to the client's request (until client wants to log out)
		while (connected) {
			System.out.println("It is connected now, listening to request. ");
			String request = "null";
			try {
				request = fromClient.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			connected = listenRequest(request);
		}

	}

}
