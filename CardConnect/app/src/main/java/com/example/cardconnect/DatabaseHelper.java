package com.example.cardconnect;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "contacts.db";
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

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "( ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT NOT NULL, PHONE TEXT NOT NULL, EMAIL TEXT NOT NULL, ADDRESS TEXT NOT NULL, ORGANISATION TEXT, POSITION TEXT, WEBSITE TEXT, NOTES TEXT, TIME TEXT, DATE TEXT, LOCATION TEXT) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String Name, String Phone, String Email, String Address, String Organisation , String Position, String Website, String Notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentvalues = new ContentValues();

        SimpleDateFormat day = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
        Date date = new Date();

        contentvalues.put(Col_2, Name);
        contentvalues.put(Col_3, Phone);
        contentvalues.put(Col_4, Email);
        contentvalues.put(Col_5, Address);
        contentvalues.put(Col_6, Organisation);
        contentvalues.put(Col_7, Position);
        contentvalues.put(Col_8, Website);
        contentvalues.put(Col_9, Notes);
        contentvalues.put(Col_10, day.format(date));
        contentvalues.put(Col_11, time.format(date));
        //contentvalues.put(Col_12, Location);
        long result = db.insert(TABLE_NAME, null, contentvalues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean insertProfileData(String id, String Name, String Phone, String Email, String Address, String Organisation, String Position, String Website, String Notes) {
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
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(Col_2, Name);
        contentvalues.put(Col_3, Phone);
        contentvalues.put(Col_4, Email);
        contentvalues.put(Col_5, Address);
        contentvalues.put(Col_6, Organisation);
        contentvalues.put(Col_7, Position);
        contentvalues.put(Col_8, Website);
        contentvalues.put(Col_9, Notes);
        db.update(TABLE_NAME, contentvalues, "id = ?", new String[] {id});
        return true;

    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " WHERE NOT ID = 0 ", null);
        return res;


    }

    public boolean isZeroThere() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + Col_1 + " ASC LIMIT 0,1", null);
        res.moveToFirst();
        String last_index = res.getString(0);

        if(last_index.equals("0")){
            return true;
        }else{
            return false;
        }


    }

    public String[] getEditData(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] editData = new String[10];
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE ID = " + id, null);
        res.moveToFirst();
        editData[0] = res.getString(1);
        editData[1] = res.getString(2);
        editData[2] = res.getString(3);
        editData[3] = res.getString(4);
        editData[4] = res.getString(5);
        editData[5] = res.getString(6);
        editData[6] = res.getString(7);
        editData[7] = res.getString(8);
        editData[8] = res.getString(9);
        editData[9] = res.getString(10);
        //editData[8] = res.getString(9);

        return editData;

    }

    public String getContactData(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE ID = " + id + " AND NOT ID = 0", null);
        res.moveToFirst();
        StringBuffer buffer = new StringBuffer();
        String[] headings = new String[]{"Name: ", "Phone: ", "Email: " , "Address: ", "Organisation: ", "Position: ", "Website: ", "Notes: ", "Read Date: ", "Read time: " };
        if (res != null) {

            int i = 0;
            while(i<10){
                if(!res.getString(i+1).equals("") && !res.getString(i+1).equals(" ") ){
                    buffer.append(headings[i] + res.getString(i+1) + "\n");
                }else{

                }
                i++;

            }
            String contactid = buffer.toString();
            return contactid;
        }else{
            return null;
        }
    }


    public String getExportData(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE ID = " + id, null);
        res.moveToFirst();
        if (res != null) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(res.getString(1) + "~");
            buffer.append(res.getString(2) + "~");
            buffer.append(res.getString(3) + "~");
            buffer.append(res.getString(4) + "~");
            buffer.append(res.getString(5) + "~");
            buffer.append(res.getString(6) + "~");
            buffer.append(res.getString(7) + "~");
            buffer.append(res.getString(8) + "~");
            String csvid = buffer.toString();
            return csvid;
        }
        return id;
    }

    public String findLastId(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + Col_1 + " DESC LIMIT 1", null);
        res.moveToFirst();
        String last_index = res.getString(0);
        return last_index;
    }

    public void contactDelete(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, Col_1 + "=" + id, null );
    }




}

