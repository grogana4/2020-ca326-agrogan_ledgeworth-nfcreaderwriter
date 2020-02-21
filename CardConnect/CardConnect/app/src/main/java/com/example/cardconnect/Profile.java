package com.example.cardconnect;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class Profile extends AppCompatActivity{

    public static final String TAG = Profile.class.getSimpleName();

    private EditText Name;
    private EditText Phone_Number;
    private EditText Email;
    private EditText Address;
    private EditText Organisation;
    private EditText Position;
    private EditText Website;
    private EditText Notes;
    private String[] editData;
    private Button Save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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
        Intent intent = getIntent();
        String id = intent.getStringExtra("MyID");

        Name = (EditText) findViewById(R.id.name_message);
        Phone_Number= (EditText) findViewById(R.id.phone_message);
        Email = (EditText) findViewById(R.id.email_message);
        Address = (EditText) findViewById(R.id.address_message);
        Organisation = (EditText) findViewById(R.id.org_message);
        Position = (EditText) findViewById(R.id.pos_message);
        Website = (EditText) findViewById(R.id.website_message);
        Notes = (EditText) findViewById(R.id.notes_message);
        Save = (Button) findViewById(R.id.btn_save);

        try {
            editData = MainActivity.appDb.getEditData(id);
            Name.setText(editData[0], TextView.BufferType.EDITABLE);
            Phone_Number.setText(editData[1], TextView.BufferType.EDITABLE);
            Email.setText(editData[2], TextView.BufferType.EDITABLE);
            Address.setText(editData[3], TextView.BufferType.EDITABLE);
            Organisation.setText(editData[4], TextView.BufferType.EDITABLE);
            Position.setText(editData[5], TextView.BufferType.EDITABLE);
            Website.setText(editData[6], TextView.BufferType.EDITABLE);
            Notes.setText(editData[7], TextView.BufferType.EDITABLE);


        }catch (CursorIndexOutOfBoundsException e){

        }

        Save.setOnClickListener(view -> saveDetails());
    }


    private void saveDetails() {


        String name = Name.getText().toString();
        String phone = Phone_Number.getText().toString();
        String email = Email.getText().toString();
        String address = Address.getText().toString();
        String org = Organisation.getText().toString();
        String pos = Position.getText().toString();
        String website = Website.getText().toString();
        String notes = Notes.getText().toString();

        if(!name.equals("") && !phone.equals("")) {
            try{
                if(MainActivity.appDb.isZeroThere()) {
                    MainActivity.appDb.updateData("0", name, phone, email, address, org, pos, website, notes);
                }else{
                    MainActivity.appDb.insertProfileData("0", name, phone, email, address, org, pos, website, notes);
                }
            }catch(NullPointerException | IndexOutOfBoundsException e){
                MainActivity.appDb.insertProfileData("0", name, phone, email, address, org, pos, website, notes);
            }

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Please enter the required fields marked with *", Toast.LENGTH_LONG).show();

        }


    }
}

