package com.example.mymapapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mymapapplication.database.MyUserDBHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AddUserFragment extends Fragment {

    private ImageView mUserPhoto;
    private Button mUpdatePhoto;
    private EditText mUserName;
    private EditText mUserEmailId;
    private EditText mUserPhone;
    private Button mAddUser;

    private String userName;
    private String userEmailId;
    private String userPhone;
    private String userPhoto;

    private MyUserDBHelper userDbHelper;
    private Boolean recordExistFlag;

    public AddUserFragment() {
        //Required
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_add_user, container, false);
        mUserPhoto = view.findViewById(R.id.imageview_Photo);
        mUpdatePhoto = view.findViewById(R.id.button_UploadPhoto);
        mUserName = view.findViewById(R.id.edittext_Name);
        mUserEmailId = view.findViewById(R.id.edittext_EmailId);
        mUserPhone = view.findViewById(R.id.edittext_Phone);
        mAddUser = view.findViewById(R.id.button_Add_User);

        userDbHelper = new MyUserDBHelper(getContext());

        mAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkUserData()){
                    // Save User Data
                    saveUserData();
                    clearData();
                }

            }
        });


        return view;
    }

    // Function to check User Data
    public boolean checkUserData(){

        boolean checkFlag = false;

        userName = mUserName.getText().toString().trim();
        userEmailId = mUserEmailId.getText().toString().trim();
        userPhone = mUserPhone.getText().toString().trim();
        userPhoto = "PHOTO";

        if(userName.isEmpty()){
            mUserName.setError("Please Enter Name");
            mUserName.setFocusable(true);
        }else if(userEmailId.isEmpty() || !isValidEmail(userEmailId)){
            mUserEmailId.setError("Please Enter Email Id");
            mUserEmailId.setFocusable(true);
        }else if(userPhone.isEmpty() || userPhone.length() != 10){
            mUserPhone.setError("Please Enter Valid Phone Number");
            mUserPhone.setFocusable(true);
        }else{
            checkFlag = true;
        }

        return checkFlag;
    }

    public boolean isValidEmail(String emaildId){

        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(emaildId);

        return matcher.matches();
    }

    public void saveUserData(){

        Cursor cursor = userDbHelper.readUsers();
        recordExistFlag = false;

        while (cursor.moveToNext()) {

            String userEmail = cursor.getString(2);

            if (userEmail.equals(userEmailId)) {
                recordExistFlag = true;
                break;
            }
        }

        if (recordExistFlag) {
            Toast.makeText(getContext(), "User With this Email ID already Exists!", Toast.LENGTH_LONG).show();
        } else {
            userDbHelper.insertUser(userName,userEmailId,userPhone,userPhoto);
            Toast.makeText(getContext(),"User Data Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public void clearData(){
        mUserName.setText("");
        mUserEmailId.setText("");
        mUserPhone.setText("");
        mUserPhoto.setImageResource(R.drawable.user_img);
    }

}
