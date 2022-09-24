package com.eating.driver_appp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.eating.driver_appp.ConversationActivity;
import com.eating.driver_appp.Model.Chat_User_Model;
import com.eating.driver_appp.R;

import java.util.List;

public class User_Adapter extends RecyclerView.Adapter<User_Adapter.MyViewHolder> {

    Context context;
    List<Chat_User_Model> models;

    public User_Adapter(Context context, List<Chat_User_Model> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.chat_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {


        holder.user_name.setText(models.get(position).getDispatcher_name());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle b = new Bundle();
                b.putString("name",models.get(position).getDispatcher_name());
                b.putString("desp_id",models.get(position).getDispatcher_id());
                context.startActivity(new Intent(context, ConversationActivity.class).putExtras(b));
            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView user_name;
        CardView card;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            user_name = itemView.findViewById(R.id.user_name);
            card = itemView.findViewById(R.id.card);


        }
    }
}
