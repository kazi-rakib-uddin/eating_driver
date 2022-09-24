package com.eating.driver_appp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.eating.driver_appp.Adapter.User_Adapter;
import com.eating.driver_appp.Model.Chat_User_Model;
import com.eating.driver_appp.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    RecyclerView rv_chat;
    FirebaseDatabase database;
    ArrayList<Chat_User_Model> chat_user_models;
    User_Adapter adapter;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Chats");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user = new User(this);
        database = FirebaseDatabase.getInstance();

        //Mapping
        rv_chat = findViewById(R.id.rv_chat);


        chat_user_models = new ArrayList<>();
        adapter = new User_Adapter(this,chat_user_models);
        rv_chat.setAdapter(adapter);

        database.getReference().child("chat").orderByChild("driver_id").equalTo(user.getDriver_id().trim()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                chat_user_models.clear();

                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    Chat_User_Model user_model = snapshot1.getValue(Chat_User_Model.class);

                    chat_user_models.add(user_model);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            /*Intent intent = new Intent(Cancel_Order.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}