package com.simpad.pathaknotebook;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.simpad.pathaknotebook.NetwokSyncs.UserSession;

public class AccountActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView displayName,emailDisplay,pageName;
    private TextInputEditText nameText,emailText,phoneNumberText;
    private Button share,continueShopping;
    private Context context;
    private DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    ImageView closeDrawer,openDrawer,back;
    private TextView displayNameNav,emailId;
    private UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        initialise();
        initializeDrawer();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        userSession = new UserSession(AccountActivity.this);
        displayName.setText(user.getDisplayName());
        emailDisplay.setText(user.getEmail());
        pageName.setText("Profile");
        nameText.setText(user.getDisplayName());
        emailText.setText(user.getEmail());
        phoneNumberText.setText(user.getPhoneNumber());
        toolbar = findViewById(R.id.mytoolbar);
        View headerView = (View) navigationView.getHeaderView(0);
        emailId = headerView.findViewById(R.id.emailDisplay);
        displayNameNav = headerView.findViewById(R.id.displayName);
        displayNameNav.setText(user.getDisplayName());
        emailId.setText(user.getEmail());
        closeDrawer = headerView.findViewById(R.id.closeNavDrawer);
        openDrawer = findViewById(R.id.navDrawer);
        closeDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.RIGHT);
            }
        });
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        openDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.RIGHT);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = context.getPackageName();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "I have bought these amazing,easy to use notebooks from an awesome app called " + appPackageName+" Interested people can take look at it, and order it from here");
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);
            }
        });
        continueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initialise() {
        displayName = findViewById(R.id.displayName);
        emailDisplay = findViewById(R.id.emailDisplay);
        pageName = findViewById(R.id.pageName);
        nameText = findViewById(R.id.nameText);
        emailText = findViewById(R.id.emailText);
        phoneNumberText = findViewById(R.id.phoneNumberText);
        share = findViewById(R.id.share);
        continueShopping = findViewById(R.id.continueShopping);
        context = AccountActivity.this;

    }
    private void initializeDrawer() {
        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        toggle.syncState();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_cart:{
                Intent intent = new Intent(context,CartActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_favourite:{
                Intent intent = new Intent(context,WhishlistActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_account:{
                Intent intent = new Intent(context,AccountActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_info:{
                Intent intent = new Intent(AccountActivity.this,AboutActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_help:{
                Intent intent = new Intent(AccountActivity.this,HelpActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_share:{
                final String appPackageName = AccountActivity.this.getPackageName();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "I have bought these amazing,easy to use notebooks from an awesome app called " + appPackageName+" Interested people can take look at it, and order it from here");
                sendIntent.setType("text/plain");
                AccountActivity.this.startActivity(sendIntent);
                break;
            }
            case R.id.nav_logOut:{
                confirm();
                break;
            }
        }
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    private void confirm() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccountActivity.this);

        // set title

        alertDialogBuilder.setTitle("Log Out");
        alertDialogBuilder.setIcon(R.drawable.ic_baseline_exit_to_app_24);

        // set dialog message
        alertDialogBuilder
                .setMessage("Do you really want to log out")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        FirebaseAuth.getInstance().signOut();
                        userSession.logoutUser();
                        Intent intent = new Intent(AccountActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();



        // show it
        alertDialog.show();
    }
}