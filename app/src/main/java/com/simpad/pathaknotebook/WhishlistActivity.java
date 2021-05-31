package com.simpad.pathaknotebook;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simpad.pathaknotebook.NetwokSyncs.UserSession;
import com.simpad.pathaknotebook.adapters.NoteBooksAdapter;
import com.simpad.pathaknotebook.models.NotebookData;

import java.util.ArrayList;
import java.util.List;

public class WhishlistActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private static final String TAG = "WishListActivity";
    TextView pageName,emptyText;
    RecyclerView recyclerView;
    private DrawerLayout drawer;
    NavigationView navigationView;
    FirebaseDatabase db;
    DatabaseReference mRef;
    List<NotebookData> favourites;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    ImageView closeDrawer,openDrawer,back;
    FirebaseUser user;
    RelativeLayout relativeLayout,relativeLayoutRecycler;
    LottieAnimationView empty;
    private TextView emailId,displayNameNav;
    private UserSession userSession;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whishlist);
        initialize();
        pageName.setText("WishList");
        initializeDrawer();
        toolbar = findViewById(R.id.mytoolbar);
        View headerView = (View) navigationView.getHeaderView(0);
        emailId = headerView.findViewById(R.id.emailDisplay);
        displayNameNav = headerView.findViewById(R.id.displayName);
        displayNameNav.setText(user.getDisplayName());
        userSession = new UserSession(WhishlistActivity.this);
        emailId.setText(user.getEmail());
        closeDrawer = headerView.findViewById(R.id.closeNavDrawer);
        openDrawer = findViewById(R.id.navDrawer);
        closeDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.RIGHT);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back = findViewById(R.id.back);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }
        });

        openDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.RIGHT);
            }
        });
        mRef.child("user").child(user.getUid()).child("favourites").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favourites.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    NotebookData notebookData = (NotebookData) dataSnapshot.getValue(NotebookData.class);
                    Log.d(TAG, "onDataChange: "+ notebookData.getSerialNumber());
                    favourites.add(notebookData);

                }

                if(favourites.size()>0){
                    relativeLayout.setVisibility(View.INVISIBLE);
                    relativeLayoutRecycler.setVisibility(View.VISIBLE);
                    NoteBooksAdapter noteBooksAdapter = new NoteBooksAdapter(favourites,favourites,WhishlistActivity.this,user);
                    recyclerView.setLayoutManager(new LinearLayoutManager(WhishlistActivity.this));
                    recyclerView.setAdapter(noteBooksAdapter);
                }
                else {
                    relativeLayout.setVisibility(View.VISIBLE);
                    relativeLayoutRecycler.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initialize() {
        pageName = findViewById(R.id.pageName);
        recyclerView = findViewById(R.id.wishListRecyclerView);
        db = FirebaseDatabase.getInstance();
        mRef = db.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        favourites = new ArrayList<>();
        relativeLayout = findViewById(R.id.emptyRelativeLayout);
        empty = findViewById(R.id.emptyLottie);
        emptyText = findViewById(R.id.emptyText);
        relativeLayoutRecycler = findViewById(R.id.relativeLayoutRecycler);
        back = findViewById(R.id.back);
    }
    private void initializeDrawer() {
        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(3).setChecked(true);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_cart:{
                Intent intent = new Intent(WhishlistActivity.this,CartActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_favourite:{
                Intent intent = new Intent(WhishlistActivity.this,WhishlistActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_account:{
                Intent intent = new Intent(WhishlistActivity.this,AccountActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_info:{
                Intent intent = new Intent(WhishlistActivity.this,AboutActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_help:{
                Intent intent = new Intent(WhishlistActivity.this,HelpActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_share:{
                final String appPackageName = WhishlistActivity.this.getPackageName();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "I have bought these amazing,easy to use notebooks from an awesome app called " + appPackageName+" Interested people can take look at it, and order it from here");
                sendIntent.setType("text/plain");
                WhishlistActivity.this.startActivity(sendIntent);
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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WhishlistActivity.this);

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
                        Intent intent = new Intent(WhishlistActivity.this,LoginActivity.class);
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