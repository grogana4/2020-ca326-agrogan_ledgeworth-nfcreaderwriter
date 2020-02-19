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
import com.example.cardconnect.Contacts;

import java.util.ArrayList;


public class viewContacts extends AppCompatActivity{

    private TextView contactInfo;

    private Button export_to_contacts;
    private Button Edit;
    private Button Delete;
    private  String edit_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
        edit_intent = intent.getStringExtra("MyID");
        if (intent.getStringExtra("MyID") != null) {
            String contact = MainActivity.appDb.getContactData(intent.getStringExtra("MyID"));
            contactInfo.setText(contact);
        }

        //if(getIntent().getExtras()!= null){
        //    Toast.makeText(getApplicationContext(), "nothing in extras", Toast.LENGTH_LONG).show();
        //}

        export_to_contacts.setOnClickListener(view -> exportContact(intent.getStringExtra("MyID")));
        Edit.setOnClickListener(view -> contactEdit());
        Delete.setOnClickListener(view -> deleteContact());

    }


    public void exportContact(String csvId) {
        String Contact = MainActivity.appDb.getExportData(csvId);
        String[] csv = Contact.split("~", -1);

        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.NAME, csv[0]);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, csv[1]);
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, csv[2]);
        intent.putExtra(ContactsContract.Intents.Insert.POSTAL, csv[3]);
        intent.putExtra(ContactsContract.Intents.Insert.COMPANY, csv[4]);
        intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, csv[5]);
        //intent.putExtra(ContactsContract.Intents.Insert.WEBSITE, csv[6]);
        intent.putExtra(ContactsContract.Intents.Insert.NOTES, csv[7]);

        startActivity(intent);
    }

    public void deleteContact() {
        Intent intent = getIntent();
        try {
            MainActivity.appDb.contactDelete(intent.getStringExtra("MyID"));
            String delnum = intent.getStringExtra("delete_number");
            Contacts.list.remove(Integer.parseInt(delnum));
            Contacts.adapter.notifyDataSetChanged();
        }catch(NumberFormatException e){
            MainActivity.appDb.contactDelete(intent.getStringExtra("MyID"));
        }
        Intent Newintent = new Intent(viewContacts.this, Contacts.class);
        onActivityResult(1, RESULT_OK, Newintent);
        finish();
    }

    public void contactEdit() {
        Intent editIntent = new Intent(viewContacts.this, editContact.class);
        editIntent.putExtra("MyID", edit_intent);

        startActivity(editIntent);



    }
}
