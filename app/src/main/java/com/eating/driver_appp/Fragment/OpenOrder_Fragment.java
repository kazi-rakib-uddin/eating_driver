package com.eating.driver_appp.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eating.driver_appp.Interface.ApiInterface;
import com.eating.driver_appp.Model.CurrentOrder_Model;
import com.eating.driver_appp.ApiClient.ApiClient;
import com.eating.driver_appp.Map_Activity;
import com.eating.driver_appp.Model.User;
import com.eating.driver_appp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OpenOrder_Fragment extends Fragment {

    RecyclerView rv_current;
    List<CurrentOrder_Model> modelList = new ArrayList<>();;
    CurrentOrder_Adapter adapter;
    ApiInterface apiInterface;
    User user;
    ProgressDialog progressDialog;
    String currentDateandTime;
    int REQUEST_PHONE_CALL = 110;
    SwipeRefreshLayout swipyRefreshLayout;
    DateFormat sdf;
    DatabaseReference databaseReference;
    Query query;
    String hold_waiting_minutes="";
    RadioButton rb;
    FirebaseDatabase database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_open,container,false);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        user = new User(getContext());

        //database = FirebaseDatabase.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("order_list1");
        query=databaseReference.orderByChild("driver_id").equalTo(user.getDriver_id().trim());

       /* HashMap<String,String> hashMap = new HashMap();
        hashMap.put("driver_id",user.getDriver_id());
        hashMap.put("driver_name",user.getName());
        hashMap.put("dispatcher_id","DES123456");
        hashMap.put("dispatcher_name","Rintu");

         database.getReference("chat").child(user.getDriver_id()).setValue(hashMap);*/


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        rv_current = view.findViewById(R.id.rv_current);
        swipyRefreshLayout = view.findViewById(R.id.swipe_refresh);

        rv_current.setHasFixedSize(true);
        rv_current.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
        }



        sdf = new SimpleDateFormat("hh:mm a");
        currentDateandTime = sdf.format(new Date());

        /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");

        Date date1 = null;
        Date date2 = null;
        try {
            date1 = simpleDateFormat.parse("08:00 AM");
            date2 = simpleDateFormat.parse("08:05 PM");
        } catch (ParseException e) {
            e.printStackTrace();
        }


        long difference = date2.getTime() - date1.getTime();
        int days = (int) (difference / (1000*60*60*24));
        int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
        int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
        hours = (hours < 0 ? -hours : hours);
        //Log.d("Hours","Hours: "+hours+", Mins: "+min);

        if (hours==0)
        {
            Log.d("Hours", "Mins: "+min);
        }
        else
        {
            Log.d("Hours","Hours: "+hours+", Mins: "+min);
        }*/


        adapter = new CurrentOrder_Adapter(getActivity(), modelList);
        rv_current.setAdapter(adapter);
        swipyRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //fetchOrder();
                //fetch_orders();
            }
        });

        modelList.clear();

        //fetchOrder();
        fetch_orders();

        return view;
    }






    private void fetch_orders()
    {
        //modelList = new ArrayList<>();
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
             // modelList.clear();
                HashMap<String, String> value = (HashMap<String, String>) dataSnapshot.getValue();
                String status = value.get("status");

                 //Toast.makeText(getActivity(), "" + status, Toast.LENGTH_SHORT).show();
                if (status.equals("Assign") || status.equals("Accepted") || status.equals("Reached") || status.equals("Out for Delivery")) {
                    //   Toast.makeText(getActivity(), ""+datas.child("order_id").getValue().toString(), Toast.LENGTH_SHORT).show();

                    Call<String> call = apiInterface.fetch_order(value.get("order_id"));
                    if(progressDialog != null && progressDialog.isShowing())
                        progressDialog.show();
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.body() != null) {
                                String res = response.body();
                                try {
                                    JSONObject jsonObject = new JSONObject(res);
                                    //String id = jsonObject.getString("id");
                                    String id = value.get("order_id");
                                    String restaurant_name = jsonObject.getString("restaurant_name");
                                    String delivery_time = jsonObject.getString("delivery_time");
                                    String created_at_time = jsonObject.getString("created_at");
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
                                    String status = value.get("status");
                                    String drinks = value.get("drinks");
                                    String waiting_status = value.get("waiting_status");
                                    String dispatcher_id = value.get("dispatcher_id");

                                    CurrentOrder_Model model = new CurrentOrder_Model(id,restaurant_name,delivery_time,address,
                                            status,tip,delivery_fee,checkout_total,restaurant_price,user_name,phone_no,delivery_date,
                                            f_ready_time,address2,drinks,waiting_status,dispatcher_id,value.get("priority"),created_at_time);
                                    modelList.add(model);

                                    /*Collections.sort(modelList, new Comparator() {
                                        @Override
                                        public int compare(Object o1, Object o2) {
                                            CurrentOrder_Model p1 = (CurrentOrder_Model) o1;
                                            CurrentOrder_Model p2 = (CurrentOrder_Model) o2;

                                            Date start = null,end = null;
                                            try {
                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMMM dd, HH:mmaaa");

                                                start = sdf.parse(p1.getF_ready_time());
                                                end = sdf.parse(p2.getF_ready_time());

                                            }catch (ParseException p){

                                            }


                                            return (start.getTime() > end.getTime() ? 1 : -1);
                                        }
                                    });

                                    Collections.sort(modelList, new Comparator() {
                                        @Override
                                        public int compare(Object o1, Object o2) {
                                            CurrentOrder_Model p1 = (CurrentOrder_Model) o1;
                                            CurrentOrder_Model p2 = (CurrentOrder_Model) o2;
                                            return p1.getPriority().compareToIgnoreCase(p2.getPriority());
                                        }
                                    });*/

                                    adapter.notifyDataSetChanged();
                                    swipyRefreshLayout.setRefreshing(false);

                                    progressDialog.dismiss();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    swipyRefreshLayout.setRefreshing(false);

                                    progressDialog.dismiss();
                                    //Toast.makeText(getActivity(), "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "No Response", Toast.LENGTH_SHORT).show();
                                swipyRefreshLayout.setRefreshing(false);

                                progressDialog.dismiss();
                            }

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            swipyRefreshLayout.setRefreshing(false);

                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }



            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                HashMap<String, String> value = (HashMap<String, String>) dataSnapshot.getValue();
                String status = value.get("status");
                CurrentOrder_Model model = dataSnapshot.getValue(CurrentOrder_Model.class);
                //Toast.makeText(getActivity(), ""+model.getPriority()+" "+model.getDispatcher_id()+" "+model.getStatus(), Toast.LENGTH_SHORT).show();
                for (int i = 0; i < modelList.size(); i++) {
                    if (modelList.get(i).getOrder_id().equals(model.getOrder_id())) {
                        if (model.getStatus().equals("Assign") || model.getStatus().equals("Accepted") || model.getStatus().equals("Reached") || model.getStatus().equals("Out for Delivery")) {
                            Call<String> call = apiInterface.fetch_order(model.getOrder_id());
                            if(progressDialog != null && progressDialog.isShowing())
                            progressDialog.show();
                            int finalI = i;
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if (response.body() != null) {
                                        String res = response.body();
                                        try {
                                            JSONObject jsonObject = new JSONObject(res);

                                            model.setOrder_id(model.getOrder_id());
                                            model.setRestaurant(model.getRestaurant());
                                            model.setTime(model.getTime());
                                            model.setCustomer(model.getCustomer());
                                            model.setTip(model.getTip());
                                            model.setDelivery_fee(model.getDelivery_fee());
                                            model.setCheckout_total(model.getCheckout_total());
                                            model.setRestaurant_price(model.getRestaurant_price());
                                            model.setUser_name(model.getUser_name());
                                            model.setPhone_no(model.getPhone_no());
                                            model.setDelivery_date(model.getDelivery_date());
                                            model.setF_ready_time(model.getF_ready_time());
                                            model.setDrinks(model.getDrinks());


                                            modelList.set(finalI, model);
                                            adapter.notifyItemChanged(finalI);

                                            swipyRefreshLayout.setRefreshing(false);

                                            progressDialog.dismiss();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            swipyRefreshLayout.setRefreshing(false);

                                            progressDialog.dismiss();
                                            //Toast.makeText(getActivity(), "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), "No Response", Toast.LENGTH_SHORT).show();
                                        swipyRefreshLayout.setRefreshing(false);

                                        progressDialog.dismiss();
                                    }

                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    swipyRefreshLayout.setRefreshing(false);

                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else if(model.getStatus().equals("Completed")){

                                    modelList.remove(i);
                                    //adapter.notifyItemRemoved(i);
                                    if (modelList.size()==0){
                                        adapter.notifyItemRemoved(i);
                                        modelList.clear();
                                        fetch_orders();

                                    }else{
                                        adapter.notifyItemRemoved(i);
                                    }


                        }
                        break;
                    }
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                Toast.makeText(getActivity(), "remove", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(getActivity(), "moved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
            }
        });



    }





    private void fetchOrder()
    {

        modelList = new ArrayList<>();

        Call<String> call = apiInterface.fetch_order(user.getDriver_id());
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
                            String drinks = jsonObject.getString("drinks");
                            String waiting_status = jsonObject.getString("waiting_status");
                            String dispatcher_id = jsonObject.getString("dispatcher_id");

                            CurrentOrder_Model model = new CurrentOrder_Model(id,restaurant_name,delivery_time,address,status,tip,delivery_fee,checkout_total,restaurant_price,user_name,phone_no,delivery_date,f_ready_time,address2,drinks,waiting_status,dispatcher_id,jsonObject.getString("priority"),"");
                            modelList.add(model);
                        }

                        adapter = new CurrentOrder_Adapter(getActivity(),modelList);
                        rv_current.setAdapter(adapter);
                        swipyRefreshLayout.setRefreshing(false);

                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {

                    Toast.makeText(getContext(), "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    swipyRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(getContext(), "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                swipyRefreshLayout.setRefreshing(false);
            }
        });

    }






    public class CurrentOrder_Adapter extends RecyclerView.Adapter<CurrentOrder_Adapter.MyViewHolder> implements View.OnClickListener {

        Context context;
        List<CurrentOrder_Model> models;
        int row_index = -1;


        public CurrentOrder_Adapter(Context context, List<CurrentOrder_Model> models) {
            this.context = context;
            this.models = models;
        }

        @NonNull
        @Override
        public CurrentOrder_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.single_current_order, parent, false);

            MyViewHolder viewHolder=new MyViewHolder(view);

            viewHolder.tip.setTag(viewHolder);
            viewHolder.tip.setOnClickListener(CurrentOrder_Adapter.this);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CurrentOrder_Adapter.MyViewHolder holder, final int position) {



            try {
                DateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date date = inputFormatter.parse(models.get(position).getTime());
                DateFormat outputFormatter = new SimpleDateFormat("HH:mm a");
                holder.time.setText(outputFormatter.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.restaurant.setText(models.get(position).getRestaurant());
            holder.tip.setText(models.get(position).getTip());
            holder.customer.setText(models.get(position).getCustomer());

            holder.delivery_fee.setText(models.get(position).getDelivery_fee());
            holder.checkout_total.setText(models.get(position).getCheckout_total());
            holder.restaurant_fee.setText(models.get(position).getRestaurant_price());
            holder.customer_name.setText(models.get(position).getUser_name());
            holder.food_ready_time.setText(models.get(position).getF_ready_time());
            holder.request_delivery_time.setText(models.get(position).getDelivery_date()+ "  " +models.get(position).getTime());

            if (models.get(position).getDrinks()!=null) {
                if (models.get(position).getDrinks().equals("Yes") && !models.get(position).getDrinks().equals("null")) {
                    holder.lin_drinks.setVisibility(View.VISIBLE);
                } else {
                    holder.lin_drinks.setVisibility(View.GONE);
                }
            }else {
                holder.lin_drinks.setVisibility(View.GONE);
            }



            if(models.get(position).getStatus().equals("Assign")){
                holder.linear.setBackgroundColor(Color.parseColor("#FFEEB9"));
                holder.btn_accept.setText("Accept");
                holder.card_wait.setVisibility(View.GONE);

            }else if(models.get(position).getStatus().equals("Accepted")){

                holder.linear.setBackgroundColor(Color.parseColor("#B3DDFF"));
                holder.btn_accept.setText("Reached");
                holder.card_wait.setVisibility(View.GONE);

            }else if(models.get(position).getStatus().equals("Reached")){

                holder.linear.setBackgroundColor(Color.parseColor("#B3DDFF"));
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(100); //You can manage the blinking time with this parameter
                anim.setStartOffset(100);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                holder.linear.startAnimation(anim);
                holder.btn_accept.setText("Out for Delivery");
                holder.card_wait.setVisibility(View.VISIBLE);
            }
            else if (models.get(position).getStatus().equals("Out for Delivery"))
            {
                holder.linear.setBackgroundColor(Color.parseColor("#B2FDB5"));
                holder.btn_accept.setText("Completed");
                holder.card_wait.setVisibility(View.GONE);
            }

            if (models.get(position).getWaiting_status().equals("Waiting"))
            {
                holder.btn_wait.setText("Waiting");
            }
            else
            {
                holder.btn_wait.setText("Wait");
            }


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



            if (holder.btn_accept.getText().toString().equals("Accept"))
            {

                holder.btn_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        HashMap hashMap = new HashMap();
                        hashMap.put("status","Accepted");
                        hashMap.put("priority","3");

                        databaseReference.child(models.get(position).getOrder_id()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {

                                if (task.isSuccessful())
                                {
                                    accept(models.get(position).getOrder_id(),"Accepted","3", models.get(position).getRestaurant(), models.get(position).getDispatcher_id());
                                }
                                else
                                {
                                    Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });



                    }
                });

            }
            else if (holder.btn_accept.getText().toString().equals("Reached"))
            {

                holder.btn_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        HashMap hashMap = new HashMap();
                        hashMap.put("status","Reached");
                        hashMap.put("priority","2");

                        databaseReference.child(models.get(position).getOrder_id()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {

                                if (task.isSuccessful())
                                {
                                    reached(models.get(position).getOrder_id(),"Reached","2", models.get(position).getRestaurant(), models.get(position).getDispatcher_id());
                                }
                                else
                                {
                                    Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });




                    }
                });
            }
            else if (holder.btn_accept.getText().toString().equals("Out for Delivery"))
            {
                holder.btn_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        HashMap hashMap = new HashMap();
                        hashMap.put("status","Out for Delivery");
                        hashMap.put("waiting_status","null");
                        hashMap.put("priority","1");

                        databaseReference.child(models.get(position).getOrder_id()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {

                                if (task.isSuccessful())
                                {
                                    out_for_delivery(models.get(position).getOrder_id(),"Out for Delivery","1", models.get(position).getRestaurant(), models.get(position).getDispatcher_id());
                                }
                                else
                                {
                                    Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });




                    }
                });
            }

            else if (holder.btn_accept.getText().toString().equals("Completed"))
            {
                holder.btn_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        completed(models.get(position).getOrder_id(),"Completed","0", models.get(position).getRestaurant(),user.getDriver_id(), models.get(position).getDispatcher_id());

                    }
                });
            }



                holder.btn_wait.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if (holder.btn_wait.getText().toString().equals("Waiting"))
                        {


                        }
                        else
                        {

                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                            alertDialog.setTitle("Waiting Minutes");
                            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                            View view1 = layoutInflater.inflate(R.layout.dialog_wating, null);

                        RadioGroup rg_waiting = view1.findViewById(R.id.rg_waiting);


                        rg_waiting.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                               rb = (RadioButton) rg_waiting.findViewById(checkedId);

                                hold_waiting_minutes = rb.getText().toString();

                            }
                        });


                        alertDialog.setView(view1);

                        alertDialog.setPositiveButton("WAIT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (hold_waiting_minutes.equals(""))
                                {
                                    Toast.makeText(context, "Please Select Waiting Minutes", Toast.LENGTH_LONG).show();
                                }
                                else {

                                    HashMap hashMap = new HashMap();
                                    hashMap.put("waiting_status", "Waiting");

                                    databaseReference.child(models.get(position).getOrder_id()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {

                                            if (task.isSuccessful()) {
                                                waiting(models.get(position).getOrder_id(), models.get(position).getRestaurant(), models.get(position).getDispatcher_id());
                                            } else {
                                                Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });



                                }


                            }
                        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                hold_waiting_minutes="";
                            }
                        });

                        final AlertDialog dialog = alertDialog.create();
                        dialog.show();

                        // Get the alert dialog buttons reference
                        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                        positiveButton.setTextColor(Color.parseColor("#FF0B8B42"));
                        negativeButton.setTextColor(Color.parseColor("#4588f5"));


                        }


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
            LinearLayout linear,linear_order,rqst_dlvry_tm,lin_tips,unit, lin_drinks;
            Button btn_accept, btn_wait;
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
                btn_wait = itemView.findViewById(R.id.btn_wait);
                lin_drinks = itemView.findViewById(R.id.lin_drinks);



            }
        }
    }




    private void accept(String id, String status, String priority, String res_name, String dispatcher_id)
    {
        Call<String> call = apiInterface.accept(id,status,priority,res_name,currentDateandTime,dispatcher_id);
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    
                    if (jsonObject.getString("rec").equals("1"))
                    {
                        //Toast.makeText(getContext(), "Accepted", Toast.LENGTH_SHORT).show();
                      //  fetch_orders();
                        progressDialog.dismiss();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Not Accept", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    
                } catch (JSONException e) {

                    Toast.makeText(getContext(), "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(getContext(), "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }



    private void reached(String id, String status, String priority, String res_name, String dispatcher_id)
    {
        Call<String> call = apiInterface.reached(id,status,priority,res_name,currentDateandTime, dispatcher_id);
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {
                        //Toast.makeText(getContext(), "Accepted", Toast.LENGTH_SHORT).show();
                       // fetch_orders();
                        progressDialog.dismiss();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Not Reached", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {

                    Toast.makeText(getContext(), "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(getContext(), "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }



    private void completed(String id, String status, String priority, String res_name, String driver_id, String dispatcher_id)
    {
        String currentTime = sdf.format(new Date());

        Call<String> call = apiInterface.completed(id,status,priority, res_name,currentTime, driver_id, dispatcher_id);
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {
                        databaseReference.child(id).child("status").setValue("Completed");
                        databaseReference.child(id).child("priority").setValue("0");
                        //Toast.makeText(getContext(), "Accepted", Toast.LENGTH_SHORT).show();
                      //  fetch_orders();
                        progressDialog.dismiss();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Not Completed", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {

                    Toast.makeText(getContext(), "Somthing went wrong" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(getContext(), "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }



    private void out_for_delivery(String id, String status, String priority, String res_name, String dispatcher_id)
    {
        String currentTime = sdf.format(new Date());

        Call<String> call = apiInterface.out_for_delivery(id,status,priority, res_name,currentTime, dispatcher_id);
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {
                        //Toast.makeText(getContext(), "Accepted", Toast.LENGTH_SHORT).show();
                      //  fetch_orders();
                        progressDialog.dismiss();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Not Out for delivery", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {

                    Toast.makeText(getContext(), "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(getContext(), "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }



    private void waiting(String id, String res_name, String dispatcher_id)
    {
        Call<String> call = apiInterface.waiting(id,res_name,dispatcher_id, hold_waiting_minutes);
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {
                        Toast.makeText(getContext(), "Order is Waiting", Toast.LENGTH_SHORT).show();
                      //  fetch_orders();
                        progressDialog.dismiss();
                        hold_waiting_minutes="";

                    }
                    else
                    {
                        Toast.makeText(getContext(), "Not Waiting", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {

                    Toast.makeText(getContext(), "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(getContext(), "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PHONE_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //makePhoneCall();
                //Toast.makeText(getActivity(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }



}
