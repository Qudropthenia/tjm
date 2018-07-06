package ru.luxoft.common;

import java.io.*;
import java.net.Socket;

public class ConnectionImpl implements Connection, Runnable {
    private Socket socket;
    private ConnectionListener connectionListener;
    private boolean running = true;
    private OutputStream out;
    private InputStream in;

    public ConnectionImpl(Socket socket, ConnectionListener connectionListener) {
        try {
            this.socket = socket;
            this.connectionListener = connectionListener;
            out = socket.getOutputStream();
            in = socket.getInputStream();
            Thread t = new Thread(this);
            t.setPriority(Thread.MIN_PRIORITY);
            t.start();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void send(Message msg) {
        try {
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    @Override
    public void close() {
        running = false;
    };

    @Override
    public void run() {
        while (running) {
            try {
                // Ожидание сообщений от клиента
                int amount = in.available();
                if (amount != 0) {
                    ObjectInputStream objIn = new ObjectInputStream(in);
                    Message msg = (Message) objIn.readObject();
                    connectionListener.recivedContent(msg);
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
