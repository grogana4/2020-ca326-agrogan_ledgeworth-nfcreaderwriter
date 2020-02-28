//this code was primarily worked on by Abe Grogan.
package com.example.cardconnect;

import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class Profile extends AppCompatActivity{ //Profile starts up when Edit your Profile is pressed



    private EditText Name; //EditText fields for user profile created
    private EditText Phone_Number;
    private EditText Email;
    private EditText Address;
    private EditText Organisation;
    private EditText Position;
    private EditText Website;
    private EditText Notes;
    private String[] editData; //String array for storing fields for exporting and saving
    private Button Save; //save button for storing into database created


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile); //set the layout
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar); //create and set the toolbar with an options menu
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //setDisplay so the user can return to the previous page using the phone's back button


        initViews(); //start the the other app functions
    }





    private void initViews() {
        Intent intent = getIntent(); //get the profile data from the intent
        String id = intent.getStringExtra("MyID");

        Name = (EditText) findViewById(R.id.name_message); //assign button and editText fields for their xml objects
        Phone_Number= (EditText) findViewById(R.id.phone_message);
        Email = (EditText) findViewById(R.id.email_message);
        Address = (EditText) findViewById(R.id.address_message);
        Organisation = (EditText) findViewById(R.id.org_message);
        Position = (EditText) findViewById(R.id.pos_message);
        Website = (EditText) findViewById(R.id.website_message);
        Notes = (EditText) findViewById(R.id.notes_message);
        Save = (Button) findViewById(R.id.btn_save);

        try { //if profile already exists
            editData = MainActivity.appDb.getEditData(id); //try to get profile data
            Name.setText(editData[0], TextView.BufferType.EDITABLE); // set text to each field from the String array
            Phone_Number.setText(editData[1], TextView.BufferType.EDITABLE);
            Email.setText(editData[2], TextView.BufferType.EDITABLE);
            Address.setText(editData[3], TextView.BufferType.EDITABLE);
            Organisation.setText(editData[4], TextView.BufferType.EDITABLE);
            Position.setText(editData[5], TextView.BufferType.EDITABLE);
            Website.setText(editData[6], TextView.BufferType.EDITABLE);
            Notes.setText(editData[7], TextView.BufferType.EDITABLE);


        }catch (CursorIndexOutOfBoundsException e){ //if it doesn't, do nothing so the fields are blank

        }

        Save.setOnClickListener(view -> saveDetails()); //set button to save details function
    }


    private void saveDetails() {


        String name = Name.getText().toString(); //get strings from the editText fields
        String phone = Phone_Number.getText().toString();
        String email = Email.getText().toString();
        String address = Address.getText().toString();
        String org = Organisation.getText().toString();
        String pos = Position.getText().toString();
        String website = Website.getText().toString();
        String notes = Notes.getText().toString();

        if(!name.equals("") && !phone.equals("")) { //as long aas the key fields aren't empty
            try{
                if(MainActivity.appDb.isZeroThere()) { //if profile data is already in the database
                    MainActivity.appDb.updateData("0", name, phone, email, address, org, pos, website, notes); ///update the data
                }else{
                    MainActivity.appDb.insertProfileData("0", name, phone, email, address, org, pos, website, notes); //otherwise insert the profile data
                }
            }catch(NullPointerException | IndexOutOfBoundsException e){ //if profile data does not exist
                MainActivity.appDb.insertProfileData("0", name, phone, email, address, org, pos, website, notes);//insert the profile data
            }

            Intent intent = new Intent(this, MainActivity.class); //start a new intent and goes back to the main menu
            startActivity(intent);
        }else{ //if there's nothing in the key fields
            Toast.makeText(this, "Please enter the required fields marked with *", Toast.LENGTH_LONG).show(); //tell the user to enter the key fields

        }


    }
}

