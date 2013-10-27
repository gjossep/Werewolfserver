package nl.gjosse.internet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MultiThreadedUser implements Runnable {
	
	Socket user;
	int id;
	int port;
	String name;
	boolean allowedIn;
	public MultiThreadedUser(Socket accept, int id, int port, boolean open) {
		user = accept;
		this.id = id;
		this.port = port;
		this.allowedIn = open;
	}	

	@Override
	public void run() {
		if(allowedIn == true) {
			System.out.println("User connected! Sending id!");
			sendMessage(""+id);

			try {
				System.out.println("DEBUG: "+port);
				BufferedReader br = new BufferedReader(new InputStreamReader(user.getInputStream()));
				double real = ((id + 15) * (id - 15)) * port;
				System.out.println(real);
				double response = Double.parseDouble(br.readLine());
				if(response == real) {
					sendMessage("correct");
					System.out.println("User is right!");
					name = br.readLine();
				} else {
					sendMessage("wrong");
					user.close();
				}
				
				Thread readIn = new Thread(new ReadIn(id, this, user));
				readIn.start();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			sendMessage(""+ (-1));
		}
	}
	
	public void sendMessage(String message) {
		try {
			PrintWriter pw = new PrintWriter(user.getOutputStream(), true);
			pw.println(message);
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
