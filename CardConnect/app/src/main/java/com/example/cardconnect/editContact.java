//this code was primarily worked on by luke Edgeworth.
package com.example.cardconnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class editContact extends AppCompatActivity { //editContact starts up if Contacts button is pressed

    private EditText Name; //variables initiated
    private EditText Phone_Number;
    private EditText Email;
    private EditText Address;
    private EditText Organisation;
    private EditText Position;
    private EditText Website;
    private EditText Notes;
    private Button Save;
    private String[] editData;
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) { //when the Activity starts
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact); //set the layout
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar); //create and set the toolbar with an options menu
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //setDisplay so the user can return to the previous page using the phone's back button


        initViews(); //start the the other app functions
    }





    private void initViews() {
        Intent intent = getIntent(); //get th id of the contact from the intent
        id = intent.getStringExtra("MyID");

        Name = (EditText) findViewById(R.id.name_message);  //initialise the the variable edit views
        Phone_Number= (EditText) findViewById(R.id.phone_message);
        Email = (EditText) findViewById(R.id.email_message);
        Address = (EditText) findViewById(R.id.address_message);
        Organisation = (EditText) findViewById(R.id.org_message);
        Position = (EditText) findViewById(R.id.pos_message);
        Website = (EditText) findViewById(R.id.website_message);
        Notes = (EditText) findViewById(R.id.notes_message);
        Save = (Button) findViewById(R.id.btn_save);

        editData = MainActivity.appDb.getEditData(id); // get the edit data from the contact and set it to the editText fields
        Name.setText(editData[0], TextView.BufferType.EDITABLE);
        Phone_Number.setText(editData[1], TextView.BufferType.EDITABLE);
        Email.setText(editData[2], TextView.BufferType.EDITABLE);
        Address.setText(editData[3], TextView.BufferType.EDITABLE);
        Organisation.setText(editData[4], TextView.BufferType.EDITABLE);
        Position.setText(editData[5], TextView.BufferType.EDITABLE);
        Website.setText(editData[6], TextView.BufferType.EDITABLE);
        Notes.setText(editData[7], TextView.BufferType.EDITABLE);

        Save.setOnClickListener(view -> saveDetails()); //assign the button to the function
    }


    private void saveDetails() { //if the save button is pressed



       String name = Name.getText().toString(); //get the strings from the editText fields
       String phone = Phone_Number.getText().toString();
       String email = Email.getText().toString();
       String address = Address.getText().toString();
        String organisation = Organisation.getText().toString();
        String pos = Position.getText().toString();
        String website = Website.getText().toString();
        String notes = Notes.getText().toString();



       MainActivity.appDb.updateData(id, name, phone, email, address, organisation, pos, website, notes); //update the contact data

        Intent intent = new Intent(this, Contacts.class); //bring the user back to contacts
        startActivity(intent);

    }
}
