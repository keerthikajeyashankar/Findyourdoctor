package com.find.my.doctorapp;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import common.AsyncPOST;
import common.Configg;
import common.Response;


public class SignUpPatient extends AppCompatActivity implements Response {

    private AutoCompleteTextView edt_first_name;
    private EditText edt_address;
    private AutoCompleteTextView edt_hospital;
    private AutoCompleteTextView edt_last_name;
    private AutoCompleteTextView edt_mobile;
    private AutoCompleteTextView edt_email;
    private AutoCompleteTextView edt_pwd;
    private Button btn_submit;
    private AutoCompleteTextView edt_confirmpwd;
    private AsyncPOST asyncPOST;
    private List<NameValuePair> nameValuePairs_signup = new ArrayList<NameValuePair>();
    private EditText edt_pid;
    private EditText edt_age;
    private EditText edt_sex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_patient);
        edt_pid = (EditText) findViewById(R.id.edt_pid);
        edt_age = (EditText) findViewById(R.id.edt_age);
        edt_sex = (EditText) findViewById(R.id.edt_sex);
        edt_first_name = (AutoCompleteTextView) findViewById(R.id.edt_first_name);
        edt_address = (EditText) findViewById(R.id.edt_address);
        edt_hospital = (AutoCompleteTextView) findViewById(R.id.edt_hospital);
        edt_last_name = (AutoCompleteTextView) findViewById(R.id.edt_last_name);
        edt_mobile = (AutoCompleteTextView) findViewById(R.id.edt_mobile);
        edt_email = (AutoCompleteTextView) findViewById(R.id.edt_email);
        edt_pwd = (AutoCompleteTextView) findViewById(R.id.edt_pwd);
        edt_confirmpwd = (AutoCompleteTextView) findViewById(R.id.edt_confirmpwd);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!signUpValidation()) {
                    nameValuePairs_signup.add(new BasicNameValuePair("first_name", edt_first_name.getText().toString()));
                    nameValuePairs_signup.add(new BasicNameValuePair("last_name", edt_last_name.getText().toString()));
                    nameValuePairs_signup.add(new BasicNameValuePair("pid", edt_pid.getText().toString()));
                    nameValuePairs_signup.add(new BasicNameValuePair("age", edt_age.getText().toString()));
                    nameValuePairs_signup.add(new BasicNameValuePair("sex", edt_sex.getText().toString()));

                    nameValuePairs_signup.add(new BasicNameValuePair("email", edt_email.getText().toString()));
                    nameValuePairs_signup.add(new BasicNameValuePair("mobile", edt_mobile.getText().toString()));
                    nameValuePairs_signup.add(new BasicNameValuePair("password", edt_pwd.getText().toString()));
                    nameValuePairs_signup.add(new BasicNameValuePair("address", edt_address.getText().toString().trim()));



                    System.out.println("signup_params" + nameValuePairs_signup.toString());

                    asyncPOST = new AsyncPOST(nameValuePairs_signup, SignUpPatient.this, Configg.MAIN_URL + Configg.SIGNUP_PATIENT_ROOT, SignUpPatient.this);
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
        if (edt_pid.getText().toString().equals("")) {
            edt_pid.requestFocus();
            Toast.makeText(getApplicationContext(), "Kindly Fill Patient.Id", Toast.LENGTH_SHORT).show();
            return true;

        }
        if (edt_age.getText().toString().equals("")) {
            edt_age.requestFocus();
            Toast.makeText(getApplicationContext(), "Kindly Fill Your Age.", Toast.LENGTH_SHORT).show();
            return true;

        }
        if (edt_sex.getText().toString().equals("")) {
            edt_sex.requestFocus();
            Toast.makeText(getApplicationContext(), "Kindly Fill Sex Field", Toast.LENGTH_SHORT).show();
            return true;

        }
        if (edt_email.getText().toString().equals("")) {
            edt_email.requestFocus();
            Toast.makeText(getApplicationContext(), "Kindly Fill Your Email Detail", Toast.LENGTH_SHORT).show();
            return true;

        }
        if (!Config.isValidEmaillId(edt_email.getText().toString().trim())) {
            edt_email.requestFocus();
            Toast.makeText(getApplicationContext(), "enter valid email address", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (edt_mobile.getText().toString().equals("")) {
            edt_mobile.requestFocus();
            Toast.makeText(getApplicationContext(), "Mobile Number Should Not Empty.", Toast.LENGTH_SHORT).show();
            return true;

        }
        if (edt_address.getText().toString().equals("")) {
            edt_mobile.requestFocus();
            Toast.makeText(getApplicationContext(), "Address Should Should Not Empty.", Toast.LENGTH_SHORT).show();
            return true;

        }
        if (edt_pwd.getText().toString().equals("")) {
            edt_pwd.requestFocus();
            Toast.makeText(getApplicationContext(), "Password Should Not Empty.", Toast.LENGTH_SHORT).show();
            return true;

        }
        if (edt_confirmpwd.getText().toString().equals("")) {
            edt_confirmpwd.requestFocus();
            Toast.makeText(getApplicationContext(), "Confirm Your Password.", Toast.LENGTH_SHORT).show();
            return true;

        }
        if (!edt_confirmpwd.getText().toString().equals(edt_pwd.getText().toString())) {
            edt_pwd.requestFocus();
            Toast.makeText(getApplicationContext(), "Password Doesn't Match With Confirm Password.", Toast.LENGTH_SHORT).show();
            return true;

        }
        return false;
    }


    @Override
    public void processFinish(String output) {
        System.out.println("signup_output" + output);

        try {
            final JSONObject jsonObject = new JSONObject(output);
            if (jsonObject.getString("success").equals("3")) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SignUpPatient.this);
                builder.setMessage("Patient Already Exist.").setTitle("Response from Servers")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // do nothing
                                finish();
                            }
                        });
                android.app.AlertDialog alert = builder.create();
                alert.show();
            } else if (jsonObject.getString("success").equals("1")) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SignUpPatient.this);
                builder.setMessage("Patient Registered Successfully.").setTitle("Response from Servers")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // do nothing
                                finish();
                                Configg.storeDATA(SignUpPatient.this,"patient_email",edt_email.getText().toString());
                                Configg.storeDATA(SignUpPatient.this,"patient_password",edt_pwd.getText().toString());
                                Configg.storeDATA(SignUpPatient.this,"patient_mobile",edt_mobile.getText().toString());


                            }
                        });
                android.app.AlertDialog alert = builder.create();
                alert.show();

            }

//            else if (jsonObject.getString("json").equals("confirmation")) {
//                if (jsonObject.getString("success").equals("1")) {
//                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SignUp.this);
//                    builder.setMessage(jsonObject.getString("message")).setTitle("Welcome You....")
//                            .setCancelable(false)
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    // do nothing
//                                    finish();
//                                }
//                            });
//                    android.app.AlertDialog alert = builder.create();
//                    alert.show();
//                }
//                else if (jsonObject.getString("success").equals("2")) {
//                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SignUp.this);
//
//                    builder.setMessage(jsonObject.getString("message")).setTitle("Warning....")
//                            .setCancelable(false)
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    // do nothing
//                                }
//                            });
//                    android.app.AlertDialog alert = builder.create();
//                    alert.show();
//
//                }
//
//            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
