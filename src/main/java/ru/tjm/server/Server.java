package ru.tjm.server;

import java.net.DatagramSocket;
import java.net.SocketException;

public class Server {

    private int port;
    private DatagramSocket socket;
    // Поток для запуска сервера
    private Thread serverRun, manage, receive;
    private boolean running = false;

    public Server(int port) {
        this.port = port;
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        serverRun = new Thread(new Runnable() {
            public void run() {
                running = true;
                manage();
                receive();
            }
        }, "serverRun");
    }

    private void manage() {
        manage = new Thread(new Runnable() {
            public void run() {
                while (running) {

                }
            }
        });
        manage.start();
    }

    private void receive() {
        receive = new Thread(new Runnable() {
            public void run() {
                while (running) {

                }
            }
        });
        receive.start();
    }
}

/**
 * manage() - метод отвечающий за работу с клиентами:
 * удаление, занесение в базу данных если они активны,
 * проверка на валидность и т.п.
 *
 * receive() - метод обработка входищих соединений
 */