package com.example.cardconnect;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class viewContacts extends AppCompatActivity {

    DatabaseHelper appDb;
    private TextView contactInfo;

    private Button Save_to_contacts;
    private Button Edit;
    private Button Delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        appDb = new DatabaseHelper(this);

        Intent intent = getIntent();
        String id = intent.getStringExtra("MyID");

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

        Save_to_contacts = (Button) findViewById(R.id.btn_save_contact);
        Edit = (Button) findViewById(R.id.btn_edit);
        Delete = (Button) findViewById(R.id.btn_delete);
        contactInfo = (TextView) findViewById(R.id.contact_info);



        //contactInfo = appDb.


        //Save_to_contacts.setOnClickListener(view -> contactSaveFragment());
        //Edit.setOnClickListener(view -> contactEditFragment());
        // Delete.setOnClickListener(view -> contactDeleteFragment());

    }


//private void contactSaveFragment() {


// }

//private void contactEditFragment() {


//}

//private void contactDeleteFragment() {


//}
}
