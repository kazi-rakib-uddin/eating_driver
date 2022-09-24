package com.eating.driver_appp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eating.driver_appp.Model.Order_Model;
import com.eating.driver_appp.R;

import java.util.List;

public class Order_Adapter extends RecyclerView.Adapter<Order_Adapter.MyViewHolder> {

    Context context;
    List<Order_Model> models;

    public Order_Adapter(Context context, List<Order_Model> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_order_history, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.restaurant.setText(models.get(position).getRestaurant());
        holder.time.setText(models.get(position).getTime());
        holder.tip.setText(models.get(position).getTip());
        holder.customer.setText(models.get(position).getCustomer());
        holder.status.setText(models.get(position).getStatus());

        if (models.get(position).getStatus().equals("Accepted"))
        {
            holder.linear.setBackgroundColor(Color.parseColor("#A5D7F6"));
            //blue
        }
        else if (models.get(position).getStatus().equals("Reached"))
        {
            holder.linear.setBackgroundColor(Color.parseColor("#A5AEF5"));
            //purple
        }
        else if (models.get(position).getStatus().equals("Out for Delivery"))
        {
            holder.linear.setBackgroundColor(Color.parseColor("#B2FDB5"));
        }
        else if (models.get(position).getStatus().equals("Completed"))
        {
            holder.linear.setBackgroundColor(Color.parseColor("#52F259"));
        }
        else if (models.get(position).getStatus().equals("Cancelled"))
        {
            holder.linear.setBackgroundColor(Color.parseColor("#F4908A"));
        }


    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView restaurant,time,customer,status,tip;
        LinearLayout linear,linear_order,rqst_dlvry_tm,lin_tips,unit;
        Button button_info;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.status);
            restaurant = itemView.findViewById(R.id.restaurant);
            time = itemView.findViewById(R.id.time);
            customer = itemView.findViewById(R.id.customer);
            tip = itemView.findViewById(R.id.tip);
            linear = itemView.findViewById(R.id.linear);
            linear_order = itemView.findViewById(R.id.linear_order);
            rqst_dlvry_tm = itemView.findViewById(R.id.rqst_dlvry_tm);
            lin_tips = itemView.findViewById(R.id.lin_tips);
            unit = itemView.findViewById(R.id.unit);



        }
    }
}
