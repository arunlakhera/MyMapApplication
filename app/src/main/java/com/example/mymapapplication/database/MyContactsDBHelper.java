package com.example.mymapapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class MyContactsDBHelper extends SQLiteOpenHelper {

    // Database Name
    private static final String DATABASE_NAME = "MyContactsDB.db";
    private static final int DATABASE_VERSION = 2;

    // Table Name
    private final static String TABLE_NAME = "my_phone_contact";

    // Table USER Columns
    private final static String _ID = BaseColumns._ID;
    private final static String COLUMN_CONTACT_NAME = "name";
    private final static String COLUMN_CONTACT_PHONE = "phone";

    public MyContactsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create Table
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_TABLE_CONTACT_DIARY = "CREATE TABLE " + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_CONTACT_NAME + " TEXT NOT NULL," +
                COLUMN_CONTACT_PHONE + " TEXT" + ");";

        Log.v("MyUserDBHelper", "create table: " + CREATE_TABLE_CONTACT_DIARY);
        sqLiteDatabase.execSQL(CREATE_TABLE_CONTACT_DIARY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    public void removeContacts(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
    }

    // Insert User in Database
    public void insertContact(String userName, String userPhone) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_NAME, userName);
        values.put(COLUMN_CONTACT_PHONE, userPhone);
        db.insert(TABLE_NAME, null, values);
    }

    // Read Users from database
    public Cursor readContacts() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                _ID,
                COLUMN_CONTACT_NAME,
                COLUMN_CONTACT_PHONE
        };

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        return cursor;
    }

}