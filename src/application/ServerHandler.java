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

	public int loginRequest(String request) {
		Scanner inputf = null;
		try {
			inputf = new Scanner(userinfo);
		} catch (FileNotFoundException e) {
			System.out.println("err ServerHandler: registerRequest: scanner setting problem");
			e.printStackTrace();
		}
		String[] msg = request.split(" ");
		// String idNpassword = msg[1];
		String id = msg[1];
		String password = msg[2];

		//return true if the user and password is in file
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
			String books = showbooklist();
			toClient.println(books);
			toClient.flush();
		}
		return 0;
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
		String id = msg[1];
		String password = msg[2];
		String[] info;

		while (inputf.hasNext()) {
			info = inputf.nextLine().split(" ");
			if (id.equals(info[0])) {
				System.out.println("User already registered");
				inputf.close();
				toClient.println("fail");
				toClient.flush();
				return false;
			}
		}

		try {
			inputf.close();
			outputf.write(id+" "+password + " 0\n");
//			setuserstate(id, 0);
			outputf.close();
			toClient.println("success");
			toClient.flush();
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

	public ArrayList<String> showcontent(String request) {
		String[] commands = request.split(" ");
		String bookname = commands[1] + ".txt";
		File book = new File(bookname);
		Scanner inputc = null;
		try {
			inputc = new Scanner(book);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		ArrayList<String> content = new ArrayList<String>();
//		String[] content = null;
		if (inputc != null) {
//			content = new String[1];
//			content[0] = inputc.nextLine();
//			int index = 0;
			while (inputc.hasNext()) {
				content.add(inputc.nextLine() );
			}
		}
		// System.out.println(content);
		return content;
	}

	public void setuserstate(String username, int state){
		Scanner inputf = null;
		File transfer = new File("alter.txt");
		FileWriter outputf = null;
//		FileWriter outputrewrite = null;
		try {
			inputf = new Scanner(userinfo);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public Boolean listenRequest(String request) {
		if (request.startsWith("register")) {
//			Boolean success = registerRequest(request);
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
//				toClient.print(true);
				toClient.println("searchsuccess");
				toClient.flush();
				// String content = showcontent(request);
				ArrayList<String> content = showcontent(request);

				int length = content.size();
				toClient.print(length);
//				toClient.println(length + "");
				toClient.flush();
				for (int i = 0; i < length; i++) {
					toClient.println(content.get(i));
					toClient.flush();
					// System.out.println();
				}
				// toClient.println(content);
				// toClient.flush();
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
				request = fromClient.readLine();
				// msg = (Message)fromClient.readObject();
			} catch (IOException e) {
//				System.out.println("err ServerHandler: run: problem receiving message from client, line31");
//				e.printStackTrace();
			}
			connected = listenRequest(request);
		}

	}


}
