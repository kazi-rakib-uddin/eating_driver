package com.eating.driver_appp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.eating.driver_appp.Adapter.Order_Adapter;
import com.eating.driver_appp.ApiClient.ApiClient;
import com.eating.driver_appp.Interface.ApiInterface;
import com.eating.driver_appp.Model.Order_Model;
import com.eating.driver_appp.Model.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Order_History extends AppCompatActivity {

    RecyclerView rv_current;
    List<Order_Model> modelList;
    Order_Adapter adapter;
    LinearLayout lin_date,lin_calendar,lin_recycler;
    Button button_apply;
    TextView txt_date;
    ApiInterface apiInterface;
    User user;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__history);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        user = new User(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        rv_current = findViewById(R.id.rv_current);
        lin_date = findViewById(R.id.lin_date);
        lin_calendar = findViewById(R.id.lin_calendar);
        button_apply = findViewById(R.id.button_apply);
        lin_recycler = findViewById(R.id.lin_recycler);
        txt_date = findViewById(R.id.txt_date);


        //modelList = new ArrayList<>();
        rv_current.setHasFixedSize(true);
        rv_current.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
     /*   modelList.add(new Order_Model("","Lao","4:14","2123 1/2 Ridge, Eva","2.4","Completed"));
        modelList.add(new Order_Model("","Peppercorns","4:21","2147 Ridge, Eva","2.49","Comp"));
        modelList.add(new Order_Model("","Littlework","4:48","Oakton, Eva","0","Comp"));
        modelList.add(new Order_Model("","Littlework","4:14","4837 louise, Sko","0","Comp"));
        modelList.add(new Order_Model("","Littlework","4:14","2123 1/2 Ridge, Eva","6","Comp"));
        modelList.add(new Order_Model("","Lao","4:14","2123 1/2 Ridge, Eva","2.4","Comp"));
        modelList.add(new Order_Model("","Lao","4:14","2123 1/2 Ridge, Eva","2.4","Comp"));
        adapter = new Order_Adapter(this,modelList);
        rv_current.setAdapter(adapter);*/

        lin_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //lin_calendar.setVisibility(View.VISIBLE);
                //lin_recycler.setVisibility(View.GONE);



                Calendar mcurrentDate = Calendar.getInstance();

                int mDay   = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mYear  = mcurrentDate.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Order_History.this, new DatePickerDialog.OnDateSetListener() {
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
                                fetchOrderHistory();

                            }
                            else if (month >= 10 && dayOfMonth < 10) {

                                fmonth = "0" + month;
                                month = Integer.parseInt(fmonth) + 1;
                                fDate = "0" + dayOfMonth;
                                String paddedMonth = String.format("%02d", month);
                                txt_date.setText(year + "-" + paddedMonth + "-" + fDate);
                                fetchOrderHistory();


                            }
                            else {

                                fmonth = "0" + month;
                                month = Integer.parseInt(fmonth) + 1;
                                String paddedMonth = String.format("%02d", month);
                                txt_date.setText(year + "-" + paddedMonth + "-" + dayOfMonth);
                                fetchOrderHistory();

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
        button_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin_calendar.setVisibility(View.GONE);
                lin_recycler.setVisibility(View.VISIBLE);
            }
        });




    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            /*Intent intent = new Intent(Order_History.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/
            finish();
        }

        return super.onOptionsItemSelected(item);
    }





    private void fetchOrderHistory()
    {

        modelList = new ArrayList<>();

        Call<String> call = apiInterface.fetch_order_history(user.getDriver_id(), txt_date.getText().toString());
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONArray jsonArray = new JSONArray(response.body());

                    if (jsonArray.length()==0)
                    {
                        progressDialog.dismiss();
                        rv_current.setVisibility(View.GONE);
                        Toast.makeText(Order_History.this, "No Order Histoty", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        modelList.clear();
                        rv_current.setVisibility(View.VISIBLE);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            //String id = jsonObject.getString("id");
                            String restaurant_name = jsonObject.getString("restaurant_name");
                            String delivery_time = jsonObject.getString("delivery_time");
                            String address = jsonObject.getString("address");
                            String tip = jsonObject.getString("tips");
                            String status = jsonObject.getString("status");

                            Order_Model model = new Order_Model("",restaurant_name,delivery_time,address,tip,status);
                            modelList.add(model);
                        }

                        adapter = new Order_Adapter(Order_History.this,modelList);
                        rv_current.setAdapter(adapter);

                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {

                    Toast.makeText(Order_History.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(Order_History.this, "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }




}