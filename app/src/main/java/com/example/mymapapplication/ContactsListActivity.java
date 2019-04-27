package com.example.mymapapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.mymapapplication.database.ContactsListAdapter;
import com.example.mymapapplication.database.MyContactsDBHelper;

import java.util.ArrayList;
import java.util.List;

public class ContactsListActivity extends AppCompatActivity {

    private Button mButton_LoadContacts;
    private Button mButton_ClearContacts;
    private MyContactsDBHelper contactsDbHelper;
    private ContactsListAdapter mCursorAdapter;
    ListView contactListView;
    private Cursor cursor;
    private int total_contacts;

    String id;
    String name;
    String phoneNumber;

    static final int PICK_CONTACT = 1;
    String st;
    final private int REQUEST_MULTIPLE_PERMISSIONS = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);

        contactsDbHelper = new MyContactsDBHelper(this);

        initViews();

        mButton_LoadContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccessContact();
                saveContacts();
                showContacts();
            }
        });

        mButton_ClearContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearContacts();
            }
        });
    }

    public void initViews(){

        mButton_LoadContacts = findViewById(R.id.button_Load);
        mButton_ClearContacts = findViewById(R.id.button_Clean);
        contactListView = findViewById(R.id.listview_ContactsList);

    }

    public void clearContacts(){

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Do You want to clear all the contacts.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        contactsDbHelper.removeContacts();
                        showContacts();
                    }
                });
        alertDialog.show();

    }

    public void showContacts(){

        Cursor readCursor = contactsDbHelper.readContacts();
        mCursorAdapter = new ContactsListAdapter(getApplicationContext(), readCursor);
        contactListView.setAdapter(mCursorAdapter);

    }

    private void AccessContact()
    {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS))
            permissionsNeeded.add("Read Contacts");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_CONTACTS))
            permissionsNeeded.add("Write Contacts");
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_MULTIPLE_PERMISSIONS);

                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_MULTIPLE_PERMISSIONS);
            return;
        }
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);

            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {

        new AlertDialog.Builder(ContactsListActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }



    public void saveContacts(){

        StringBuilder builder = new StringBuilder();
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null, null,null);
        total_contacts = cursor.getCount();

        int c = 0;

        if(cursor.getCount() > 0){
            while(cursor.moveToNext() && c < 10){

                id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

                if(hasPhoneNumber > 0){
                    Cursor cursor2 = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[] {id},null);

                    while (cursor2.moveToNext()){

                        phoneNumber = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        builder.append("Contact: ").append(name).append(", Phone Number: ").append(phoneNumber).append("\n\n");

                    }
                    contactsDbHelper.insertContact(name,phoneNumber);
                    cursor2.close();
                }
                c =c + 1;
            }

        }

        cursor.close();

        Log.e("CONTACTS---->",String.valueOf(total_contacts));

    }

}
