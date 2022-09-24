package com.eating.driver_appp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.eating.driver_appp.ApiClient.ApiClient;
import com.eating.driver_appp.Interface.ApiInterface;
import com.eating.driver_appp.Model.CurrentOrder_Model;
import com.eating.driver_appp.Model.User;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Delivery_Order extends AppCompatActivity {

    RecyclerView rv_current;
    List<CurrentOrder_Model> modelList;
    CurrentOrder_Adapter adapter;
    ApiInterface apiInterface;
    User user;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swipyRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery__order);
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
        swipyRefreshLayout = findViewById(R.id.swipe_refresh);

        //modelList = new ArrayList<>();
        rv_current.setHasFixedSize(true);
        rv_current.setLayoutManager(new LinearLayoutManager(Delivery_Order.this,LinearLayoutManager.VERTICAL,false));
       /* modelList.add(new CurrentOrder_Model("","Lao","4:14","2123 1/2 Ridge, Eva","Aid","2.4","","","","","","","",""));
        adapter = new CurrentOrder_Adapter(this,modelList);
        rv_current.setAdapter(adapter);*/

        swipyRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                fetchCompleteOrder();
            }
        });


        fetchCompleteOrder();
    }





    private void fetchCompleteOrder()
    {

        modelList = new ArrayList<>();

        Call<String> call = apiInterface.fetch_out_for_delivery(user.getDriver_id());
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
                        swipyRefreshLayout.setRefreshing(false);
                    }
                    else
                    {
                        modelList.clear();
                        rv_current.setVisibility(View.VISIBLE);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String restaurant_name = jsonObject.getString("restaurant_name");
                            String delivery_time = jsonObject.getString("delivery_time");
                            String address = jsonObject.getString("user_address");
                            String address2 = jsonObject.getString("user_address2");
                            String tip = jsonObject.getString("tips");
                            String delivery_fee = jsonObject.getString("delivery_fee");
                            String checkout_total = jsonObject.getString("checkout_total");
                            String restaurant_price = jsonObject.getString("restaurant_price");
                            String user_name = jsonObject.getString("user_name");
                            String phone_no = jsonObject.getString("phone_no");
                            String delivery_date = jsonObject.getString("delivery_date");
                            String f_ready_time = jsonObject.getString("f_ready_time");
                            String status = jsonObject.getString("status");

                            CurrentOrder_Model model = new CurrentOrder_Model(id,restaurant_name,delivery_time,address,status,tip,delivery_fee,checkout_total,restaurant_price,user_name,phone_no,delivery_date,f_ready_time,address2,"","","","","");
                            modelList.add(model);
                        }

                        adapter = new CurrentOrder_Adapter(Delivery_Order.this,modelList);
                        rv_current.setAdapter(adapter);
                        swipyRefreshLayout.setRefreshing(false);

                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {

                    Toast.makeText(Delivery_Order.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    swipyRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(Delivery_Order.this, "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                swipyRefreshLayout.setRefreshing(false);
            }
        });

    }





    public class CurrentOrder_Adapter extends RecyclerView.Adapter<CurrentOrder_Adapter.MyViewHolder> implements View.OnClickListener {

        Context context;
        List<CurrentOrder_Model> models;

        public CurrentOrder_Adapter(Context context, List<CurrentOrder_Model> models) {
            this.context = context;
            this.models = models;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.single_current_order, parent, false);
            MyViewHolder viewHolder=new MyViewHolder(view);

            viewHolder.tip.setTag(viewHolder);
            viewHolder.tip.setOnClickListener(CurrentOrder_Adapter.this);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {


            holder.restaurant.setText(models.get(position).getRestaurant());
            holder.time.setText(models.get(position).getTime());
            holder.tip.setText(models.get(position).getTip());
            holder.customer.setText(models.get(position).getCustomer());

            holder.delivery_fee.setText(models.get(position).getDelivery_fee());
            holder.checkout_total.setText(models.get(position).getCheckout_total());
            holder.restaurant_fee.setText(models.get(position).getRestaurant_price());
            holder.customer_name.setText(models.get(position).getUser_name());
            holder.food_ready_time.setText(models.get(position).getF_ready_time());
            holder.request_delivery_time.setText(models.get(position).getDelivery_date()+ "  " +models.get(position).getTime());

            holder.btn_accept.setVisibility(View.GONE);
            holder.card_wait.setVisibility(View.GONE);


            holder.linear.setBackgroundColor(Color.parseColor("#B2FDB5"));
            holder.card.setVisibility(View.GONE);

            boolean isExpandes =models.get(position).isExpanded();

            if (isExpandes)
            {
                holder.linear_order.setVisibility(View.VISIBLE);

            }
            else
            {
                holder.linear_order.setVisibility(View.GONE);

            }

            /*holder.tip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.linear_order.setVisibility(View.VISIBLE);
                }
            });*/


            holder.customer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = LayoutInflater.from(context);
                    final View dialogView = inflater.inflate(R.layout.dialog_customer_adrs, null);

                    TextView txt_address =dialogView.findViewById(R.id.address);
                    TextView txt_mobile =dialogView.findViewById(R.id.customar_mobile_no);

                    txt_address.setText(models.get(position).getCustomer());
                    txt_mobile.setText(models.get(position).getPhone_no());

                    txt_mobile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String number=txt_mobile.getText().toString();
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:"+number));
                            startActivity(callIntent);
                        }
                    });

                    builder.setView(dialogView);
                    final AlertDialog alert = builder.create();
                    alert.setCanceledOnTouchOutside(true);
                    alert.show();
                }
            });
            holder.driver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
               /* AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                final View dialogView = inflater.inflate(R.layout.dialog_driver_order, null);
                builder.setView(dialogView);
                final AlertDialog alert = builder.create();
                alert.setCanceledOnTouchOutside(true);
                alert.show();*/
                }
            });
            holder.restaurant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, Map_Activity.class));
                }
            });


        }

        @Override
        public int getItemCount() {
            return models.size();
        }

        @Override
        public void onClick(View v) {

            MyViewHolder holder = (MyViewHolder) v.getTag();

            if (models.get(holder.getPosition()).isExpanded())
            {
                models.get(holder.getPosition()).setExpanded(false);
                notifyDataSetChanged();
            }
            else
            {
                for (int i=0; i<models.size(); i++)
                {
                    if (models.get(i).isExpanded())
                    {
                        models.get(i).setExpanded(false);
                    }
                }

                models.get(holder.getPosition()).setExpanded(true);
                notifyDataSetChanged();
            }

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView restaurant,time,customer,driver,tip,edit, delivery_fee, checkout_total, restaurant_fee, customer_name, request_delivery_time,
                    food_ready_time;
            LinearLayout linear,linear_order,rqst_dlvry_tm,lin_tips,unit;
            Button btn_accept;
            CardView card, card_wait;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                driver = itemView.findViewById(R.id.driver);
                restaurant = itemView.findViewById(R.id.restaurant);
                time = itemView.findViewById(R.id.time);
                customer = itemView.findViewById(R.id.customer);
                tip = itemView.findViewById(R.id.tip);
                linear = itemView.findViewById(R.id.linear);
                linear_order = itemView.findViewById(R.id.linear_order);
                rqst_dlvry_tm = itemView.findViewById(R.id.rqst_dlvry_tm);
                lin_tips = itemView.findViewById(R.id.lin_tips);
                unit = itemView.findViewById(R.id.unit);
                delivery_fee = itemView.findViewById(R.id.delivery_fee);
                checkout_total = itemView.findViewById(R.id.checkout_total);
                restaurant_fee = itemView.findViewById(R.id.restaurant_fee);
                customer_name = itemView.findViewById(R.id.customer_name);
                request_delivery_time = itemView.findViewById(R.id.request_delivery_time);
                food_ready_time = itemView.findViewById(R.id.food_ready_time);
                btn_accept = itemView.findViewById(R.id.btn_accept);
                card = itemView.findViewById(R.id.card);
                card_wait = itemView.findViewById(R.id.card_wait);



            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
           /* Intent intent = new Intent(Delivery_Order.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}