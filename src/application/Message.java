package application;

import java.io.Serializable;

public class Message implements Serializable{

	private static final long serialVersionUID = 1L;

	String id;
	String password;
	int type;

	public Message(String id, String password, int type) {
		this.id = id;
		this.password = password;
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public int getType() {
		return type;
		// type =
		// 0 -> request register
		// 1 -> request login
		// 2 -> request book
	}
}
