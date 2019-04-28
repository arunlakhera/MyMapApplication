package com.example.mymapapplication;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.mymapapplication.database.LogInUserAdapter;
import com.example.mymapapplication.database.LogInUserDBHelper;

import java.io.ByteArrayOutputStream;

public class UpdateProfileActivity extends AppCompatActivity {

    private ImageView mUserPhoto;
    private Button mUpdatePhoto;
    private EditText mUserName;
    private EditText mUserEmailId;
    private EditText mUserPhone;
    private Button mAddUser;

    private LogInUserDBHelper mLogInUserDbHelper;
    private LogInUserAdapter mCursorAdapter;

    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        initViews();

        loadData();

        updateUI();

    }

    public void initViews(){

        mUserPhoto = findViewById(R.id.imageview_Photo);
        mUpdatePhoto = findViewById(R.id.button_UploadPhoto);
        mUserName = findViewById(R.id.edittext_Name);
        mUserEmailId = findViewById(R.id.edittext_EmailId);
        mUserPhone = findViewById(R.id.edittext_Phone);
        mAddUser = findViewById(R.id.button_Update_User);
        mLogInUserDbHelper = new LogInUserDBHelper(getApplicationContext());

    }

    public void loadData(){

        Cursor readCursor = mLogInUserDbHelper.readUser();

        while (readCursor.moveToNext()){

            mCursorAdapter = new LogInUserAdapter(getApplicationContext(), readCursor);

            int userNameColumnIndex = readCursor.getColumnIndex("name");
            int userEmailColumnIndex = readCursor.getColumnIndex("email_id");
            int userPhoneColumnIndex = readCursor.getColumnIndex("phone");
            int userPhotoColumnIndex = readCursor.getColumnIndex("photo");

            String userName = readCursor.getString(userNameColumnIndex);
            String userEmail = readCursor.getString(userEmailColumnIndex);
            String userPhone = readCursor.getString(userPhoneColumnIndex);
            String userPhoto = readCursor.getString(userPhotoColumnIndex);

            mUserName.setText(userName);
            mUserEmailId.setText(userEmail);
            mUserPhone.setText(userPhone);
            mUserPhoto.setImageResource(R.drawable.user_img);

            if(userPhoto == null || userPhoto.equals("NA")){

                mUserPhoto.setImageResource(R.drawable.user_img);
            }else{
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] imageBytes = baos.toByteArray();
                imageBytes = Base64.decode(userPhoto, Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                mUserPhoto.setImageBitmap(decodedImage);
            }

        }


    }

    public void updateUI(){


    }


}
