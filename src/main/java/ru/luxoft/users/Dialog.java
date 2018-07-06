package ru.luxoft.users;

public class Dialog {
    private User from;
    private String msg;
    private User to;

    public Dialog(User from, String msg, User to) {
        this.from = from;
        this.msg = msg;
        this.to = to;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

}
