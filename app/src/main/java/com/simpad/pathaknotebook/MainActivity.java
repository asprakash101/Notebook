package com.simpad.pathaknotebook;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simpad.pathaknotebook.NetwokSyncs.CheckInternetConnection;
import com.simpad.pathaknotebook.NetwokSyncs.UserSession;
import com.simpad.pathaknotebook.adapters.BannerAdapter;
import com.simpad.pathaknotebook.adapters.MoreAdapter;
import com.simpad.pathaknotebook.adapters.NoteBooksAdapter;
import com.simpad.pathaknotebook.adapters.SimilarNoteBooksAdapter;
import com.simpad.pathaknotebook.models.Banner;
import com.simpad.pathaknotebook.models.MoreProducts;
import com.simpad.pathaknotebook.models.NotebookData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "MainActivity" ;
    private ViewPager viewPager;
    private RecyclerView recyclerView,recyclerViewRecent,moreProductsRecyclerView;
    private LinearLayout dotsLayout;
    private TextView[] mDots;
    private int currentPage;
    UserSession userSession;
    FirebaseUser user;
    private DrawerLayout drawer;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    ImageView closeDrawer,openDrawer,moreProducts,heart,cart,account;
    NavigationView navigationView;
    TextView emailId,displayName;
    private DatabaseReference mRef;
    private FirebaseDatabase database;
    private List<NotebookData> notebooks,popular,notebook,favourites,cartProducts;
    private List<MoreProducts> products;
    BannerAdapter bannerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeDrawer();

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#00948f"));
        }

        new CheckInternetConnection(this).checkConnection();

        moreProductsRecyclerView = findViewById(R.id.moreProductsRecyclerView);
        userSession = new UserSession(MainActivity.this);
        recyclerView = findViewById(R.id.recyclerview_best_deals);
        recyclerViewRecent = findViewById(R.id.recyclerview_recently_added);
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> size = new ArrayList<>();
        products = new ArrayList<>();
        dotsLayout = findViewById(R.id.layoutDots);
        final ArrayList<Float> prize = new ArrayList<>();
        navigationView = findViewById(R.id.nav_view);
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();
        notebooks = new ArrayList<>();
        notebook = new ArrayList<>();
        popular = new ArrayList<>();
        favourites = new ArrayList<>();
        cartProducts = new ArrayList<>();
        heart = findViewById(R.id.heart);
        cart = findViewById(R.id.cart);
        account= findViewById(R.id.view_profile);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AccountActivity.class);
                startActivity(intent);
            }
        });

        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,WhishlistActivity.class);
                startActivity(intent);
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CartActivity.class);
                startActivity(intent);
            }
        });

        View headerView = (View) navigationView.getHeaderView(0);
        closeDrawer = headerView.findViewById(R.id.closeNavDrawer);
        emailId = headerView.findViewById(R.id.emailDisplay);
        displayName = headerView.findViewById(R.id.displayName);
        openDrawer = findViewById(R.id.navDrawer);
        displayName.setText(user.getDisplayName());
        emailId.setText(user.getEmail());
        moreProducts = findViewById(R.id.moreProducts);
        Log.d(TAG, "onCreate: "+user.getUid());

        closeDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        moreProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AllProductsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("AllProducts", (Serializable) notebooks);
                bundle.putSerializable("AllFav", (Serializable) favourites);
                bundle.putSerializable("AllCart", (Serializable) cartProducts);
                intent.putExtra("AllProducts",bundle);
                startActivity(intent);
            }
        });

        openDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        toolbar = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);

        Banner banner1 = new Banner("https://printpathak.com/uploads/gallery_images/image_large/1587638900cover.png","Pathak NoteBook");
        Banner banner2 = new Banner("https://cdn.pixabay.com/photo/2015/12/08/08/47/business-card-1082665_960_720.png","Poster, Hand Bill, Visiting Card, Letter Head & Other");
        Banner banner3 = new Banner("https://get.pxhere.com/photo/ornate-playful-ornaments-purple-pink-pattern-flourishes-butterflies-flowers-decorative-festive-card-congratulation-postcard-wedding-birthday-heartfelt-romantic-copy-space-blank-background-invitation-greeting-card-texture-love-blue-sky-violet-computer-wallpaper-branch-organism-tree-wallpaper-screenshot-space-petal-art-illustration-flower-visual-arts-fractal-art-graphics-font-1444299.jpg","Marriage Card");

        ArrayList<Banner> banners = new ArrayList<>();
        banners.add(banner1);
        banners.add(banner2);
        banners.add(banner3);

        viewPager = findViewById(R.id.banner);
        bannerAdapter = new BannerAdapter(banners,this);
        addDots(0);
        viewPager.setOnPageChangeListener(pageChangeListener);
        viewPager.setAdapter(bannerAdapter);








    }

    private void getProduct() {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.child("notebooks").getChildren()){
                    NotebookData notebookData = (NotebookData) dataSnapshot.getValue(NotebookData.class);
                    notebooks.add(notebookData);
                    Log.d(TAG, "onDataChange: "+ notebookData.getSerialNumber());

                    //
                }
                notebook.clear();
                popular.clear();
                for (int i =0;i<10;i++){
                    NotebookData notebookData = notebooks.get(i);
                    notebook.add(notebookData);
                }
                for (int i = notebooks.size()-10;i<notebooks.size();i++){
                    NotebookData notebookData = notebooks.get(i);
                    popular.add(notebookData);
                }

                mRef.child("user").child(user.getUid()).child("favourites").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        favourites.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                            NotebookData notebookData = (NotebookData) dataSnapshot.getValue(NotebookData.class);
                            Log.d(TAG, "onDataChange: "+ notebookData.getSerialNumber());
                            favourites.add(notebookData);
                        }

                        mRef.child("user").child(user.getUid()).child("cart").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                cartProducts.clear();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    NotebookData notebookData = (NotebookData) dataSnapshot.getValue(NotebookData.class);
                                    Log.d(TAG, "onDataChange: "+ notebookData.getSerialNumber());
                                    cartProducts.add(notebookData);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        NoteBooksAdapter noteBooksAdapter = new NoteBooksAdapter(notebook,favourites,cartProducts,MainActivity.this,user);
                        SimilarNoteBooksAdapter similarNoteBooksAdapter = new SimilarNoteBooksAdapter(popular,favourites,cartProducts,MainActivity.this,user);

                        noteBooksAdapter.notifyDataSetChanged();
                        similarNoteBooksAdapter.notifyDataSetChanged();

                        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
                        recyclerView.setAdapter(noteBooksAdapter);

                        recyclerViewRecent.setLayoutManager(new LinearLayoutManager(MainActivity.this,RecyclerView.HORIZONTAL,true));
                        recyclerViewRecent.setAdapter(similarNoteBooksAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: "+error.getMessage());
            }
        });

        mRef.child("extraProducts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                products.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    products.add(dataSnapshot.getValue(MoreProducts.class));
                    Log.d(TAG, "onDataChange: "+dataSnapshot.getValue(MoreProducts.class).getProductName());
                }
                MoreAdapter adapter = new MoreAdapter(products,MainActivity.this);
                moreProductsRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this,RecyclerView.HORIZONTAL,false));
                moreProductsRecyclerView.setAdapter(adapter);
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
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(1).setChecked(true);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new CheckInternetConnection(this).checkConnection();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getProduct();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_cart:{
                Intent intent = new Intent(MainActivity.this,CartActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_favourite:{
                Intent intent = new Intent(MainActivity.this,WhishlistActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_account:{
                Intent intent = new Intent(MainActivity.this,AccountActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_info:{
                Intent intent = new Intent(MainActivity.this,AboutActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_all_notebook:{
                Intent intent = new Intent(MainActivity.this,AllProductsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("AllProducts", (Serializable) notebooks);
                bundle.putSerializable("AllFav", (Serializable) favourites);
                bundle.putSerializable("AllCart", (Serializable) cartProducts);
                intent.putExtra("AllProducts",bundle);
                startActivity(intent);
                break;
            }
            case R.id.nav_help:{
                Intent intent = new Intent(MainActivity.this,HelpActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_share:{
                final String appPackageName = MainActivity.this.getPackageName();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "I have bought these amazing,easy to use notebooks from an awesome app called " + appPackageName+" Interested people can take look at it, and order it from here");
                sendIntent.setType("text/plain");
                MainActivity.this.startActivity(sendIntent);
                break;
            }
            case R.id.nav_logOut:{
                confirm();
                break;
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void addDots(int position){
        mDots = new TextView[bannerAdapter.getCount()];
        dotsLayout.removeAllViews();
        for (int i=0;i<mDots.length;i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;",1));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            dotsLayout.addView(mDots[i]);

        }
        if(mDots.length>0){
            mDots[position].setTextColor(Color.parseColor("#ffffff"));
        }







    }
    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            currentPage = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    public void confirm() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

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
                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
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