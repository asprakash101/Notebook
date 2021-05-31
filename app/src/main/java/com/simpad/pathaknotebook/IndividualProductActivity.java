package com.simpad.pathaknotebook;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.simpad.pathaknotebook.NetwokSyncs.UserSession;
import com.simpad.pathaknotebook.adapters.SimilarNoteBooksAdapter;
import com.simpad.pathaknotebook.models.NotebookData;

import java.util.ArrayList;
import java.util.List;

public class IndividualProductActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "IndividualProductActivity";
    private ImageView productImage,back,like,likeFull;
    private Button decrement,increment;
    private DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    ImageView closeDrawer,openDrawer;
    private EditText quantity;
    private TextView productName, productPrize, pageName, productDes,wishList,addToCart,buyNow;
    private ProgressBar productImageProgress;
    private RecyclerView recyclerView;
    private DatabaseReference mRef;
    private FirebaseDatabase database;
    private List<NotebookData> notebook;
    private NotebookData notebookData;
    private LinearLayout wishListButton,similar,share;
    private ScrollView scrollView;
    FirebaseUser user;
    private TextView emailId,displayNameNav;
    private UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_product);
        Bundle bundle = getIntent().getBundleExtra("Product");
        notebookData = (NotebookData) bundle.getSerializable("product");
        final Boolean isItFav = bundle.getBoolean("isFav");
        initialize();
        initializeDrawer();
        toolbar = findViewById(R.id.mytoolbar);
        userSession = new UserSession(IndividualProductActivity.this);
        View headerView = (View) navigationView.getHeaderView(0);
        emailId = headerView.findViewById(R.id.emailDisplay);
        displayNameNav = headerView.findViewById(R.id.displayName);
        displayNameNav.setText(user.getDisplayName());
        emailId.setText(user.getEmail());
        closeDrawer = headerView.findViewById(R.id.closeNavDrawer);
        pageName.setText("Your Cart");
        buyNow = findViewById(R.id.buy_now);
        openDrawer = findViewById(R.id.navDrawer);
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
        Glide.with(this).load(notebookData.getImgUrl()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                productImageProgress.setVisibility(View.GONE);
                return false;
            }
        }).into(productImage);
        productName.setText(notebookData.getItem());
        productPrize.setText("Rs. " + notebookData.getSalePerPcs());
        pageName.setText(notebookData.getItem());
        if (isItFav){
            like.setVisibility(View.INVISIBLE);
            likeFull.setVisibility(View.VISIBLE);
            wishList.setText("WishListed");
        }
        else {
            like.setVisibility(View.VISIBLE);
            likeFull.setVisibility(View.INVISIBLE);
        }
        String pages, design;
        pages = notebookData.getDes1();
        if (notebookData.getDes2().equals("SL"))
            design = "Single Line";
        else if (notebookData.getDes2().equals("DL"))
            design = "Double Line";
        else if (notebookData.getDes2().equals("4L"))
            design = "Double Line";
        else if (notebookData.getDes2().equals("Four Line"))
            design = "Double Line";
        else if (notebookData.getDes2().equals("BSL"))
            design = "Broad Single Line For Kids";
        else if (notebookData.getDes2().equals("B4L"))
            design = "Broad Four Line For Kids";
        else if (notebookData.getDes2().equals("3 in 1"))
            design = "3 in 1";
        else if (notebookData.getDes2().equals("SLP"))
            design = "SLP";
        else if (notebookData.getDes2().equals("Plain"))
            design = "Plain";
        else if (notebookData.getDes2().equals("Check")) {
            if (notebookData.getDes3().equals("10 mm"))
                design = "Check with 10 mm";
            else
                design = "Check with 20 mm";
        } else design = "Design Not Available";
        productDes.setText("\u2022 " + design + "\n" + "\u2022 " + pages + "\n" + "\u2022 " + "Binding Type " + notebookData.getDes4() + "\n" + "\u2022 " + notebookData.getSize() + "mm"+"\n"+"\u2022 " + "60 gsm Paper Quality");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantityNum = Integer.parseInt(quantity.getText().toString());
                quantityNum ++;
                quantity.setText(String.valueOf(quantityNum));
            }
        });
        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantityNum = Integer.parseInt(quantity.getText().toString());
                quantityNum --;
                quantity.setText(String.valueOf(quantityNum));
            }
        });
        mRef.child("user").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.child("cart").getChildren()){
                    if(snapshot1.child("serialNumber").getValue().equals(notebookData.getSerialNumber())){
                        addToCart.setText("Added");
                        addToCart.setClickable(false);
                    }
                    Log.d(TAG, "onDataChange: "+snapshot1.getChildrenCount());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        similar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like.setVisibility(View.INVISIBLE);
                likeFull.setVisibility(View.VISIBLE);
                wishList.setText("WishListed");
                Snackbar.make(v,"Added to Favourites",Snackbar.LENGTH_SHORT).show();
                mRef.child("user").child(user.getUid()).child("favourites").child(notebookData.getSerialNumber()).setValue(notebookData);
            }
        });
        likeFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like.setVisibility(View.VISIBLE);
                likeFull.setVisibility(View.INVISIBLE);
                wishList.setText("WishList");
                Query removeFav = mRef.child("user").child(user.getUid()).child("favourites").orderByChild("serialNumber").equalTo(notebookData.getSerialNumber());
                removeFav.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Log.d("TAG", "onDataChange: "+dataSnapshot.getValue());
                            dataSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = IndividualProductActivity.this.getPackageName();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "I have bought these amazing,easy to use notebooks from an awesome app called " + appPackageName+" Interested people can take look at it, and order it from here");
                sendIntent.setType("text/plain");
                IndividualProductActivity.this.startActivity(sendIntent);
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.child("user").child(user.getUid()).child("cart").child(notebookData.getSerialNumber()).setValue(notebookData);
                mRef.child("user").child(user.getUid()).child("cart").child(notebookData.getSerialNumber()).child("quantity").setValue(quantity.getText().toString());
                Snackbar.make(v,"Added to Cart",Snackbar.LENGTH_SHORT).show();
                addToCart.setText("Added");
                addToCart.setClickable(false);
            }


        });
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addToCart.getText().equals("Added")){
                    Intent intent = new Intent(IndividualProductActivity.this,CartActivity.class);
                    startActivity(intent);
                }
                else {
                    mRef.child("user").child(user.getUid()).child("cart").child(notebookData.getSerialNumber()).setValue(notebookData);
                    mRef.child("user").child(user.getUid()).child("cart").child(notebookData.getSerialNumber()).child("quantity").setValue(quantity.getText().toString());
                    Snackbar.make(v,"Added to Cart",Snackbar.LENGTH_SHORT).show();
                    addToCart.setText("Added");
                    addToCart.setClickable(false);
                    Intent intent = new Intent(IndividualProductActivity.this,CartActivity.class);
                    startActivity(intent);
                }
            }
        });

        getProduct();
    }

    private void initialize() {
        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productPrize = findViewById(R.id.productPrice);
        productImageProgress = findViewById(R.id.productImageProgress);
        productImageProgress.setVisibility(View.VISIBLE);
        pageName = findViewById(R.id.pageName);
        productDes = findViewById(R.id.productdesc);
        recyclerView = findViewById(R.id.similarProducts);
        notebook = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();
        back = findViewById(R.id.back);
        like= findViewById(R.id.like);
        wishList = findViewById(R.id.text_action3);
        decrement = findViewById(R.id.decrementQuantity);
        increment = findViewById(R.id.incrementQuantity);
        quantity = findViewById(R.id.quantityProductPage);
        addToCart = findViewById(R.id.add_to_cart);
        user = FirebaseAuth.getInstance().getCurrentUser();
        wishListButton = findViewById(R.id.layout_action3);
        similar = findViewById(R.id.layout_action2);
        share = findViewById(R.id.layout_action1);
        likeFull = findViewById(R.id.likeFull);
        scrollView = findViewById(R.id.individualScrollView);
    }

    private void getProduct() {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.child("notebooks").getChildren()) {
                    NotebookData notebookData1 = (NotebookData) dataSnapshot.getValue(NotebookData.class);

                    if(notebookData1.getSize().equals(notebookData.getSize()) || notebookData1.getDes2().equals(notebookData.getDes2())){
                       // Log.d(TAG, "onDataChange: "+productData1.getSize());
                        notebook.add(notebookData1);
                    }



                }
                SimilarNoteBooksAdapter similarNoteBooksAdapter = new SimilarNoteBooksAdapter(notebook,IndividualProductActivity.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(IndividualProductActivity.this,RecyclerView.HORIZONTAL,false));
                recyclerView.setAdapter(similarNoteBooksAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: " + error.getMessage());
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
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_cart:{
                Intent intent = new Intent(IndividualProductActivity.this,CartActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_favourite:{
                Intent intent = new Intent(IndividualProductActivity.this,WhishlistActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_account:{
                Intent intent = new Intent(IndividualProductActivity.this,AccountActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_info:{
                Intent intent = new Intent(IndividualProductActivity.this,AboutActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_help:{
                Intent intent = new Intent(IndividualProductActivity.this,HelpActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_share:{
                final String appPackageName = IndividualProductActivity.this.getPackageName();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "I have bought these amazing,easy to use notebooks from an awesome app called " + appPackageName+" Interested people can take look at it, and order it from here");
                sendIntent.setType("text/plain");
                IndividualProductActivity.this.startActivity(sendIntent);
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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(IndividualProductActivity.this);

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
                        Intent intent = new Intent(IndividualProductActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();



        // show it
        alertDialog.show();
    }
}