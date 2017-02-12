package com.example.arkajitde.virasat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    SharedPreferences prefs;

    String NAME="default";
    TextView signup_main;

    CoordinatorLayout coordinatorLayout;
     EditText email,password; TextView ForgotPassword;
    ProgressDialog progressDialog;

    CheckBox keeplog ;
    //View view;
    public void progressDialogDismiss(){
        if(progressDialog.isShowing())
            progressDialog.dismiss();
    }
   // TextInputLayout emailError,PassError;
    public void myErrorHandler(EditText et, String error_mssg){
       // View focus;
        et.setError(""+error_mssg);
        et.requestFocus();
       // focus=et;
    }

    public boolean isEmailValid(String email) {
        return email.contains("@");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());


        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinatorLayoutlogin);
         email = (EditText) findViewById(R.id.emailLogin);
        password = (EditText) findViewById(R.id.passwordLogin);

        signup_main = (TextView) findViewById(R.id.signup_main);
       // final TextView tvRegisterLink = (TextView) findViewById(R.id.tvRegisterLink);
        final Button bLogin = (Button) findViewById(R.id.email_sign_in_button);

        keeplog = (CheckBox) findViewById(R.id.loggedIn);

      /*  tvRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });*/
        if(!Utility.isNetworkAvailable(LoginActivity.this)) {
            Snackbar.make(coordinatorLayout, "You are OFFLINE", Snackbar.LENGTH_INDEFINITE)
                    .setAction("DISMISS", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(LoginActivity.this, "Please connect your phone to Internet!", Toast.LENGTH_SHORT).show();
                        }
                    }).show();
        }

       // view=this.getCurrentFocus();
        if(prefs.getBoolean("isLoggedIn",false)){

            progressDialog=new ProgressDialog(LoginActivity.this);
            progressDialog.setTitle("Logging In!");
            progressDialog.setMessage("Please wait a moment");
            progressDialog.show();
            String Email,Password;
            if (savedInstanceState == null) {

                Bundle extras = getIntent().getExtras();

                  Email  = extras.getString("email");
                  Password=extras.getString("pass");

            } else {
                Email= (String) savedInstanceState.getSerializable("email");
                Password=(String)savedInstanceState.getSerializable("pass");
            }
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String success = jsonResponse.getString("success");
                        NAME=jsonResponse.getString("name");

                        if (success.equals("true")) {
                            // String name = jsonResponse.getString("name");
                            //int age = jsonResponse.getInt("age");
                            SharedPreferences.Editor editor=prefs.edit();
                            editor.putString("name",NAME);
                            editor.commit();
                            progressDialogDismiss();

                            Intent intent = new Intent(LoginActivity.this, User.class);
                            //  intent.putExtra("name", name);
                            //intent.putExtra("age", age);
                            //intent.putExtra("username", username);
                            LoginActivity.this.startActivity(intent);
                            LoginActivity.this.finish();
                        } else if(success.equals("false")){
                            SharedPreferences.Editor editor=prefs.edit();
                            progressDialogDismiss();
                            editor.clear().commit();
                            Snackbar.make(coordinatorLayout,"Invalid Credentials",Snackbar.LENGTH_INDEFINITE)
                                    .setAction("RETRY",new View.OnClickListener(){
                                        @Override
                                        public void onClick(View v) {

                                            password.setText(null);
                                            password.requestFocus();


                                        }

                                    })
                                    .show();
                        }
                        else{
                            progressDialogDismiss();
                            Snackbar.make(coordinatorLayout,"Email not verified",Snackbar.LENGTH_INDEFINITE)
                                    .setAction("VERIFY",new View.OnClickListener(){
                                        @Override
                                        public void onClick(View v) {

                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://mail.google.com"));
                                            startActivity(browserIntent);


                                        }

                                    })
                                    .show();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            LoginRequest loginRequest = new LoginRequest(Email, Password, responseListener);
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            queue.add(loginRequest);
        }
        else {
            bLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog=new ProgressDialog(LoginActivity.this);
                    progressDialog.setTitle("Logging In!");
                    progressDialog.setMessage("Please wait a moment");
                    progressDialog.show();


                    final String Email = email.getText().toString();
                    final String Password = password.getText().toString();

                    //  emailError=(TextInputLayout)findViewById(R.id.Emailerrorlogin);
                    //  PassError=(TextInputLayout)findViewById(R.id.PassErrorlogin);
                    if (!isEmailValid(Email)) {
                        //progressDialogDismiss();
                        myErrorHandler(email, "Invalid Email");
                        return;
                    }
                    if (Password.isEmpty()) {
                        // progressDialogDismiss();
                        myErrorHandler(password, "Password cannot be empty");
                        return;
                    }


                    // Response received from the server
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                String success = jsonResponse.getString("success");
                                NAME=jsonResponse.getString("name");

                                if (success.equals("true")) {
                                    SharedPreferences.Editor editor=prefs.edit();
                                    // String name = jsonResponse.getString("name");
                                    //int age = jsonResponse.getInt("age");
                                    if (keeplog.isChecked()) {

                                        editor.putString("email", Email);
                                        editor.putString("pass", Password);
                                        editor.putBoolean("isLoggedIn", true);
                                        editor.putString("name",NAME);
                                        editor.commit();
                                    }
                                    editor.putString("email", Email);
                                    editor.putString("name",NAME);
                                    editor.commit();
                                    progressDialogDismiss();
                                    Intent intent = new Intent(LoginActivity.this, User.class);
                                    //  intent.putExtra("name", name);
                                    //intent.putExtra("age", age);
                                    //intent.putExtra("username", username);
                                    LoginActivity.this.startActivity(intent);
                                    LoginActivity.this.finish();
                                } else if (success.equals("false")) {
                                    progressDialogDismiss();
                                    SharedPreferences.Editor editor=prefs.edit();
                                    editor.clear().commit();
                                    Snackbar.make(coordinatorLayout, "Invalid Credentials", Snackbar.LENGTH_INDEFINITE)
                                            .setAction("RETRY", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    password.setText(null);
                                                    password.requestFocus();


                                                }

                                            })
                                            .show();
                                } else {
                                    progressDialogDismiss();
                                    Snackbar.make(coordinatorLayout, "Email not verified", Snackbar.LENGTH_INDEFINITE)
                                            .setAction("VERIFY", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://mail.google.com"));
                                                    startActivity(browserIntent);


                                                }

                                            })
                                            .show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    LoginRequest loginRequest = new LoginRequest(Email, Password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    queue.add(loginRequest);
                }
            });
        }

        ForgotPassword=(TextView)findViewById(R.id.ForgotPassword);
        ForgotPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
            }
        });

        signup_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
        LoginActivity.this.finish();
    }





}