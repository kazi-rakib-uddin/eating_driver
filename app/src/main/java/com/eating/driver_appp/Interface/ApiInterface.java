package com.eating.driver_appp.Interface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login.php")
    Call<String> login(@Field("driver_id") String driver_id, @Field("pass") String pass);

    @FormUrlEncoded
    @POST("fetch_order.php")
    Call<String> fetch_order(@Field("order_id") String order_id);

    @FormUrlEncoded
    @POST("fetch_complete_order.php")
    Call<String> fetch_complete_order(@Field("driver_id") String driver_id);

    @FormUrlEncoded
    @POST("fetch_out_for_delivery.php")
    Call<String> fetch_out_for_delivery(@Field("driver_id") String driver_id);

    @FormUrlEncoded
    @POST("fetch_order_history.php")
    Call<String> fetch_order_history(@Field("driver_id") String driver_id, @Field("date") String date);

    @FormUrlEncoded
    @POST("fetch_cancel_order.php")
    Call<String> fetch_cancel_order(@Field("driver_id") String driver_id);

    @FormUrlEncoded
    @POST("accept.php")
    Call<String> accept(@Field("id") String id, @Field("status") String status, @Field("priority") String priority,
                        @Field("res_name") String res_name, @Field("time") String time, @Field("dispatcher_id") String dispatcher_id);

    @FormUrlEncoded
    @POST("reached.php")
    Call<String> reached(@Field("id") String id, @Field("status") String status, @Field("priority") String priority,
                        @Field("res_name") String res_name, @Field("time") String time, @Field("dispatcher_id") String dispatcher_id);

    @FormUrlEncoded
    @POST("out_for_delivery.php")
    Call<String> out_for_delivery(@Field("id") String id, @Field("status") String status, @Field("priority") String priority,
                                  @Field("res_name") String res_name, @Field("time") String time, @Field("dispatcher_id") String dispatcher_id);

    @FormUrlEncoded
    @POST("completed.php")
    Call<String> completed(@Field("id") String id, @Field("status") String status, @Field("priority") String priority,
                           @Field("res_name") String res_name, @Field("time") String time, @Field("driver_id") String driver_id,
                           @Field("dispatcher_id") String dispatcher_id);

    @FormUrlEncoded
    @POST("waiting.php")
    Call<String> waiting(@Field("id") String id, @Field("res_name") String res_name, @Field("dispatcher_id") String dispatcher_id,
                         @Field("waiting_minutes") String waiting_minutes);

    @FormUrlEncoded
    @POST("active.php")
    Call<String> active(@Field("driver_id") String driver_id, @Field("in_time") String in_time, @Field("date") String date,
                        @Field("month") String month, @Field("year") String year, @Field("status") String status);

    @FormUrlEncoded
    @POST("deactive.php")
    Call<String> deactive(@Field("driver_id") String driver_id, @Field("out_time") String out_time, @Field("date") String date);

    @FormUrlEncoded
    @POST("fetch_active.php")
    Call<String> fetch_active(@Field("driver_id") String driver_id);


    @FormUrlEncoded
    @POST("fetch_quantity.php")
    Call<String> fetch_quantity(@Field("driver_id") String driver_id, @Field("date") String date);

    @FormUrlEncoded
    @POST("fetch_driver_history.php")
    Call<String> fetch_driver_history(@Field("driver_id") String driver_id, @Field("date") String date);

    @FormUrlEncoded
    @POST("fetch_quantity_month_wise.php")
    Call<String> fetch_quantity_month_wise(@Field("driver_id") String driver_id, @Field("month") String month, @Field("year") String year);


    @FormUrlEncoded
    @POST("fetch_profile.php")
    Call<String> fetch_profile(@Field("driver_id") String driver_id);

    @FormUrlEncoded
    @POST("insert_token.php")
    Call<String> insert_token(@Field("driver_id") String driver_id, @Field("token") String token);

    @FormUrlEncoded
    @POST("break.php")
    Call<String> break_status(@Field("driver_id") String driver_id, @Field("break_status") String break_status);

    @FormUrlEncoded
    @POST("fetch_break.php")
    Call<String> fetch_break(@Field("driver_id") String driver_id);


}
