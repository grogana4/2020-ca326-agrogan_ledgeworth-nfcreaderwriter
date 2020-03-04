package com.example.cardconnect;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.webkit.WebView;



public class Manual extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) { //when the Activity starts
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual); //set the layout
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar); //create and set the toolbar with an options menu


        WebView manualView = new WebView(this);
        manualView.getSettings().setJavaScriptEnabled(true);
        manualView.getSettings().setBuiltInZoomControls(true);
        manualView.loadUrl("file:///android_asset/manual.html");
        setContentView(manualView);


        //final String mimeType = "text/html";
        //final String encoding = "UTF-8";
        //final String html = "";
        //manualView.loadDataWithBaseURL("", html, mimeType, encoding, "");
    }





}
