package com.townfeednews.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.townfeednews.BuildConfig;
import com.townfeednews.IOClickListener.CategoryListHoriClickListener;
import com.townfeednews.IOClickListener.RefreshDataListener;
import com.townfeednews.R;
import com.townfeednews.adapterNew.MainViewPagerAdapter;
import com.townfeednews.adapterOld.DrawerCategoryAdapter;
import com.townfeednews.adapterOld.HorizontalRVAdapter;
import com.townfeednews.connection.ApiInterface;
import com.townfeednews.connection.TownFeedBaseUrl;
import com.townfeednews.model.news.NewsResponseData;
import com.townfeednews.model.rv_list.CategoryData;
import com.townfeednews.model.userProfile.ProfileResponse;
import com.townfeednews.roomDB.MyAppDatabase;
import com.townfeednews.roomDB.News;
import com.townfeednews.utils.AppConstant;
import com.townfeednews.utils.AppPrefsMain;
import com.townfeednews.utils.AppUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.townfeednews.utils.AppConstant.mainViewPager;
import static com.townfeednews.utils.AppConstant.newsArrayList;
import static com.townfeednews.utils.AppConstant.newsResponseDataList;
import static com.townfeednews.utils.AppConstant.textToSpeech;
import static com.townfeednews.utils.AppConstant.userProfileUpdated;

public class HomeActivityMain extends AppCompatActivity {

    private RecyclerView categoryRecyclerViewToolbar;
    private RecyclerView categoryRecyclerViewDrawer;
    private ArrayList<CategoryData> categoryDataList = new ArrayList<>();
    private HorizontalRVAdapter horizontalRVAdapter;
    private DrawerCategoryAdapter drawerCategoryAdapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView genderSelectTextView;
    private static final String TAG = "HomeActivityMain";
    //    Navigation Drawer Views
    private TextView userNameTextView;
    private EditText userNameEditText;
    private TextView userEmailTextView;
    private EditText userEmailEditText;
    private LinearLayout phoneLinearLayout;
    private LinearLayout genderSelectLinearLayout;
    private TextView updatePhoneNumberTextView;
    private TextView settingsTextView;
    private TextView aboutUsTextView;
    private TextView policyTextView;
    private TextView shareTextView;
    private CheckBox drAdultCheckBox;
    public static String deviceId;
    private boolean isBackPressed = false;

    private List<String> drawerRVCategoryNames = new ArrayList<>();
    private List<String> drawerRVCategoryIds = new ArrayList<>();

    //toolbar category list
    private List<String> toolbarRVCategoryNames = new ArrayList<>();
    private List<String> toolbarRVCategoryIds = new ArrayList<>();
    String selectedCategories = "";
    private int[] enableCategoryIcon;
    private int[] disableCategoryIcon;
    private MainViewPagerAdapter mainViewPagerAdapter;

    private ImageView tempImageView;

    //handing app RoomDB
    private MyAppDatabase myAppDatabase;

    RefreshDataListener refreshDataListener = new RefreshDataListener() {
        @Override
        public void refreshListData() {
            Log.d(TAG, "refreshListData: Refreshing list Data... ");
            onResume();
        }


    };

    CategoryListHoriClickListener categoryListClickListener = new CategoryListHoriClickListener() {
        @Override
        public void onHoriCategoryClick(String cat_Id, int position) {
            if (!cat_Id.equals("")) {
                // TODO: 29-01-2020 filter category data and show all available category news
//                Toast.makeText(HomeActivityMain.this, "" + cat_Id, Toast.LENGTH_SHORT).show();
            } else {
                // TODO: 29-01-2020 filter on all available data as per selected category.
//                Toast.makeText(HomeActivityMain.this, "Show All Categories", Toast.LENGTH_SHORT).show();
            }

            // TODO: 29-01-2020 filter data according to selected cat_id and position
            AppConstant.selectedPositionHorizontalCategoryList = position;
            horizontalRVAdapter.notifyDataSetChanged();

        }

        @Override
        public void onDrawerCategoryClick(String cat_id, int position) {
            userProfileUpdated = true;
//            drawerCategoryAdapter.notifyDataSetChanged();
            // TODO: 29-01-2020 update category as selected in prefsMain and on Drawer Close update profile to server will be perform (Done)

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_main);
        Toolbar toolbar = findViewById(R.id.custome_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        myAppDatabase = Room.databaseBuilder(getApplicationContext(), MyAppDatabase.class, "news").build();

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        mainViewPager = findViewById(R.id.mainViewPager);
        // TODO: 27-04-2020 onClick

//      initialize navigation header view items
        initHeaderView();

//      FCM ID to update on userprofile api
        String fcmID = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "onCreate: FCM ID" + fcmID);

//      testing
        String[] drawerCategoryNo = getResources().getStringArray(R.array.drawerCategoryNumber);

        if (AppPrefsMain.getUserLanguage(HomeActivityMain.this).equals("hi")) {

            toolbarRVCategoryNames.add(0, "सभी केटेगरी");
            toolbarRVCategoryNames.addAll(Arrays.asList(getResources().getStringArray(R.array.hindiCategoryNameList)));
            drawerRVCategoryNames.addAll(Arrays.asList(getResources().getStringArray(R.array.hindiCategoryNameList)));
            toolbarRVCategoryIds.add(0, "");
            toolbarRVCategoryIds.addAll(Arrays.asList(getResources().getStringArray(R.array.hindiCategoryIDList)));
            drawerRVCategoryIds.addAll(Arrays.asList(getResources().getStringArray(R.array.hindiCategoryIDList)));

        } else {
            toolbarRVCategoryNames.add(0, "All Categories");
            toolbarRVCategoryNames.addAll(Arrays.asList(getResources().getStringArray(R.array.engCategoryNameList)));
            drawerRVCategoryNames.addAll(Arrays.asList(getResources().getStringArray(R.array.engCategoryNameList)));
            toolbarRVCategoryIds.add(0, "");
            toolbarRVCategoryIds.addAll(Arrays.asList(getResources().getStringArray(R.array.engCategoryIDList)));
            drawerRVCategoryIds.addAll(Arrays.asList(getResources().getStringArray(R.array.engCategoryIDList)));

        }

        // setCategory recyclerview in navigation drawer.
        categoryRecyclerViewToolbar = findViewById(R.id.categoryRecyclerViewToolbar);
        enableCategoryIcon = new int[]{R.drawable.ic_cat_jobs_en, R.drawable.ic_cat_sports_en,
                R.drawable.ic_cat_entertainment_en, R.drawable.ic_cat_technology_en,
                R.drawable.ic_cat_education_en, R.drawable.ic_cat_politics_en, R.drawable.ic_cat_bollywood_en,
                R.drawable.ic_cat_business_en, R.drawable.ic_cat_tranding_en,
                R.drawable.ic_cat_health_en, R.drawable.ic_cat_lifestyle_en,
                R.drawable.ic_cat_adult_en};

        disableCategoryIcon = new int[]{R.drawable.ic_cat_jobs_dis, R.drawable.ic_cat_sports_dis,
                R.drawable.ic_cat_entertainment_dis, R.drawable.ic_cat_tech_dis,
                R.drawable.ic_cat_education_dis, R.drawable.ic_cat_politics_dis, R.drawable.ic_cat_bollywood_dis,
                R.drawable.ic_cat_business_dis, R.drawable.ic_cat_trending_dis,
                R.drawable.ic_cat_health_dis, R.drawable.ic_cat_lifestyle_dis,
                R.drawable.ic_cat_adult_dis};
        categoryDataList.clear();

        for (int i = 0; i < drawerRVCategoryIds.size(); i++) {
            categoryDataList.add(new CategoryData(drawerRVCategoryNames.get(i), toolbarRVCategoryIds.get(i), enableCategoryIcon[i], disableCategoryIcon[i]));
            Log.d(TAG, "onCreate: Category Data list " + categoryDataList.size());
        }

        //Category Drawer List Preparation.
        drawerCategoryAdapter = new DrawerCategoryAdapter(HomeActivityMain.this, categoryDataList, categoryListClickListener);
        categoryRecyclerViewDrawer.setLayoutManager(new GridLayoutManager(HomeActivityMain.this, 3));
        categoryRecyclerViewDrawer.setItemAnimator(new DefaultItemAnimator());
        categoryRecyclerViewDrawer.setAdapter(drawerCategoryAdapter);

        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
//                Toast.makeText(HomeActivityMain.this, "Drawer is Open Now", Toast.LENGTH_SHORT).show();
                checkUpdateName();
                checkUpdateUserEmail();
                userProfileUpdated = false;
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
//                Toast.makeText(HomeActivityMain.this, "Drawer is closed now", Toast.LENGTH_SHORT).show();
                AppUtils.hideKeyboard(HomeActivityMain.this);
                if (userProfileUpdated) {
                    onResume();
                    userProfileUpdated = false;
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        try {
            getNewsDataFromServer();
        } catch (Exception e) {
            Log.d(TAG, "onResponse: Exception Found" + e.getMessage());
        }
    }

    private void updateProfileToServer() {
        Log.d(TAG, "updateProfileToServer: " + selectedCategories);
        ApiInterface retrofit = TownFeedBaseUrl.getRetrofit().create(ApiInterface.class);
        retrofit.updateUserProfile(AppPrefsMain.getUserName(this), AppPrefsMain.getUserEmail(this), AppPrefsMain.getUserPhone(this), selectedCategories, String.valueOf(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)), AppPrefsMain.getUserLanguage(this), AppPrefsMain.getUserGender(this), FirebaseInstanceId.getInstance().getToken(), AppPrefsMain.getNotifications(this)).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                Log.d(TAG, "onResponse: Profile Update" + response.body().getResponse());
                selectedCategories = "";
                try {
                    getNewsDataFromServer();
                } catch (Exception e) {
                    Log.d(TAG, "onResponse: Exception Found" + e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: Profile Update Filed" + t.getMessage());
                selectedCategories = "";
                try {
                    getNewsDataFromServer();
                } catch (Exception e) {
                    Log.d(TAG, "onResponse: Exception Found" + e.getMessage());
                }
            }
        });
    }

    private void getNewsDataFromServer() {
        final ProgressDialog progressDialog = new ProgressDialog(HomeActivityMain.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG, "Device Id is : " + deviceId);
        ApiInterface retrofit = TownFeedBaseUrl.getRetrofit().create(ApiInterface.class);
        Date currentTime = Calendar.getInstance().getTime();

        retrofit.getNewsData(deviceId, AppPrefsMain.getUserLanguage(HomeActivityMain.this), String.valueOf(currentTime.getTime())).enqueue(new Callback<NewsResponseData>() {    //, String.valueOf(currentTime.getTime())
            @Override
            public void onResponse(Call<NewsResponseData> call, final Response<NewsResponseData> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    newsResponseDataList.clear();
                    newsResponseDataList.addAll(response.body().getNews());

                    mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), refreshDataListener);
                    mainViewPager.setAdapter(mainViewPagerAdapter);
                    mainViewPager.setCurrentItem(0);

                    mainViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            if (position == 1) {
                                Log.d(TAG, "onPageSelected: MainView  Pager " + position);
                                if (textToSpeech != null) {
                                    if (textToSpeech.isSpeaking()) {
                                        textToSpeech.stop();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });

                    AppConstant.webViewFragment.setCurrentItem(newsResponseDataList.get(0).getPermalink());
                    Log.d(TAG, "onResponse: checking body Data " + newsResponseDataList.get(0).getPermalink());
                    Log.d(TAG, "onResponse: checking title " + newsResponseDataList.get(0).getTitle());
//                    new Thread() {
//                        @Override
//                        public void run() {
//                            myAppDatabase.myDataAccessObject().deleteAllNews();
//                            for (int i = 0; i < response.body().getNews().size(); i++) {
//                                final int position = i;
//                                new Thread() {
//                                    @Override
//                                    public void run() {
//                                        News news = new News();
//                                        try {
//                                            news.setImage(getByteArrayFromImageURL(response.body().getNews().get(position).getImage()));
//                                        } catch (Exception e) {
//                                            news.setImage("");
//                                            Log.d(TAG, "Image Convert problem : " + e.getMessage());
//                                        }
//
//                                        news.setNews_id(response.body().getNews().get(position).getNewsId());
////                                        //setTitle HTML Encoded
//                                        String title = Html.fromHtml((String) response.body().getNews().get(position).getTitle()).toString();
//                                        news.setTitle(title);
//                                        news.setPermalink(response.body().getNews().get(position).getPermalink());
//                                        //setDetails HTML Encoded
//                                        String details = Html.fromHtml((String) response.body().getNews().get(position).getDetails()).toString();
//                                        news.setDetails(details);
//                                        news.setDatetime(response.body().getNews().get(position).getDatetime());
//                                        news.setCategory(response.body().getNews().get(position).getCategory());
//                                        news.setCategory_id(response.body().getNews().get(position).getCat_id());
//                                        news.setSource(response.body().getNews().get(position).getSource());
//                                        myAppDatabase.myDataAccessObject().addNewsToDb(news);
////                                Toast.makeText(HomeActivityMain.this, "News Added successfully" + i, Toast.LENGTH_SHORT).show();
//                                        Log.d(TAG, "News added successfully to DB " + position);
//                                    }
//                                }.start();
//                            }
//                        }
//                    }.start();

                    // TODO: 03-02-2020 load data in adapter and clear RoomDB
                    // TODO: 03-02-2020 Using background thread save data in RoomDB as views are created
                    // TODO: 03-02-2020 convert image url in to base64 to save image in RoomDB
                }
            }

            @Override
            public void onFailure(Call<NewsResponseData> call, Throwable t) {
                progressDialog.dismiss();
//                newsResponseDataList.clear();
//                Log.d(TAG, "onFailure: Not getting news Data.");
//                new Thread() {
//                    @Override
//                    public void run() {
//                        List<News> news = myAppDatabase.myDataAccessObject().getNews();
//                        for (int i = 0; i <= news.size(); i++) {
//                            if (i < news.size()) {
//                                com.townfeednews.model.news.News news1 = new com.townfeednews.model.news.News();
//                                news1.setNewsId(news.get(i).getNews_id());
//                                news1.setTitle(news.get(i).getTitle());
//                                news1.setPermalink(news.get(i).getPermalink());
//                                news1.setDetails(news.get(i).getDetails());
//                                news1.setHdImage("");
//                                news1.setImage(news.get(i).getImage());
//                                news1.setThumbnail("");
//                                news1.setDatetime(news.get(i).getDatetime());
//                                news1.setCategory(news.get(i).getCategory());
//                                news1.setSource(news.get(i).getSource());
//                                news1.setCat_id(news.get(i).getCategory_id());
//                                newsResponseDataList.add(news1);
//                            } else {
//                                mainViewPager = findViewById(R.id.mainViewPager);
//                                MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), refreshDataListener);
//                                mainViewPager.setAdapter(mainViewPagerAdapter);
//                                mainViewPager.setCurrentItem(0);
//                            }
//                        }
//                    }
//                }.start();

                // TODO: 03-02-2020 onFail to load data from server
                // TODO: 03-02-2020 load the data from locally saved data

            }
        });
    }

    private String getByteArrayFromImageURL(String url) {

        try {
            URL imageUrl = new URL(url);
            URLConnection ucon = imageUrl.openConnection();
            InputStream is = ucon.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[10240];
            int read = 0;
            while ((read = is.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, read);
            }
            return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        } catch (Exception e) {
            Log.d("Error", e.toString());
        }
        return null;
    }


    private void initHeaderView() {

//Set Gender Alert Dialog (Select Gender, Male, Female)
        View headerView = navigationView.getHeaderView(0);
        genderSelectTextView = headerView.findViewById(R.id.genderSelectTextView);
        userNameTextView = headerView.findViewById(R.id.userNameTextView);
        userNameEditText = headerView.findViewById(R.id.userNameEditText);
        userEmailTextView = headerView.findViewById(R.id.userEmailTextView);
        userEmailEditText = headerView.findViewById(R.id.userEmailEditText);
        phoneLinearLayout = headerView.findViewById(R.id.phoneLinearLayout);
        updatePhoneNumberTextView = headerView.findViewById(R.id.updatePhoneNumberTextView);
        settingsTextView = headerView.findViewById(R.id.settingsTextView);
        aboutUsTextView = headerView.findViewById(R.id.aboutUsTextView);
        policyTextView = headerView.findViewById(R.id.policyTextView);
        shareTextView = headerView.findViewById(R.id.shareTextView);
        categoryRecyclerViewDrawer = headerView.findViewById(R.id.categoriesRecyclerView);
        drAdultCheckBox = headerView.findViewById(R.id.drAdultCheckBox);
//        mainViewPager = headerView.findViewById(R.id.mainViewPager);

        if (AppPrefsMain.isUserAdult(HomeActivityMain.this)) {
            drAdultCheckBox.setVisibility(View.GONE);
        } else {
            drAdultCheckBox.setVisibility(View.VISIBLE);
        }

        drAdultCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppPrefsMain.setUserAdult(HomeActivityMain.this, true);
                    drAdultCheckBox.setVisibility(View.GONE);
                    onResume();
                }
            }
        });
//        set User Email from Pref
        String userEmail = AppPrefsMain.getUserEmail(HomeActivityMain.this);

        if (userEmail.length() == 0 || !AppUtils.isValidEmail(userEmail)) {
            userEmailEditText.setText("Email ID");
            userEmailTextView.setText("Email ID");
        } else {
            userEmailEditText.setText(userEmail);
            userEmailTextView.setText(userEmail);
        }
//        End user Email from Pref

//        Set User Name from Pref
        String userName = AppPrefsMain.getUserName(HomeActivityMain.this);

        if (userName.length() == 0) {
            userNameEditText.setText("Name");
            userNameTextView.setText("Name");
        } else {
            userNameTextView.setText(userName);
            userNameEditText.setText(userName);
        }
//        End User name from Pref

//        Edit user Name
        userNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNameTextView.setVisibility(View.GONE);
                userNameEditText.setVisibility(View.VISIBLE);
                userNameEditText.requestFocus();
                userProfileUpdated = true;
                checkUpdateUserEmail();
            }
        });
//        End of Edit user Name

//        Edit user Email
        userEmailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmailEditText.setVisibility(View.VISIBLE);
                userEmailTextView.setVisibility(View.GONE);
                userEmailEditText.requestFocus();
                userProfileUpdated = true;
                checkUpdateName();
            }
        });
//        End of Edit User Email


//        Set User gender from pref
        if (AppPrefsMain.getUserGender(HomeActivityMain.this).equals("m")) {
            genderSelectTextView.setText("Male");
            genderSelectTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else if (AppPrefsMain.getUserGender(HomeActivityMain.this).equals("f")) {
            genderSelectTextView.setText("Female");
            genderSelectTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else {
            genderSelectTextView.setText("Select");
            genderSelectTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_drop_down, 0);
        }

//        End of user gender from pref.

        genderSelectLinearLayout = headerView.findViewById(R.id.genderSelectLinearLayout);
        genderSelectLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userProfileUpdated = true;
                checkUpdateName();
                checkUpdateUserEmail();
                genderSelectLinearLayout.setClickable(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        genderSelectLinearLayout.setClickable(true);
                    }
                }, 1000);
                // show gender select custome alert dialog box
                final Dialog dialog = new Dialog(HomeActivityMain.this);
                dialog.setContentView(R.layout.gender_select_alert_dialog);
                dialog.setTitle("Select Gender");

                RadioGroup genderSelectRadioGroup = dialog.findViewById(R.id.genderSelectRadioGroup);

                genderSelectRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == R.id.genderMaleRadioButton) {
                            genderSelectTextView.setText("Male");
                            AppPrefsMain.setUserGender(HomeActivityMain.this, "m");
                        } else {
                            genderSelectTextView.setText("Female");
                            AppPrefsMain.setUserGender(HomeActivityMain.this, "f");
                        }
                        userProfileUpdated = true;
                        genderSelectTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        dialog.dismiss();
                    }
                });

                dialog.show();

//                End Gender Select dialog box
            }
        });

//        set phone number from prefs
        String phoneNumber = AppPrefsMain.getUserPhone(HomeActivityMain.this);

        if (phoneNumber.length() != 0) {
            updatePhoneNumberTextView.setText(phoneNumber);
        } else {
            updatePhoneNumberTextView.setText("Update Number");
        }

//        end set phone number prefs

//        Edit Phone Number with user OTP verification

        phoneLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivityMain.this, MobileVerification.class));
                checkUpdateUserEmail();
                checkUpdateName();
            }
        });
//        End of Phone number verification Edit number

        settingsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivityMain.this, SettingActivity.class));
            }
        });

        aboutUsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutUsTextView.setClickable(false);
//                drawerLayout.closeDrawers();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        aboutUsTextView.setClickable(true);
                    }
                }, 1000);

                Intent intent = new Intent(HomeActivityMain.this, WebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", AppConstant.aboutUsUrl);
                bundle.putString("title", "About Us");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        policyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                policyTextView.setClickable(false);
//                drawerLayout.closeDrawers();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        policyTextView.setClickable(true);
                    }
                }, 1000);
                Intent intent = new Intent(HomeActivityMain.this, WebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", AppConstant.privacyPolicyUrl);
                bundle.putString("title", "Policy");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        shareTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new method to share same sharing as old method.
//                ShareCompat.IntentBuilder.from(HomeActivityMain.this)
//                        .setType("text/plain")
//                        .setChooserTitle("Chooser title")
//                        .setText("http://play.google.com/store/apps/details?id=" + HomeActivityMain.this.getPackageName())
//                        .startChooser();

//                old method to share app link
//
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/html");  //image/jpeg, audio/mpeg4-generic, text/html, audio/mpeg
//                    audio/aac, audio/wav, audio/ogg, audio/midi, audio/x-ms-wma, video/mp4, video/x-msvideo
//                    video/x-ms-wmv, image/png, image/jpeg, image/gif, .xml ->text/xml, .txt -> text/plain
//                    .cfg -> text/plain, .csv -> text/plain, .conf -> text/plain, .rc -> text/plain
//                    .htm -> text/html,.html -> text/html,.pdf -> application/pdf,.apk -> application/vnd.android.package-archive
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "TownFeed");
                    String shareMessage = "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    Toast.makeText(HomeActivityMain.this, "Oops ! Something went wrong. Please try after sometime.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void checkUpdateName() {
        userNameEditText.setVisibility(View.GONE);
        userNameTextView.setVisibility(View.VISIBLE);
        String userNameUpdated = userNameEditText.getText().toString();
        userNameTextView.setText(userNameUpdated);

        if (userNameUpdated.length() != 0) {
            AppPrefsMain.setUserName(HomeActivityMain.this, userNameUpdated);
            userProfileUpdated = true;
        }
    }

    private void checkUpdateUserEmail() {
        userEmailEditText.setVisibility(View.GONE);
        userEmailTextView.setVisibility(View.VISIBLE);
        String userEmailUpdated = userEmailEditText.getText().toString();
        userEmailTextView.setText(userEmailUpdated);

        if (AppUtils.isValidEmail(userEmailUpdated)) {
            AppPrefsMain.setUserEmail(HomeActivityMain.this, userEmailUpdated);
            userProfileUpdated = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (mainViewPager.getCurrentItem() == 1) {
                mainViewPager.setCurrentItem(0);
            } else {
                if (isBackPressed) {
                    super.onBackPressed();
                }

                AppConstant.webViewFragment.setCurrentItem("about:blank");

                Toast.makeText(this, "Please press again to exit the App", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isBackPressed = false;
                    }
                }, 2000);
                isBackPressed = true;
            }
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
//        Toast.makeText(this, "On Resume Chal gya ", Toast.LENGTH_SHORT).show();
        ArrayList<CategoryData> toolbarRVCategoryData = new ArrayList<>();
//        if (userProfileUpdated) {
        for (int i = 0; i <= 12; i++) {
            if (i < 12) {
                if (AppPrefsMain.isCategorySelected(HomeActivityMain.this, String.valueOf(i))) {
                    if (selectedCategories.length() == 0) {
                        selectedCategories = drawerRVCategoryIds.get(i);
                        if (AppPrefsMain.getUserLanguage(HomeActivityMain.this).equals("eng") && toolbarRVCategoryData.size() == 0) {
                            toolbarRVCategoryData.add(new CategoryData("All", "", enableCategoryIcon[i], disableCategoryIcon[i]));
                            toolbarRVCategoryData.add(new CategoryData(drawerRVCategoryNames.get(i), drawerRVCategoryIds.get(i), enableCategoryIcon[i], disableCategoryIcon[i]));
                        } else if (AppPrefsMain.getUserLanguage(HomeActivityMain.this).equals("hi") && toolbarRVCategoryData.size() == 0) {
                            toolbarRVCategoryData.add(new CategoryData("सभी केटेगरी", "", enableCategoryIcon[i], disableCategoryIcon[i]));
                            toolbarRVCategoryData.add(new CategoryData(drawerRVCategoryNames.get(i), drawerRVCategoryIds.get(i), enableCategoryIcon[i], disableCategoryIcon[i]));
                        } else {
                            toolbarRVCategoryData.add(new CategoryData(drawerRVCategoryNames.get(i), drawerRVCategoryIds.get(i), enableCategoryIcon[i], disableCategoryIcon[i]));
                        }

                    } else {
                        selectedCategories = selectedCategories + "," + drawerRVCategoryIds.get(i);
                        toolbarRVCategoryData.add(new CategoryData(drawerRVCategoryNames.get(i), drawerRVCategoryIds.get(i), enableCategoryIcon[i], disableCategoryIcon[i]));
                    }
                }
            } else {
                if (userProfileUpdated) {
                    updateProfileToServer();
                    userProfileUpdated = false;
                }
                //toolbar recycler view adapter
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivityMain.this,
                        LinearLayoutManager.HORIZONTAL, false);

                categoryRecyclerViewToolbar.setLayoutManager(linearLayoutManager);
                categoryRecyclerViewToolbar.setItemAnimator(new DefaultItemAnimator());

                horizontalRVAdapter = new HorizontalRVAdapter(HomeActivityMain.this, toolbarRVCategoryData, categoryListClickListener);
                categoryRecyclerViewToolbar.setAdapter(horizontalRVAdapter);
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (AppConstant.textToSpeech != null) {
            AppConstant.textToSpeech.stop();
        }
    }
}
