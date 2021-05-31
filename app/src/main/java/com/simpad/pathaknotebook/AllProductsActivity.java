package com.simpad.pathaknotebook;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
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

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.simpad.pathaknotebook.NetwokSyncs.UserSession;
import com.simpad.pathaknotebook.adapters.NoteBooksAdapter;
import com.simpad.pathaknotebook.models.NotebookData;

import java.util.ArrayList;

public class AllProductsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "AllProductsActivity";
    ArrayList<NotebookData> noteBooks,favourites,cartProducts,searchProducts;
    private TextView pageName;
    private RecyclerView recyclerView;
    private DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    ImageView backButton,closeDrawer,openDrawer;
    Spinner pages,pageDesign;
    String pageSelected,designSelected;
    private TextView displayNameNav,emailId;
    private UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);
        Bundle bundle = getIntent().getBundleExtra("AllProducts");
        assert bundle != null;
        initializeDrawer();
        userSession = new UserSession(AllProductsActivity.this);
        noteBooks = (ArrayList<NotebookData>) bundle.getSerializable("AllProducts");
        favourites = (ArrayList<NotebookData>) bundle.getSerializable("AllFav");
        cartProducts = (ArrayList<NotebookData>) bundle.getSerializable("AllCart");
        searchProducts = new ArrayList<>();
        pageName = findViewById(R.id.pageName);
        pageName.setText("Notebooks");
        pages = findViewById(R.id.pagesSpinner);
        pageDesign = findViewById(R.id.pageDesignSpinner);
        recyclerView = findViewById(R.id.recyclerViewPageName);
        NoteBooksAdapter noteBooksAdapter = new NoteBooksAdapter(noteBooks,favourites,cartProducts,this, FirebaseAuth.getInstance().getCurrentUser());
        noteBooksAdapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(noteBooksAdapter);
        backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar = findViewById(R.id.mytoolbar);
        View headerView = (View) navigationView.getHeaderView(0);
        emailId = headerView.findViewById(R.id.emailDisplay);
        displayNameNav = headerView.findViewById(R.id.displayName);
        displayNameNav.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        emailId.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        closeDrawer = headerView.findViewById(R.id.closeNavDrawer);
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
        pages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pageSelected = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "onItemSelected: "+pageSelected+designSelected);
                getSearch(pageSelected,designSelected);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                pageSelected = parent.getItemAtPosition(1).toString();
                Log.d(TAG, "onNothingSelected: "+pageSelected+designSelected);
            }
        });
        pageDesign.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                designSelected = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "onItemSelected: "+pageSelected+designSelected);
                getSearch(pageSelected,designSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                designSelected = parent.getItemAtPosition(1).toString();
                Log.d(TAG, "onNothingSelected: "+pageSelected+designSelected);

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
                Intent intent = new Intent(AllProductsActivity.this,CartActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_favourite:{
                Intent intent = new Intent(AllProductsActivity.this,WhishlistActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_account:{
                Intent intent = new Intent(AllProductsActivity.this,AccountActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_info:{
                Intent intent = new Intent(AllProductsActivity.this,AboutActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_help:{
                Intent intent = new Intent(AllProductsActivity.this,HelpActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_share:{
                final String appPackageName = AllProductsActivity.this.getPackageName();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "I have bought these amazing,easy to use notebooks from an awesome app called " + appPackageName+" Interested people can take look at it, and order it from here");
                sendIntent.setType("text/plain");
                AllProductsActivity.this.startActivity(sendIntent);
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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AllProductsActivity.this);
        alertDialogBuilder.setTitle("Log Out");
        alertDialogBuilder.setIcon(R.drawable.ic_baseline_exit_to_app_24);
        alertDialogBuilder
                .setMessage("Do you really want to log out")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        FirebaseAuth.getInstance().signOut();
                        userSession.logoutUser();
                        Intent intent = new Intent(AllProductsActivity.this,LoginActivity.class);
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
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    void getSearch(String pageSelected,String designSelected ){
        searchProducts.clear();
        if (pageSelected.equals("No. Of Pages") || designSelected.equals("Page Design") || pageSelected == null || designSelected==null)
        {
            NoteBooksAdapter noteBooksAdapter = new NoteBooksAdapter(noteBooks,favourites,cartProducts,this, FirebaseAuth.getInstance().getCurrentUser());
            recyclerView.setAdapter(noteBooksAdapter);
        }

        else {
            searchProducts.clear();
            for (NotebookData notebookData : noteBooks) {
                if (notebookData.getDes2().equals(designSelected) && notebookData.getDes1().equals(pageSelected))
                    searchProducts.add(notebookData);
            }
            NoteBooksAdapter noteBooksAdapter1 = new NoteBooksAdapter(searchProducts, favourites, cartProducts, this, FirebaseAuth.getInstance().getCurrentUser());
            recyclerView.setAdapter(noteBooksAdapter1);

        }
    }
}