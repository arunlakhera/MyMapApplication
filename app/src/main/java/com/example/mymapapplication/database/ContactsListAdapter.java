package com.example.mymapapplication.database;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.mymapapplication.R;

public class ContactsListAdapter extends CursorAdapter {

    private MyContactsDBHelper contactsDbHelper;
    Context mContext;

    private TextView mContactName;
    private TextView mContactPhone;

    public ContactsListAdapter(Context context, Cursor c) {
        super(context, c, 0);
        this.mContext = context;
        contactsDbHelper = new MyContactsDBHelper(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.contacts_listview, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        mContactName = view.findViewById(R.id.textview_ContactName);
        mContactPhone = view.findViewById(R.id.textview_ContactPhone);

        int contactNameColumnIndex = cursor.getColumnIndex("name");
        int contactPhoneColumnIndex = cursor.getColumnIndex("phone");

        String contactName = cursor.getString(contactNameColumnIndex);
        String contactPhone = cursor.getString(contactPhoneColumnIndex);

        setValues(contactName,contactPhone);

    }

    public void setValues(String name, String phone){

        mContactName.setText(name);
        mContactPhone.setText(phone);

    }
}
