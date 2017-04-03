package com.find.my.doctorapp;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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

public class SatatusActivity extends AppCompatActivity implements Response {
    TextView txt_patient_name, txt_doctor_name, txt_hospital, txt_problem, txt_appoitment, txt_time;
    ToggleButton tog_status;
    ImageView imageView;
    private AsyncPOST asyncPOST;
    //    Button btn_submit;
    List<NameValuePair> nameValuePairs;
    private String status = "";
    private CheckBox chk_decline, chk_fix;
    private String confirmation = "";
    private LinearLayout linear_doctor;
    private TextView txt_status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satatus);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Status-" + getIntent().getStringExtra("status"));
        txt_patient_name = (TextView) findViewById(R.id.txt_patient_name);
        txt_doctor_name = (TextView) findViewById(R.id.txt_doctor_name);
//        btn_submit = (Button) findViewById(R.id.btn_submit);
        txt_hospital = (TextView) findViewById(R.id.txt_hospital);
        txt_problem = (TextView) findViewById(R.id.txt_problem);
        txt_appoitment = (TextView) findViewById(R.id.txt_appoitment);
        txt_status = (TextView) findViewById(R.id.txt_status);
        txt_time = (TextView) findViewById(R.id.txt_time);
//        tog_status = (ToggleButton) findViewById(R.id.tog_status);
        imageView = (ImageView) findViewById(R.id.imageView);
        linear_doctor = (LinearLayout) findViewById(R.id.linear_doctor);
        Picasso.with(this).load(getIntent().getStringExtra("image_path")).into(imageView);
        txt_patient_name.setText(getIntent().getStringExtra("patient_name"));
        txt_doctor_name.setText(getIntent().getStringExtra("doctor_name"));
        txt_hospital.setText(getIntent().getStringExtra("hospital"));
        txt_problem.setText(getIntent().getStringExtra("problem"));
        txt_appoitment.setText(getIntent().getStringExtra("date"));
        txt_time.setText(getIntent().getStringExtra("time"));
        chk_fix = (CheckBox) findViewById(R.id.chk_fix);
        chk_decline = (CheckBox) findViewById(R.id.chk_decline);
        if (getIntent().getStringExtra("type").equals("doctor")) {
            linear_doctor.setVisibility(View.VISIBLE);
            txt_status.setVisibility(View.GONE);
        } else {
            linear_doctor.setVisibility(View.GONE);
            txt_status.setVisibility(View.VISIBLE);
        }
        if (!getIntent().getStringExtra("status").equals(""))
            txt_status.setText(getIntent().getStringExtra("status"));
        else
            txt_status.setText("pending");

        chk_fix.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                               @Override
                                               public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                   if (b) {
                                                       chk_decline.setChecked(false);
                                                       chk_fix.setChecked(true);
                                                       status = "fixed";
                                                       if (!status.equals("")) {
                                                           SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                                                           String regId = pref.getString("regId", "");
                                                           nameValuePairs = new ArrayList<NameValuePair>();
                                                           nameValuePairs.add(new BasicNameValuePair("doctor_id", getIntent().getStringExtra("doctor_id")));
                                                           nameValuePairs.add(new BasicNameValuePair("status", status));
                                                           nameValuePairs.add(new BasicNameValuePair("patient_id", getIntent().getStringExtra("patient_id")));
                                                           nameValuePairs.add(new BasicNameValuePair("nid", getIntent().getStringExtra("nid")));
                                                           nameValuePairs.add(new BasicNameValuePair("push_type", "individual"));

                                                           nameValuePairs.add(new BasicNameValuePair("token", regId));
                                                           System.out.println("please" + nameValuePairs.toString());

                                                           asyncPOST = new AsyncPOST(nameValuePairs, SatatusActivity.this, Configg.MAIN_URL + Configg.POST_STATUS, SatatusActivity.this);

                                                           asyncPOST.execute();
                                                       } else {
                                                           Toast.makeText(getApplicationContext(), "Please Select The Status", Toast.LENGTH_SHORT).show();
                                                       }

                                                   } else {
                                                   }
                                               }
                                           }
        );
        chk_decline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                   @Override
                                                   public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                       if (b) {
                                                           chk_decline.setChecked(true);
                                                           chk_fix.setChecked(false);
                                                           status = "canceled";
                                                           if (!status.equals("")) {
                                                               nameValuePairs = new ArrayList<NameValuePair>();
                                                               nameValuePairs.add(new BasicNameValuePair("doctor_id", getIntent().getStringExtra("doctor_id")));
                                                               nameValuePairs.add(new BasicNameValuePair("status", status));
                                                               nameValuePairs.add(new BasicNameValuePair("patient_id", getIntent().getStringExtra("patient_id")));
                                                               nameValuePairs.add(new BasicNameValuePair("nid", getIntent().getStringExtra("nid")));

                                                               asyncPOST = new AsyncPOST(nameValuePairs, SatatusActivity.this, Configg.MAIN_URL + Configg.POST_STATUS, SatatusActivity.this);
                                                               asyncPOST.execute();
                                                           } else {
                                                               Toast.makeText(getApplicationContext(), "Please Select The Status", Toast.LENGTH_SHORT).show();
                                                           }

                                                       } else {
                                                       }
                                                   }
                                               }
        );


//        btn_submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!status.equals("")) {
//                    nameValuePairs = new ArrayList<NameValuePair>();
//                    nameValuePairs.add(new BasicNameValuePair("doctor_id", getIntent().getStringExtra("doctor_id")));
//                    nameValuePairs.add(new BasicNameValuePair("status", status));
//                    asyncPOST = new AsyncPOST(nameValuePairs, SatatusActivity.this, Configg.MAIN_URL + Configg.POST_STATUS, SatatusActivity.this);
//                    asyncPOST.execute();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Please Select The Status", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });


    }

    @Override
    public void processFinish(String output) {

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", "");
        txt_status.setText(getIntent().getStringExtra("status"));

        volley_post_user("individual",status,"appointment",getIntent().getStringExtra("token"));
        nameValuePairs.add(new BasicNameValuePair("title", "Hello"));
        nameValuePairs.add(new BasicNameValuePair("message", "kafkjdafh"));
        nameValuePairs.add(new BasicNameValuePair("push_type", "individual"));
        nameValuePairs.add(new BasicNameValuePair("regId", getIntent().getStringExtra("token")));

        asyncPOST = new AsyncPOST(nameValuePairs, SatatusActivity.this, "http://vajralabs.com/Taxi/fire/index_2.php", SatatusActivity.this);
//        asyncPOST.execute();

//        overridePendingTransition( 0, 0);
//        startActivity(getIntent());
//        overridePendingTransition( 0, 0);

    }


    private void volley_post_user(final String push_type, final String message, final String title,final String regid) {
        RequestQueue queue = Volley.newRequestQueue(SatatusActivity.this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        StringRequest request = new StringRequest(Request.Method.POST, "http://vajralabs.com/Taxi/fire/index_2.php", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("permission_response" + response);

                pDialog.dismiss();
                finish();



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
                params.put("push_type", "individual");
                params.put("title", "Appoitment");
                params.put("regId", regid);
                params.put("message", status);


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
}
