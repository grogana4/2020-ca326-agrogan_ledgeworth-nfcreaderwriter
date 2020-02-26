//this code was primarily worked on by Abe Grogan.
package com.example.cardconnect;



import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;



public class NfcReadFragment extends DialogFragment { //Read Fragment starts up when Read to NFC is pressed

    public static final String TAG = NfcReadFragment.class.getSimpleName(); //tag is created

    public static NfcReadFragment newInstance() { //creates a new instance of read fragment

        return new NfcReadFragment();
    }

    private TextView mTvMessage; //textview for displaying messages in the box
    private Listener mListener; // generates Listener for nfc




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_read,container,false); //on creation, generate view box
        initViews(view);
        return view;
    }

    private void initViews(View view) {

        mTvMessage = (TextView) view.findViewById(R.id.tv_message); // bring textView to the container
    }

    @Override
    public void onAttach(Context context) { // when read to nfc is pressed
        super.onAttach(context);
        mListener = (MainActivity)context; //bring context to listener
        mListener.onDialogDisplayed(); //display dialog box
    }

    @Override
    public void onDetach() { //when finished reading
        super.onDetach();
        mListener.onDialogDismissed(); //remove dialog box
    }

    public void onNfcDetected(Ndef ndef){ //if nfc card is detected

        readFromNFC(ndef); //read nfc card
    }



    private void readFromNFC(Ndef ndef) {

        try {
            ndef.connect(); //try to connect to card
            NdefMessage ndefMessage = ndef.getNdefMessage(); //get the data payload from the card
            String message = new String(ndefMessage.getRecords()[0].getPayload());
            Log.d(TAG, "readFromNFC: "+message); //save the nfc action to logcat for debugging

            String[] split_message = message.split("~",-1); //split the message to an array
            MainActivity.appDb.insertData(split_message[0], split_message[1], split_message[2], split_message[3], split_message[4], split_message[5], split_message[6], split_message[7], MainActivity.location_address); //insert the data into the database
            Intent intent = new Intent(getActivity(), viewContacts.class); // bring us to View contact using an intent
            String id = MainActivity.appDb.findLastId(); //find the last id of the list
            intent.putExtra("MyID",id); //place id in the extras of the intent
            startActivity(intent); //start activity

            ndef.close(); // close connection


        } catch (IOException | FormatException | NullPointerException e) { //if a null exception occurred, like nfc not being picked up
            e.printStackTrace(); //print stack trace
            Toast.makeText(getActivity(), "exception occurred", Toast.LENGTH_LONG); //let user know an error has occurred

        }
    }


}
