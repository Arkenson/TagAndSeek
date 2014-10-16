package com.example.myapplication.drawyourcity.app;

    import android.app.AlertDialog;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.location.Criteria;
    import android.location.GpsStatus;
    import android.location.LocationListener;
    import android.location.LocationManager;
    import android.location.LocationProvider;
    import android.provider.Settings;
    import android.support.v7.app.ActionBarActivity;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;

    import com.parse.Parse;
    import com.parse.ParseAnalytics;
    import com.parse.ParseObject;

    import android.location.Location;
    import android.widget.Toast;


    public class MainActivity extends ActionBarActivity implements LocationListener {
        private LocationManager locationManager;
        private String provider;
        private Location location;
        private double latitude;
        private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume() {
        super.onResume();


        //Set up critera for location manager
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE); //ACCURACY_FINE
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        //Set up location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this); //GPS_PROVIDER

            provider = locationManager.getBestProvider(criteria, true);
            //AlertDialog alertDialog = new AlertDialog.Builder(this).create();


            //Delay code to prevent crash on resume, since it takes time for the GPS to start working
            /*try {
                Thread.sleep(5500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/


            //fetch location
            location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                //Toast, a small pop-up message
                Toast.makeText(this, "Your location was found. Go go, get started!", Toast.LENGTH_LONG).show();
            }

            //alertDialog.setMessage("lat: " + latitude + " long: " + longitude);
            //alertDialog.show();



        } else {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("No location service found")
                    .setMessage("In order to set up this game we have to use your current location. Please enable your GPS.\n(The GPS can be disabled once you start setting up the game.)")
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .show();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this); //pausing the updates from GPS
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    public void HowTo(View view) {
        Intent intent = new Intent(this, HowTo.class);
        if (latitude != 0 && longitude != 0){
            intent.putExtra("lat", latitude);
            intent.putExtra("long", longitude);
            startActivity(intent);
        }else
        {
            Toast.makeText(this, "Your location was not found", Toast.LENGTH_LONG).show();
        }
    }

    public void GetStarted(View view) {

        Intent intent = new Intent(this, LoadingScreenStart.class);

        if (latitude != 0 && longitude != 0){
        intent.putExtra("lat", latitude);
        intent.putExtra("long", longitude);
        startActivity(intent);
        }else
        {
            Toast.makeText(this, "Your location was not found", Toast.LENGTH_LONG).show();
        }

    }

    //Needed for implements LocationListener
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("LocListn","StatusChanged");

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("LocListn","provDis");

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("LocListn","provEnabl");

    }

    @Override
    public void onLocationChanged(Location l) {
        Log.d("LocListn","onLocChng");
        location = l; //stores the most current location when updated
        latitude = location.getLatitude();
        longitude = location.getLongitude();

    }
}
