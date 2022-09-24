package com.eating.driver_appp.Model;

public class CurrentOrder_Model {

    String order_id,restaurant,time,customer,status,tip, delivery_fee, checkout_total, restaurant_price, user_name, phone_no,
            delivery_date, f_ready_time, address, waiting_status, drinks, dispatcher_id, priority, created_at_time;

    private boolean expanded;

    public CurrentOrder_Model(String order_id, String restaurant, String time, String customer, String status, String tip,
                              String delivery_fee, String checkout_total, String restaurant_price, String user_name,
                              String phone_no, String delivery_date, String f_ready_time, String address,
                              String drinks, String waiting_status, String dispatcher_id, String priority, String created_at_time) {
        this.order_id = order_id;
        this.restaurant = restaurant;
        this.time = time;
        this.customer = customer;
        this.status = status;
        this.tip = tip;
        this.delivery_fee = delivery_fee;
        this.checkout_total = checkout_total;
        this.restaurant_price = restaurant_price;
        this.user_name = user_name;
        this.phone_no = phone_no;
        this.delivery_date = delivery_date;
        this.f_ready_time = f_ready_time;
        this.address = address;
        this.waiting_status = waiting_status;
        this.drinks = drinks;
        this.dispatcher_id = dispatcher_id;
        this.priority = priority;
        this.created_at_time = created_at_time;

        this.expanded = false;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public void setDelivery_fee(String delivery_fee) {
        this.delivery_fee = delivery_fee;
    }

    public void setCheckout_total(String checkout_total) {
        this.checkout_total = checkout_total;
    }

    public void setRestaurant_price(String restaurant_price) {
        this.restaurant_price = restaurant_price;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public void setF_ready_time(String f_ready_time) {
        this.f_ready_time = f_ready_time;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setWaiting_status(String waiting_status) {
        this.waiting_status = waiting_status;
    }

    public void setDrinks(String drinks) {
        this.drinks = drinks;
    }

    public void setDispatcher_id(String dispatcher_id) {
        this.dispatcher_id = dispatcher_id;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public CurrentOrder_Model() {
    }

    public String getOrder_id() {
        return order_id;
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

    public String getStatus() {
        return status;
    }

    public String getTip() {
        return tip;
    }


    public String getDelivery_fee() {
        return delivery_fee;
    }

    public String getCheckout_total() {
        return checkout_total;
    }

    public String getRestaurant_price() {
        return restaurant_price;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public String getF_ready_time() {
        return f_ready_time;
    }

    public String getAddress() {
        return address;
    }

    public String getWaiting_status() {
        return waiting_status;
    }

    public String getDrinks() {
        return drinks;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getDispatcher_id() {
        return dispatcher_id;
    }

    public String getCreated_at_time() {
        return created_at_time;
    }

    public void setCreated_at_time(String created_at_time) {
        this.created_at_time = created_at_time;
    }
}
