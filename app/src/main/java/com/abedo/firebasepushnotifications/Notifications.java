package com.abedo.firebasepushnotifications;

/**
 * created by Abedo95 on 11/26/2019
 */
public class Notifications {

    private String from , message;

    public Notifications(String from, String message) {
        this.from = from;
        this.message = message;
    }

    public Notifications() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Notifications{" +
                "from='" + from + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
