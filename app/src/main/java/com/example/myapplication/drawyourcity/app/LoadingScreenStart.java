package com.example.myapplication.drawyourcity.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Caroline Arkenson on 2014-06-11.
 */
public class LoadingScreenStart extends Activity {
    double latitude;
    double longitude;



    //Splash screen timer
    private static int SPLASH_TIME_OUT = 5000;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen_start);


        Bundle extras = getIntent().getExtras();
        latitude = extras.getDouble("lat");
        longitude = extras.getDouble("long");



        new Handler().postDelayed(new Runnable(){
            /*Showing splash screen with a timer */

            @Override
            public void run(){
                Intent intent = new Intent(LoadingScreenStart.this, GetStarted.class);
                intent.putExtra("lat", latitude);
                intent.putExtra("long", longitude);

                // intent.putExtra("picturename", picturename);
                // intent.putExtra("hint", hint);
                startActivity(intent);

                //Close this activity
                finish();
            }
        } , SPLASH_TIME_OUT);
    }
}

