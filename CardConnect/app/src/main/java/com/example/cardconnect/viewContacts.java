//this code was primarily worked on by luke Edgeworth.
package com.example.cardconnect;


import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.*;

import java.util.ArrayList;


public class viewContacts extends AppCompatActivity{ //viewContacts starts up if an entry in Contacts is pressed

    private TextView contactInfo; //variables initiated

    private Button export_to_contacts;
    private Button Edit;
    private Button Delete;
    private  String edit_intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) { //when the Activity starts
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact); //set the layout
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar); //create and set the toolbar with an options menu
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //create and set the toolbar with an options menu


        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // Handle action bar item clicks here. this is mainly fo the options menu
        int id = item.getItemId();

        if (id == R.id.action_favorite) { //if manual is clicked, the manual page will appear
            Intent intent = new Intent(this, Manual.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void initViews() {

        export_to_contacts = (Button) findViewById(R.id.btn_export_contact); //buttons and text view  set
        Edit = (Button) findViewById(R.id.btn_edit);
        Delete = (Button) findViewById(R.id.btn_delete);
        TextView contactInfo = (TextView) findViewById(R.id.contact_info);
        Intent intent = getIntent();
        //contactInfo.setText(appDb);
        edit_intent = intent.getStringExtra("MyID"); //get the id from the intent
        if (intent.getStringExtra("MyID") != null) { //if it exists, get the data
            String contact = MainActivity.appDb.getContactData(intent.getStringExtra("MyID"));
            contactInfo.setText(contact);
        }

        //if(getIntent().getExtras()!= null){
        //    Toast.makeText(getApplicationContext(), "nothing in extras", Toast.LENGTH_LONG).show();
        //}

        export_to_contacts.setOnClickListener(view -> exportContact(intent.getStringExtra("MyID"))); //set buttons to functions
        Edit.setOnClickListener(view -> contactEdit());
        Delete.setOnClickListener(view -> deleteContact());

    }


    public void exportContact(String csvId) { //if the export button is pressed
        String[] csv= MainActivity.appDb.getEditData(csvId); //get the data ready to export
        //String[] csv = Contact.split("~", -1);

        Intent intent = new Intent(Intent.ACTION_INSERT); //create an intent to to place the contact data to the Phone contacts
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.NAME, csv[0]); //place the data in the fields
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, csv[1]);
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, csv[2]);
        intent.putExtra(ContactsContract.Intents.Insert.POSTAL, csv[3]);
        intent.putExtra(ContactsContract.Intents.Insert.COMPANY, csv[4]);
        intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, csv[5]);
        ArrayList<ContentValues> data = new ArrayList<ContentValues>(); //use an arraylist containing content values specfically for the website

        ContentValues row1 = new ContentValues(); //establish new row
        row1.put(ContactsContract.Data.MIMETYPE, Website.CONTENT_ITEM_TYPE); //specify the type
        row1.put(Website.URL, csv[6]); //insert the website
        row1.put(Website.TYPE, Website.TYPE_WORK);

        data.add(row1); //add it to the data entry


        intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, data); //insert the data row in extras

        //intent.putExtra(ContactsContract.Intents.Insert.DATA, csv[6]);
        intent.putExtra(ContactsContract.Intents.Insert.NOTES, csv[7]);

        startActivity(intent); //start the activity
    }

    public void deleteContact() { //if the delete button is pressed
        Intent intent = getIntent(); //start the intent
        try {
            MainActivity.appDb.contactDelete(intent.getStringExtra("MyID")); //try to delete  contact from the database
            String delnum = intent.getStringExtra("delete_number");
            Contacts.list.remove(Integer.parseInt(delnum));
            Contacts.listindex.remove(Integer.parseInt(delnum)); //delete it from the list of database id's and the listView
            Contacts.adapter.notifyDataSetChanged(); //update the list view
        }catch(NumberFormatException e){ //if we cant find the entry in the list
            MainActivity.appDb.contactDelete(intent.getStringExtra("MyID")); //just delete the contact from the database
        }
        Intent Newintent = new Intent(viewContacts.this, Contacts.class); //  start an intent to go back to contacts
        onActivityResult(1, RESULT_OK, Newintent); //start activity
        finish();
    }

    public void contactEdit() { // if the edit button is pressed
        Intent editIntent = new Intent(viewContacts.this, editContact.class); //start the intent to go back to edit contact
        editIntent.putExtra("MyID", edit_intent);

        startActivity(editIntent); //start the activity



    }
}
