package com.townfeednews.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.townfeednews.R;
import com.townfeednews.connection.ApiInterface;
import com.townfeednews.connection.BaseUrlOld;
import com.townfeednews.model.newsOld.NewsResponseData;
import com.townfeednews.roomDBOld.TownFeedAppDB;
import com.townfeednews.utils.AppConstant;
import com.townfeednews.utils.AppPrefs;
import com.townfeednews.utils.AppUtils;
import com.townfeednews.utils.UserProfilePrefs;

import java.security.MessageDigest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.townfeednews.utils.AppConstant.newsArrayList;

public class SplashScreen extends AppCompatActivity {

    private TextView hindiTextView, englishTextView;
    private static final String TAG = "SplashScreen";
    public static final int MULTIPLE_PERMISSIONS = 10;

    @SuppressLint({"HardwareIds", "SourceLockedOrientationActivity"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        AppConstant.deviceId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        AppConstant.townFeedAppDB = Room.databaseBuilder(getApplicationContext(), TownFeedAppDB.class, "userdb").build();
        AppConstant.selectedCategories.clear();
        AppUtils.getCategoryInfo(AppPrefs.getUserLanguage(SplashScreen.this));

        //get deeplink data
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            Log.d(TAG, "onSuccess: DeepLink " + deepLink);
                            try {
                                String referLink = deepLink.toString();
                                referLink = referLink.substring(referLink.lastIndexOf("=") + 1);
                                Log.d(TAG, "onSuccess: referLink " + referLink);
                            } catch (Exception e) {

                            }


                        }


                        // Handle the deep link. For example, open the linked
                        // content, or apply promotional credit to the user's
                        // account.
                        // ...

                        // ...
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "getDynamicLink:onFailure", e);
                    }
                });

        UserProfilePrefs.setSelectedCategories(SplashScreen.this, "");
        if (AppPrefs.getNightMode(SplashScreen.this)) {
//            AppPrefs.setNightMode(SplashScreen.this, true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
//            AppPrefs.setNightMode(SplashScreen.this, false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }


        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

//        initView();

        Log.d(TAG, "onCreate device Id: " + AppConstant.deviceId);
        getSupportActionBar().hide();

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d(TAG, "KeyHash: " + Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));

                //debug keyhash p3sS1vg5Khr6sezK9Y0OZuuCEb0=
                // TODO: 14-11-2019 to generate release KeyHash change build variants in left of IDE and run project
                // TODO: 14-11-2019 logd print KeyHash for release (Note: App Release credentials required)
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception : " + e.getMessage());
        }

        if (AppPrefs.isFirstTimeUser(SplashScreen.this)) {
            startActivity(new Intent(SplashScreen.this, SocialLoginAndLanguage.class));
            finish();
        } else {
            ApiInterface retrofit = BaseUrlOld.getRetrofit().create(ApiInterface.class);
//            retrofit.getNewsData().enqueue(new Callback<NewsResponseData>() {
//                @Override
//                public void onResponse(Call<NewsResponseData> call, Response<NewsResponseData> response) {
//                    Log.d("TAG", "onResponse: News Data" + response);
//                    if (response.isSuccessful()) {
//                        newsArrayList = response.body().getNews();
//                        startActivity(new Intent(SplashScreen.this, HomeActivityOld.class));
//                        finish();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<NewsResponseData> call, Throwable t) {
//                    // TODO: 03-12-2019 asign room data to newsArrayList
////                    Toast.makeText(SplashScreen.this, "Oops ! Something went wrong with Internet.", Toast.LENGTH_SHORT).show();
//                }
//            });
        }
    }
}
