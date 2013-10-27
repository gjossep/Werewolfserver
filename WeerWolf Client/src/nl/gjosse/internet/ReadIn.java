package nl.gjosse.internet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReadIn implements Runnable {
	public static int id;
	public static MultiThreadedUser user;
	public static Socket socket;
	
	public ReadIn(int id, MultiThreadedUser multiThreadedUser, Socket socket) {
		this.id = id;
		this.user = user;
		this.socket = socket;
		if(socket == null) {
			System.out.println("null");
		}
	}

	@Override
	public void run() {
		System.out.println("Starting Server ReadIn Thread on User "+id);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String input;
			while((input = br.readLine()) != null) {
				System.out.println("Input is "+input);
				if(input.contains("add_selection")) {
					newSelection(input);
				} else if(input.contains("removeSelection")) {
					//removeSelection(input);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private void newSelection(String input) {
		System.out.println(input);
		String[] split = input.split(" ");
		int idToSelect = Integer.parseInt(split[1]);
		int rColor = Integer.parseInt(split[2]);
		int gColor = Integer.parseInt(split[3]);
		int bColor = Integer.parseInt(split[4]);

		for(MultiThreadedUser otherUser : ServerThread.connections) {
			System.out.println("ID of User:"+otherUser.id);
			int id2 = otherUser.id;
			if(id2 != id) {
				otherUser.sendMessage("new_selection "+idToSelect+" "+rColor+" "+gColor+" "+bColor);
			}
		}
	}

	private void removeSelection(String input) {
		System.out.println(input);
		String[] split = input.split(" ");
		int idToRemove = Integer.parseInt(split[1]);
		
		for(MultiThreadedUser otherUser : ServerThread.connections) {
				otherUser.sendMessage("remove_selection "+idToRemove);
		}
	}
}
