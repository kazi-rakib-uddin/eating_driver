package com.eating.driver_appp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eating.driver_appp.ApiClient.ApiClient;
import com.eating.driver_appp.Fragment.CancelOrder_Fragment;
import com.eating.driver_appp.Fragment.CompleteOrder_Fragment;
import com.eating.driver_appp.Fragment.OpenOrder_Fragment;
import com.eating.driver_appp.Interface.ApiInterface;
import com.eating.driver_appp.Model.LatLng_Model;
import com.eating.driver_appp.Model.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LocationListener {

    NavigationView navview;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    User user;
    public MenuItem item;
    public TextView duty_status;
    ApiInterface apiInterface;
    private String format = "", currentDateandTime, currentDateandDate, month, year;
    TextView txt_qty, txt_hours, txt_salary, txt_tip, txt_total;
    protected LocationManager locationManager;
    private double lat=0.0, lng=0.0;
    FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    String driver_name, status;
    Toolbar toolbar;
    DateFormat sdf;
    CheckBox ch_break;
    String break_status="";

    CardView card_open,card_complete;
    Button button_open,button_complete,button_cancel;
    LinearLayout linear_open,linear_complete;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    DatabaseReference reference2;

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {

            if (locationResult == null)
            {
                return;
            }
            for (Location location :locationResult.getLocations())
            {
                lat = location.getLatitude();
                lng = location.getLongitude();

                Log.d("TAG", "lat: " + lat);
                Log.d("TAG", "lng: " + lng);

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        user = new User(this);
        rootNode = FirebaseDatabase.getInstance();


        //SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
        sdf = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy-MM-dd");

        currentDateandDate = sdf_date.format(new Date());

        String date = currentDateandDate;
        String [] dateParts = date.split("-");
         year = dateParts[0];
         month = dateParts[1];
        String day = dateParts[2];


        drawer = findViewById(R.id.drawer);
        navview = findViewById(R.id.navview);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabLayout);

        txt_qty = findViewById(R.id.qty);
        txt_hours = findViewById(R.id.hours);
        txt_salary = findViewById(R.id.salary);
        txt_tip = findViewById(R.id.tip);
        txt_total = findViewById(R.id.total);
        ch_break = findViewById(R.id.ch_break);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel("MyNotification","MyNotification", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ChatActivity.class));
            }
        });


        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {

                if (task.isSuccessful())
                {
                    String newToken = task.getResult();

                    Call<String> call=apiInterface.insert_token(user.getDriver_id(),newToken);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                }
            }
        });



        navview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem menuItem) {

                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.drawer_home:
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        break;
                    case R.id.drawer_user:
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        break;

                    case R.id.drawer_driver_history:
                        startActivity(new Intent(MainActivity.this, Driver_History_Activity.class));
                        break;

                    case R.id.drawer_previous:
                        startActivity(new Intent(MainActivity.this, Order_History.class));
                        break;
                    case R.id.drawer_cancel:
                        startActivity(new Intent(MainActivity.this, Cancel_Order.class));
                        break;
                    case R.id.drawer_out:
                        startActivity(new Intent(MainActivity.this, Delivery_Order.class));
                        break;
                    /*case R.id.drawer_follow:
                        startActivity(new Intent(MainActivity.this, FollowUs_Activity.class));
                        break;*/
                    case R.id.drawer_logout:
                        user.removeUser();
                        startActivity(new Intent(MainActivity.this, Login_Activity.class));
                        finish();
                        break;

                    default:
                        return true;
                }
                return true;
            }
        });

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);


        fetchQuantity();
        fetchProfile();
        fetchBreak();

        checkLocationPermission();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
            //checkGPS();
        } else {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            // Initialize the location fields
            if (location != null) {

                onLocationChanged(location);
            } else {
                //txt.setText("Location not available");

                //Toast.makeText(MainActivity.this, "Location not available", Toast.LENGTH_SHORT).show();
            }

        }


        ch_break.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {

                if (isCheck)
                {
                    break_status = "Break";
                    reference = rootNode.getReference("driver");
                    reference.child(user.getDriver_id()).child("break").setValue(break_status);
                    break_status();
                }
                else
                {
                    break_status="";
                    reference = rootNode.getReference("driver");
                    reference.child(user.getDriver_id()).child("break").setValue(break_status);
                    break_status();
                }
            }
        });


    }

      private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),0);
        viewPagerAdapter.addFragment(new OpenOrder_Fragment(), "Open");
        viewPagerAdapter.addFragment(new CompleteOrder_Fragment(), "Completed");
        viewPagerAdapter.addFragment(new CancelOrder_Fragment(), "Cancelled");
//        viewPagerAdapter.addFragment(new CancelOrder_Fragment(), "Cancelled");
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {


        lat = (double) (location.getLatitude());
        lng = (double) (location.getLongitude());



        Call<String> call = apiInterface.fetch_active(user.getDriver_id());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {

                        String status= jsonObject.getString("status");

                        if (status.equals("Active"))
                        {
                           /* duty_status.setText("Active");
                            duty_status.setTextColor(Color.parseColor("#ffffff"));
                            duty_status.setBackgroundColor(Color.parseColor("#4CAF50"));*/

                           /* reference = rootNode.getReference("driver");
                            LatLng_Model model = new LatLng_Model(user.getDriver_id(),String.valueOf(lat),String.valueOf(lng));
                            reference.child(user.getDriver_id()).setValue(model);*/

                            firebaseLatLng(status);
                        }
                        else
                        {
                            firebaseLatLng(status);
                            /*duty_status.setText("Deactive");
                            duty_status.setBackgroundColor(Color.parseColor("#e62e00"));
                            duty_status.setTextColor(Color.parseColor("#ffffff"));*/
                        }


                    }

                    else
                    {
                        //Toast.makeText(MainActivity.this, "Not Active", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {

                    Toast.makeText(MainActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
            }
        });




        Log.d("onLocationChanged",  "Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

        Log.d("Latitude","enable");
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

        Log.d("Latitude","disable");
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        item = menu.findItem(R.id.action_duty);
        MenuItemCompat.setActionView(item, R.layout.custom_btn_active);
        LinearLayout notifcount = (LinearLayout) MenuItemCompat.getActionView(item);
        duty_status = notifcount.findViewById(R.id.duty_status);

        fetchActive();


        duty_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentDateandTime = sdf.format(new Date());

                if (duty_status.getText().toString().equals("Active"))
                {
                    status = "Deactive";


                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        OnGPS();

                    }
                    else
                    {
                        active(currentDateandTime,currentDateandDate);
                    }



                }
                else
                {

                    status = "Active";


                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        OnGPS();
                    }
                    else
                    {
                        active(currentDateandTime,currentDateandDate);

                        /*if (lat==0.0)
                        {
                            Toast.makeText(MainActivity.this, "Please Wait", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            active(currentDateandTime,currentDateandDate);
                        }*/

                    }


                }


            }
        });



        return true;

    }

    private void firebaseLatLng(String status)
    {

        reference = rootNode.getReference("driver");

        LatLng_Model model = new LatLng_Model(user.getDriver_id(),String.valueOf(lat),String.valueOf(lng),status,break_status,user.getArea());


        reference.child(user.getDriver_id()).setValue(model);

    }

    private void active(String time, String date)
    {
        Call<String> call = apiInterface.active(user.getDriver_id(),time,date,month,year, status);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {

                        firebaseLatLng(status);
                        fetchActive();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Not Active", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {

                    Toast.makeText(MainActivity.this, "Somthing went wrong + : " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
            }
        });
    }





    private void deactive(String time, String date)
    {
        Call<String> call = apiInterface.deactive(user.getDriver_id(),time,date);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {
                        //Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        fetchActive();
                    }
                    else if (jsonObject.getString("rec").equals("2"))
                    {
                        Toast.makeText(MainActivity.this, "Already Done", Toast.LENGTH_SHORT).show();
                    }

                    else
                    {
                        Toast.makeText(MainActivity.this, "Not Deavtive", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {

                    Toast.makeText(MainActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void break_status()
    {
        Call<String> call = apiInterface.break_status(user.getDriver_id(),break_status);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {

                    }

                    else
                    {
                        Toast.makeText(MainActivity.this, "Not Break", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {

                    Toast.makeText(MainActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void fetchBreak()
    {
        Call<String> call = apiInterface.fetch_break(user.getDriver_id());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {

                        String break_status= jsonObject.getString("break_status");

                        if (break_status.equals("Break"))
                        {
                            ch_break.setChecked(true);

                        }
                        else
                        {
                            ch_break.setChecked(false);
                        }


                    }

                    else
                    {
                        //Toast.makeText(MainActivity.this, "Not fetch break", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {

                    Toast.makeText(MainActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void fetchActive()
    {
        Call<String> call = apiInterface.fetch_active(user.getDriver_id());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {

                        String status= jsonObject.getString("status");

                        if (status.equals("Active"))
                        {

                            duty_status.setText("Active");
                            duty_status.setTextColor(Color.parseColor("#ffffff"));
                            duty_status.setBackgroundColor(Color.parseColor("#4CAF50"));
                        }
                        else
                        {
                            duty_status.setText("Deactive");
                            duty_status.setBackgroundColor(Color.parseColor("#e62e00"));
                            duty_status.setTextColor(Color.parseColor("#ffffff"));
                        }


                    }

                    else
                    {
                        Toast.makeText(MainActivity.this, "Not Active Found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {

                    Toast.makeText(MainActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void fetchProfile()
    {
        Call<String> call = apiInterface.fetch_profile(user.getDriver_id());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {


                        //edit_name.setText(jsonObject.getString("driver_name"));
                        driver_name = jsonObject.getString("driver_name");
                        toolbar.setTitle(driver_name + " "+ "("+user.getDriver_id() + ")");
                        /*edit_number.setText(jsonObject.getString("phone"));
                        edit_email.setText(jsonObject.getString("email"));
                        edit_address.setText(jsonObject.getString("area"));
                        String hourly_rate = jsonObject.getString("hourly_rate");*/

                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Profile Details Not found", Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {

                    Toast.makeText(MainActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
            }
        });
    }




    public void showStartTime(int hour, int min) {
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }



        String minutes = "";
        if (min < 10)

            minutes = "0" + min ;
        else
            minutes = String.valueOf(min);


        String hours = "";
        if (hour < 10)

            hours = "0" + hour ;
        else
            hours = String.valueOf(hour);


        /*txt_start_time.setText(new StringBuilder().append(hours).append(":").append(minutes)
                .append(" ").append(format));*/
    }



    private void fetchQuantity()
    {
        Call<String> call = apiInterface.fetch_quantity(user.getDriver_id(), currentDateandDate);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                   txt_qty.setText(jsonObject.getString("qty"));
                   txt_salary.setText(jsonObject.getString("salary"));
                   txt_total.setText(jsonObject.getString("total"));

                   String tip = jsonObject.getString("tip");

                    txt_tip.setText(new DecimalFormat("##.##").format(Double.parseDouble(tip)));

                    String hours =jsonObject.getString("hour");

                    if (hours.equals("null"))
                    {
                        txt_hours.setText("0.0");
                    }
                    else
                    {
                        txt_hours.setText(hours);
                    }


                } catch (JSONException e) {

                    Toast.makeText(MainActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(getActivity(), "Permission not available requesting permission", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        } else {


            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 200: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    finish();

                } else {
                    Toast.makeText(MainActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
                return;
            }


        }
    }



    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("For a better experience, turn on device location.")
                .setTitle("GPS")
                .setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No Thanks", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // Get the alert dialog buttons reference
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        positiveButton.setTextColor(Color.parseColor("#FF0B8B42"));
        negativeButton.setTextColor(Color.parseColor("#4588f5"));
    }


    @Override
    protected void onResume() {
        super.onResume();


        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //OnGPS();
        } else {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);



            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            // Initialize the location fields
            if (location != null) {

                onLocationChanged(location);
            } else {
                //txt.setText("Location not available");

                // Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
            }

        }




    }


    @Override
    protected void onPause() {
        super.onPause();

        locationManager.removeUpdates(this);

    }


}