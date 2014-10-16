package com.example.myapplication.drawyourcity.app;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class Instructions extends ActionBarActivity {


    ImageView hint_oldman;
    ImageView hint_girl;
    ImageView hint_guy;
    ImageView hint_dude;
    ImageView hint_chef;

    Button gotItButton;
    Button showMapButton;
    Button showHintButton;
    Button startScan;

    ImageView harry;

    TextView hintText;
    TextView instructionText;

    //extras from intent
    String siteName;
    String category;
    String address;
    String instructions;
    int counter;
    String engname;

    String order;

    //Strings to be found by DB
    String picturename;
    String hint;
    ParseGeoPoint siteGeoLoc;

    FrameLayout mapFrame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        Bundle extras = getIntent().getExtras();
        siteName = extras.getString("siteName");
        category = extras.getString("category");
        instructions = extras.getString("instructions");
        address = extras.getString("address");
        counter = extras.getInt("counter", 1);
        engname = extras.getString("engname");



        if (counter == 1){
            order = "first";
        }
        else if (counter > 1 && counter < 5){
            order = "next";
        }
        else if (counter == 5){
            order = "last";
        }


        instructionText = (TextView)findViewById(R.id.instructionText);
        String instructionConcatString = "Your " + order + " site is a " + category + " called " + siteName + " ("+ engname +").\n\nThe address where you will find it is " + address + ".\n\n" + instructions ;
        instructionText.setText(instructionConcatString);

        TextView progress = (TextView)findViewById(R.id.progress);
        progress.setText("Step "+counter+" of 5");

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#B7D200"))); //sets the action bar to green
        bar.setDisplayShowTitleEnabled(false);  // required to force redraw, without, gray color
        bar.setDisplayShowTitleEnabled(true);

        getHintFromDB(); //calls method to fetch hint, character picture and geoloc


        hint_oldman = (ImageView)findViewById(R.id.hint_oldman);
        hint_girl = (ImageView)findViewById(R.id.hint_girl);
        hint_guy = (ImageView)findViewById(R.id.hint_guy);
        hint_dude = (ImageView)findViewById(R.id.hint_dude);
        hint_chef = (ImageView)findViewById(R.id.hint_chef);

        gotItButton = (Button)findViewById(R.id.gotItButton);
        startScan = (Button)findViewById(R.id.startScan);

        hintText = (TextView)findViewById(R.id.hintText);

        harry = (ImageView)findViewById(R.id.harry);
        showMapButton = (Button)findViewById(R.id.showMap);
        showHintButton = (Button)findViewById(R.id.hint);

        mapFrame = (FrameLayout)findViewById(R.id.mapFrame);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.instructions, menu);
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


    public void getHintFromDB(){
        //fetches hint, character picture and location of site from DB when called in onCreate

        ParseQuery<ParseObject> query = ParseQuery.getQuery("routes");
        query.whereEqualTo("sitename", siteName);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {

                if (e == null) {
                    for (ParseObject fetchHint : parseObjects) {
                        hint = fetchHint.getString("hint");
                        picturename = fetchHint.getString("picturename");
                        siteGeoLoc = fetchHint.getParseGeoPoint("mapgeolocation");

                        Log.d("hint", hint);

                        //Initialize map when location is found.
                        InitializeMap();

                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

    }

    public void InitializeMap() {

        // Get a handle to the Map Fragment
        GoogleMap map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        //Gets the lat and long values from ParseGeoPoint, so a maps LatLng can be used
        LatLng siteLocation = new LatLng(siteGeoLoc.getLatitude(), siteGeoLoc.getLongitude());


        //Sets the location and zoom level of camera
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(siteLocation, 15));

        //Pinpoints the location with a marker
        map.addMarker(new MarkerOptions()
                .title(siteName)
                .snippet("This is the location of " + siteName)
                .position(siteLocation));
    }

    public void handleInstructionVisibility(int state) {
        //shows or hides instruction data

        if(state == 1){

            instructionText.setVisibility(View.VISIBLE);
            harry.setVisibility(View.VISIBLE);
            showMapButton.setVisibility(View.VISIBLE);
            showHintButton.setVisibility(View.VISIBLE);
            startScan.setVisibility(View.VISIBLE);
        }
        else if (state == 0){

            instructionText.setVisibility(View.GONE);
            harry.setVisibility(View.GONE);
            showMapButton.setVisibility(View.GONE);
            showHintButton.setVisibility(View.GONE);
            startScan.setVisibility(View.GONE);
        }

    }


    public void handleMap(int state){
        //shows or hides mapframe
        if(state == 1){
            mapFrame.setVisibility(View.VISIBLE);
        }
        else if(state == 0){
            mapFrame.setVisibility(View.GONE);
        }
    }

    public void handleHint(int state){
        //shows or hides hint data

        if (state == 1) {
            hintText.setText(hint);
            hintText.setVisibility(View.VISIBLE);

            if (picturename.equals("hint_oldman")) {
                hint_oldman.setVisibility(View.VISIBLE);
            } else if (picturename.equals("hint_girl")) {
                hint_girl.setVisibility(View.VISIBLE);
            } else if (picturename.equals("hint_guy")) {
                hint_guy.setVisibility(View.VISIBLE);
            } else if (picturename.equals("hint_dude")) {
                hint_dude.setVisibility(View.VISIBLE);
            } else if (picturename.equals("hint_chef")) {
                hint_chef.setVisibility(View.VISIBLE);
            }

            gotItButton.setVisibility(View.VISIBLE);
        }


        else if (state == 0){
            hintText.setVisibility(View.GONE);

            if(picturename.equals("hint_oldman")){
                hint_oldman.setVisibility(View.GONE);
            }
            else if(picturename.equals("hint_girl")){
                hint_girl.setVisibility(View.GONE);
            }
            else if(picturename.equals("hint_guy")){
                hint_guy.setVisibility(View.GONE);
            }
            else if(picturename.equals("hint_dude")){
                hint_dude.setVisibility(View.GONE);
            }
            else if(picturename.equals("hint_chef")){
                hint_chef.setVisibility(View.GONE);
            }

            gotItButton.setVisibility(View.GONE);
        }


    }

    public void ShowMap(View view){

        handleMap(1); //shows map
        handleInstructionVisibility(0); //hides instructions
        gotItButton.setVisibility(View.VISIBLE); //shows "ok, got it"-button
    }


    public void getHint(View view) {

        handleInstructionVisibility(0); //will hide the instructions when hint button is clicked
        handleHint(1); //will show hint when hint button is clicked

    }


    public void OkGotIt(View view) {

        handleInstructionVisibility(1); //show instructions
        handleHint(0); //hide hints
        handleMap(0); //hide map

    }



    public void StartScan(View view) {

        Intent intent = new Intent(this, Info.class);
        intent.putExtra("counter", counter);
        intent.putExtra("checksitename", siteName);
        startActivity(intent);

    }

}
