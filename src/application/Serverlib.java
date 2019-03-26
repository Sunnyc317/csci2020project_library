package application;

import java.net.*;
import java.io.*;

public class Serverlib{
    int port = 1998;
    ServerSocket sSocket = null;

    // Serverlib constructor, create server socket using a port number
    public Serverlib() {
        try {
            sSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("err Serverlib: ServerSocket setting problem");
            e.printStackTrace();
        }
    }

    public void start() throws IOException {
        // This loop would receive Clients' connection request constantly
        // Every new Client that requests a connection would create a new thread
        while(true) {
            Socket socket = sSocket.accept();
            System.out.println("Serverlib: run: reached here, socket accepted");
            ServerHandler handle = new ServerHandler(socket);
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
    }


}
