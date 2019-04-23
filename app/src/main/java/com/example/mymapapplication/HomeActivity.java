package com.example.mymapapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
public class HomeActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;

    FrameLayout userFrameLayout;
    TabLayout userTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // get the reference of FrameLayout and TabLayout
        userFrameLayout = findViewById(R.id.frameLayout_User);
        userTabLayout = findViewById(R.id.tabLayout_User);

        //TextView textview_Info = findViewById(R.id.textview_info);
        //Button button_Logout = findViewById(R.id.button_Logout);
        String personName = getIntent().getExtras().getString("person_name");
        String personGivenName = getIntent().getExtras().getString("person_given_name");
        String personFamilyName = getIntent().getExtras().getString("person_family_name");
        String personEmail = getIntent().getExtras().getString("person_email");
        String personId = getIntent().getExtras().getString("person_id");
        String personPhoto = getIntent().getExtras().getString("person_photo");

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Create a Tab to add user"
        TabLayout.Tab addUserTab = userTabLayout.newTab();
        addUserTab.setText("Add User"); // set the Text for the first Tab
        userTabLayout.addTab(addUserTab); // add  the tab at in the TabLayout
        showSelectedTab(addUserTab);

        // Create a new Tab to view users
        TabLayout.Tab userListTab = userTabLayout.newTab();
        userListTab.setText("Users List"); // set the Text for the second Tab
        userTabLayout.addTab(userListTab); // add  the tab  in the TabLayout

        userTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showSelectedTab(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

/*
        button_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        signOut();
            }
        });
        */
    }

    // Function to Logout
    public void signOut(){
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    }
                });
    }

    // Function to show selected tab
    public void showSelectedTab(TabLayout.Tab tab){

        // get the current selected tab's position and replace the fragment accordingly
        Fragment fragment = null;
        switch (tab.getPosition()) {
            case 0:
                fragment = new AddUserFragment();
                break;
            case 1:
                fragment = new UserListFragment();
                break;
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameLayout_User, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }
}
