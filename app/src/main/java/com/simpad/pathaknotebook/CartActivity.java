package com.simpad.pathaknotebook;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simpad.pathaknotebook.NetwokSyncs.UserSession;
import com.simpad.pathaknotebook.adapters.CartAdapter;
import com.simpad.pathaknotebook.models.NotebookData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    ImageView closeDrawer,openDrawer,back;
    private static final String TAG = "CartActivity" ;
    private DatabaseReference mRef;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private List<NotebookData> cartProducts;
    private RecyclerView recyclerView;
    TextView quantityText,pageName;
    Button checkOut;
    private RelativeLayout empty;
    private CardView amountBar;
    private float sum = 0.0f;
    private TextView displayNameNav,emailId;
    public static final String GET_CART_ITEMS = "get_cart_items";
    public static final String GET_SUM   = "get_sum";
    public static final String GET_BUNDLE   = "get_BUNDLE";
    private UserSession userSession;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();
        cartProducts = new ArrayList<>();
        userSession = new UserSession(CartActivity.this);
        recyclerView = findViewById(R.id.cartRecyclerView);
        quantityText = findViewById(R.id.finalPaymentHead);
        pageName = findViewById(R.id.pageName);
        amountBar = findViewById(R.id.card_view);
        empty=findViewById(R.id.emptyRelativeLayout);
        initializeDrawer();
        checkOut = findViewById(R.id.continueToPayment);
        toolbar = findViewById(R.id.mytoolbar);
        View headerView = (View) navigationView.getHeaderView(0);
        emailId = headerView.findViewById(R.id.emailDisplay);
        displayNameNav = headerView.findViewById(R.id.displayName);
        displayNameNav.setText(user.getDisplayName());
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(GET_CART_ITEMS, (Serializable) cartProducts);
                bundle.putFloat(GET_SUM,sum);
                Intent intent1 = new Intent(CartActivity.this,CheckOutActivity.class);
                intent1.putExtra(GET_BUNDLE,bundle);
                startActivity(intent1);
            }
        });
        emailId.setText(user.getEmail());
        closeDrawer = headerView.findViewById(R.id.closeNavDrawer);
        pageName.setText("Your Cart");
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


        mRef.child("user").child(user.getUid()).child("cart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartProducts.clear();
                sum =0.0f;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    NotebookData notebookData = (NotebookData) dataSnapshot.getValue(NotebookData.class);
                    Log.d(TAG, "onDataChange: "+ notebookData.getSerialNumber());
                    cartProducts.add(notebookData);
                    try {
                        sum = sum + (Float.parseFloat(notebookData.getQuantity())*Float.parseFloat(notebookData.getSalePerPcs()));
                    }catch (Exception e){
                        Log.d(TAG, "onDataChange: "+e.getMessage());
                    }
                }
                if(cartProducts.size()>0){
                    empty.setVisibility(View.INVISIBLE);
                    amountBar.setVisibility(View.VISIBLE);
                    CartAdapter adapter = new CartAdapter(cartProducts,CartActivity.this,user);
                    recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
                    recyclerView.setAdapter(adapter);
                    quantityText.setText("\u20B9 "+String.valueOf(sum));
                }
                else{
                    empty.setVisibility(View.VISIBLE);
                    amountBar.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void initializeDrawer() {
        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(2).setChecked(true);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_cart:{
                Intent intent = new Intent(CartActivity.this,CartActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_favourite:{
                Intent intent = new Intent(CartActivity.this,WhishlistActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_account:{
                Intent intent = new Intent(CartActivity.this,AccountActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_info:{
                Intent intent = new Intent(CartActivity.this,AboutActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_help:{
                Intent intent = new Intent(CartActivity.this,HelpActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_share:{
                final String appPackageName = CartActivity.this.getPackageName();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "I have bought these amazing,easy to use notebooks from an awesome app called " + appPackageName+" Interested people can take look at it, and order it from here");
                sendIntent.setType("text/plain");
                CartActivity.this.startActivity(sendIntent);
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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CartActivity.this);

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
                        Intent intent = new Intent(CartActivity.this,LoginActivity.class);
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