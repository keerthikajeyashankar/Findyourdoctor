package com.find.my.doctorapp;


import android.content.DialogInterface;
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


public class SignUp extends AppCompatActivity implements Response {

    private AutoCompleteTextView edt_first_name;
    private AutoCompleteTextView edt_speciallist;
    private AutoCompleteTextView edt_hospital;
    private AutoCompleteTextView edt_last_name;
    private AutoCompleteTextView edt_mobile;
    private AutoCompleteTextView edt_email;
    private AutoCompleteTextView edt_pwd;
    private Button btn_submit;
    private AutoCompleteTextView edt_confirmpwd;
    private AsyncPOST asyncPOST;
    private List<NameValuePair> nameValuePairs_signup = new ArrayList<NameValuePair>();
    private EditText edt_qualification, edt_age;
    private EditText edt_lanline;
    private EditText edt_constarint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edt_qualification = (EditText) findViewById(R.id.edt_qualification);
        edt_age = (EditText) findViewById(R.id.edt_age);
        edt_first_name = (AutoCompleteTextView) findViewById(R.id.edt_first_name);
        edt_speciallist = (AutoCompleteTextView) findViewById(R.id.edt_speciallist);
        edt_hospital = (AutoCompleteTextView) findViewById(R.id.edt_hospital);
        edt_last_name = (AutoCompleteTextView) findViewById(R.id.edt_last_name);
        edt_constarint = (EditText) findViewById(R.id.edt_constarint);
        edt_mobile = (AutoCompleteTextView) findViewById(R.id.edt_mobile);
        edt_email = (AutoCompleteTextView) findViewById(R.id.edt_email);
        edt_pwd = (AutoCompleteTextView) findViewById(R.id.edt_pwd);
        edt_confirmpwd = (AutoCompleteTextView) findViewById(R.id.edt_confirmpwd);
        edt_lanline=(EditText) findViewById(R.id.edt_lanline);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!signUpValidation()) {
                    nameValuePairs_signup.add(new BasicNameValuePair("first_name", edt_first_name.getText().toString()));
                    nameValuePairs_signup.add(new BasicNameValuePair("last_name", edt_last_name.getText().toString()));
                    nameValuePairs_signup.add(new BasicNameValuePair("age", edt_age.getText().toString()));
                    nameValuePairs_signup.add(new BasicNameValuePair("qualification", edt_qualification.getText().toString()));

                    nameValuePairs_signup.add(new BasicNameValuePair("email", edt_email.getText().toString()));
                    nameValuePairs_signup.add(new BasicNameValuePair("mobile", edt_mobile.getText().toString()));
                    nameValuePairs_signup.add(new BasicNameValuePair("password", edt_pwd.getText().toString()));
                    nameValuePairs_signup.add(new BasicNameValuePair("specialist", edt_speciallist.getText().toString().trim()));
                    nameValuePairs_signup.add(new BasicNameValuePair("hospital", edt_hospital.getText().toString().trim()));
                    nameValuePairs_signup.add(new BasicNameValuePair("landline", edt_lanline.getText().toString().trim()));
                    nameValuePairs_signup.add(new BasicNameValuePair("constraint", edt_constarint.getText().toString()));



                    System.out.println("signup_params" + nameValuePairs_signup.toString());

                    asyncPOST = new AsyncPOST(nameValuePairs_signup, SignUp.this, Configg.MAIN_URL + Configg.SIGNUP_ROOT, SignUp.this);
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
        if (edt_lanline.getText().toString().equals(edt_pwd.getText().toString())) {
            edt_lanline.requestFocus();
            Toast.makeText(getApplicationContext(), "Landline Number Missing.", Toast.LENGTH_SHORT).show();
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
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SignUp.this);
                builder.setMessage("User Already Exist.").setTitle("Response from Servers")
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
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SignUp.this);
                builder.setMessage("Doctor Registered Successfully.").setTitle("Response from Servers")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // do nothing
                                finish();

                                Configg.storeDATA(SignUp.this,"doctor_email",edt_email.getText().toString());
                                Configg.storeDATA(SignUp.this,"doctor_password",edt_pwd.getText().toString());
                                Configg.storeDATA(SignUp.this,"doctor_mobile",edt_mobile.getText().toString());

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
