package com.find.my.doctorapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

import common.*;
import common.Configg;

public class DoctorList extends AppCompatActivity implements Response {

    private AsyncGET asyncGET;
    private ArrayList<HashMap<String,String>> hashMapArrayList=new ArrayList<HashMap<String, String>>();
    private AdapterDoctorList adapterDoctorList;
    private ListView list_doctor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        list_doctor=(ListView) findViewById(R.id.list_doctor);
        new AsyncGET(this, this, "get").execute(Configg.MAIN_URL+ Configg.DOCTOR_LIST);
    }

    @Override
    public void processFinish(String output) {
        if (!(hashMapArrayList.size() ==0)){
            hashMapArrayList.clear();
        }
        try {
            System.out.println("doctor_list"+output);
            JSONArray jsonArray=new JSONArray(output);
            HashMap<String,String> stringHashMap;
            for (int i=0;i<jsonArray.length();i++){
                stringHashMap=new HashMap<String, String>();
                stringHashMap.put("first_name",jsonArray.getJSONObject(i).getString("first_name"));
                stringHashMap.put("specialist",jsonArray.getJSONObject(i).getString("specialist"));
                stringHashMap.put("mobile",jsonArray.getJSONObject(i).getString("mobile"));
                stringHashMap.put("hospital",jsonArray.getJSONObject(i).getString("hospital"));
                stringHashMap.put("sid",jsonArray.getJSONObject(i).getString("sid"));
                stringHashMap.put("doctor_name",jsonArray.getJSONObject(i).getString("first_name"));
                stringHashMap.put("doctor_contact",jsonArray.getJSONObject(i).getString("mobile"));
                stringHashMap.put("hospital",jsonArray.getJSONObject(i).getString("hospital"));
                stringHashMap.put("landline",jsonArray.getJSONObject(i).getString("landline"));
                stringHashMap.put("in_out",jsonArray.getJSONObject(i).getString("in_out"));



                hashMapArrayList.add(stringHashMap);

            }
            adapterDoctorList=new AdapterDoctorList(DoctorList.this,hashMapArrayList,"");
            list_doctor.setAdapter(adapterDoctorList);

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
