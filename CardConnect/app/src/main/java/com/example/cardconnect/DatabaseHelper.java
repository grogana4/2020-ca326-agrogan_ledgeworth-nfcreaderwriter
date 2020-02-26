//this code was primarily worked on by luke Edgeworth.
package com.example.cardconnect;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.Date;



public class DatabaseHelper extends SQLiteOpenHelper{ //Database starts up with MainActivity
    public static final String DB_NAME = "contacts.db"; //name of the database, table and columns are initialised
    public static final String TABLE_NAME = "contact_table";
    public static final String Col_1 = "ID";
    public static final String Col_2 = "Name";
    public static final String Col_3 = "Phone";
    public static final String Col_4 = "Email";
    public static final String Col_5 = "Address";
    public static final String Col_6 = "Organisation";
    public static final String Col_7 = "Position";
    public static final String Col_8 = "Website";
    public static final String Col_9 = "Notes";
    public static final String Col_10 = "Date";
    public static final String Col_11 = "Time";
    public static final String Col_12 = "Location";



    public DatabaseHelper(@Nullable Context context) { //get a database we can write to
        super(context, DB_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //utilises sqlite to create a table with id as a primary key,columns, columns types and whether the entries can not be null entering the table
        db.execSQL("create table " + TABLE_NAME + "( ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT NOT NULL, PHONE TEXT NOT NULL, EMAIL TEXT NOT NULL, ADDRESS TEXT NOT NULL, ORGANISATION TEXT, POSITION TEXT, WEBSITE TEXT, NOTES TEXT, TIME TEXT, DATE TEXT, LOCATION TEXT) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //drop and recreates the table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String Name, String Phone, String Email, String Address, String Organisation , String Position, String Website, String Notes, String Location) {
        SQLiteDatabase db = this.getWritableDatabase(); //get a database we can write to
        ContentValues contentvalues = new ContentValues(); //create a ContentValues array to store the values

        SimpleDateFormat day = new SimpleDateFormat("dd/MM/yyyy"); //create date formats for the date and time
        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
        Date date = new Date(); //get current date


        contentvalues.put(Col_2, Name); //insert contact fields into contentvalues
        contentvalues.put(Col_3, Phone);
        contentvalues.put(Col_4, Email);
        contentvalues.put(Col_5, Address);
        contentvalues.put(Col_6, Organisation);
        contentvalues.put(Col_7, Position);
        contentvalues.put(Col_8, Website);
        contentvalues.put(Col_9, Notes);
        contentvalues.put(Col_10, day.format(date));
        contentvalues.put(Col_11, time.format(date));
        contentvalues.put(Col_12, Location);
        long result = db.insert(TABLE_NAME, null, contentvalues); //insert the info into the database
        if (result == -1) //if the insert fails
            return false;
        else
            return true;
    }

    public boolean insertProfileData(String id, String Name, String Phone, String Email, String Address, String Organisation, String Position, String Website, String Notes) { //insert profile data at the beginning
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(Col_1, id);
        contentvalues.put(Col_2, Name);
        contentvalues.put(Col_3, Phone);
        contentvalues.put(Col_4, Email);
        contentvalues.put(Col_5, Address);
        contentvalues.put(Col_6, Organisation);
        contentvalues.put(Col_7, Position);
        contentvalues.put(Col_8, Website);
        contentvalues.put(Col_9, Notes);
        long result = db.insert(TABLE_NAME, null, contentvalues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean updateData(String id, String Name, String Phone, String Email, String Address, String Organisation, String Position, String Website, String Notes){
        SQLiteDatabase db = this.getWritableDatabase(); //get a database we can write to
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(Col_2, Name); //insert contact fields into contentvalues
        contentvalues.put(Col_3, Phone);
        contentvalues.put(Col_4, Email);
        contentvalues.put(Col_5, Address);
        contentvalues.put(Col_6, Organisation);
        contentvalues.put(Col_7, Position);
        contentvalues.put(Col_8, Website);
        contentvalues.put(Col_9, Notes);
        db.update(TABLE_NAME, contentvalues, "id = ?", new String[] {id}); //find the id of the contact we're looking for and update the table with new info
        return true;

    }

    public Cursor getAllData() { //used to get the whole table
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " WHERE NOT ID = 0 ", null);
        return res;


    }

    public boolean isZeroThere() { // used to check if the profile row is there
        SQLiteDatabase db = this.getReadableDatabase(); //get a database we can read
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + Col_1 + " ASC LIMIT 0,1", null); //query the specific id
        res.moveToFirst(); //move the cursor to the beginning
        String last_index = res.getString(0); //get specifically the id column

        if(last_index.equals("0")){ //if zero or the profile exists
            return true;
        }else{ //if we got nothing in th query
            return false;
        }


    }

    public String[] getEditData(String id){ //to get data we can stick in the edit contact fields
        SQLiteDatabase db = this.getReadableDatabase(); //get a database we can read
        String[] editData = new String[10]; //create an array
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE ID = " + id, null); //query the contact by the specific id
        res.moveToFirst(); //move the cursor to the beginning
        editData[0] = res.getString(1); //set db data in the array
        editData[1] = res.getString(2);
        editData[2] = res.getString(3);
        editData[3] = res.getString(4);
        editData[4] = res.getString(5);
        editData[5] = res.getString(6);
        editData[6] = res.getString(7);
        editData[7] = res.getString(8);
        editData[8] = res.getString(9);
        editData[9] = res.getString(10);

        return editData; //return the array

    }

    public String getContactData(String id) { //get data for viewContact text view
        SQLiteDatabase db = this.getReadableDatabase(); //get a database we can read
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE ID = " + id + " AND NOT ID = 0", null); //query the contact by the specific id
        res.moveToFirst(); //move the cursor to the beginning
        StringBuffer buffer = new StringBuffer(); //create a String buffer to create text
        String[] headings = new String[]{"Name:     ", "Phone:      ", "Email:      " , "Address:       ", "Organisation:       ", "Position:       ", "Website:        ", "Notes:      ", "Read Time:      ", "Read Date:      ", "You met this person at:     " }; //create headings for dynamic entries
        if (res != null) { //if cursor is not null

            int i = 0;
            while(i<11){ //go through each data entry
                if(!res.getString(i+1).equals("") && !res.getString(i+1).equals(" ") ){ // as long as its not empty string or a space
                    buffer.append(headings[i] + res.getString(i+1) + "\n"); //append together an entry of the contact
                }else{ // else do nothing

                }
                i++;

            }
            String contactid = buffer.toString(); // assign the buffer to a string and return it
            return contactid;
        }else{ //if the cursor is null
            return null; //return null
        }
    }


    public String getExportData(String id) { //used to export data to phone contacts
        SQLiteDatabase db = this.getReadableDatabase(); //get a database we can read
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE ID = " + id, null); //query the contact by the specific id
        res.moveToFirst(); //move the cursor to the beginning
        if (res != null) { //if cursor is not null
            StringBuffer buffer = new StringBuffer(); //create a String buffer to create text
            buffer.append(res.getString(1) + "~"); //append together an entry of the contact
            buffer.append(res.getString(2) + "~");
            buffer.append(res.getString(3) + "~");
            buffer.append(res.getString(4) + "~");
            buffer.append(res.getString(5) + "~");
            buffer.append(res.getString(6) + "~");
            buffer.append(res.getString(7) + "~");
            buffer.append(res.getString(8) + "~");
            String csvid = buffer.toString(); // assign the buffer to a string and return it
            return csvid;
        }
        return id; //return id if the cursor is null
    }

    public String findLastId(){ //use to find the latest entry
        SQLiteDatabase db = this.getReadableDatabase(); //get a database we can read
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + Col_1 + " DESC LIMIT 1", null); //query the contact by the specific id
        res.moveToFirst(); //move the cursor to the beginning
        String last_index = res.getString(0); //get specifically the id column and return it
        return last_index;
    }

    public void contactDelete(String id) { //used to delete the database
        SQLiteDatabase db = this.getWritableDatabase(); //get a database we can write to
        db.delete(TABLE_NAME, Col_1 + "=" + id, null ); //delete the row that has the same id
    }




}

