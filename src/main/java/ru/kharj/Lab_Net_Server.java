package ru.kharj;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.SocketSecurityException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Lab_Net_Server {
	private static JFrame frame;
	private static JLabel textLabel;
	private static Thread t;
	public static void main(String[] args) {
		frame = new JFrame();
		frame.setSize(640, 480);
		frame.setTitle("Java server");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		//width height fields
		textLabel = new JLabel("");
		textLabel.setPreferredSize(new Dimension(200,20));
		
		//frame.setLayout(new BorderLayout());
		frame.getContentPane().add(textLabel);
		frame.addWindowListener(new WindowAdapter() {
			   public void windowClosing(WindowEvent evt) {
				     //disconnect
				   t.stop();
				   frame.dispose();
			   }
			  });
		frame.pack();
		frame.setVisible(true);
		try {
			server();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void server() throws IOException{
		textLabel.setText("Connecting");
		int port = 27125;
		ServerSocket ss = new ServerSocket(port);
		textLabel.setText("Waiting for a client...\n");
		new ServerThread(ss);
		
		//Socket socket = ss.accept(); 
		//textLabel.setText("Got a client...\n");			
		/*while (true) {
			 try {
                 Socket socket1 = ss.accept();
                 new ConnectionHandler(socket1);
             } catch (IOException e) {
                 e.printStackTrace();
             }
		}	*/		
	}
	
	static class ServerThread implements Runnable {

	     private ServerSocket sso;

	     public ServerThread(ServerSocket serv) {
	    	sso = serv;
	         t = new Thread(this);
	         t.start();
	     }

	     public void run() {
	    	 while (true) {
				 try {
	                 Socket socket1 = sso.accept();
	                 textLabel.setText("Got a client...\n");
	                 new ConnectionHandler(socket1);
	             } catch (IOException e) {
	                 e.printStackTrace();
	             }
			}		
	     }
	}
	
	static class ConnectionHandler implements Runnable {

	     private Socket socket;

	     public ConnectionHandler(Socket socket) {
	         this.socket = socket;
	         Thread t = new Thread(this);
	         t.start();
	     }

	     public void run() {
	         try {
	            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

				//DataOutputStream out = new DataOutputStream(oos);
	           for(int i=0;i<2;i++){
	        	   int r = (int)(100.0 - Math.random()*200.0);
	        	   System.out.println("Sent "+r);
	        	   oos.writeInt(r);
	        	   oos.flush();
	            try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	           }
	           //  oos.close();
	             socket.close();

	     		textLabel.setText("Disconnected");
	         } catch (IOException e) {
	             e.printStackTrace();
	         }
	     }
	}
}
	
