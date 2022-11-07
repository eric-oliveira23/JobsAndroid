package com.eric.jobs.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.eric.jobs.adapter.MyViewPagerAdapter;
import com.eric.jobs.R;
import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends AppCompatActivity {

    private boolean doubleBackPressed = false;
    private Animation main;
    private TabLayout tabLayout;
    private boolean loaded = false;
    TextView txvLoading;
    private LottieAnimationView animLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txvLoading = findViewById(R.id.txvLoading);
        animLoading = findViewById(R.id.animLoading);

        main = AnimationUtils.loadAnimation(this, R.anim.anim_home);
        tabLayout = findViewById(R.id.tabLayout);

        ViewPager2 viewPager2;
        MyViewPagerAdapter myViewPagerAdapter;

        //configuração fragments

        viewPager2 = findViewById(R.id.view_pager);
        myViewPagerAdapter = new MyViewPagerAdapter(this);
        viewPager2.setAdapter(myViewPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
                int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.selected_color);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.unselected_color);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

        //desativa slide entre fragments
        viewPager2.setUserInputEnabled(false);
    }

    @Override
    public void onBackPressed() {

       if (doubleBackPressed){
           super.onBackPressed();
           this.finishAffinity();
       }
       this.doubleBackPressed = true;
        Toast.makeText(this,
                "Pressione novamente para sair",
                Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackPressed = false;
            }
        },2000);
    }

    public void hideViews(){
        animLoading.setVisibility(View.GONE);
        txvLoading.setVisibility(View.GONE);
    }

    public void showViews(){
        animLoading.setVisibility(View.VISIBLE);
        txvLoading.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        txvLoading = findViewById(R.id.txvLoading);
        animLoading = findViewById(R.id.animLoading);

        main = AnimationUtils.loadAnimation(this, R.anim.anim_home);
        tabLayout = findViewById(R.id.tabLayout);

        if (!loaded) {
            showViews();
            tabLayout.setVisibility(View.INVISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    tabLayout.setVisibility(View.VISIBLE);
                    hideViews();
                    tabLayout.startAnimation(main);
                    loaded = true;
                }
            }, 2000);
        }
    }
}