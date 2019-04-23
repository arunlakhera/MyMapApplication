package com.example.mymapapplication;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
public class WebViewActivity extends AppCompatActivity {

    private Context mContext;
    private Activity mActivity;

    private CoordinatorLayout mCLayout;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        // Get the application context
        mContext = getApplicationContext();
        mActivity = WebViewActivity.this;

        // Get the widget reference from XML layout
        mCLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mWebView = (WebView) findViewById(R.id.web_view);

        mWebView.setWebViewClient( new WebViewClient());
        mWebView.loadUrl("https://developer.android.com/guide/index.html");
        
    }

    protected void showAppExitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

        builder.setTitle("Please confirm");
        builder.setMessage("No back history found, want to exit the app?");
        builder.setCancelable(true);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do something when user want to exit the app
                // Let allow the system to handle the event, such as exit the app
                WebViewActivity.super.onBackPressed();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do something when want to stay in the app
                Toast.makeText(mContext,"thank you",Toast.LENGTH_LONG).show();
            }
        });

        // Create the alert dialog using alert dialog builder
        AlertDialog dialog = builder.create();

        // Finally, display the dialog when user press back button
        dialog.show();
    }

    @Override
    public void onBackPressed(){
        if(mWebView.canGoBack()){
            // If web view have back history, then go to the web view back history
            mWebView.goBack();
            Snackbar.make(mCLayout,"Go to back history",Snackbar.LENGTH_LONG).show();
        }else {
            // Ask the user to exit the app or stay in here
            showAppExitDialog();
        }
    }

}
