package com.example.mymapapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class HomeActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;

    FrameLayout userFrameLayout;
    TabLayout userTabLayout;

    //Navigation Drawer
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private Button menuButton;
    private TextView mTextView_UserName;
    Fragment fragment;

    HelperFile mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // get the reference of FrameLayout and TabLayout
        userFrameLayout = findViewById(R.id.frameLayout_User);
        userTabLayout = findViewById(R.id.tabLayout_User);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        menuButton = findViewById(R.id.button_Menu);

        mHelper = new HelperFile();
        mHelper.userProfile(getApplicationContext());

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

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDrawerLayout.openDrawer(navigationView);

                showUserProfile();

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                // set item as selected to persist highlight
                menuItem.setChecked(true);

                if(menuItem.getTitle().equals("Home")){

                    mDrawerLayout.closeDrawers();

                }else if(menuItem.getTitle().equals("Map Task")){
                    startActivity(new Intent(HomeActivity.this, MapTaskActivity.class));

                }else if(menuItem.getTitle().equals("About Us")){
                    startActivity(new Intent(HomeActivity.this, WebViewActivity.class));

                }else if(menuItem.getTitle().equals("Contact List")){
                    startActivity(new Intent(HomeActivity.this, ContactsListActivity.class));

                }else if(menuItem.getTitle().equals("Update Profile")){
                    startActivity(new Intent(HomeActivity.this, UpdateProfileActivity.class));

                }else if(menuItem.getTitle().equals("API Task")){


                }else if(menuItem.getTitle().equals("Logout")){

                    signOut();

                }

                // close drawer when item is tapped
                mDrawerLayout.closeDrawers();

                return true;
            }
        });
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
        fragment = null;
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

    public void showUserProfile(){
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user_name = hView.findViewById(R.id.textview_UserProfileName);
        ImageView nav_user_photo = hView.findViewById(R.id.imageview_UserProfileImage);

        nav_user_name.setText(mHelper.personName);

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.user_img)
                .error(R.mipmap.ic_launcher_round);

        try {
            Glide.with(this)
                    .load(mHelper.personPhoto)
                    .apply(options)
                    .into(nav_user_photo);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Could not Load image.." + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Override this method in the activity that hosts the Fragment and call super
        // in order to receive the result inside onActivityResult from the fragment.
        super.onActivityResult(requestCode, resultCode, data);
    }
}
