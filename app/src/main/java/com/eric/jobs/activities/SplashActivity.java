package com.eric.jobs.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.eric.jobs.R;

public class SplashActivity extends AppCompatActivity {

     TextView txvLogo;
     LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        animationView = findViewById(R.id.animationView);
        txvLogo = findViewById(R.id.txvLogo);

        animationView.animate().translationX(-1600).setDuration(1000).setStartDelay(3000);
        txvLogo.animate().translationX(1600).setDuration(1000).setStartDelay(3500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        },4250);
    }
}