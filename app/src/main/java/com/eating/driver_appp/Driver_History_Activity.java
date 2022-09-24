package com.eating.driver_appp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eating.driver_appp.ApiClient.ApiClient;
import com.eating.driver_appp.Interface.ApiInterface;
import com.eating.driver_appp.Model.ProgressUtils;
import com.eating.driver_appp.Model.User;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Driver_History_Activity extends AppCompatActivity {

    TextView txt_qty, txt_hours, txt_salary, txt_tip, txt_total, txt_date, txt_month;
    LinearLayout lin_month;
    ApiInterface apiInterface;
    User user;
    private String status = "", currentDateandTime, currentDateandDate, hold_month, hold_year;
    TextView total_cmplt_order,total_tip,total_fee,total,fee_collected,driver_own,tip_collected,eating_owe,hr,salary,pending_owe;
    CardView card_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_history);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        user = new User(this);

        //SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
        DateFormat sdf = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy-MM-dd");
        currentDateandTime = sdf.format(new Date());
        currentDateandDate = sdf_date.format(new Date());


        txt_qty = findViewById(R.id.qty);
        txt_hours = findViewById(R.id.hours);
        txt_salary = findViewById(R.id.salary);
        txt_tip = findViewById(R.id.tip);
        txt_total = findViewById(R.id.total);
        txt_date = findViewById(R.id.txt_date);
        txt_month = findViewById(R.id.txt_month);
        lin_month = findViewById(R.id.lin_month);

        total_cmplt_order = findViewById(R.id.total_cmplt_order);
        total_tip = findViewById(R.id.total_tip);
        total_fee = findViewById(R.id.total_fee);
        fee_collected = findViewById(R.id.fee_collected);
        driver_own = findViewById(R.id.driver_own);
        tip_collected = findViewById(R.id.tip_collected);
        eating_owe = findViewById(R.id.eating_owe);
        hr = findViewById(R.id.hr);
        salary = findViewById(R.id.salary);
        pending_owe = findViewById(R.id.pending_owe);
        card_details = findViewById(R.id.card_details);

        txt_date.setText(currentDateandDate);

        txt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status = "date";

                Calendar mcurrentDate = Calendar.getInstance();

                int mDay   = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mYear  = mcurrentDate.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Driver_History_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        String fmonth, fDate;


                        try {
                            if (month < 10 && dayOfMonth < 10) {

                                fmonth = "0" + month;
                                month = Integer.parseInt(fmonth) + 1;
                                fDate = "0" + dayOfMonth;
                                String paddedMonth = String.format("%02d", month);
                                txt_date.setText(year + "-" + paddedMonth + "-" + fDate);
                                fetch_statistics();

                            }
                            else if (month >= 10 && dayOfMonth < 10) {

                                fmonth = "0" + month;
                                month = Integer.parseInt(fmonth) + 1;
                                fDate = "0" + dayOfMonth;
                                String paddedMonth = String.format("%02d", month);
                                txt_date.setText(year + "-" + paddedMonth + "-" + fDate);
                                fetch_statistics();


                            }
                            else {

                                fmonth = "0" + month;
                                month = Integer.parseInt(fmonth) + 1;
                                String paddedMonth = String.format("%02d", month);
                                txt_date.setText(year + "-" + paddedMonth + "-" + dayOfMonth);
                                fetch_statistics();

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, mYear, mMonth, mDay);

                //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() -1000);
                datePickerDialog.show();



            }
        });



        lin_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                txt_date.setText("Select Date");

                final Calendar today = Calendar.getInstance();

                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(Driver_History_Activity.this,
                        new MonthPickerDialog.OnDateSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onDateSet(int month, int selectedYear) {

                                String fmonth, fDate;

                                try {
                                    if (month < 10) {

                                        fmonth = "0" + month;
                                        month = Integer.parseInt(fmonth) + 1;
                                        String paddedMonth = String.format("%02d", month);
                                        txt_month.setText((getMonthAbbr(month - 1)) + " - " + selectedYear);
                                        hold_month = paddedMonth;
                                        hold_year = String.valueOf(selectedYear);
                                        fetch_statistics_month_wise();
                                    }
                                    else if (month >= 10) {

                                        fmonth = "0" + month;
                                        month = Integer.parseInt(fmonth) + 1;
                                        String paddedMonth = String.format("%02d", month);
                                        hold_month = paddedMonth;
                                        hold_year = String.valueOf(selectedYear);
                                        txt_month.setText((getMonthAbbr(month - 1)) + " - " + selectedYear);
                                        fetch_statistics_month_wise();
                                    }
                                    else {

                                        fmonth = "0" + month;
                                        month = Integer.parseInt(fmonth) + 1;
                                        String paddedMonth = String.format("%02d", month - 1);
                                        hold_month = paddedMonth;
                                        txt_month.setText((getMonthAbbr(month - 1)) + " - " + selectedYear);
                                        hold_year = String.valueOf(selectedYear);
                                       fetch_statistics_month_wise();

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

                builder.setActivatedMonth(today.get(Calendar.MONTH))
                        .setMinYear(1990)
                        .setActivatedYear(today.get(Calendar.YEAR))
                        .setMaxYear(2050)
                        .setTitle("Select month year")
                        .build().show();



            }
        });




        //fetchQuantity();

    }


    private String getMonthAbbr(int monthOfYear) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, monthOfYear);
        return new SimpleDateFormat("LLL").format(c.getTime());
    }


    private void fetch_statistics() {
        Call<String> call = apiInterface.fetch_driver_history(user.getDriver_id(),txt_date.getText().toString());
        ProgressUtils.showLoadingDialog(Driver_History_Activity.this);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res=response.body();
                if (res==null){
                    ProgressUtils.cancelLoading();
                    Toast.makeText(Driver_History_Activity.this, "No Details", Toast.LENGTH_SHORT).show();
                    card_details.setVisibility(View.GONE);
                }else {
                    try {
                        card_details.setVisibility(View.VISIBLE);
                        JSONObject jsonObject = new JSONObject(res);
                        total_cmplt_order.setText(jsonObject.getString("complete"));
                        total_tip.setText("  $" +jsonObject.getString("total_tips"));
                        total_fee.setText("  $" +jsonObject.getString("total_fee"));
                        //total.setText("  $" +jsonObject.getString("total"));
                        fee_collected.setText("  $" +jsonObject.getString("total_fee_collect"));
                        driver_own.setText("  $" +jsonObject.getString("total_fee_collect")+" FEE");
                        tip_collected.setText("  $" +jsonObject.getString("total_tip_collect"));
                        eating_owe.setText("  $" +jsonObject.getString("total_tip_eating")+" TIP");
                        salary.setText("  $" +jsonObject.getString("salary"));
                        pending_owe.setText("  $" +jsonObject.getString("pending").substring(1));

                        if(jsonObject.getString("pending").startsWith("-")){
                            pending_owe.setTextColor(Color.RED);
                        }else{
                            pending_owe.setTextColor(Color.GREEN);
                        }

                        hr.setText(jsonObject.getString("hours"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                        Toast.makeText(Driver_History_Activity.this, "Something Went Wrong"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    ProgressUtils.cancelLoading();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ProgressUtils.cancelLoading();
                Toast.makeText(Driver_History_Activity.this, "Slow Network", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetch_statistics_month_wise() {
        Call<String> call = apiInterface.fetch_quantity_month_wise(user.getDriver_id(),hold_month,hold_year);
        ProgressUtils.showLoadingDialog(Driver_History_Activity.this);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res=response.body();
                if (res==null){
                    ProgressUtils.cancelLoading();
                    Toast.makeText(Driver_History_Activity.this, "No Details", Toast.LENGTH_SHORT).show();
                    card_details.setVisibility(View.GONE);
                }else {
                    try {
                        card_details.setVisibility(View.VISIBLE);
                        JSONObject jsonObject = new JSONObject(res);
                        total_cmplt_order.setText(jsonObject.getString("complete"));
                        total_tip.setText("  $" +jsonObject.getString("total_tips"));
                        total_fee.setText("  $" +jsonObject.getString("total_fee"));
                        //total.setText("  $" +jsonObject.getString("total"));
                        fee_collected.setText("  $" +jsonObject.getString("total_fee_collect"));
                        driver_own.setText("  $" +jsonObject.getString("total_fee_collect")+" FEE");
                        tip_collected.setText("  $" +jsonObject.getString("total_tip_collect"));
                        eating_owe.setText("  $" +jsonObject.getString("total_tip_eating")+" TIP");
                        salary.setText("  $" +jsonObject.getString("salary"));
                        pending_owe.setText("  $" +jsonObject.getString("pending").substring(1));
                        if(jsonObject.getString("pending").startsWith("-")){
                            pending_owe.setTextColor(Color.RED);
                        }else{
                            pending_owe.setTextColor(Color.GREEN);
                        }

                        hr.setText(jsonObject.getString("hours"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                        Toast.makeText(Driver_History_Activity.this, "Something Went Wrong"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    ProgressUtils.cancelLoading();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ProgressUtils.cancelLoading();
                Toast.makeText(Driver_History_Activity.this, "Slow Network", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void fetchQuantity()
    {

        if (status.equals("date"))
        {
            currentDateandDate = txt_date.getText().toString();

            Call<String> call = apiInterface.fetch_quantity(user.getDriver_id(), currentDateandDate);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body());

                        txt_qty.setText(jsonObject.getString("qty"));
                        txt_salary.setText(jsonObject.getString("salary"));
                        txt_tip.setText(jsonObject.getString("tip"));
                        txt_total.setText(jsonObject.getString("total"));
                        String hours =jsonObject.getString("hour");

                        if (hours.equals("null"))
                        {
                            txt_hours.setText("0.0");
                        }
                        else
                        {
                            txt_hours.setText(hours);
                        }

                    } catch (JSONException e) {

                        Toast.makeText(Driver_History_Activity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                    Toast.makeText(Driver_History_Activity.this, "Slow Network", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else
        {

            Call<String> call = apiInterface.fetch_quantity(user.getDriver_id(), currentDateandDate);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body());

                        txt_qty.setText(jsonObject.getString("qty"));
                        txt_salary.setText(jsonObject.getString("salary"));
                        txt_tip.setText(jsonObject.getString("tip"));
                        txt_total.setText(jsonObject.getString("total"));
                        String hours =jsonObject.getString("hour");

                        if (hours.equals("null"))
                        {
                            txt_hours.setText("0.0");
                        }
                        else
                        {
                            txt_hours.setText(hours);
                        }

                    } catch (JSONException e) {

                        Toast.makeText(Driver_History_Activity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                    Toast.makeText(Driver_History_Activity.this, "Slow Network", Toast.LENGTH_SHORT).show();
                }
            });

        }


    }






    private void fetchQuantityMonthWise()
    {

            Call<String> call = apiInterface.fetch_quantity_month_wise(user.getDriver_id(),hold_month,hold_year);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body());

                        txt_qty.setText(jsonObject.getString("qty"));
                        txt_salary.setText(jsonObject.getString("salary"));
                        txt_tip.setText(jsonObject.getString("tip"));
                        txt_total.setText(jsonObject.getString("total"));
                        String hours =jsonObject.getString("hour");

                        if (hours.equals("null"))
                        {
                            txt_hours.setText("0.0");
                        }
                        else
                        {
                            txt_hours.setText(hours);
                        }

                    } catch (JSONException e) {

                        Toast.makeText(Driver_History_Activity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                    Toast.makeText(Driver_History_Activity.this, "Slow Network", Toast.LENGTH_SHORT).show();
                }
            });




    }



}