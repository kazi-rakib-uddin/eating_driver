package com.eating.driver_appp.Model;

import android.content.Context;
import android.content.SharedPreferences;

public class User {

    private Context context;
    private String driver_id, mobile_no, email, name, area;
    private SharedPreferences sharedPreferences;

    public User(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("login_details",Context.MODE_PRIVATE);
    }

    public String getDriver_id() {

        driver_id = sharedPreferences.getString("driver_id","");
        return driver_id;
    }

    public void setDriver_id(String driver_id) {

        this.driver_id = driver_id;
        sharedPreferences.edit().putString("driver_id",driver_id).commit();
    }

    public String getMobile_no() {

        mobile_no = sharedPreferences.getString("mobile_no","");
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {

        this.mobile_no = mobile_no;
        sharedPreferences.edit().putString("mobile_no",mobile_no).commit();
    }

    public String getEmail() {

        email = sharedPreferences.getString("email","");
        return email;
    }

    public void setEmail(String email) {

        this.email = email;
        sharedPreferences.edit().putString("email",email).commit();
    }

    public String getName() {

        name = sharedPreferences.getString("name","");
        return name;
    }

    public void setName(String name) {

        this.name = name;
        sharedPreferences.edit().putString("name",name).commit();
    }

    public String getArea() {
        area = sharedPreferences.getString("area","");
        return area;
    }

    public void setArea(String area) {
        this.area = area;
        sharedPreferences.edit().putString("area",area).commit();
    }

    public void removeUser()
    {
        sharedPreferences.edit().clear().commit();
    }


}
