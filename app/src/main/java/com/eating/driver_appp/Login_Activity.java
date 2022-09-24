package com.eating.driver_appp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.eating.driver_appp.ApiClient.ApiClient;
import com.eating.driver_appp.Interface.ApiInterface;
import com.eating.driver_appp.Model.User;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_Activity extends AppCompatActivity {

    LinearLayout register;
    Button button_login;
    EditText edt_email,edt_password;
    ApiInterface apiInterface;
    User user;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        user = new User(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        if (user.getName()!="")
        {
            startActivity(new Intent(Login_Activity.this,MainActivity.class));
            finish();
        }
        register = findViewById(R.id.register);
        button_login = findViewById(R.id.button_login);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Activity.this,Registration_Activity.class));
            }
        });
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_email.getText().toString().equals("")){
                    edt_email.setError("Registered email is required");
                }else if (edt_password.getText().toString().equals("")){
                    edt_password.setError("Registered password id required");
                }else {

                    login();
                }
            }
        });
    }


    private void login()
    {
        Call<String> call = apiInterface.login(edt_email.getText().toString(),edt_password.getText().toString());
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {
                        Toast.makeText(Login_Activity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        user.setName(jsonObject.getString("name"));
                        user.setEmail(jsonObject.getString("email"));
                        user.setMobile_no(jsonObject.getString("phone"));
                        user.setArea(jsonObject.getString("area"));
                        user.setDriver_id(edt_email.getText().toString());

                        startActivity(new Intent(Login_Activity.this,MainActivity.class));
                        finish();

                    }
                    else
                    {
                        Toast.makeText(Login_Activity.this, "Invalid login Details", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {

                    Toast.makeText(Login_Activity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(Login_Activity.this, "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

}