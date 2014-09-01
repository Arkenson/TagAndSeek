package com.example.myapplication.drawyourcity.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class HowTo extends ActionBarActivity {
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to);

        Bundle extras = getIntent().getExtras();
        latitude = extras.getDouble("lat");
        longitude = extras.getDouble("long");

        //Toast, a small pop-up message
        //Toast.makeText(this, "lat: " + latitude + " long: " + longitude, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.how_to, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void GetStarted(View view) {
        Intent intent = new Intent(this, LoadingScreenStart.class);
        intent.putExtra("lat", latitude);
        intent.putExtra("long", longitude);
        startActivity(intent);
    }

}
