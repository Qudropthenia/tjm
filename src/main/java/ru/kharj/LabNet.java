package ru.kharj;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;


public class LabNet {
	private static int width = 1;
	private static int height = 1;
	private static JTable table;
	private static JFrame frame;
	private static Thread netThread;

	public static void main(String[] args) {
		frame = new JFrame();
		frame.setSize(640, 480);
		frame.setTitle("Java lab");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//width height fields
		final JTextField widthField = new JTextField("2");
		widthField.setPreferredSize(new Dimension(200,20));
		final JTextField heightField = new JTextField("1");
		heightField.setPreferredSize(new Dimension(200,20));
		JButton createButton = new JButton("Create");
		
		
		createButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String widthText = widthField.getText();
				String heightText = heightField.getText();
				try{
					 width = Integer.parseInt(widthText);
					 height = Integer.parseInt(heightText);
					 CreateTable();
					 new ClientThread();
					 
				}catch(NumberFormatException ex){
					ex.printStackTrace();
				
				}
				
			}
		});
		frame.setLayout(new BorderLayout());
		frame.getContentPane().add(widthField, BorderLayout.LINE_START);
		frame.getContentPane().add(heightField, BorderLayout.LINE_END);
		frame.getContentPane().add(createButton, BorderLayout.PAGE_START);
		
		frame.pack();
		frame.setVisible(true);
	}
	static class MyTableCellRender extends DefaultTableCellRenderer {  
		   
		public MyTableCellRender() {  
		setOpaque(true);  
		}  
		   
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {  
		int number = 0;
if(value!=null)
	number= (int)value;  
		
		int val = number;
		    
		        if(val < 0) {  
		        setForeground(Color.black);  
		        setBackground(Color.red);  
		        }  
		        else {  
		        setForeground(Color.black);  
		        setBackground(Color.white);  
		        } 
		setText(value !=null ? value.toString() : "");  
		return this;  
		}  
		}   
	static class ClientThread implements Runnable {

	     private ServerSocket sso;

	     public ClientThread() {

				netThread = new Thread(this);
				netThread.start();
	     }

	     public void run() {
				 try {
			    	 InetAddress ipAddress = InetAddress.getByName("127.0.0.1");
						Socket socket = new Socket(ipAddress, 27125);
						InputStream sin = socket.getInputStream();
						OutputStream sout = socket.getOutputStream();
						DataInputStream in = new DataInputStream(sin);
						DataOutputStream out = new DataOutputStream(sout);
						for(int i=0;i<width;i++) 
							for(int j=0;j<height;j++) {
								try{
								int line = in.readInt();// ���� ���� ������ ������� ������ ������.
								System.out.println("got this : " + line);
								table.setValueAt(line, i, j);
								
								}catch(EOFException e){
									
								}
						}
						in.close();
						out.close();
						socket.close();
	             } catch (IOException e) {
	                 e.printStackTrace();
	             }
			}		
	}
	static void CreateTable(){
		frame.getContentPane().removeAll();
		table = new JTable(width, height);
		table.setDefaultRenderer(Object.class, new MyTableCellRender());
		table.setRowHeight(20);
		//table.setPreferredSize(new Dimension(40*width, 20*height));
		table.setValueAt(5, 0, 0);
		table.setValueAt(5, 1, 0);
		
		frame.getContentPane().add(table);
		frame.pack();
	
	}

}
