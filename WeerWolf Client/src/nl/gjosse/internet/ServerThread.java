package nl.gjosse.internet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class ServerThread implements Runnable {

	static ServerSocket server;
	static int ids = 0;
	public static ArrayList<MultiThreadedUser> connections = new ArrayList<MultiThreadedUser>();
	public static int port = 9999;
	
	
	@Override
	public void run() {
		try {
			server = new ServerSocket(port);
			server.setReuseAddress(true);
			System.out.println("Server started!");
			System.out.println("IP: "+InetAddress.getLocalHost().getHostAddress());
			System.out.println("Port: "+port);
			System.out.println("Accepting Connections!");
			while(true) {
				MultiThreadedUser user = new MultiThreadedUser(server.accept(), ids, port, ServerWindow.open);
				Thread socket = new Thread(user);
				socket.start();
				connections.add(user);
				ids ++;

			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}


}
