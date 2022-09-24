package com.eating.driver_appp.Model;

public class Message_Model {

    String message_id, message, sender_id, driver_id;
    private long time_tamp;

    public Message_Model() {
    }

    public Message_Model(String message, String sender_id, String driver_id, long time_tamp) {
        this.message = message;
        this.sender_id = sender_id;
        this.time_tamp = time_tamp;
        this.driver_id = driver_id;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public long getTime_tamp() {
        return time_tamp;
    }

    public void setTime_tamp(long time_tamp) {
        this.time_tamp = time_tamp;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }
}
