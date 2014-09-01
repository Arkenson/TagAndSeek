package com.example.myapplication.drawyourcity.app;

        import android.app.ActionBar;
        import android.app.Activity;
        import android.app.PendingIntent;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.content.IntentFilter.MalformedMimeTypeException;
        import android.graphics.Color;
        import android.graphics.drawable.ColorDrawable;
        import android.nfc.NdefMessage;
        import android.nfc.NdefRecord;
        import android.nfc.NfcAdapter;
        import android.nfc.Tag;
        import android.nfc.tech.Ndef;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.ScrollView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.parse.FindCallback;
        import com.parse.ParseObject;
        import com.parse.ParseQuery;

        import java.io.UnsupportedEncodingException;
        import java.util.Arrays;
        import java.util.List;

public class Info extends Activity {

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";

    private TextView mTextView;
    private NfcAdapter mNfcAdapter;

    //Buttons in arrived view
    Button showNext;
    Button seeInfo;

    //Siteinfo
    String siteinfo;

    //Needed for handleArrivedAt
    String picturename;
    String arrived_icon;
    String arrived_text;
    String engname;
    String toshowsitename;

    ImageView arrive_girl;
    ImageView arrive_oldman;
    ImageView arrive_guy;
    ImageView arrive_dude;
    ImageView arrive_chef;

    TextView bubble;
    ImageView lin_pic;
    ImageView liao_pic;
    ImageView wu_pic;
    ImageView confucius_pic;
    ImageView beef_pic;

    TextView arrivedText;

    Button showNext_old;

    ScrollView SV;

    //passing strings
    String category;
    String siteName;
    String address;
    String instructions;

    //Intent extras variables
    int counter;
    String checksitename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Bundle extras = getIntent().getExtras();
        counter = extras.getInt("counter", 2);
        checksitename = extras.getString("checksitename");

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F4B907"))); //sets the action bar to green
        bar.setDisplayShowTitleEnabled(false);  // required to force redraw, without, gray color
        bar.setDisplayShowTitleEnabled(true);

        mTextView = (TextView) findViewById(R.id.textView_explanation);
        showNext =(Button)findViewById(R.id.showNext);
        seeInfo =(Button)findViewById(R.id.seeInfo);
        showNext_old = (Button)findViewById(R.id.showNext_old);

        arrive_girl = (ImageView)findViewById(R.id.arrive_girl);
        arrive_guy = (ImageView)findViewById(R.id.arrive_guy);
        arrive_dude = (ImageView)findViewById(R.id.arrive_dude);
        arrive_oldman = (ImageView)findViewById(R.id.arrive_oldman);
        arrive_chef = (ImageView)findViewById(R.id.arrive_chef);

        beef_pic = (ImageView)findViewById(R.id.beef_pic);
        liao_pic = (ImageView)findViewById(R.id.liao_pic);
        lin_pic = (ImageView)findViewById(R.id.lin_pic);
        wu_pic = (ImageView)findViewById(R.id.wu_pic);
        confucius_pic = (ImageView)findViewById(R.id.confucius_pic);

        bubble = (TextView)findViewById(R.id.bubble);

        arrivedText = (TextView)findViewById(R.id.arrivedText);
        SV = (ScrollView)findViewById(R.id.SV);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;

        }

        if (!mNfcAdapter.isEnabled()) {
            mTextView.setText("NFC is disabled. \n\nIn order to continue, you must enable NFC in the connection and sharing settings.");
        } else {
            mTextView.setText("Use your phone to tap the NFC tag placed on the board.");
        }


        handleIntent(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();

        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
        setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        stopForegroundDispatch(this, mNfcAdapter);

        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        Toast.makeText(this, "Tag was successfully scanned!", Toast.LENGTH_LONG).show();
        handleIntent(intent);

    }

    private void handleIntent(Intent intent) {
            String action = intent.getAction();
            if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

                String type = intent.getType();
                if (MIME_TEXT_PLAIN.equals(type)) {

                    Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                    new NdefReaderTask().execute(tag);

                } else {
                    Log.d(TAG, "Wrong mime type: " + type);
                }
            } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

                // In case we would still use the Tech Discovered Intent
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                String[] techList = tag.getTechList();
                String searchedTech = Ndef.class.getName();

                for (String tech : techList) {
                    if (searchedTech.equals(tech)) {
                        new NdefReaderTask().execute(tag);
                        break;
                    }
                }
            }
        }
    /**
     * @param activity The corresponding {@link Activity} requesting the foreground dispatch.
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    /**
     * @param activity The corresponding {@li nk BaseActivity} requesting to stop the foreground dispatch.
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }


    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {



        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e("tag", "Unsupported Encoding", e);
                    }
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {

                if (result.equals(checksitename)) {
                    toshowsitename = result;
                    getInfo(result);
                }
                else {
                    mTextView.setText("Uh oh! Seems like you are at the wrong site!\n\nYou are currently at " +result+ " but should search for " + checksitename + ". \n Go go, find " + checksitename +"!");
                }
            }
        }
    }

    public void getInfo(final String result){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("routes");
        query.whereEqualTo("sitename", result);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {

                int routeorder;
                if (e == null) {
                    for (ParseObject fetchArrived : parseObjects) {
                        siteinfo = fetchArrived.getString("siteinfo");
                        arrived_icon = fetchArrived.getString("arrivepic");
                        arrived_text = fetchArrived.getString("arrived");
                        routeorder = fetchArrived.getInt("routeorder");
                        engname = fetchArrived.getString("engsitename");
                        picturename = fetchArrived.getString("picturename");

                        //mTextView.setText(siteName + "\n" + siteinfo);
                        //showNext.setVisibility(View.VISIBLE);

                       handleArrivedAt(1);
                       getNextInfo(routeorder);

                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void getNextInfo(int routeorder) {

        if (counter < 5) {
            if (routeorder < 5) {
                routeorder += 1;
                counter += 1;
            } else if (routeorder == 5) {
                routeorder = 1;
                counter += 1;
            }
        }

        else if (counter == 5) {
            showNext.setText("Finish route");
            showNext_old.setText("Ok, I am done!");
            counter = 6;
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("routes");
        query.whereEqualTo("routeorder", routeorder);
        query.whereEqualTo("level", "medium");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {


                if (e == null) for (ParseObject fetchNext : parseObjects) {
                    category = fetchNext.getString("category");
                    siteName = fetchNext.getString("sitename");
                    address = fetchNext.getString("address");
                    instructions = fetchNext.getString("instructions");
                    engname = fetchNext.getString("engsitename");

                }
                else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

    }

    public void handleArrivedAt(int state) {

        handleSeeInfo(0);

        if (state == 1) {
            //show pictures, bubble, you successfully arrived at plus buttons

            arrivedText.setText("You successfully arrived at " + engname);
            arrivedText.setVisibility(View.VISIBLE);
            bubble.setVisibility(View.VISIBLE);

            if (picturename.equals("hint_oldman")) {
                arrive_oldman.setVisibility(View.VISIBLE);
                bubble.setText(arrived_text);

            } else if (picturename.equals("hint_girl")) {
                arrive_girl.setVisibility(View.VISIBLE);
                bubble.setText(arrived_text);


            } else if (picturename.equals("hint_guy")) {
                arrive_guy.setVisibility(View.VISIBLE);
                bubble.setText(arrived_text);


            } else if (picturename.equals("hint_dude")) {
                arrive_dude.setVisibility(View.VISIBLE);
                bubble.setText(arrived_text);

            } else if (picturename.equals("hint_chef")) {
                arrive_chef.setVisibility(View.VISIBLE);
                bubble.setText(arrived_text);
            }

            showNext.setVisibility(View.VISIBLE);
            seeInfo.setVisibility(View.VISIBLE);

        } else if (state == 0) {
            //hide pictures, bubble, you successfully arrived at plus buttons

            arrivedText.setVisibility(View.GONE);
            bubble.setVisibility(View.GONE);

            if (picturename.equals("hint_oldman")) {
                arrive_oldman.setVisibility(View.GONE);

            } else if (picturename.equals("hint_girl")) {
                arrive_girl.setVisibility(View.GONE);

            } else if (picturename.equals("hint_guy")) {
                arrive_guy.setVisibility(View.GONE);

            } else if (picturename.equals("hint_dude")) {
                arrive_dude.setVisibility(View.GONE);

            } else if (picturename.equals("hint_chef")) {
                arrive_chef.setVisibility(View.GONE);
            }

            showNext.setVisibility(View.GONE);
            seeInfo.setVisibility(View.GONE);
        }
    }

    public void handleSeeInfo(int state){

        if (state == 1){
            //show sitename, siteinfo, sitepic
            mTextView.setText(toshowsitename + "\n\n" + siteinfo);
            SV.setVisibility(View.VISIBLE);
            showNext_old.setVisibility(View.VISIBLE);

            if (picturename.equals("hint_oldman")) {
                confucius_pic.setVisibility(View.VISIBLE);

            } else if (picturename.equals("hint_girl")) {
                liao_pic.setVisibility(View.VISIBLE);

            } else if (picturename.equals("hint_guy")) {
                lin_pic.setVisibility(View.VISIBLE);

            } else if (picturename.equals("hint_dude")) {
                wu_pic.setVisibility(View.VISIBLE);

            } else if (picturename.equals("hint_chef")) {
                beef_pic.setVisibility(View.VISIBLE);
            }

        }
        else if(state == 0){
            //hide sitename, siteinfo, sitepic
            SV.setVisibility(View.GONE);
            showNext_old.setVisibility(View.GONE);


        }
    }



    public void seeInfo(View view){

        handleArrivedAt(0);
        handleSeeInfo(1);
    }


    public void showNext(View view){

        if (counter <= 5) {

            Intent intent = new Intent(this, Instructions.class);
            intent.putExtra("siteName", siteName);
            intent.putExtra("category", category);
            intent.putExtra("instructions", instructions);
            intent.putExtra("address", address);
            intent.putExtra("counter", counter);
            intent.putExtra("engname", engname);

            startActivity(intent);
        }

        else if (counter == 6){
            Intent intent = new Intent(this, LoadingScreenEnd.class);
            intent.putExtra("siteName", siteName);

            startActivity(intent);
        }


    }
}