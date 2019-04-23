package com.example.mymapapplication;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class HelperFile {

    String personName;
    String personGivenName;
    String personFamilyName;
    String personEmail;
    String personId;
    String personPhoto;


    public void userProfile(Context context){

        GoogleSignInAccount mAccount = GoogleSignIn.getLastSignedInAccount(context);

        personName = mAccount.getDisplayName();
        personGivenName = mAccount.getGivenName();
        personFamilyName = mAccount.getFamilyName();
        personEmail = mAccount.getEmail();
        personId = mAccount.getId();
        personPhoto = String.valueOf(mAccount.getPhotoUrl());

    }

}
