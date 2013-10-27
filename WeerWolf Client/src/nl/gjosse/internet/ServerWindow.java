package nl.gjosse.internet;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ServerWindow {

	private JFrame frame;
	private JTextField textField;
	JTextArea textArea;
	JButton btnStop;
	JButton btnSend;
	
	Thread th;
	ServerThread st;
	
	public static boolean open = true;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerWindow window = new ServerWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ServerWindow() {
		initialize();
		setUpOutputs();
	}


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 737, 405);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnStart = new JButton("Start");
		btnStart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				st = new ServerThread();
				th = new Thread(st);
				th.start();
				btnSend.setEnabled(false);
			}
		});
		btnStart.setBounds(6, 348, 117, 29);
		frame.getContentPane().add(btnStart);
		
		btnStop = new JButton("Stop");
		btnStop.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				open = false;
				btnSend.setEnabled(true);
				System.out.println("Not Accepting Connections!");
			}
		});
		btnStop.setBounds(135, 348, 117, 29);
		frame.getContentPane().add(btnStop);
		
		btnSend = new JButton("Send");
		btnSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				sendCommand(textField.getText());
			}
		});
		btnSend.setBounds(614, 348, 117, 29);
		frame.getContentPane().add(btnSend);
		
		textField = new JTextField();
		textField.setBounds(324, 347, 278, 28);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 6, 725, 327);
		frame.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
	}
	
	protected void sendCommand(String text) {
		if(text.equalsIgnoreCase("addCards")); {
			System.out.println("Setting size of players..");
			for(MultiThreadedUser user : st.connections) {
				user.sendMessage("setsize "+st.connections.size());
			}
			for(MultiThreadedUser user : st.connections) {
				int id = user.id;
				String name = user.name;
				System.out.println("Adding card!");
				user.sendMessage("new_player "+name +" "+ id );
				for(MultiThreadedUser user2 : st.connections) {
					int id2 = user2.id;
					if(id2 != id) {
						System.out.println("Adding card!");
						user2.sendMessage("new_player "+name +" "+ id );
					}
				}
			}
		}
	}

	private void setUpOutputs() {
			  OutputStream out = new OutputStream() {
			    @Override
			    public void write(int b) throws IOException {
			      updateTextArea(String.valueOf((char) b));
			    }

			    @Override
			    public void write(byte[] b, int off, int len) throws IOException {
			     updateTextArea(new String(b, off, len));
			    }

			    @Override
			    public void write(byte[] b) throws IOException {
			      write(b, 0, b.length);
			    }
			  };

			  System.setOut(new PrintStream(out, true));
			  //System.setErr(new PrintStream(out, true));
	}

	protected void updateTextArea(String string) {
		textArea.append(string);
	}
}
