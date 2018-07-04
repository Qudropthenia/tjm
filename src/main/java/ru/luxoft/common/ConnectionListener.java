package ru.luxoft.common;

public interface ConnectionListener {

    public void connectionCreated(Connection c);

    public void connectionClosed(Connection c);

    public void connectionException(Connection c, Exception ex);

    public void recivedContent(Message msg);

}
