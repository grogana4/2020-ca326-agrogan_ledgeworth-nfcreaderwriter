package com.example.cardconnect;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.ContactsContract;

import java.io.File;
import java.io.FileOutputStream;


public class viewContacts extends AppCompatActivity {

    private TextView contactInfo;
    private DatabaseHelper appDb;

    private Button export_to_contacts;
    private Button Edit;
    private Button Delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        appDb = new DatabaseHelper(this);


        initViews();
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
            Toast.makeText(this, "Manual clicked", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void initViews() {

        export_to_contacts = (Button) findViewById(R.id.btn_export_contact);
        Edit = (Button) findViewById(R.id.btn_edit);
        Delete = (Button) findViewById(R.id.btn_delete);
        TextView contactInfo = (TextView) findViewById(R.id.contact_info);
        Intent intent = getIntent();
        //contactInfo.setText(appDb);
        if(intent.getStringExtra("MyID") != null){
            String contact = appDb.getContactData(intent.getStringExtra("MyID"));
            contactInfo.setText(contact);
        }

        //if(getIntent().getExtras()!= null){
        //    Toast.makeText(getApplicationContext(), "nothing in extras", Toast.LENGTH_LONG).show();
        //}

        export_to_contacts.setOnClickListener(view -> exportContact(intent.getStringExtra("MyID")));
        //Edit.setOnClickListener(view -> contactEditFragment());
        // Delete.setOnClickListener(view -> contactDeleteFragment());

    }


    public void exportContact(String csvId){
        String csvContact = appDb.getCsvData(csvId);

        Intent intent = new Intent(Intent.ACTION_INSERT,
                ContactsContract.Contacts.CONTENT_URI);
        startActivity(intent);
    }

}
