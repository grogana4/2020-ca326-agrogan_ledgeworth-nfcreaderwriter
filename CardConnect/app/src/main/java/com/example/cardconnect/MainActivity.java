//this code was primarily worked on through Pair Programming.

package com.example.cardconnect;


import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements Listener{ //MainActivity starts up

    public static final String TAG = MainActivity.class.getSimpleName(); //tag is established for the dialog fragment

    private Button mBtWrite; //buttons variables made
    private Button mBtRead;
    private Button Profile;
    private Button Contacts;
    private NfcWriteFragment mNfcWriteFragment; //nfc variables made
    private NfcReadFragment mNfcReadFragment;
    public static DatabaseHelper appDb; //database variable made
    public static String location_address = ""; //if location services isn't on or we don't have permission to use it, the location field is empty
    private boolean isDialogDisplayed = false; //when the app is on and in the menu, a dialog box isn't shown
    private boolean isWrite = false; //when the app is on and in the menu, we are not writing to nfc yet

    private NfcAdapter mNfcAdapter; ////nfc adapter and related intent made
    private PendingIntent mPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //when the Activity starts
        super.onCreate(savedInstanceState);
        appDb = new DatabaseHelper(this); //start the database to be used for the app
        setContentView(R.layout.activity_main); //set the layout
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar); //create and set the toolbar with an options menu
        setSupportActionBar(myToolbar);


        initViews(); //start the the other app functions
        initNFC(); //start the nfc detection
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {// Handle action bar item clicks here. this is mainly fo the options menu
        int id = item.getItemId();

        if (id == R.id.action_favorite) { //if manual is clicked, the manual page will appear
            Toast.makeText(MainActivity.this, "Manual clicked", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void initViews() {

        mBtWrite = (Button) findViewById(R.id.btn_write); //assign buttons if for their xml objects
        mBtRead = (Button) findViewById(R.id.btn_read);
        Profile = (Button) findViewById(R.id.btn_profile);
        Contacts = (Button) findViewById(R.id.btn_contacts);
        mBtWrite.setOnClickListener(view -> showWriteFragment()); //assign buttons to functions
        mBtRead.setOnClickListener(view -> showReadFragment());
        Profile.setOnClickListener(view -> showProfileFragment());
        Contacts.setOnClickListener(view -> showContactFragment());

        //test database entries
        //appDb.insertData("mike", "0831234567", "Hello@iol.ie", "123 jeff lane", "DELL", "Manager", "cardconnect.com", "He a Cool guy", "");
        //appDb.insertData("luke", "0831233986", "Hey@iol.ie", "123 jeff lane", "", "", "coolmathgames.com", "support me on fortnite with my creator code /smushsmish", "");
        //appDb.insertData("Gabe Null", "0831232106778", "aManHasFallenIntoTheRiver@iol.ie", "Lego City", "", "", "", "Hey!", "");

    }

    private void initNFC(){

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this); // initialises the nfc adapter
        if(mNfcAdapter == null){ //if nfc is not present(off or not on the device
            //Toast.makeText(MainActivity.this, "Please enable NFC on your device.", Toast.LENGTH_LONG).show();
            return;
        }
        mPendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    private void showWriteFragment() { //if the "Write to NFC tag" button is pressed

        isWrite = true; //isWrite is set to true

        mNfcWriteFragment = (NfcWriteFragment) getFragmentManager().findFragmentByTag(NfcWriteFragment.TAG); //bring up the dialog box using our established fragment

        if (mNfcWriteFragment == null) { //if a NullPointerException occurs with the tag

            mNfcWriteFragment = NfcWriteFragment.newInstance(); // create a new instance
        }
        mNfcWriteFragment.show(getFragmentManager(),NfcWriteFragment.TAG); // show the dialog box

    }

    private void showReadFragment() { //if the "Read to NFC tag" button is pressed

        LocationFinder.LocationResult locationResult = new LocationFinder.LocationResult(){ //generate location
            @Override
            public void gotLocation(Location location){ //if we get our location from LocationFinder
                Double lat = location.getLatitude(); //get latitude
                Double lon = location.getLongitude(); //get longitude
                Geocoder geocoder; //establish geocoder  and the address list
                List<Address> addresses;
                geocoder = new Geocoder(MainActivity.this, Locale.getDefault()); //create a new instance of geocoder

                try {
                    addresses = geocoder.getFromLocation(lat, lon, 1); //do a reverse geocoder search to get the address from the co-ordinates

                    String address = addresses.get(0).getAddressLine(0); // get the first address line from the address list
                    location_address = address; //location_address now has an address instead of an empty string
                }catch (IOException e){ // if we don't get a location return nothing
                }
            }
        };
        LocationFinder myLocation = new LocationFinder(); //initialise LocationFinder
        myLocation.getLocation(this, locationResult); //get location



        mNfcReadFragment = (NfcReadFragment) getFragmentManager().findFragmentByTag(NfcReadFragment.TAG); //bring up the dialog box using our established fragment

        if (mNfcReadFragment == null) { //if a NullPointerException occurs with the tag

            mNfcReadFragment = NfcReadFragment.newInstance(); // create a new instance
        }
        mNfcReadFragment.show(getFragmentManager(),NfcReadFragment.TAG); // show the dialog box

    }

    private void showProfileFragment() { //if the "Edit Your Profile" button is pressed

        Intent intent = new Intent(this, Profile.class); //create a new intent to th Profile activity
        intent.putExtra("MyID", "0"); // store 0 in intents to bring up user profile
        startActivity(intent); //go to Profile page

    }

    private void showContactFragment() { //if the "Contacts" button is pressed

        Intent intent = new Intent(this, Contacts.class); //create a new intent to the Contacts activity
        startActivity(intent); //go to Contacts page

    }

    @Override
    public void onDialogDisplayed() { //if the dialog box is displayed, isDialogDisplayed is true

        isDialogDisplayed = true;
    }

    @Override
    public void onDialogDismissed() { //if the dialog box is gone, isDialogDisplayed and isWrite is false

        isDialogDisplayed = false;
        isWrite = false;
    }

    @Override
    protected void onResume() { //when we go back to MainActivity
        super.onResume();
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED); //create a series of intent filters for discovering nfc tags
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected,tagDetected,ndefDetected};

        if(mNfcAdapter!= null)  //if a NullPointerException occurs with the adapter
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, nfcIntentFilter, null); //allow other activities to handle this intent

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mNfcAdapter!= null) //if a NullPointerException occurs with the adapter
            mNfcAdapter.disableForegroundDispatch(this); //don't allow other activities to handle this intent
    }

    @Override
    protected void onNewIntent(Intent intent) { //when we come back to this activity through a new intent
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG); //retrieve the extended data from the intent

        Log.d(TAG, "onNewIntent: "+intent.getAction()); //save the intent message to logcat for debugging

        if(tag != null) { //if the tag is detected by the nfc sensor
            Toast.makeText(this, getString(R.string.message_tag_detected), Toast.LENGTH_SHORT).show(); //tell the user the tag is detected
            Ndef ndef = Ndef.get(tag); //start exchanging information with the tag

            if (isDialogDisplayed) { //if the dialog box is present

                if (isWrite) { //if we are writing
                    String messageToWrite = appDb.getExportData("0"); //export the profile data
                    mNfcWriteFragment = (NfcWriteFragment) getFragmentManager().findFragmentByTag(NfcWriteFragment.TAG); // get the nfc write fragment
                    mNfcWriteFragment.onNfcDetected(ndef,messageToWrite); //write the message to the card

                } else {

                    mNfcReadFragment = (NfcReadFragment)getFragmentManager().findFragmentByTag(NfcReadFragment.TAG); //get the nfc read fragment
                    mNfcReadFragment.onNfcDetected(ndef); //read the message on the card
                }
            }
        }
    }
}