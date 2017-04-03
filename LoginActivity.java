package com.find.my.doctorapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import common.*;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, Response {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private AsyncPOST asyncPOST;
    private List<NameValuePair> nameValuePairs_signup_verification = new ArrayList<NameValuePair>();

    private List<NameValuePair> nameValuePairs_login;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private CheckBox chk_patient;
    private CheckBox chk_doctor;
    private String checker = "";
    private TextView txt_forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        txt_forgot = (TextView) findViewById(R.id.txt_forgot);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();
        chk_patient = (CheckBox) findViewById(R.id.chk_patient);
        chk_doctor = (CheckBox) findViewById(R.id.chk_doctor);
        chk_patient.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    chk_doctor.setChecked(false);
                    checker = "patient";
                }
            }
        });
        chk_doctor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    chk_patient.setChecked(false);
                    checker = "doctor";
                }
            }
        });
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        txt_forgot.setOnClickListener(new OnClickListener() {
            String chk = "";

            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(LoginActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.forgot_post);
                final CheckBox chk_doctor = (CheckBox) dialog.findViewById(R.id.chk_doctor);
                final CheckBox chk_patient = (CheckBox) dialog.findViewById(R.id.chk_patient);
                final EditText edt_email=(EditText) dialog.findViewById(R.id.edt_email);
                chk_patient.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        chk = "patient";
                        chk_doctor.setChecked(false);
                    }
                });
                chk_doctor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        chk = "doctor";
                        chk_patient.setChecked(false);
                    }
                });

                Button btn_submit = (Button) dialog.findViewById(R.id.btn_submit);
                btn_submit.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!edt_email.getText().toString().equals("")) {
                            if (chk.equals("patient")) {
                                dialog.dismiss();
                                new SendMail(LoginActivity.this, Configg.getDATA(LoginActivity.this, "patient_email"),
                                        "Find My Doctor", "Password:" + Configg.getDATA(LoginActivity.this, "patient_password")).execute();
                            } else if (chk.equals("doctor")) {
                                dialog.dismiss();
                                new SendMail(LoginActivity.this, edt_email.getText().toString(),
                                        "Find My Doctor", "Password:" + Configg.getDATA(LoginActivity.this, "doctor_password")).execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "Choose The Type", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Enter Your Registered Email", Toast.LENGTH_SHORT).show();

                        }
                    }

                });
                dialog.show();

            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                attemptLogin();
                if (!mEmailView.getText().toString().equals("")) {
                    if (!mPasswordView.getText().toString().equals("")) {
                        nameValuePairs_login = new ArrayList<NameValuePair>();
                        if (mEmailView.getText().toString().matches("\\d+(?:\\.\\d+)?")) {
                            System.out.println("Mobile");
                            nameValuePairs_login.add(new BasicNameValuePair("mobile", mEmailView.getText().toString()));
//                            Toast.makeText(getApplicationContext(), "mo", 1000).show();


                        } else {
                            System.out.println("email");
                            nameValuePairs_login.add(new BasicNameValuePair("email", mEmailView.getText().toString()));
//                            Toast.makeText(getApplicationContext(), "em", 1000).show();


                        }
                        nameValuePairs_login.add(new BasicNameValuePair("pwd", mPasswordView.getText().toString()));
                        System.out.println("login_params" + nameValuePairs_login.toString());

                        if (!checker.equals("")) {

                            if (checker.equals("patient")) {
                                asyncPOST = new AsyncPOST(nameValuePairs_login, LoginActivity.this, Configg.MAIN_URL + Configg.USER_LOGIN, LoginActivity.this);
                                asyncPOST.execute();

                            } else if (checker.equals("doctor")) {
                                asyncPOST = new AsyncPOST(nameValuePairs_login, LoginActivity.this, Configg.MAIN_URL + Configg.USER_LOGIN_DOCTOR, LoginActivity.this);
                                asyncPOST.execute();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Choose Login Type.", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Enter Valid Password.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Enter Valid Email.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button email_sign_up_button = (Button) findViewById(R.id.email_sign_up_button);
        email_sign_up_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = {"Patient", "Doctor",
                        "Cancel"};

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Registration!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Patient")) {
                            Intent intent = new Intent(LoginActivity.this, SignUpPatient.class);
                            startActivity(intent);

                        } else if (items[item].equals("Doctor")) {
                            Intent intent = new Intent(LoginActivity.this, SignUp.class);
                            startActivity(intent);

                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEmailView.getText().clear();
        mPasswordView.getText().clear();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    @Override
    public void processFinish(String output) {
        try {
            final JSONObject jsonObject = new JSONObject(output);
            if (jsonObject.getString("success").equals("1")) {
                JSONArray jsonArray = new JSONArray(jsonObject.getString("patient_list"));
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("type", checker);
                if (checker.equals("patient")) {
                    intent.putExtra("patient_id", jsonArray.getJSONObject(0).getString("pid"));
                    intent.putExtra("patient_name", jsonArray.getJSONObject(0).getString("first_name"));
                    intent.putExtra("first_name", jsonArray.getJSONObject(0).getString("first_name"));
                    intent.putExtra("last_name", jsonArray.getJSONObject(0).getString("last_name"));
                    intent.putExtra("age", jsonArray.getJSONObject(0).getString("age"));
                    intent.putExtra("address", jsonArray.getJSONObject(0).getString("address"));
                } else if (checker.equals("doctor")) {
                    intent.putExtra("doctor_id", jsonArray.getJSONObject(0).getString("sid"));
                    intent.putExtra("in_out", jsonArray.getJSONObject(0).getString("in_out"));
                    intent.putExtra("first_name", jsonArray.getJSONObject(0).getString("first_name"));
                    intent.putExtra("last_name", jsonArray.getJSONObject(0).getString("last_name"));
                    intent.putExtra("age", jsonArray.getJSONObject(0).getString("age"));
                    intent.putExtra("qualification", jsonArray.getJSONObject(0).getString("qualification"));
                    intent.putExtra("specialist", jsonArray.getJSONObject(0).getString("specialist"));
                    intent.putExtra("hospital", jsonArray.getJSONObject(0).getString("hospital"));
                    intent.putExtra("mobile", jsonArray.getJSONObject(0).getString("mobile"));
                    intent.putExtra("landline", jsonArray.getJSONObject(0).getString("landline"));


                }
                intent.putExtra("name", jsonArray.getJSONObject(0).getString("first_name") +
                        jsonArray.getJSONObject(0).getString("last_name"));

                startActivity(intent);
                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getApplicationContext(), "Enter Valid Credentials.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Enter Valid Credentials.", Toast.LENGTH_SHORT).show();

        }
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

