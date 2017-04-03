package com.find.my.doctorapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import common.AsyncPOST;
import common.Configg;
import common.Response;

public class EditProfileDoctor extends AppCompatActivity implements Response {
    private EditText edt_first_name;
    private EditText edt_speciallist;
    private EditText edt_hospital;
    private EditText edt_last_name;
    private EditText edt_mobile;
    private EditText edt_email;
    private EditText edt_pwd;
    private Button btn_submit;
    private EditText edt_confirmpwd;
    private AsyncPOST asyncPOST;
    private List<NameValuePair> nameValuePairs_signup = new ArrayList<NameValuePair>();
    private EditText edt_qualification, edt_age;
    private EditText edt_lanline;
    private EditText edt_constarint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Edit-Profile");
        edt_qualification = (EditText) findViewById(R.id.edt_qualification);
        edt_age = (EditText) findViewById(R.id.edt_age);
        edt_first_name = (EditText) findViewById(R.id.edt_first_name);
        edt_speciallist = (EditText) findViewById(R.id.edt_speciallist);
        edt_hospital = (EditText) findViewById(R.id.edt_hospital);
        edt_last_name = (EditText) findViewById(R.id.edt_last_name);
        edt_mobile = (EditText) findViewById(R.id.edt_mobile);
        edt_lanline = (EditText) findViewById(R.id.edt_lanline);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        edt_age.setText(getIntent().getStringExtra("age"));
        edt_first_name.setText(getIntent().getStringExtra("first_name"));
        edt_last_name.setText(getIntent().getStringExtra("last_name"));
        edt_qualification.setText(getIntent().getStringExtra("qualification"));
        edt_mobile.setVisibility(View.GONE);
        edt_speciallist.setText(getIntent().getStringExtra("specialist"));
        edt_lanline.setText(getIntent().getStringExtra("landline"));
        edt_hospital.setText(getIntent().getStringExtra("hospital"));

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!signUpValidation()) {
                    Toast.makeText(getApplicationContext(),""+getIntent().getStringExtra("doctor_id"),Toast.LENGTH_SHORT).show();
                    nameValuePairs_signup.add(new BasicNameValuePair("first_name", edt_first_name.getText().toString()));
                    nameValuePairs_signup.add(new BasicNameValuePair("last_name", edt_last_name.getText().toString()));
                    nameValuePairs_signup.add(new BasicNameValuePair("age", edt_age.getText().toString()));
                    nameValuePairs_signup.add(new BasicNameValuePair("did", getIntent().getStringExtra("doctor_id")));

                    nameValuePairs_signup.add(new BasicNameValuePair("qualification", edt_qualification.getText().toString()));

//                    nameValuePairs_signup.add(new BasicNameValuePair("mobile", edt_mobile.getText().toString()));
                    nameValuePairs_signup.add(new BasicNameValuePair("specialist", edt_speciallist.getText().toString().trim()));
                    nameValuePairs_signup.add(new BasicNameValuePair("hospital", edt_hospital.getText().toString().trim()));
                    nameValuePairs_signup.add(new BasicNameValuePair("landline", edt_lanline.getText().toString().trim()));


                    System.out.println("signup_params" + nameValuePairs_signup.toString());

                    asyncPOST = new AsyncPOST(nameValuePairs_signup, EditProfileDoctor.this, Configg.MAIN_URL + Configg.EDIT_DOCTOR, EditProfileDoctor.this);
                    asyncPOST.execute();

                }
            }
        });

    }

    private boolean signUpValidation() {
        if (edt_first_name.getText().toString().equals("")) {
            edt_first_name.requestFocus();

            Toast.makeText(getApplicationContext(), "Kindly Fill Your First Name", Toast.LENGTH_SHORT).show();
            return true;

        }
        if (edt_last_name.getText().toString().equals("")) {
            edt_last_name.requestFocus();
            Toast.makeText(getApplicationContext(), "Kindly Fill Your Last Name", Toast.LENGTH_SHORT).show();
            return true;

        }
        if (edt_age.getText().toString().equals("")) {
            edt_age.requestFocus();
            Toast.makeText(getApplicationContext(), "Kindly Fill Age.", Toast.LENGTH_SHORT).show();
            return true;

        }
        if (edt_qualification.getText().toString().equals("")) {
            edt_qualification.requestFocus();
            Toast.makeText(getApplicationContext(), "Kindly Fill The Qualification.", Toast.LENGTH_SHORT).show();
            return true;

        }


        if (edt_mobile.getText().toString().equals("")) {
            edt_mobile.requestFocus();
            Toast.makeText(getApplicationContext(), "Mobile Number Should Not Empty.", Toast.LENGTH_SHORT).show();
            return true;

        }

        if (edt_lanline.getText().toString().equals("")) {
            edt_lanline.requestFocus();
            Toast.makeText(getApplicationContext(), "Landline Number Missing.", Toast.LENGTH_SHORT).show();
            return true;

        }
        return false;
    }

    @Override
    public void processFinish(String output) {
Toast.makeText(getApplicationContext(),"Doctor Profile Updated Successfully.",Toast.LENGTH_SHORT).show();
        finish();
    }
}
