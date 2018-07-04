package ru.luxoft.common;

public interface Connection {

    public static final int PORT = 8080;

    public void send(Message msg);

    public void close();

}
