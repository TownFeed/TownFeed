package com.townfeednews.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.townfeednews.R;
import com.townfeednews.utils.AppPrefsMain;

public class ChooseNewsLanguageActivity extends AppCompatActivity {
    private Button englishLangButton, hindiLangButton, continueButton;
    private CheckBox adultCheckBox;
    private TextView skipButton;
    private static final String TAG = "ChooseNewsLanguageActiv";

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_news_language);
        getSupportActionBar().hide();

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initView();
    }

    private void initView() {

        englishLangButton = findViewById(R.id.englishLangButton);
        hindiLangButton = findViewById(R.id.hindiLangButton);
        continueButton = findViewById(R.id.continueButton);
        adultCheckBox = findViewById(R.id.adultCheckBox);
        skipButton = findViewById(R.id.skipButton);
//        AppPrefsMain.setUserLanguage(ChooseNewsLanguageActivity.this, "eng");

        if (AppPrefsMain.getUserLanguage(ChooseNewsLanguageActivity.this).equals("eng")){
            hindiLangButton.setBackgroundResource(R.drawable.language_btn_bg_inactive);
            englishLangButton.setBackgroundResource(R.drawable.language_btn_bg_active);
            hindiLangButton.setTextColor(Color.parseColor("#707070"));
            englishLangButton.setTextColor(Color.parseColor("#2680EB"));
            AppPrefsMain.setUserLanguage(ChooseNewsLanguageActivity.this, "eng");
            Log.d(TAG, "chooseLanguage: " + AppPrefsMain.getUserLanguage(ChooseNewsLanguageActivity.this));

        } else {
            hindiLangButton.setBackgroundResource(R.drawable.language_btn_bg_active);
            englishLangButton.setBackgroundResource(R.drawable.language_btn_bg_inactive);
            hindiLangButton.setTextColor(Color.parseColor("#2680EB"));
            englishLangButton.setTextColor(Color.parseColor("#707070"));
//                Log.d(TAG, "chooseLanguage: Hindi");
            AppPrefsMain.setUserLanguage(ChooseNewsLanguageActivity.this, "hi");
            Log.d(TAG, "chooseLanguage: " + AppPrefsMain.getUserLanguage(ChooseNewsLanguageActivity.this));

        }


        if (AppPrefsMain.isUserAdult(ChooseNewsLanguageActivity.this)) {
            adultCheckBox.setChecked(true);
        } else {
            adultCheckBox.setChecked(false);
        }

        adultCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppPrefsMain.setUserAdult(ChooseNewsLanguageActivity.this, true);
                } else {
                    AppPrefsMain.setUserAdult(ChooseNewsLanguageActivity.this, false);
                }
            }
        });
    }

    public void chooseLanguage(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.englishLangButton:
                hindiLangButton.setBackgroundResource(R.drawable.language_btn_bg_inactive);
                englishLangButton.setBackgroundResource(R.drawable.language_btn_bg_active);
                hindiLangButton.setTextColor(Color.parseColor("#707070"));
                englishLangButton.setTextColor(Color.parseColor("#2680EB"));
                AppPrefsMain.setUserLanguage(ChooseNewsLanguageActivity.this, "eng");
                Log.d(TAG, "chooseLanguage: " + AppPrefsMain.getUserLanguage(ChooseNewsLanguageActivity.this));
                break;
            case R.id.hindiLangButton:
                hindiLangButton.setBackgroundResource(R.drawable.language_btn_bg_active);
                englishLangButton.setBackgroundResource(R.drawable.language_btn_bg_inactive);
                hindiLangButton.setTextColor(Color.parseColor("#2680EB"));
                englishLangButton.setTextColor(Color.parseColor("#707070"));
//                Log.d(TAG, "chooseLanguage: Hindi");
                AppPrefsMain.setUserLanguage(ChooseNewsLanguageActivity.this, "hi");
                Log.d(TAG, "chooseLanguage: " + AppPrefsMain.getUserLanguage(ChooseNewsLanguageActivity.this));
                break;
        }

    }

    public void proceedToHomeScreen(View view) {
        startActivity(new Intent(ChooseNewsLanguageActivity.this, HomeActivityMain.class));
        finish();
    }
}
