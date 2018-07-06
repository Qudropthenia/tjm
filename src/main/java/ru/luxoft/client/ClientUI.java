package ru.luxoft.client;

import ru.luxoft.common.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class ClientUI implements ConnectionListener{
    private Connection c;

    private JFrame frame = new JFrame("Test");
    private JTextField tf_host;
    private JButton btn_connection;
    private JTextArea textArea1;
    private JTextField tf_msg;
    private JButton btn_send;
    private JPanel p_main;
    private JTextField tf_login;
    private JLabel l_login;
    private JLabel l_id;
    private JTextField tf_id;
    private JLabel l_host;
    private JLabel l_port;
    private JTextField tf_port;
    private JPanel p_auth;
    private JList list1;

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
//        frame = new JFrame("Test");
        frame.setContentPane(p_main);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setSize(600, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final Random random = new Random();
        tf_id.setText(String.valueOf(random.nextInt(9999)));
        btn_connection.addActionListener(this::AL_btn_connection);
        btn_send.addActionListener(this::AL_btn_send);
    }

    // ActionListener for component
    private void AL_btn_connection(ActionEvent e) {
        try {
            String host = tf_host.getText().trim();
//            Socket socket = new Socket(InetAddress.getByName(host), Connection.PORT);
            if (tf_login.getText().trim().length() == 0 ||
                    tf_id.getText().trim().length() == 0 ||
                    tf_port.getText().trim().length() == 0 ||
                    tf_login.getText().trim().length() == 0) return;
            Socket socket = new Socket(InetAddress.getByName(host), Integer.parseInt(tf_port.getText().trim()));
            c = new ConnectionImpl(socket, this);
            connectionCreated(c);
        } catch (IOException e1) {
            switch (e1.getMessage()) {
                case "Connection refused: connect": {
                    JOptionPane.showMessageDialog(null, "Ошибка при подключении к срверу", "Ошибка соединения", JOptionPane.ERROR_MESSAGE);
                } break;
                default: { }
            }
//            e1.printStackTrace();
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

//        p_auth.setVisible(!isConnected);
        tf_host.setEnabled(!isConnected);
        tf_port.setEnabled(!isConnected);
        tf_login.setEnabled(!isConnected);
        tf_id.setEnabled(!isConnected);
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
