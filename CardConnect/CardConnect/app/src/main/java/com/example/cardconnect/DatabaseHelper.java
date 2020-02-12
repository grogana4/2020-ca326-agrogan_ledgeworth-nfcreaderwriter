package com.example.cardconnect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.widget.Toast;
import android.view.View;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "contacts.db";
    public static final String TABLE_NAME = "contact_table";
    public static final String Col_1 = "ID";
    public static final String Col_2 = "Name";
    public static final String Col_3 = "Phone";
    public static final String Col_4 = "Email";
    public static final String Col_5 = "Address";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "( ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, PHONE TEXT, EMAIL TEXT, ADDRESS TEXT) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String Name, String Phone, String Email, String Address){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(Col_2, Name);
        contentvalues.put(Col_3, Phone);
        contentvalues.put(Col_4, Email);
        contentvalues.put(Col_5, Address);
         long result = db.insert(TABLE_NAME, null, contentvalues);
         if(result == -1)
             return false;
         else
             return true;
    }
    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;


    }


    public String getContactData(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE ID = " + id, null ) ;
        res.moveToFirst();
        if (res != null) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("Name: " + res.getString(1)+ "\n");
            buffer.append("Phone: " + res.getString(2)+ "\n");
            buffer.append("Email: " + res.getString(3)+ "\n");
            buffer.append("Address: " + res.getString(4)+ "\n");
            String contactid =  buffer.toString();
            return contactid;
        }
        return id;
    }

    public String getCsvData(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE ID = " + id, null ) ;
        res.moveToFirst();
        if (res != null) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(res.getString(1)+ ",");
            buffer.append(res.getString(2)+ ",");
            buffer.append(res.getString(3)+ ",");
            buffer.append(res.getString(4)+ ",");
            String csvid =  buffer.toString();
            return csvid;
        }
        return id;
    }
}
