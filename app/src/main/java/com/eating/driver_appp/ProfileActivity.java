package com.eating.driver_appp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.eating.driver_appp.ApiClient.ApiClient;
import com.eating.driver_appp.Interface.ApiInterface;
import com.eating.driver_appp.Model.User;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private ApiInterface apiInterface;
    User user;
    EditText edit_name, edit_number, edit_email, edit_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Driver Details");


        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        user = new User(this);

        edit_name = findViewById(R.id.edit_name);
        edit_number = findViewById(R.id.edit_number);
        edit_email = findViewById(R.id.edit_email);
        edit_address = findViewById(R.id.edit_address);



        fetchProfile();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
          /*  Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    private void fetchProfile()
    {
        Call<String> call = apiInterface.fetch_profile(user.getDriver_id());
        ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {


                        edit_name.setText(jsonObject.getString("driver_name"));
                        edit_number.setText(jsonObject.getString("phone"));
                        edit_email.setText(jsonObject.getString("email"));
                        edit_address.setText(jsonObject.getString("area"));
                        String hourly_rate = jsonObject.getString("hourly_rate");
                        progressDialog.dismiss();
                    }
                    else
                    {
                        Toast.makeText(ProfileActivity.this, "Profile Details Not found", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {

                    Toast.makeText(ProfileActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(ProfileActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }
}