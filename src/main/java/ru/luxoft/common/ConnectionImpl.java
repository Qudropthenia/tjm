package ru.luxoft.common;

import java.io.*;
import java.net.Socket;

public class ConnectionImpl implements Connection, Runnable {
    private Socket socket;
    private ConnectionListener connectionListener;
    private boolean running = true;
    private OutputStream out;

    public ConnectionImpl(Socket socket, ConnectionListener connectionListener) {
        this.socket = socket;
        this.connectionListener = connectionListener;
        Thread t = new Thread(this);
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }

    @Override
    public void send(Message msg) {
        try(ObjectOutputStream objOut = new ObjectOutputStream(out)) {
            objOut.writeObject(out);
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
                InputStream in = socket.getInputStream();
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
