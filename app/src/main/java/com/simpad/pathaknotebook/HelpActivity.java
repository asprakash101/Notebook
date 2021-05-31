package com.simpad.pathaknotebook;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.simpad.pathaknotebook.NetwokSyncs.UserSession;

public class HelpActivity extends AppCompatActivity implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener {

    private RelativeLayout phoneNumber,email,workAddress,sendEmail;
    private ImageView getmoreProducts,getMoreProducts1,getMoreProducts2,getMoreProducts3;
    private TextView pageName;
    FirebaseUser user;
    private DrawerLayout drawer;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    ImageView closeDrawer,openDrawer,moreProducts,heart,cart,account;
    NavigationView navigationView;
    TextView emailId,displayName;
    ImageView back;
    UserSession userSession;
    Button sendEmailButton;
    EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        initialise();
        initializeDrawer();
        userSession = new UserSession(HelpActivity.this);
        getmoreProducts.setOnClickListener(this);
        getMoreProducts1.setOnClickListener(this);
        getMoreProducts2.setOnClickListener(this);
        getMoreProducts3.setOnClickListener(this);
        View headerView = (View) navigationView.getHeaderView(0);
        closeDrawer = headerView.findViewById(R.id.closeNavDrawer);
        emailId = headerView.findViewById(R.id.emailDisplay);
        displayName = headerView.findViewById(R.id.displayName);
        openDrawer = findViewById(R.id.navDrawer);
        displayName.setText(user.getDisplayName());
        emailId.setText(user.getEmail());
        moreProducts = findViewById(R.id.moreProducts);
        Log.d("TAG", "onCreate: "+user.getUid());

        closeDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.RIGHT);
            }
        });


        openDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.RIGHT);
            }
        });
        message.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0){
                    sendEmailButton.setEnabled(false);
                }
                else sendEmailButton.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!message.getText().toString().isEmpty()){
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto","pathakprint@gmail.com", null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feed Back Regarding Print Pathak App");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, message.getText().toString());
                    startActivity(Intent.createChooser(emailIntent, "Send Feedback"));
                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initialise() {
        getmoreProducts = findViewById(R.id.moreProducts);
        getMoreProducts1 = findViewById(R.id.moreProducts1);
        getMoreProducts2 = findViewById(R.id.moreProducts2);
        getMoreProducts3 = findViewById(R.id.moreProducts3);
        phoneNumber = findViewById(R.id.phoneNumberRel);
        email = findViewById(R.id.emailRel);
        workAddress = findViewById(R.id.workAddressRel);
        sendEmail = findViewById(R.id.sendEmailRel);
        phoneNumber.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        workAddress.setVisibility(View.GONE);
        sendEmail.setVisibility(View.GONE);
        pageName = findViewById(R.id.pageName);
        pageName.setText("Help");
        navigationView = findViewById(R.id.nav_view);
        user = FirebaseAuth.getInstance().getCurrentUser();
        sendEmailButton = findViewById(R.id.send);
        message = findViewById(R.id.message);
        back = findViewById(R.id.back);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.moreProducts : if(phoneNumber.getVisibility()==View.VISIBLE){
                phoneNumber.setVisibility(View.GONE);
                break;
            }
            else{
                phoneNumber.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.moreProducts1 : if(email.getVisibility()==View.VISIBLE){
                email.setVisibility(View.GONE);
                break;
            }
            else{
                email.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.moreProducts2 : if(workAddress.getVisibility()==View.VISIBLE){
                workAddress.setVisibility(View.GONE);
                break;
            }
            else{
                workAddress.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.moreProducts3 : if(sendEmail.getVisibility()==View.VISIBLE){
                sendEmail.setVisibility(View.GONE);
            }
            else{
                sendEmail.setVisibility(View.VISIBLE);
            }
                break;
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_cart:{
                Intent intent = new Intent(HelpActivity.this,CartActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_favourite:{
                Intent intent = new Intent(HelpActivity.this,WhishlistActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_account:{
                Intent intent = new Intent(HelpActivity.this,AccountActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_info:{
                Intent intent = new Intent(HelpActivity.this,AboutActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.nav_help:{
                Intent intent = new Intent(HelpActivity.this,HelpActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_share:{
                final String appPackageName = HelpActivity.this.getPackageName();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "I have bought these amazing,easy to use notebooks from an awesome app called " + appPackageName+" Interested people can take look at it, and order it from here");
                sendIntent.setType("text/plain");
                HelpActivity.this.startActivity(sendIntent);
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
    private void initializeDrawer() {
        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(1).setChecked(true);
        toggle.syncState();
    }
    public void confirm() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HelpActivity.this);

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
                        Intent intent = new Intent(HelpActivity.this,LoginActivity.class);
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