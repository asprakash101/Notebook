package com.simpad.pathaknotebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.simpad.pathaknotebook.NetwokSyncs.UserSession;

public class SplashActivity extends AppCompatActivity {

    private UserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        session = new UserSession(SplashActivity.this);


        int SPLASH_TIME_OUT = 5000;

        YoYo.with(Techniques.FlipInX)
                .duration(4000)
                .playOn(findViewById(R.id.logo));


        YoYo.with(Techniques.Bounce)
                    .duration(7000)
                    .playOn(findViewById(R.id.logo));

        YoYo.with(Techniques.FadeInUp)
                    .duration(5000)
                    .playOn(findViewById(R.id.appname));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,WelcomeActivity.class));
                finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }