package com.eating.driver_appp.Model;

public class Order_Model {
    String id,restaurant,time,customer,tip,status;

    public Order_Model(String id, String restaurant, String time, String customer, String tip, String status) {
        this.id = id;
        this.restaurant = restaurant;
        this.time = time;
        this.customer = customer;
        this.tip = tip;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public String getTime() {
        return time;
    }

    public String getCustomer() {
        return customer;
    }

    public String getTip() {
        return tip;
    }

    public String getStatus() {
        return status;
    }
}
