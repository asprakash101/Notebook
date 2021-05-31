package com.simpad.pathaknotebook;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.simpad.pathaknotebook.NetwokSyncs.UserSession;
import com.simpad.pathaknotebook.adapters.OnBoardingAdapter;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private TextView[] mDots;
    private Button next,skip;
    private int currentPage;
    private UserSession pref;


    private OnBoardingAdapter onBoardingAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        next = findViewById(R.id.btn_next);
        skip = findViewById(R.id.btn_skip);

        pref = new UserSession(WelcomeActivity.this);
        if(!pref.isFirstTimeLaunch()){
            launchHomeScreen();
            finish();
        }

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);
        onBoardingAdapter = new OnBoardingAdapter(this);

        viewPager.setAdapter(onBoardingAdapter);
        addDots(0);

        viewPager.setOnPageChangeListener(pageChangeListener);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPage != mDots.length-1)
                    viewPager.setCurrentItem(currentPage+1);
                else{
                    launchHomeScreen();
                }
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               launchHomeScreen();
            }
        });

    }

    public void addDots(int position){
        mDots = new TextView[onBoardingAdapter.getCount()];
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

        if(position == mDots.length-1)
            next.setText(R.string.Finish);





    }

    private void launchHomeScreen() {
        pref.setFirstTimeLaunch(false);
        if(pref.isLoggedIn()) {
            Intent i = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(i);
        }else{
            Intent i = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(i);
        }
        finish();
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

}
