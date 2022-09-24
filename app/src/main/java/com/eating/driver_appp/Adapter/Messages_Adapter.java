package com.eating.driver_appp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eating.driver_appp.Model.Message_Model;
import com.eating.driver_appp.Model.User;
import com.eating.driver_appp.R;
import com.eating.driver_appp.databinding.ItemReceiverBinding;
import com.eating.driver_appp.databinding.ItemSendBinding;


import java.util.ArrayList;

public class Messages_Adapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Message_Model> message_models;
    final int ITEM_SENT = 1;
    final int ITEM_RECEIVE = 2;
    String senderRoom, receiverRoom;
    User user;

    public Messages_Adapter(Context context, ArrayList<Message_Model> message_models) {

        this.context = context;
        this.message_models = message_models;
       /* this.senderRoom = senderRoom;
        this.receiverRoom = receiverRoom;*/

       user = new User(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_SENT)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.item_send,parent,false);
            return new SendViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(context).inflate(R.layout.item_receiver,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        Message_Model model = message_models.get(position);

        if (holder.getClass() == SendViewHolder.class)
        {
            SendViewHolder viewHolder = (SendViewHolder)holder;
            viewHolder.binding.txtMessage.setText(model.getMessage());

        }
        else
        {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder)holder;
            viewHolder.binding.txtMessage.setText(model.getMessage());
        }

    }



    @Override
    public int getItemViewType(int position) {

        Message_Model message = message_models.get(position);
        if (user.getDriver_id().equals(message.getSender_id()))
        {
            return ITEM_SENT;
        }
        else
        {
            return ITEM_RECEIVE;
        }

    }


    @Override
    public int getItemCount() {
        return message_models.size();
    }

    public class SendViewHolder extends RecyclerView.ViewHolder
    {

        ItemSendBinding binding;

        public SendViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemSendBinding.bind(itemView);
        }
    }




    public class ReceiverViewHolder extends RecyclerView.ViewHolder
    {
        ItemReceiverBinding binding;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemReceiverBinding.bind(itemView);
        }
    }


}
