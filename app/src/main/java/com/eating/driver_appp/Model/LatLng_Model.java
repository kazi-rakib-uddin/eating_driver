package com.eating.driver_appp.Model;

public class LatLng_Model {

    String driver_id, lat, lng, status, break_status, landmark;

    public LatLng_Model(String driver_id, String lat, String lng, String status, String break_status, String landmark) {
        this.driver_id = driver_id;
        this.lat = lat;
        this.lng = lng;
        this.status = status;
        this.break_status = break_status;
        this.landmark = landmark;
    }

    public LatLng_Model(String driver_id, String lat, String lng) {
        this.driver_id = driver_id;
        this.lat = lat;
        this.lng = lng;
    }

    public LatLng_Model() {
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBreak_status() {
        return break_status;
    }

    public void setBreak_status(String break_status) {
        this.break_status = break_status;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }
}
