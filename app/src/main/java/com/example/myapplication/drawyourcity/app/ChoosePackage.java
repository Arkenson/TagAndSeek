package com.example.myapplication.drawyourcity.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.ParseException;
import java.util.List;


public class ChoosePackage extends ActionBarActivity implements View.OnClickListener {

    double latitude;
    double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_package);

        ImageButton icecreamb = (ImageButton)findViewById(R.id.icecream);
        ImageButton ph_med_1 = (ImageButton)findViewById(R.id.ph_med_1);
        ImageButton ph_med_2 = (ImageButton)findViewById(R.id.ph_med_2);
        ImageButton ph_med_3 = (ImageButton)findViewById(R.id.ph_med_3);

        ImageButton puddingb = (ImageButton)findViewById(R.id.pudding);
        ImageButton fishb = (ImageButton)findViewById(R.id.fish);

        //Receives the string passed in the intent from GetStarted
        Bundle extras = getIntent().getExtras();
        String levelChoice = extras.getString("levelChoice");
        latitude = extras.getDouble("lat");
        longitude = extras.getDouble("long");

        //Toast, a small pop-up message
        //Toast.makeText(this, "lat: " + latitude + " long: " + longitude, Toast.LENGTH_SHORT).show();

        if(levelChoice.equals("medium")) {
            icecreamb.setVisibility(View.VISIBLE);
            ph_med_1.setVisibility(View.VISIBLE);
            ph_med_2.setVisibility(View.VISIBLE);
            ph_med_3.setVisibility(View.VISIBLE);
            icecreamb.setOnClickListener(this);

        }
        else if(levelChoice.equals("short")){
            puddingb.setVisibility(View.VISIBLE);
            puddingb.setOnClickListener(this);
        }
        else if(levelChoice.equals("long")){
            fishb.setVisibility(View.VISIBLE);
            fishb.setOnClickListener(this);

        }

        //Toast.makeText(this,"You chose: " + levelChoice, Toast.LENGTH_SHORT).show();

        //Calls method DB with the string levelChoice
       // DB(levelChoice);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.choose_package, menu);
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

    @Override
    public void onClick(View v) {
        String packageChoice;

        switch(v.getId()) {
            case R.id.icecream:
                packageChoice = "Ice cream";
                Toast.makeText(this, "Finding the first site. Hang in there!", Toast.LENGTH_SHORT).show();

                //Calls method DB with the string packageChoice
                DB(packageChoice);
            break;

            case R.id.fish:
                packageChoice = "fish";
                Toast.makeText(this, "Please note that this is a demo version of Tag and Seek, and that this route is not available for game play.", Toast.LENGTH_LONG).show();
                //Calls method DB with the string packageChoice
                //DB(packageChoice);
            break;

            case R.id.pudding:
                packageChoice = "pudding";
                Toast.makeText(this, "Please note that this is a demo version of Tag and Seek, and that this route is not available for game play.", Toast.LENGTH_LONG).show();
                //Calls method DB with the string packageChoice
                //DB(packageChoice);
            break;



        }
    }

    //fetches information from Parse Database with the passed string levelChoice
   public void DB(String packageChoice){



        ParseGeoPoint userLocation = new ParseGeoPoint(latitude,longitude);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("routes");
        query.whereEqualTo("routebelonging", packageChoice);
        query.whereNear("geolocation", userLocation);
        query.setLimit(1);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {

                if (e == null) {
                    String siteName = null;
                    String category = null;
                    String instructions = null;
                    String address = null;
                    String engname = null;

                    for (ParseObject findClosest : parseObjects) {
                        siteName = findClosest.getString("sitename");
                        category = findClosest.getString("category");
                        instructions = findClosest.getString("instructions");
                        address = findClosest.getString("address");
                        engname = findClosest.getString("engsitename");


                        Log.d("sitneame", siteName);
                    }
                    //Log.d("score", "Retrieved " + parseObjects.size() + " scores");

                    sendToLD(siteName, category, instructions, address, engname);

                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });


    }

    public void sendToLD(String siteName, String category, String instructions, String address, String engname) {
        Intent intent = new Intent(this, LoadingScreen.class);
        intent.putExtra("siteName", siteName);
        intent.putExtra("category", category);
        intent.putExtra("instructions", instructions);
        intent.putExtra("address", address);
        intent.putExtra("engname", engname);


        startActivity(intent);

    }


}
