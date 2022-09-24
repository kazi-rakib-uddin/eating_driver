package com.eating.driver_appp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class Registration_Activity extends AppCompatActivity {

    LinearLayout login_back;
    Button button_register;
    EditText edt_name,edt_adrs,edt_number,edt_email,edt_pswrd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        login_back = findViewById(R.id.login_back);
        button_register = findViewById(R.id.button_register);
        edt_name = findViewById(R.id.edt_name);
        edt_adrs = findViewById(R.id.edt_adrs);
        edt_number = findViewById(R.id.edt_number);
        edt_email = findViewById(R.id.edt_email);
        edt_pswrd = findViewById(R.id.edt_pswrd);

        login_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration_Activity.this,Login_Activity.class));
            }
        });
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration_Activity.this,MainActivity.class));

            }
        });
    }
}