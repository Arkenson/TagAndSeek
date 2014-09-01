package com.example.myapplication.drawyourcity.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;



public class CompletedRoute extends ActionBarActivity {

   /* TextView congratulations;
    ImageView icecream_map;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_route);

                Bundle extras = getIntent().getExtras();
                String sitename = extras.getString("siteName");
                getRouteBelonging(sitename); //to find map and coupon



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.completed_route, menu);
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

    public void getRouteBelonging(String sitename){


        ParseQuery<ParseObject> query = ParseQuery.getQuery("routes");
        query.whereEqualTo("sitename", sitename);
        query.whereEqualTo("level", "medium");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {

                String routebelonging = null;

                if (e == null) for (ParseObject fetchNext : parseObjects) {
                    routebelonging = fetchNext.getString("routebelonging");
                    showCoupon(routebelonging);
                }
                else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });


    }

    public void showCoupon(String routebelonging){

        TextView congratulations = (TextView)findViewById(R.id.congratulations);

        String congratsString = "Congratulations! \nYou completed the "+ routebelonging + " route!";
        congratulations.setText(congratsString);

        if (routebelonging.equals("Ice cream")){
        ImageView icecream_map = (ImageView)findViewById(R.id.icecream_map);
        icecream_map.setVisibility(View.VISIBLE);
        }

    }

    public void getCoupon(View view){

        TextView congratulations = (TextView)findViewById(R.id.congratulations);
        congratulations.setText("");

        ImageView icecream_map = (ImageView)findViewById(R.id.icecream_map);
        icecream_map.setVisibility(View.GONE);

        ImageView icecream_coupon = (ImageView)findViewById(R.id.icecream_coupon);
        icecream_coupon.setVisibility(View.VISIBLE);

        Button getcoupon = (Button)findViewById(R.id.getcoupon);
        getcoupon.setVisibility(View.GONE);

        TextView demotext = (TextView)findViewById(R.id.demotext);
        demotext.setVisibility(View.VISIBLE);


    }

}
