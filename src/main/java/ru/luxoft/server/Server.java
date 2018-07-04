package ru.luxoft.server;

import ru.luxoft.common.Connection;
import ru.luxoft.common.ConnectionImpl;
import ru.luxoft.common.ConnectionListener;
import ru.luxoft.common.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashSet;
import java.util.Set;

public class Server implements ConnectionListener {

    // Список с соблюдением порядка
    private Set<Connection> connections;
    private ServerSocket serverSocket;

    public Server() {
        try {
            serverSocket = new ServerSocket(Connection.PORT);
            connections = new LinkedHashSet<>();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startServer() {
        System.out.println("Server started.");
        while (true) {
            try {
                // Ожидание подключения и получение клюентского сокета
                Socket socket = serverSocket.accept();
                connections.add(new ConnectionImpl(socket, this));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void connectionCreated(Connection c) {
        connections.add(c);
        c.close();
        System.out.println("Connection was added");
    }

    @Override
    public void connectionClosed(Connection c) {
        connections.remove(c);
        c.close();
        System.out.println("Connection was closed");
    }

    @Override
    public void connectionException(Connection c, Exception ex) {
        connections.remove(c);
        c.close();
        System.out.println("Connection was closed");
        ex.printStackTrace();
    }

    @Override
    public void recivedContent(Message msg) {
        for (Connection c: connections) {
            c.send(msg);
        }
    }
}
