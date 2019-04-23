package com.example.mymapapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class MapTaskActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;

    //Navigation Drawer
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private Button menuButton;
    private HelperFile mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_task);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        menuButton = findViewById(R.id.button_Menu);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mHelper = new HelperFile();
        mHelper.userProfile(getApplicationContext());

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

                    startActivity(new Intent(MapTaskActivity.this, HomeActivity.class));

                }else if(menuItem.getTitle().equals("Map Task")){

                    mDrawerLayout.closeDrawers();

                }else if(menuItem.getTitle().equals("About Us")){
                    

                }else if(menuItem.getTitle().equals("Contact List")){


                }else if(menuItem.getTitle().equals("Update Profile")){


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
                        startActivity(new Intent(MapTaskActivity.this, LoginActivity.class));
                    }
                });
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
}
