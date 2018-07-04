package ru.tjm.net;

import java.io.IOException;
import java.net.*;

public class Net {

    private DatagramSocket socket = null;
    private InetAddress ip = null;
    private Thread send;
    private int port;

    public Net(int port) {
        this.port = port;
    }

    public boolean openConnection(String address) {
        try {
            // Cоздаваемый сокет присоединяется к любому свободному порту на локальной машине
            socket = new DatagramSocket();
            // Возвращает IP адрес удаленного узла
            ip = InetAddress.getByName(address);
        } catch (SocketException e) {
            e.printStackTrace();
            return false;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public String receive() {
        byte[] data = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String message = new String(packet.getData());
        return message;
    }


    public void send(final byte[] data) {
        send = new Thread(new Runnable() {
            public void run() {
                DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
                try {
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        send.start();
    }

}

// http://kek.ksu.ru/eos/Java/Tutorial_Java_AU/Glava19/Index1.htm - Сетевые средства Java
// http://kek.ksu.ru/eos/Java/Tutorial_Java_AU/Glava19/Index3.htm - udp