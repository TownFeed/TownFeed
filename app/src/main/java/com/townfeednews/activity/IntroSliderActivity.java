package com.townfeednews.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.townfeednews.R;
import com.townfeednews.adapterOld.IntroSliderPagerAdapter;
import com.townfeednews.utils.AppConstant;

import me.relex.circleindicator.CircleIndicator;

public class IntroSliderActivity extends AppCompatActivity {

    private ViewPager introSliderViewPager;
    private Button btnLogin, btnSkip;
    private IntroSliderPagerAdapter introSliderPagerAdapter;
    private CircleIndicator indicator;
//    private int currentPage = 0;
//    private boolean pagerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_slider);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initView();

    }

    private void initView() {
        introSliderViewPager = findViewById(R.id.introSliderViewPager);
        btnLogin = findViewById(R.id.btnLogin);
        btnSkip = findViewById(R.id.btnSkip);
        introSliderPagerAdapter = new IntroSliderPagerAdapter(getSupportFragmentManager());
        introSliderViewPager.setAdapter(introSliderPagerAdapter);
        indicator = findViewById(R.id.indicator);
        indicator.createIndicators(4, 0);
        indicator.setViewPager(introSliderViewPager);
        introSliderPagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());

//        nextViewPager(currentPage);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IntroSliderActivity.this, SocialLoginAndLanguage.class));
                finish();
                // TODO: 24-12-2019 open login activity and mark user as logged in don't show intro slider again.
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstant.userProfileUpdated = true;
                startActivity(new Intent(IntroSliderActivity.this, ChooseNewsLanguageActivity.class));
                finish();
            }
        });

    }

//    private void nextViewPager(int currentPage) {
//
//        if (currentPage < 4 && !pagerRunning) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            },2000);
//        }
//    }
}
