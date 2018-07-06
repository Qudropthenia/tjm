package ru.luxoft.client;

import ru.luxoft.common.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientUI implements ConnectionListener{
    private Connection c;

    private JTextField tf_addres;
    private JButton btn_connection;
    private JTextArea textArea1;
    private JTextField tf_msg;
    private JButton btn_send;
    private JPanel p_main;

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientUI().p_main.setVisible(true);
            }
        });
    }

    public ClientUI() {
        setState(false);
        p_main.setSize(600, 600);
        JFrame frame = new JFrame("Test");
        frame.setContentPane(p_main);
        frame.setLocationRelativeTo(null);
        frame.setSize(600, 600);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        btn_connection.addActionListener(this::AL_btn_connection);
        btn_send.addActionListener(this::AL_btn_send);
    }

    // ActionListener for component
    private void AL_btn_connection(ActionEvent e) {
        try {
            String host = tf_addres.getText().trim();
            Socket socket = new Socket(InetAddress.getByName(host), Connection.PORT);
            c = new ConnectionImpl(socket, this);
            connectionCreated(c);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void AL_btn_send(ActionEvent e) {
        try {
            String content = tf_msg.getText().trim();
            Message msg = new MessageImpl(InetAddress.getByName("localhost").getHostName(), content, Message.CONTENT_TYPE);
            c.send(msg);
            System.out.println(msg.getContent());
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }
    }

    private void setState(boolean isConnected) {
        textArea1.setEditable(isConnected);
        tf_msg.setEnabled(isConnected);
        btn_send.setEnabled(isConnected);

        tf_addres.setEnabled(!isConnected);
        btn_connection.setEnabled(!isConnected);
    }

    @Override
    public void connectionCreated(Connection c) {
        System.out.println("Client connection was created");
        setState(true);
        this.c = c;
    }

    @Override
    public void connectionClosed(Connection c) {
        System.out.println("Client connection was closed");
    }

    @Override
    public void connectionException(Connection c, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void recivedContent(Message msg) {
        textArea1.append("\n");
        textArea1.append(msg.toString());
    }
}
