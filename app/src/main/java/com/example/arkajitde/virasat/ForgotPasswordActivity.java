package com.example.arkajitde.virasat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends AppCompatActivity {

    CoordinatorLayout coordinatorLayoutForgotPass;

    EditText forgotPassEmail;
    Button forgotPasswordSubmit;

    ProgressDialog progressDialog;
    //View view;

    public void myErrorHandler(EditText et,String s){
        et.setError(""+s);
        et.requestFocus();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        forgotPassEmail=(EditText)findViewById(R.id.emailForgotPass);
        forgotPasswordSubmit=(Button)findViewById(R.id.forgotPasswordButton);
        coordinatorLayoutForgotPass=(CoordinatorLayout)findViewById(R.id.coordinatorLayout_forgot_password);

        if(!Utility.isNetworkAvailable(ForgotPasswordActivity.this)) {
            Snackbar.make(coordinatorLayoutForgotPass, "You are OFFLINE", Snackbar.LENGTH_INDEFINITE)
                    .setAction("DISMISS", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(ForgotPasswordActivity.this, "Please connect your phone to Internet!", Toast.LENGTH_SHORT).show();
                        }
                    }).show();
        }

         //view=this.getCurrentFocus();

       forgotPasswordSubmit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {




               String regEmail=forgotPassEmail.getText().toString();
               if(!regEmail.contains("@")){
                   myErrorHandler(forgotPassEmail,"Invalid Email ID");
                   return;
               }
              // Toast.makeText(ForgotPasswordActivity.this,""+regEmail,Toast.LENGTH_LONG).show();
               progressDialog=new ProgressDialog(ForgotPasswordActivity.this);
               progressDialog.setTitle("Checking Email ID");
               progressDialog.setMessage("Please wait a moment");
               progressDialog.show();

               Response.Listener<String> responseListener = new Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {
                       try {
                           JSONObject jsonResponse = new JSONObject(response);
                           String success = jsonResponse.getString("success");

                           if (success.equals("true")) {
                               if(progressDialog.isShowing())
                                   progressDialog.dismiss();
                               Snackbar.make(coordinatorLayoutForgotPass,"Check your Email for the password",Snackbar.LENGTH_INDEFINITE)
                                       .setAction("CHECK",new View.OnClickListener(){
                                           @Override
                                           public void onClick(View v) {

                                               Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://mail.google.com"));
                                               startActivity(browserIntent);


                                           }

                                       })
                                       .show();
                           }

                           else{
                               if(progressDialog.isShowing())
                                   progressDialog.dismiss();

                               Snackbar.make(coordinatorLayoutForgotPass,"Email not registered",Snackbar.LENGTH_INDEFINITE)
                                       .setAction("DISMISS",new View.OnClickListener(){
                                           @Override
                                           public void onClick(View v) {

                                               forgotPassEmail.setText(null);
                                               forgotPassEmail.requestFocus();


                                           }

                                       })
                                       .show();

                           }

                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }
               };

               ForgotPasswordRequest forgotRequest = new ForgotPasswordRequest(regEmail, responseListener);
               RequestQueue queue = Volley.newRequestQueue(ForgotPasswordActivity.this);
               queue.add(forgotRequest);




           }
       });


    }
    public void onBackPressed(){
        startActivity(new Intent(ForgotPasswordActivity.this,LoginActivity.class));
        ForgotPasswordActivity.this.finish();
    }
}
