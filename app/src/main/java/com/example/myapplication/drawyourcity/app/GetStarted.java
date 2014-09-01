package com.example.myapplication.drawyourcity.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class GetStarted extends ActionBarActivity implements View.OnClickListener {

    Button shorts;
    Button medium;
    Button longs;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
        shorts = (Button) findViewById(R.id.shorts);
        medium = (Button) findViewById(R.id.medium);
        longs = (Button) findViewById(R.id.longs);

        shorts.setOnClickListener(this);
        medium.setOnClickListener(this);
        longs.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        latitude = extras.getDouble("lat");
        longitude = extras.getDouble("long");

        //Toast, a small pop-up message
        //Toast.makeText(this, "lat: " + latitude + " long: " + longitude, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.get_started, menu);
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

    public void onClick(View v) {

        String levelChoice;
        Intent intent = new Intent(this, ChoosePackage.class);

        switch(v.getId()) {

            case R.id.shorts:
                Toast.makeText(this, "Please note that this is a demo version of Tag and Seek, and that this route is not available for game play.", Toast.LENGTH_LONG).show();
                levelChoice = "short";

                //Add the level choice to the intent which will be sent to ChoosePackage
                intent.putExtra("levelChoice", levelChoice);
            break;
            case R.id.medium:
                //Toast.makeText(this,"Medium was clicked", Toast.LENGTH_LONG).show();
                levelChoice = "medium";
                intent.putExtra("levelChoice", levelChoice);
            break;
            case R.id.longs:
                Toast.makeText(this, "Please note that this is a demo version of Tag and Seek, and that this route is not available for game play.", Toast.LENGTH_LONG).show();
                levelChoice = "long";
                intent.putExtra("levelChoice", levelChoice);
            break;

        }

        intent.putExtra("lat", latitude);
        intent.putExtra("long", longitude);
        startActivity(intent);

    }


}
