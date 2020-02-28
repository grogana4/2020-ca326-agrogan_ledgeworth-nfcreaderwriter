//this code was primarily worked on by luke Edgeworth.
package com.example.cardconnect;


import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
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

public class Contacts extends AppCompatActivity{ //Contacts starts up if Contacts button is pressed

    public static ListView listView; //variables created
    public static List<String> list = new ArrayList<String>();
    public static List<String> listindex = new ArrayList<String>();
    public static ArrayAdapter adapter;
    private Button Delete;
    private String identifier;
    private String deleteNumber;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts); //set the layout
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar); //create and set the toolbar with an options menu
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //setDisplay so the user can return to the previous page using the phone's back button



       initViews(); //start the the other app functions

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {// Handle action bar item clicks here. this is mainly fo the options menu.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) { //if manual is clicked, the manual page will appear
            Intent intent = new Intent(this, Manual.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void initViews(){
        ViewAll(); //get all the contacts in the database

        listView = (ListView) findViewById(R.id.list_view); //create a clickable listview
        listView.setClickable(true);

        adapter = new ArrayAdapter(Contacts.this, android.R.layout.simple_list_item_1, list); //create the adapter for the list view, this is for making the list view dynamic for the changing database
        listView.setAdapter(adapter); //set the adapter to the listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //if an item is clicked
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                identifier = String.valueOf(listindex.get(position)); //get contact id
                deleteNumber = String.valueOf(position); //get the entries place in the list


                Intent intent = new Intent(Contacts.this, viewContacts.class); //start an intent that takes us to viewContacts
                intent.putExtra("MyID", identifier);//place the id number and delete number in the extras
                intent.putExtra("delete_number", deleteNumber);
                startActivityForResult(intent, 1); //start the intent

            }
        });

    }



    public void ViewAll() {
        listindex = new ArrayList<String>(); //create the list and list index
        list = new ArrayList<String>();
        Cursor res = MainActivity.appDb.getAllData(); //get all database data from the database
        StringBuffer buffer = new StringBuffer(); //create a String buffer to create text
        while (res.moveToNext()) { // for each entry of the of the database
            buffer.append("ID: " + res.getString(0)); //append the entry together
            buffer.append("Name: " + res.getString(1));
            buffer.append("Phone: " + res.getString(2));
            buffer.append("Email: " + res.getString(3));
            buffer.append("Address: " + res.getString(4));
            list.add(res.getString(1)); //add that name to the view list
            listindex.add(res.getString(0)); //add that id to the id list

        }


    }


}