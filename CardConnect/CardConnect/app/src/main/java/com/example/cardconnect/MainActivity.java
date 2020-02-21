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
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements Listener{

    public static final String TAG = MainActivity.class.getSimpleName();

    private Button mBtWrite;
    private Button mBtRead;
    private Button Profile;
    private Button Contacts;
    private NfcWriteFragment mNfcWriteFragment;
    private NfcReadFragment mNfcReadFragment;
    public static DatabaseHelper appDb;
    public static String location_address = "";
    private boolean isDialogDisplayed = false;
    private boolean isWrite = false;

    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDb = new DatabaseHelper(this);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        initViews();
        initNFC();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
            Toast.makeText(MainActivity.this, "Manual clicked", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void initViews() {

        mBtWrite = (Button) findViewById(R.id.btn_write);
        mBtRead = (Button) findViewById(R.id.btn_read);
        Profile = (Button) findViewById(R.id.btn_profile);
        Contacts = (Button) findViewById(R.id.btn_contacts);
        mBtWrite.setOnClickListener(view -> showWriteFragment());
        mBtRead.setOnClickListener(view -> showReadFragment());
        Profile.setOnClickListener(view -> showProfileFragment());
        Contacts.setOnClickListener(view -> showContactFragment());
        appDb = new DatabaseHelper(this);
        appDb.insertData("mike", "0831234567", "Hello@iol.ie", "123 jeff lane", "DELL", "Manager", "cardconnect.com", "He a Cool guy", "");
        appDb.insertData("luke", "0831233986", "Hey@iol.ie", "123 jeff lane", "", "", "coolmathgames.com", "support me on fortnite with my creator code /smushsmish", "");
        appDb.insertData("Gabe Null", "0831232106778", "aManHasFallenIntoTheRiver@iol.ie", "Lego City", "", "", "", "Hey!", "");

    }

    private void initNFC(){

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(mNfcAdapter == null){
            return;
        }
        mPendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    private void showWriteFragment() {

        isWrite = true;

        mNfcWriteFragment = (NfcWriteFragment) getFragmentManager().findFragmentByTag(NfcWriteFragment.TAG);

        if (mNfcWriteFragment == null) {

            mNfcWriteFragment = NfcWriteFragment.newInstance();
        }
        mNfcWriteFragment.show(getFragmentManager(),NfcWriteFragment.TAG);

    }

    private void showReadFragment() {

        LocationFinder.LocationResult locationResult = new LocationFinder.LocationResult(){
            @Override
            public void gotLocation(Location location){
                Double lat = location.getLatitude();
                Double lon = location.getLongitude();
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    location_address = address;
                }catch (IOException e){
                }
            }
        };
        LocationFinder myLocation = new LocationFinder();
        myLocation.getLocation(this, locationResult);



        mNfcReadFragment = (NfcReadFragment) getFragmentManager().findFragmentByTag(NfcReadFragment.TAG);

        if (mNfcReadFragment == null) {

            mNfcReadFragment = NfcReadFragment.newInstance();
        }
        mNfcReadFragment.show(getFragmentManager(),NfcReadFragment.TAG);

    }

    private void showProfileFragment() {

        Intent intent = new Intent(this, Profile.class);
        intent.putExtra("MyID", "0");
        startActivity(intent);

    }

    private void showContactFragment() {

        Intent intent = new Intent(this, Contacts.class);
        startActivity(intent);

    }

    @Override
    public void onDialogDisplayed() {

        isDialogDisplayed = true;
    }

    @Override
    public void onDialogDismissed() {

        isDialogDisplayed = false;
        isWrite = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected,tagDetected,ndefDetected};

       // PendingIntent pendingIntent = PendingIntent.getActivity(
       //         this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if(mNfcAdapter!= null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, nfcIntentFilter, null);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mNfcAdapter!= null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        Log.d(TAG, "onNewIntent: "+intent.getAction());

        if(tag != null) {
            Toast.makeText(this, getString(R.string.message_tag_detected), Toast.LENGTH_SHORT).show();
            Ndef ndef = Ndef.get(tag);

            if (isDialogDisplayed) {

                if (isWrite) {
                    String messageToWrite = appDb.getExportData("0");
                    mNfcWriteFragment = (NfcWriteFragment) getFragmentManager().findFragmentByTag(NfcWriteFragment.TAG);
                    mNfcWriteFragment.onNfcDetected(ndef,messageToWrite);

                } else {

                    mNfcReadFragment = (NfcReadFragment)getFragmentManager().findFragmentByTag(NfcReadFragment.TAG);
                    mNfcReadFragment.onNfcDetected(ndef);
                }
            }
        }
    }
}