package application;

import java.io.Serializable;

public class ServerRespond implements Serializable{
	private static final long serialVersionUID = 1L;

	Boolean success;
	String message;

	public ServerRespond(Boolean s) {
		success = s;
	}

	public ServerRespond(Boolean s, String msg) {
		success = s;
		message = msg;
	}

	public Boolean getSuccess(){
		return success;
	}

	public String getMessage() {
		return message;
	}
}
