package com.example.mymapapplication;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mymapapplication.database.LogInUserDBHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount account;
    int RC_SIGN_IN = 101;
    private LogInUserDBHelper logInUserDbHelper;
    private Boolean recordExistFlag;

    String name;
    String email;
    String phone;
    String user_photo;
    Uri logInUserImage;
    String myPhoto;
    private AsyncTask mMyTask;
    boolean saveImageFlag;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> mTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(mTask);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        saveImageFlag = false;

        logInUserDbHelper = new LogInUserDBHelper(getApplicationContext());
        SignInButton signInButton = findViewById(R.id.sign_in_button);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount mAccount = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(mAccount);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    //Function to update UI
    public void updateUI(GoogleSignInAccount account){

        if(account != null){

            Intent signInIntent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(signInIntent);

        }
    }

    //Function to sign in
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //Function to handle sign in result
    public void handleSignInResult(Task<GoogleSignInAccount> completedTask){

        try {
            account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            saveUserData(account);
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("SIGNINFAILED", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    public void saveUserData(GoogleSignInAccount userAccount){

        logInUserDbHelper.removeUser();

        name = userAccount.getGivenName() + " " + userAccount.getFamilyName();
        email = userAccount.getEmail();
        phone = "NA";

        if(userAccount.getPhotoUrl() == null || userAccount.getPhotoUrl().equals("")){
            user_photo = "NA";
        }else{

            try {
                String imageURL = userAccount.getPhotoUrl().toString();

                mMyTask = new DownloadTask().execute(stringToURL(imageURL));

            } catch(Exception e) {
                System.out.println(e);
            }

        }

    }

    private class DownloadTask extends AsyncTask<URL,Void,Bitmap>{
        public String mUserImage;
        // Before the tasks execution
        protected void onPreExecute(){
            // Display the progress dialog on async task start
        }

        // Do the task in background/non UI thread
        protected Bitmap doInBackground(URL...urls){
            URL url = urls[0];
            HttpURLConnection connection = null;

            try{
                // Initialize a new http url connection
                connection = (HttpURLConnection) url.openConnection();

                // Connect the http url connection
                connection.connect();

                // Get the input stream from http url connection
                InputStream inputStream = connection.getInputStream();

                 // Initialize a new BufferedInputStream from InputStream
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                // Convert BufferedInputStream to Bitmap object
                Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);

                // Return the downloaded bitmap
                return bmp;

            }catch(IOException e){
                e.printStackTrace();
            }finally{
                // Disconnect the http url connection
                connection.disconnect();
            }
            return null;
        }

        // When all async task done
        public void onPostExecute(Bitmap result){
            // Hide the progress dialog

            if(result != null){

                user_photo = getStringImage(result);

                Cursor cursor = logInUserDbHelper.readUser();
                recordExistFlag = false;

                while (cursor.moveToNext()) {

                    String userEmail = cursor.getString(2);

                    if (userEmail.equals(email)) {
                        recordExistFlag = true;
                        break;
                    }
                }

            }else {
                // Notify user that an error occurred while downloading image
                user_photo = "NA";
                Log.e("ERROR_RESULT-->", String.valueOf(result));
            }

            if (!recordExistFlag) {
                logInUserDbHelper.insertUser(name,email,phone,user_photo);

            }else{
                Toast.makeText(getApplicationContext(),"Welcome Back!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    // Custom method to convert string to url
    protected URL stringToURL(String urlString){
        try{
            URL url = new URL(urlString);
            return url;
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedUserImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedUserImage;
    }

}
