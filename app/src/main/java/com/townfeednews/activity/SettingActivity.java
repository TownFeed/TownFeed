package com.townfeednews.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.townfeednews.R;
import com.townfeednews.utils.AppConstant;
import com.townfeednews.utils.AppPrefsMain;

public class SettingActivity extends AppCompatActivity {

    private TextView langTextView, imgQltyTextView, logoutTextView;
    private Switch nightModeSwitch, notificationSwitch, readNewsSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar = findViewById(R.id.settingToolBar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
    }

    private void initView() {
        langTextView = findViewById(R.id.langTextView);
        imgQltyTextView = findViewById(R.id.imgQltyTextView);
        logoutTextView = findViewById(R.id.logoutTextView);
        nightModeSwitch = findViewById(R.id.nightModeSwitch);
        notificationSwitch = findViewById(R.id.notificationSwitch);
        readNewsSwitch = findViewById(R.id.readNewsSwitch);

        if (AppPrefsMain.isHDImageEnabled(SettingActivity.this)) {
            imgQltyTextView.setText("HD (720 X 1080)");
        } else {
            imgQltyTextView.setText("Medium (460 X 600)");
        }

        if (AppPrefsMain.isUserLoggedIn(SettingActivity.this)) {
            logoutTextView.setVisibility(View.VISIBLE);
        } else {
            logoutTextView.setVisibility(View.GONE);
        }

        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPrefsMain.setUserLoggedIn(SettingActivity.this, false);
                AppPrefsMain.setUserName(SettingActivity.this, "");
                AppPrefsMain.setUserPhone(SettingActivity.this, "");
                AppPrefsMain.setUserGender(SettingActivity.this, "");
                AppConstant.userProfileUpdated = true;
                Toast.makeText(SettingActivity.this, "Successfully Logged Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SettingActivity.this, SplashScreenMain.class));
                finish();
            }
        });

        imgQltyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(SettingActivity.this);
                View view = getLayoutInflater().inflate(R.layout.img_qlty_alert_dialog, null);
                final TextView hdTextView = view.findViewById(R.id.hdTextView);
                final TextView mediumTextView = view.findViewById(R.id.mediumTextView);

                if (AppPrefsMain.isHDImageEnabled(SettingActivity.this)) {
                    hdTextView.setTextColor(ContextCompat.getColor(SettingActivity.this, R.color.dsgn_color_blue));
                    mediumTextView.setTextColor(ContextCompat.getColor(SettingActivity.this, R.color.dsgn_color_grey));
                } else {
                    hdTextView.setTextColor(ContextCompat.getColor(SettingActivity.this, R.color.dsgn_color_grey));
                    mediumTextView.setTextColor(ContextCompat.getColor(SettingActivity.this, R.color.dsgn_color_blue));
                }
                alert.setView(view);

                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(true);

                hdTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hdTextView.setTextColor(ContextCompat.getColor(SettingActivity.this, R.color.dsgn_color_blue));
                        mediumTextView.setTextColor(ContextCompat.getColor(SettingActivity.this, R.color.dsgn_color_grey));
                        AppPrefsMain.setHDImageEnabled(SettingActivity.this, true);
                        imgQltyTextView.setText("HD (720 X 1080)");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                alertDialog.dismiss();
                            }
                        }, 500);
                    }
                });

                mediumTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hdTextView.setTextColor(ContextCompat.getColor(SettingActivity.this, R.color.dsgn_color_grey));
                        mediumTextView.setTextColor(ContextCompat.getColor(SettingActivity.this, R.color.dsgn_color_blue));
                        AppPrefsMain.setHDImageEnabled(SettingActivity.this, false);
                        imgQltyTextView.setText("Medium (460 X 600)");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                alertDialog.dismiss();
                            }
                        }, 500);

                    }
                });

                alertDialog.show();
            }
        });

        langTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstant.userProfileUpdated = true;
                startActivity(new Intent(SettingActivity.this, ChooseNewsLanguageActivity.class));
            }
        });

        if (AppPrefsMain.getUserLanguage(SettingActivity.this).equals("eng")) {
            langTextView.setText("English");
        } else {
            langTextView.setText("हिंदी");
        }

        if (AppPrefsMain.isNightModeOn(SettingActivity.this)) {
            nightModeSwitch.setChecked(true);
        } else {
            nightModeSwitch.setChecked(false);
        }

        nightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppPrefsMain.setNightMode(SettingActivity.this, true);
                } else {
                    AppPrefsMain.setNightMode(SettingActivity.this, false);
                }
            }
        });

        if (AppPrefsMain.getNotifications(SettingActivity.this)) {
            notificationSwitch.setChecked(true);
        } else {
            notificationSwitch.setChecked(false);
        }

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppConstant.userProfileUpdated = true;
                if (isChecked) {
                    AppPrefsMain.setNotifications(SettingActivity.this, true);
                } else {
                    AppPrefsMain.setNotifications(SettingActivity.this, false);
                }
            }
        });

        if (AppPrefsMain.getReadNewsEnabled(SettingActivity.this)) {
            readNewsSwitch.setChecked(true);
        } else {
            readNewsSwitch.setChecked(false);
        }

        readNewsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppPrefsMain.setReadNewsEnabled(SettingActivity.this, true);
                } else {
                    AppPrefsMain.setReadNewsEnabled(SettingActivity.this, false);
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
