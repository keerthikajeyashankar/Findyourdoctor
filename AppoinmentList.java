package com.find.my.doctorapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import common.*;

public class AppoinmentList extends AppCompatActivity implements Response{
    private AsyncPOST asyncPOST;
    private ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    private AdapterAppointment adapterDoctorList;
    private ListView list_appointment;
    private List<NameValuePair> nameValuePairs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoinment_list);
        list_appointment = (ListView) findViewById(R.id.list_appointment);

    }
    @Override
    protected void onResume() {
        super.onResume();
        nameValuePairs=new ArrayList<NameValuePair>();
        if (getIntent().getStringExtra("type").equals("doctor")){
            nameValuePairs.add(new BasicNameValuePair("doctor_id",getIntent().getStringExtra("doctor_id")));
            asyncPOST=new AsyncPOST(nameValuePairs,AppoinmentList.this, Configg.MAIN_URL+"doctor_appointment.php",AppoinmentList.this);
            asyncPOST.execute();

        }
        else if (getIntent().getStringExtra("type").equals("patient")){
            nameValuePairs.add(new BasicNameValuePair("patient_id",getIntent().getStringExtra("patient_id")));
            asyncPOST=new AsyncPOST(nameValuePairs,AppoinmentList.this, Configg.MAIN_URL+"patient_app.php",AppoinmentList.this);
            asyncPOST.execute();

        }
    }

    @Override
    public void processFinish(String output) {
        if (!(hashMapArrayList.size() == 0)) {
            hashMapArrayList.clear();
        }
        try {

            JSONArray jsonArray = new JSONArray(output);
            if (jsonArray.length()==0){
                if (getIntent().getStringExtra("type").equals("patient")) {
                    Toast.makeText(getApplicationContext(),"Theres No Appointment For You.",Toast.LENGTH_SHORT).show();
                }
            }
//            Toast.makeText(getApplicationContext(),"token"+jsonArray.getJSONObject(0).getString("token"),Toast.LENGTH_SHORT).show();

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
                stringHashMap.put("token", jsonArray.getJSONObject(i).getString("token"));

                stringHashMap.put("nid", jsonArray.getJSONObject(i).getString("nid"));

                stringHashMap.put("problem", jsonArray.getJSONObject(i).getString("posted_by"));
                hashMapArrayList.add(stringHashMap);

            }
            adapterDoctorList = new AdapterAppointment(AppoinmentList.this, hashMapArrayList, "");
            list_appointment.setAdapter(adapterDoctorList);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
