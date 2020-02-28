//this code was primarily worked on by Abe Grogan.
package com.example.cardconnect;

import android.app.DialogFragment;
import android.content.Context;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

public class NfcWriteFragment extends DialogFragment { //Write Fragment starts up if Write to NFC button is pressed

    public static final String TAG = NfcWriteFragment.class.getSimpleName(); //tag is created

    public static NfcWriteFragment newInstance() { //creates a new instance of Write fragment

        return new NfcWriteFragment();
    }

    public static TextView mTvMessage; //textview for displaying messages in the box
    private Listener mListener; // generates Listener for nfc

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { //on creation, generate view box
        View view = inflater.inflate(R.layout.fragment_write,container,false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {

        mTvMessage = (TextView) view.findViewById(R.id.tv_message); // bring textView to the container

    }

    @Override
    public void onAttach(Context context) { // when Write to nfc is pressed
        super.onAttach(context);
        mListener = (MainActivity)context; //bring context to listener
        mListener.onDialogDisplayed(); //display dialog box
    }

    @Override
    public void onDetach() { //when finished writing
        super.onDetach();
        mListener.onDialogDismissed(); //remove dialog box
    }

    public void onNfcDetected(Ndef ndef, String messageToWrite){ //if nfc card is detected
        writeToNfc(ndef,messageToWrite); //Write to nfc card
    }

    private void writeToNfc(Ndef ndef, String message){

        mTvMessage.setText(getString(R.string.message_write_progress)); //set text to let user know we are writing
        if (ndef != null) { // if the connection isn't null or not detected

            try {
                ndef.connect(); //try to connect to card
                NdefRecord mimeRecord = NdefRecord.createMime("text/plain", message.getBytes(Charset.forName("US-ASCII"))); //create record to store on card
                ndef.writeNdefMessage(new NdefMessage(mimeRecord)); //store the record of the message on the card
                ndef.close(); //close connection
                dismiss();
                Toast.makeText(getActivity(), getString(R.string.message_write_success) , Toast.LENGTH_LONG).show();

            } catch (IOException | FormatException e) { // if the connection doesn't succeed
                e.printStackTrace(); //print stack trace
                mTvMessage.setText(getString(R.string.message_write_error)); //let user know an error has occurred

            }


        }
    }
}