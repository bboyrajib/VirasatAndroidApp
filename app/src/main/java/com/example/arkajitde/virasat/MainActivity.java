package com.example.arkajitde.virasat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity {

    CoordinatorLayout coordinatorLayoutMain;

    Button login_main;


    SharedPreferences prefs;


    //SharedPreferences prefs=getApplicationContext().getSharedPreferences("User Prefs",MODE_PRIVATE);
 //   SharedPreferences.Editor editor=prefs.edit();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login_main = (Button) findViewById(R.id.login_main);

        coordinatorLayoutMain=(CoordinatorLayout)findViewById(R.id.activity_main);

         prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        if(!Utility.isNetworkAvailable(MainActivity.this)) {
            Snackbar.make(coordinatorLayoutMain, "You are OFFLINE", Snackbar.LENGTH_INDEFINITE)
                    .setAction("DISMISS", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(MainActivity.this, "Please connect your phone to Internet!", Toast.LENGTH_SHORT).show();
                        }
                    }).show();
        }

        login_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prefs.getBoolean("isLoggedIn",false)){
                   Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                    intent.putExtra("email", prefs.getString("email",null));
                    intent.putExtra("pass",prefs.getString("pass",null));
                    startActivity(intent);
                }
                else
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


}
