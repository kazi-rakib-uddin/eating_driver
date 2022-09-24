package com.eating.driver_appp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.eating.driver_appp.Adapter.Messages_Adapter;
import com.eating.driver_appp.Model.Message_Model;
import com.eating.driver_appp.Model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class ConversationActivity extends AppCompatActivity {

    RecyclerView recycler_view;
    Messages_Adapter adapter;
    ArrayList<Message_Model> message_models;
    String senderRoom, receiverRoom;
    FirebaseDatabase database;
    User user;
    EditText et_message;
    ImageView btn_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getExtras().getString("name"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database =FirebaseDatabase.getInstance();
        user = new User(this);
        String receiver_id = getIntent().getExtras().getString("desp_id");
        String sender_id = user.getDriver_id();

        recycler_view = findViewById(R.id.recycler_view);
        btn_send = findViewById(R.id.btn_send);
        et_message = findViewById(R.id.et_message);

        message_models = new ArrayList<>();
        adapter = new Messages_Adapter(this,message_models);
        recycler_view.setAdapter(adapter);

        senderRoom = sender_id + receiver_id;
        receiverRoom = receiver_id + sender_id;



        database.getReference().child("chat_message")
                .child(senderRoom)
                .child("message")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        message_models.clear();

                        for (DataSnapshot snapshot1 : snapshot.getChildren())
                        {
                            Message_Model model = snapshot1.getValue(Message_Model.class);
                            message_models.add(model);
                        }
                        adapter.notifyDataSetChanged();

                        recycler_view.smoothScrollToPosition(recycler_view.getAdapter().getItemCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msg = et_message.getText().toString();
                Date date = new Date();

                Message_Model model =new Message_Model(msg,sender_id, user.getDriver_id(), date.getTime());

                et_message.setText("");

                String randomKey = database.getReference().push().getKey();

                database.getReference().child("chat_message")
                        .child(senderRoom)
                        .child("message")
                        .child(randomKey)
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                        database.getReference().child("chat_message")
                                .child(receiverRoom)
                                .child("message")
                                .child(randomKey)
                                .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });




                    }
                });
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