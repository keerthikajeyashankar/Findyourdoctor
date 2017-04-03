package com.find.my.doctorapp;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.find.my.util.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.*;
import common.Configg;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Response {
    private AsyncPOST asyncPOST;
    private ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    private AdapterAppointment adapterDoctorList;
    private ListView list_appointment;
    private Button btn_doctor_list, btn_appointment;
    private List<NameValuePair> nameValuePairs;
    private Switch mySwitch;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private TextView switchStatus;

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e("", "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))
//            txtRegId.setText("Firebase Reg Id: " + regId);
            Toast.makeText(getApplicationContext(), "" + regId, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "" + "cxzc", Toast.LENGTH_SHORT).show();

//            txtRegId.setText("Firebase Reg Id is not received yet!");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btn_doctor_list = (Button) findViewById(R.id.btn_doctor_list);
        switchStatus = (TextView) findViewById(R.id.switchStatus);
        btn_appointment = (Button) findViewById(R.id.btn_appointment);
//        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//                // checking for type intent filter
//                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
//                    // gcm successfully registered
//                    // now subscribe to `global` topic to receive app wide notifications
//                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
//
//                    displayFirebaseRegId();
//
//                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
//                    // new push notification is received
//
//                    String message = intent.getStringExtra("message");
//
//                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
//
////                    txtMessage.setText(message);
//                }
//            }
//        };
//
//        displayFirebaseRegId();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View header = navigationView.getHeaderView(0);
/*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        TextView name = (TextView) header.findViewById(R.id.textView_name);
        TextView type = (TextView) header.findViewById(R.id.txt_type);
        name.setText(getIntent().getStringExtra("name"));
        type.setText(getIntent().getStringExtra("type"));
        Menu nav_Menu = navigationView.getMenu();
        if (getIntent().getStringExtra("type").equals("doctor"))
            nav_Menu.findItem(R.id.nav_camera).setVisible(false);
        if (getIntent().getStringExtra("type").equals("patient"))
            nav_Menu.findItem(R.id.nav_gallery).setVisible(false);


        list_appointment = (ListView) findViewById(R.id.list_appointment);
        mySwitch = (Switch) findViewById(R.id.mySwitch);

        //set the switch to ON
        mySwitch.setChecked(true);
        //attach a listener to check for changes in state
        if (getIntent().getStringExtra("type").equals("patient")) {
            mySwitch.setVisibility(View.GONE);
            switchStatus.setVisibility(View.GONE);
        }
        if (getIntent().getStringExtra("type").equals("doctor")) {
            if (getIntent().getStringExtra("in_out").equals("1")) {
                mySwitch.setChecked(true);
            } else {
                mySwitch.setChecked(false);

            }

        }
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    switchStatus.setText("IN");
                    volley_post_user(getIntent().getStringExtra("doctor_id"), "1");
                } else {
                    switchStatus.setText("OUT");
                    volley_post_user(getIntent().getStringExtra("doctor_id"), "0");

                }

            }
        });

        //check the current state before we display the screen
        if (mySwitch.isChecked()) {
            switchStatus.setText("IN");
        } else {
            switchStatus.setText("OUT");
        }
        btn_doctor_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DoctorList.class);
//                Toast.makeText(MainActivity.this, ""+getIntent().getStringExtra("pid"), Toast.LENGTH_SHORT).show();

                intent.putExtra("patient_id", getIntent().getStringExtra("patient_id"));
                intent.putExtra("patient_name", getIntent().getStringExtra("patient_name"));
                intent.putExtra("patient_contact", getIntent().getStringExtra("patient_contact"));
//            intent.putExtra("patient_name", getIntent().getStringExtra("patient_name"));
//            intent.putExtra("patient_contact", getIntent().getStringExtra("patient_contact"));

                startActivity(intent);

            }
        });
        btn_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getStringExtra("type").equals("doctor")) {
                    Intent intent = new Intent(MainActivity.this, AppoinmentList.class);
                    intent.putExtra("doctor_id", getIntent().getStringExtra("doctor_id"));
                    intent.putExtra("type", getIntent().getStringExtra("type"));
                    startActivity(intent);

                } else if (getIntent().getStringExtra("type").equals("patient")) {
                    Intent intent = new Intent(MainActivity.this, AppoinmentList.class);
                    intent.putExtra("patient_id", getIntent().getStringExtra("patient_id"));
                    intent.putExtra("type", getIntent().getStringExtra("type"));
                    startActivity(intent);

                }

            }
        });

//        new AsyncGET(this, this, "get").execute(common.Configg.MAIN_URL + common.Configg.GET_APPOINTMENT);
    }

    private void volley_post_user(final String doctor_id, final String in_out) {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        StringRequest request = new StringRequest(Request.Method.POST, "http://vajralabs.com/Taxi/doctor_inout.php", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("permission_response" + response);

                pDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                Toast.makeText(getApplicationContext(), "response" + response, 1000).show();


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                pDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateTime = dateFormat.format(new Date()); // Find todays date

                System.out.println("currentDateTime_param" + currentDateTime);
                Map<String, String> params = new HashMap<String, String>();
                params.put("doctor_id", doctor_id);
                params.put("in_out", in_out);

                System.out.println("permission_params" + params);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // register GCM registration complete receiver
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(Config.REGISTRATION_COMPLETE));
//
//        // register new push message receiver
//        // by doing this, the activity will be notified each time a new message arrives
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(Config.PUSH_NOTIFICATION));
//
//        // clear the notification area when the app is opened
//        NotificationUtils.clearNotifications(getApplicationContext());
        nameValuePairs = new ArrayList<NameValuePair>();
        if (getIntent().getStringExtra("type").equals("doctor")) {
            list_appointment.setVisibility(View.VISIBLE);
            btn_appointment.setVisibility(View.GONE);
            btn_doctor_list.setVisibility(View.GONE);
            nameValuePairs.add(new BasicNameValuePair("doctor_id", getIntent().getStringExtra("doctor_id")));
            asyncPOST = new AsyncPOST(nameValuePairs, MainActivity.this, Configg.MAIN_URL + "doctor_appointment.php", MainActivity.this);
            asyncPOST.execute();

        } else if (getIntent().getStringExtra("type").equals("patient")) {
            list_appointment.setVisibility(View.GONE);
            btn_appointment.setVisibility(View.VISIBLE);
            btn_doctor_list.setVisibility(View.VISIBLE);
//            nameValuePairs.add(new BasicNameValuePair("patient_id", getIntent().getStringExtra("patient_id")));
//            asyncPOST = new AsyncPOST(nameValuePairs, MainActivity.this, Configg.MAIN_URL + "patient_app.php", MainActivity.this);
//            asyncPOST.execute();

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
//            Toast.makeText(MainActivity.this, getIntent().getStringExtra("patient_contact"), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, DoctorList.class);
            intent.putExtra("pid", getIntent().getStringExtra("patient_id"));
            intent.putExtra("patient_name", getIntent().getStringExtra("patient_name"));
            intent.putExtra("patient_contact", getIntent().getStringExtra("patient_contact"));
//            intent.putExtra("patient_name", getIntent().getStringExtra("patient_name"));
//            intent.putExtra("patient_contact", getIntent().getStringExtra("patient_contact"));

            startActivity(intent);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

            if (getIntent().getStringExtra("type").equals("doctor")) {

                Intent intent = new Intent(MainActivity.this, EditProfileDoctor.class);
                intent.putExtra("age", getIntent().getStringExtra("age"));
                intent.putExtra("first_name", getIntent().getStringExtra("first_name"));
                intent.putExtra("last_name", getIntent().getStringExtra("last_name"));
                intent.putExtra("qualification", getIntent().getStringExtra("qualification"));
                intent.putExtra("specialist", getIntent().getStringExtra("specialist"));
                intent.putExtra("landline", getIntent().getStringExtra("landline"));
                intent.putExtra("doctor_id", getIntent().getStringExtra("doctor_id"));

                intent.putExtra("hospital", getIntent().getStringExtra("hospital"));
                startActivity(intent);
            } else if (getIntent().getStringExtra("type").equals("patient")) {
                Intent intent = new Intent(MainActivity.this, EditProfilePatient.class);
                intent.putExtra("age", getIntent().getStringExtra("age"));
                intent.putExtra("first_name", getIntent().getStringExtra("first_name"));
                intent.putExtra("last_name", getIntent().getStringExtra("last_name"));
                intent.putExtra("address", getIntent().getStringExtra("address"));
                intent.putExtra("pid", getIntent().getStringExtra("pid"));

                startActivity(intent);
            }


        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void processFinish(String output) {
        if (!(hashMapArrayList.size() == 0)) {
            hashMapArrayList.clear();
        }
        try {
            JSONArray jsonArray = new JSONArray(output);
            if (jsonArray.length() == 0) {
                if (getIntent().getStringExtra("type").equals("doctor")) {
                    Toast.makeText(getApplicationContext(), "Theres No Appointment For You.", Toast.LENGTH_SHORT).show();
                }
            }
            HashMap<String, String> stringHashMap;
            for (int i = 0; i < jsonArray.length(); i++) {
                stringHashMap = new HashMap<String, String>();
                stringHashMap.put("doctor_name", jsonArray.getJSONObject(i).getString("doctor_name"));
                stringHashMap.put("hospital", jsonArray.getJSONObject(i).getString("hospital"));
                stringHashMap.put("doctor_contact", jsonArray.getJSONObject(i).getString("doctor_contact"));
                stringHashMap.put("patient_id", jsonArray.getJSONObject(i).getString("patient_id"));
                stringHashMap.put("doctor_id", jsonArray.getJSONObject(i).getString("doctor_id"));
                stringHashMap.put("patient_name", jsonArray.getJSONObject(i).getString("patient_name"));
                stringHashMap.put("status", jsonArray.getJSONObject(i).getString("confirmation"));

                stringHashMap.put("patient_contact", jsonArray.getJSONObject(i).getString("patient_contact"));
                stringHashMap.put("date", jsonArray.getJSONObject(i).getString("title"));
                stringHashMap.put("time", jsonArray.getJSONObject(i).getString("content"));
                stringHashMap.put("image_path", jsonArray.getJSONObject(i).getString("image_path"));
                stringHashMap.put("nid", jsonArray.getJSONObject(i).getString("nid"));
                stringHashMap.put("token", jsonArray.getJSONObject(i).getString("token"));

                stringHashMap.put("problem", jsonArray.getJSONObject(i).getString("posted_by"));
                hashMapArrayList.add(stringHashMap);

            }
            adapterDoctorList = new AdapterAppointment(MainActivity.this, hashMapArrayList, "");
            list_appointment.setAdapter(adapterDoctorList);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
