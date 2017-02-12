package com.example.arkajitde.virasat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    CoordinatorLayout coordinatorLayout;
    EditText name, email, password, confirm_password, college, phone;
    Button register;
    RadioGroup sex;
   // TextInputLayout NameError,EmailError,PasswordError,ConfPassError,CollegeError,PhoneError;
    String sex_str;
    private View mProgressView;
    private View mRegisterFormView;
    RadioButton radioSexButton;
    ProgressDialog progressDialog;
    //View view;

    public void dismissProgressDialog(){
        if(progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void myErrorHandler(EditText til, String error_mssg){

        til.setError(""+error_mssg);
        til.requestFocus();

    }

    public boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public boolean isPasswordValid(String password) {
        return password.length() > 6;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirm_password = (EditText) findViewById(R.id.confirmpassword);
        phone = (EditText) findViewById(R.id.phone);
        college = (EditText) findViewById(R.id.school);

        /*NameError=(TextInputLayout)findViewById(R.id.NameError);
        EmailError=(TextInputLayout)findViewById(R.id.EmailError);
        PhoneError=(TextInputLayout)findViewById(R.id.PhoneError);
        CollegeError=(TextInputLayout)findViewById(R.id.SchoolError);
        PasswordError=(TextInputLayout)findViewById(R.id.PasswordError);
        ConfPassError=(TextInputLayout)findViewById(R.id.ConfPasswordError);*/

        register = (Button) findViewById(R.id.register);
        sex = (RadioGroup) findViewById(R.id.radiosex);

        if(!Utility.isNetworkAvailable(RegisterActivity.this)) {
            Snackbar.make(coordinatorLayout, "You are OFFLINE", Snackbar.LENGTH_INDEFINITE)
                    .setAction("DISMISS", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(RegisterActivity.this, "Please connect your phone to Internet!", Toast.LENGTH_SHORT).show();
                        }
                    }).show();
        }
           // view=this.getCurrentFocus();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {



                String name_str = name.getText().toString();
                String email_str = email.getText().toString();
                String password_str = password.getText().toString();
                String confirm_password_str = confirm_password.getText().toString();
                String college_str = college.getText().toString();
                String phone_str = phone.getText().toString();


              //  Toast.makeText(RegisterActivity.this,"Fields Received and sex is "+sex_str,Toast.LENGTH_SHORT).show();
                if(name_str.isEmpty()){
                   // dismissProgressDialog();
                    myErrorHandler(name,"This field is required");
                    return;
                }
                if(!isEmailValid(email_str)){
                    //dismissProgressDialog();
                    myErrorHandler(email,"Invalid Email");
                    return;
                }
                if(college_str.isEmpty()){
                    //dismissProgressDialog();
                    myErrorHandler(college,"This field is required");
                    return;
                }
                if(phone_str.isEmpty()){
                    //dismissProgressDialog();
                    myErrorHandler(phone,"This field is required");
                    return;
                }
                int sex_id=sex.getCheckedRadioButtonId();
                switch(sex_id) {
                    case R.id.male:
                        sex_str = "male";
                        break;
                    case R.id.female:
                        sex_str = "female";
                        break;
                }
                if(sex.getCheckedRadioButtonId()==-1){
                    //dismissProgressDialog();
                    Toast.makeText(RegisterActivity.this,"Please specify your gender",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!isPasswordValid(password_str)){
                    //dismissProgressDialog();
                    myErrorHandler(password,"This Password is too short");
                    return;
                }
                if(!password_str.equals(confirm_password_str)){
                    //dismissProgressDialog();
                    myErrorHandler(confirm_password,"Passwords do not match");
                    return;
                }

                progressDialog=new ProgressDialog(RegisterActivity.this);
                progressDialog.setTitle("Registering you up!");
                progressDialog.setMessage("Please wait a moment");
                progressDialog.show();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                          //  String received=jsonResponse.getString("fields");
                          //  Toast.makeText(RegisterActivity.this,"PHP working "+received,Toast.LENGTH_SHORT).show();
                            if (success) {
                                dismissProgressDialog();
                                name.setText(null);email.setText(null);password.setText(null);confirm_password.setText(null);
                                college.setText(null);phone.setText(null);
                                sex.clearCheck();
                                name.requestFocus();
                                Snackbar.make(coordinatorLayout,"Registration Successful",Snackbar.LENGTH_INDEFINITE)
                                        .setAction("CONTINUE", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                RegisterActivity.this.startActivity(intent);
                                                RegisterActivity.this.finish();
                                            }
                                        }).show();
                                //Toast.makeText(RegisterActivity.this,"PHP working",Toast.LENGTH_SHORT).show();



                            } else {
                                dismissProgressDialog();
                                Snackbar.make(coordinatorLayout,"Email already registered",Snackbar.LENGTH_INDEFINITE)
                                        .setAction("LOGIN", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                RegisterActivity.this.startActivity(intent);
                                                RegisterActivity.this.finish();
                                            }
                                        }).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(name_str, phone_str, college_str, password_str, email_str, sex_str, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);

            }
        });
        //mRegisterFormView = findViewById(R.id.register_form);
        //mProgressView = findViewById(R.id.register_progress);
    }


    @Override
    public void onBackPressed(){
        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
        RegisterActivity.this.finish();
    }
}
