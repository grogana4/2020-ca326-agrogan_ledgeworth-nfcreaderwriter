package com.example.cardconnect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.app.AppCompatActivity;
import android.nfc.Tag;
import android.widget.TextView;
import android.widget.Toast;

public class Profile extends AppCompatActivity{

    public static final String TAG = Profile.class.getSimpleName();

    private EditText Name;
    private EditText Phone_Number;
    private EditText Email;
    private EditText Address;
    private Button Edit;
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

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Profile", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        Name = (EditText) findViewById(R.id.name_message);
        Phone_Number= (EditText) findViewById(R.id.phone_message);
        Email = (EditText) findViewById(R.id.email_message);
        Address = (EditText) findViewById(R.id.address_message);
       // Edit= (Button) findViewById(R.id.btn_edit);
        Save = (Button) findViewById(R.id.btn_save);

        Name.setText(pref.getString("NAME", null), TextView.BufferType.EDITABLE);
        Phone_Number.setText(pref.getString("PHONE", null), TextView.BufferType.EDITABLE);
        Email.setText(pref.getString("EMAIL", null), TextView.BufferType.EDITABLE);
        Address.setText(pref.getString("ADDRESS", null), TextView.BufferType.EDITABLE);

       // Edit.setOnClickListener(view -> showEditFragment());
        Save.setOnClickListener(view -> saveDetails());
    }


    private void saveDetails() {


        SharedPreferences pref = getApplicationContext().getSharedPreferences("Profile", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        String name = pref.getString("NAME", "");
        String phone = pref.getString("PHONE", "");
        String email = pref.getString("EMAIL", "");
        String address = pref.getString("ADDRESS", "");


        name = Name.getText().toString();
        phone = Phone_Number.getText().toString();
        email = Email.getText().toString();
        address = Address.getText().toString();

        editor.putString("NAME", name);
        editor.putString("PHONE", phone);
        editor.putString("EMAIL", email);
        editor.putString("ADDRESS", address);
        editor.commit();

        Name.setText(pref.getString("NAME", null), TextView.BufferType.EDITABLE);
        Phone_Number.setText(pref.getString("PHONE", null), TextView.BufferType.EDITABLE);
        Email.setText(pref.getString("EMAIL", null), TextView.BufferType.EDITABLE);
        Address.setText(pref.getString("ADDRESS", null), TextView.BufferType.EDITABLE);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}

