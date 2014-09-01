package com.example.myapplication.drawyourcity.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Caroline Arkenson on 2014-06-02.
 */
public class LoadingScreen extends Activity {
    String siteName;
    String category;
    String instructions;
    String address;
    String engname;


    //Splash screen timer
    private static int SPLASH_TIME_OUT = 5000;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadingscreen);

        Bundle extras = getIntent().getExtras();
        siteName = extras.getString("siteName");
        category = extras.getString("category");
        instructions = extras.getString("instructions");
        address = extras.getString("address");
        engname = extras.getString("engname");


        new Handler().postDelayed(new Runnable(){
            /*Showing splash screen with a timer */

            @Override
            public void run(){
                Intent intent = new Intent(LoadingScreen.this, Instructions.class);
                intent.putExtra("siteName", siteName);
                intent.putExtra("category", category);
                intent.putExtra("instructions", instructions);
                intent.putExtra("address", address);
                intent.putExtra("counter",1);
                intent.putExtra("engname", engname);

               // intent.putExtra("picturename", picturename);
               // intent.putExtra("hint", hint);
                startActivity(intent);

                //Close this activity
                finish();
            }
        } , SPLASH_TIME_OUT);
    }
}

