package com.example.mymapapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class MyUserDBHelper extends SQLiteOpenHelper {

    // Database Name
    private static final String DATABASE_NAME = "MyContactsDB.db";
    private static final int DATABASE_VERSION = 2;

    // Table Name
    private final static String TABLE_NAME = "my_contact";

    // Table USER Columns
    private final static String _ID = BaseColumns._ID;
    private final static String COLUMN_USER_NAME = "name";
    private final static String COLUMN_USER_EMAIL_ID = "email_id";
    private final static String COLUMN_USER_PHONE = "phone";
    private final static String COLUMN_USER_PHOTO = "photo";

    public MyUserDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create Table
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_USER_DIARY = "CREATE TABLE " + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USER_NAME + " TEXT NOT NULL," +
                COLUMN_USER_EMAIL_ID + " TEXT NOT NULL," +
                COLUMN_USER_PHONE + " TEXT NOT NULL," +
                COLUMN_USER_PHOTO + " BLOB" + ");";

        Log.v("MyUserDBHelper", "create table: " + CREATE_TABLE_USER_DIARY);
        sqLiteDatabase.execSQL(CREATE_TABLE_USER_DIARY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    // Insert User in Database
    public void insertUser(String userName, String userEmailId, String userPhone,  String userPhoto) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, userName);
        values.put(COLUMN_USER_EMAIL_ID, userEmailId);
        values.put(COLUMN_USER_PHONE, userPhone);
        values.put(COLUMN_USER_PHOTO, userPhoto);
        db.insert(TABLE_NAME, null, values);
    }

    // Read Users from database
    public Cursor readUsers() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                _ID,
                COLUMN_USER_NAME,
                COLUMN_USER_EMAIL_ID,
                COLUMN_USER_PHONE,
                COLUMN_USER_PHOTO
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
