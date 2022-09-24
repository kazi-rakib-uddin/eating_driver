package com.eating.driver_appp.Model;

public class Chat_User_Model {

    String dispatcher_id, dispatcher_name, driver_id, driver_name;


    public Chat_User_Model() {
    }


    public Chat_User_Model(String dispatcher_id, String dispatcher_name, String driver_id, String driver_name) {
        this.dispatcher_id = dispatcher_id;
        this.dispatcher_name = dispatcher_name;
        this.driver_id = driver_id;
        this.driver_name = driver_name;
    }

    public String getDispatcher_id() {
        return dispatcher_id;
    }

    public void setDispatcher_id(String dispatcher_id) {
        this.dispatcher_id = dispatcher_id;
    }

    public String getDispatcher_name() {
        return dispatcher_name;
    }

    public void setDispatcher_name(String dispatcher_name) {
        this.dispatcher_name = dispatcher_name;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }
}
