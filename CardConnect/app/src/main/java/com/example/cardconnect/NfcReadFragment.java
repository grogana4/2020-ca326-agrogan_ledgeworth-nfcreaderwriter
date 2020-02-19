package com.example.cardconnect;

import android.app.DialogFragment;
import android.arch.persistence.room.Database;
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
import java.lang.reflect.Array;

public class NfcReadFragment extends DialogFragment {

    public static final String TAG = NfcReadFragment.class.getSimpleName();

    public static NfcReadFragment newInstance() {

        return new NfcReadFragment();
    }

    private TextView mTvMessage;
    private Listener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_read,container,false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {

        mTvMessage = (TextView) view.findViewById(R.id.tv_message);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (MainActivity)context;
        mListener.onDialogDisplayed();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.onDialogDismissed();
    }

    public void onNfcDetected(Ndef ndef){

        readFromNFC(ndef);
    }

    private void readFromNFC(Ndef ndef) {

        try {
            ndef.connect();
            NdefMessage ndefMessage = ndef.getNdefMessage();
            String message = new String(ndefMessage.getRecords()[0].getPayload());
            Log.d(TAG, "readFromNFC: "+message);
            String[] split_message = message.split("~",-1);
            MainActivity.appDb.insertData(split_message[0], split_message[1], split_message[2], split_message[3], split_message[4], split_message[5], split_message[6], split_message[7]);
            Intent intent = new Intent(getActivity(), viewContacts.class);
            String id = MainActivity.appDb.findLastId();
            intent.putExtra("MyID",id);
            startActivity(intent);

            ndef.close();


        } catch (IOException | FormatException | NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "null exception", Toast.LENGTH_LONG);

        }
    }
}
