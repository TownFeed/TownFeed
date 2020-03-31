package com.townfeednews.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.provider.Telephony;
import android.util.Log;
import android.view.WindowManager;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.townfeednews.R;
import com.townfeednews.utils.AppConstant;
import com.townfeednews.utils.AppPrefsMain;

import java.net.InetAddress;
import java.util.List;

public class SplashScreenMain extends AppCompatActivity {
    private FirebaseAnalytics firebaseAnalytics;
    private static final String TAG = "SplashScreenMain";
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_main);
        checkInternetConnection();
        getSupportActionBar().hide();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        AppConstant.deviceId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        // testing start
        Log.d(TAG, "onCreate: Splash Screen Main : " + Telephony.Sms.getDefaultSmsPackage(this));
        // testing eng here
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AppPrefsMain.isUserLoggedIn(SplashScreenMain.this)) {
                    startActivity(new Intent(SplashScreenMain.this, HomeActivityMain.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashScreenMain.this, IntroSliderActivity.class));
                    finish();
                }

            }
        }, 3000);
    }

    private void checkInternetConnection() {
        if (isInternetAvailable()) {
            AppConstant.isInternetConnected = true;
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkInternetConnection();
                    Log.d("TAG", "run: check Internet connection");
                }
            }, 1000);
            AppConstant.isInternetConnected = false;
        }
    }

    public boolean isInternetAvailable() {
        try {
            final String command = "ping -c 1 google.com";
            return Runtime.getRuntime().exec(command).waitFor() == 0;
        } catch (Exception e) {
            return false;
        }
    }
}
