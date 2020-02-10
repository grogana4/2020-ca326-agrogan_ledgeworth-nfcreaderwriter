package com.example.cardconnect;


import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;


public class Contacts extends AppCompatActivity {

    DatabaseHelper appDb;
    ListView listView;
    List<String> list = new ArrayList<String>();
    ArrayAdapter adapter;
    private Button Delete;
    private static String ID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appDb = new DatabaseHelper(this);
        appDb.insertData("paul", "0831234567", "Hello@iol.ie", "123 jeff lane");


        ViewAll();

        listView = (ListView) findViewById(R.id.list_view);
        listView.setClickable(true);

        adapter = new ArrayAdapter(Contacts.this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ID = listView.getItemAtPosition(position).toString();
                Intent intent = new Intent(Contacts.this, viewContacts.class);
                intent.putExtra("MyID", ID);
                startActivity(intent);
            }
        });

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


        Delete = (Button) findViewById(R.id.btn_delete);
        // Delete.setOnClickListener(view -> contactDeleteFragment());

    }


    public void ViewAll() {

        Cursor res = appDb.getAllData();
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("ID: " + res.getString(0));
            buffer.append("Name: " + res.getString(1));
            buffer.append("Phone: " + res.getString(2));
            buffer.append("Email: " + res.getString(3));
            buffer.append("Address: " + res.getString(4));
            list.add(res.getString(1));

        }


    }


}