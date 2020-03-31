package com.townfeednews.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.townfeednews.BuildConfig;
import com.townfeednews.IOClickListenerOld.CategoryItemClickListener;
import com.townfeednews.R;
import com.townfeednews.adapterOld.CategoriesItemsAdapter;
import com.townfeednews.adapterOld.HomeViewPagerAdapter;
//import com.townfeednews.fragments.WebViewFragment;
import com.townfeednews.helper.UserProfile;
import com.townfeednews.utils.AppConstant;
import com.townfeednews.utils.AppPrefs;
import com.townfeednews.utils.AppUtils;
import com.townfeednews.utils.UserProfilePrefs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.townfeednews.utils.AppUtils.updateUserProfileToServer;

public class HomeActivityOld extends AppCompatActivity {

    private ViewPager homePageViewPager;
    private HomeViewPagerAdapter homeViewPagerAdapter;
    private boolean isBackPressed = false;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private EditText userNameEditTextOld;
    private CheckBox checkBox;
    private TextView settingsTextViewOld;
    private TextView categoryAdult, userNameTextViewOld;
    private static final String TAG = "HomeActivityOld";
    private CircleImageView profile_image;
    private static final int SELECT_PICTURE = 100;
    private RecyclerView categoriesRecyclerView;
    private boolean catUpdated = false;
    private CheckBox adultCheckBox;
    private RadioGroup genderRadioGroup;
    private CategoriesItemsAdapter adapter;
    private TextView policyTextViewOld, aboutUsTextViewOld, shareTextViewOld, support_helpTextView, feedBackTextView;

    CategoryItemClickListener categoryItemClickListener = new CategoryItemClickListener() {
        @Override
        public void onItemClick(final String cat_id) {
            UserProfilePrefs.setProfileUpdateToServer(HomeActivityOld.this, true);
            AppConstant.categoryData.clear();
            AppUtils.updateUserProfileToDb(cat_id, AppConstant.townFeedAppDB.myDataAccessObject(), categoryItemClickListener);
        }

        @Override
        public void onCategoryListUpdated() {
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        updateCategoryList();
        userNameEditTextOld.setText(UserProfilePrefs.getUserName(HomeActivityOld.this));
        userNameTextViewOld.setText("Hi, " + UserProfilePrefs.getUserName(HomeActivityOld.this));
    }

    private void updateCategoryList() {
        if (AppConstant.categoryData.size() < 1) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateCategoryList();
                }
            }, 500);
        } else {
            adapter = new CategoriesItemsAdapter(AppConstant.categoryData, this, categoryItemClickListener);
            categoriesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            categoriesRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        drawerLayout = findViewById(R.id.activity_main);
        navigationView = findViewById(R.id.nv);
        checkBox = findViewById(R.id.adultCheckBox);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                return true;
            }
        });

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                Log.d(TAG, "onCreate: Navigation View Opened");
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                Set<String> primesWithoutDuplicates = new LinkedHashSet<String>(AppConstant.selectedCategories);
                AppConstant.selectedCategories.clear();
                AppConstant.selectedCategories.addAll(primesWithoutDuplicates);
                for (int i = 0; i < AppConstant.selectedCategories.size(); i++) {
                    UserProfilePrefs.setSelectedCategories(HomeActivityOld.this, UserProfilePrefs.getSelectedCategories(HomeActivityOld.this) + "," + AppConstant.selectedCategories.get(i));
                }

                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String newDateStr = df.format(c);
                Log.d(TAG, "Today date is : " + newDateStr);

                if (UserProfilePrefs.getProfileUpdateToServer(HomeActivityOld.this)) {

                    updateUserProfileToServer(new UserProfile(UserProfilePrefs.getUserName(HomeActivityOld.this).replace("\"", ""),
                            UserProfilePrefs.getGender(HomeActivityOld.this).replace("\"", ""),
                            UserProfilePrefs.getEmail(HomeActivityOld.this).replace("\"", ""),
                            UserProfilePrefs.getMobile(HomeActivityOld.this).replace("\"", ""),
                            UserProfilePrefs.getSelectedCategories(HomeActivityOld.this).replace("\"", ""),
                            AppConstant.deviceId.replace("\"", ""),
                            newDateStr.replace("\"", ""), AppPrefs.getUserLanguage(HomeActivityOld.this).replace("\"", "")), HomeActivityOld.this);
                }

                AppUtils.hideKeyboard(HomeActivityOld.this);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        navigationHeaderController();

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        homePageViewPager = findViewById(R.id.homePageViewPager);
//        homeViewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager());
//        homePageViewPager.setAdapter(homeViewPagerAdapter);
        homePageViewPager.setCurrentItem(0);

        homePageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    getSupportActionBar().show();
                    getSupportActionBar().setTitle(AppPrefs.getUrlForWebView(HomeActivityOld.this));
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//                    try {
//                        WebViewFragment.webView.clearHistory();
//                        WebViewFragment.webView.loadUrl(AppPrefs.getUrlForWebView(HomeActivityOld.this));
//                    } catch (Exception e) {
//                        Toast.makeText(HomeActivityOld.this, "Something went wong with url.", Toast.LENGTH_SHORT).show();
//                    }
                } else {
                    getSupportActionBar().hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void handlePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //ask for permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    SELECT_PICTURE);
        } else {
            openImageChooser();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case SELECT_PICTURE:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
                        if (showRationale) {
                            //  Show your own message here
                        } else {
                            showSettingsAlert();
                        }
                    } else {
                        handlePermission();
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showSettingsAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Gallery.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DON'T ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SETTINGS",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        openAppSettings(HomeActivityOld.this);
                    }
                });
        alertDialog.show();
    }

    public static void openAppSettings(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    /* Choose an image from Gallery */
    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (resultCode == RESULT_OK) {
                    if (requestCode == SELECT_PICTURE) {
                        // Get the url from data
                        final Uri selectedImageUri = data.getData();
                        if (null != selectedImageUri) {
                            // Get the path from the Uri
                            final String path = getPathFromURI(selectedImageUri);
                            Log.i(TAG, "Image Path : " + path);
                            // Set the image in ImageView
                            findViewById(R.id.profile_image).post(new Runnable() {
                                @Override
                                public void run() {

                                    ((CircleImageView) findViewById(R.id.profile_image)).setImageURI(selectedImageUri);
                                    try {
                                        Bitmap bmp = MediaStore.Images.Media.getBitmap(HomeActivityOld.this.getContentResolver(), selectedImageUri);
                                        Log.d(TAG, "run: base64 Image " + encodeTobase64(bmp));
                                        Log.d(TAG, "Image Base64 url : " + encodeTobase64(bmp));
                                        AppPrefs.setUserImageBase64(HomeActivityOld.this, encodeTobase64(bmp));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Log.d(TAG, "run: base64 Image Exception " + e.getMessage());
                                    }

                                }
                            });
                        }
                    }
                }
            }
        }).start();
    }

    // method for bitmap to base64
    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private void navigationHeaderController() {
        View header = navigationView.getHeaderView(0);
        userNameEditTextOld = header.findViewById(R.id.userNameEditText);
        userNameTextViewOld = header.findViewById(R.id.userNameTextView);
        profile_image = header.findViewById(R.id.profile_image);
        settingsTextViewOld = header.findViewById(R.id.settingsTextView);
        support_helpTextView = header.findViewById(R.id.support_helpTextView);
        feedBackTextView = header.findViewById(R.id.feedBackTextView);
        adultCheckBox = header.findViewById(R.id.adultCheckBox);
        genderRadioGroup = header.findViewById(R.id.genderRadioGroup);

        userNameEditTextOld.setText("Hi, Guest User");
        checkBox = header.findViewById(R.id.adultCheckBox);
        policyTextViewOld = header.findViewById(R.id.policyTextView);
        aboutUsTextViewOld = header.findViewById(R.id.aboutUsTextView);
        shareTextViewOld = header.findViewById(R.id.shareTextView);

        Log.d(TAG, "navigationHeaderController: " + UserProfilePrefs.getGender(HomeActivityOld.this));

        if (UserProfilePrefs.getGender(HomeActivityOld.this).equals("m")) {
            RadioButton radioButton = header.findViewById(R.id.maleRadioButton);
            radioButton.setChecked(true);
        } else if (UserProfilePrefs.getGender(HomeActivityOld.this).equals("f")) {
            RadioButton radioButton = header.findViewById(R.id.femaleRadioButton);
            radioButton.setChecked(true);
        } else {
            RadioButton mRadioButton = header.findViewById(R.id.maleRadioButton);
            RadioButton fRadioButton = header.findViewById(R.id.femaleRadioButton);
            mRadioButton.setChecked(false);
            fRadioButton.setChecked(false);
        }

        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                AppUtils.hideKeyboard(HomeActivityOld.this);
                userNameTextViewOld.setText("Hi, " + userNameEditTextOld.getText().toString().trim());
                userNameTextViewOld.setVisibility(View.VISIBLE);
                userNameEditTextOld.setVisibility(View.GONE);
                UserProfilePrefs.setUserName(HomeActivityOld.this, userNameEditTextOld.getText().toString().trim());
                UserProfilePrefs.setProfileUpdateToServer(HomeActivityOld.this, true);

                if (group.getCheckedRadioButtonId() == R.id.maleRadioButton) {
                    UserProfilePrefs.setGender(HomeActivityOld.this, "m");
                } else {
                    UserProfilePrefs.setGender(HomeActivityOld.this, "f");
                }

            }
        });

        categoriesRecyclerView = header.findViewById(R.id.categoriesRecyclerView);

        if (AppPrefs.getAdultChecked(HomeActivityOld.this)) {
            adultCheckBox.setChecked(true);
        } else {
            adultCheckBox.setChecked(false);
        }

        adultCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppPrefs.setAdultChecked(HomeActivityOld.this, true);
                } else {
                    AppPrefs.setAdultChecked(HomeActivityOld.this, false);
                }
                AppUtils.hideKeyboard(HomeActivityOld.this);
                userNameTextViewOld.setText("Hi, " + userNameEditTextOld.getText().toString().trim());
                userNameTextViewOld.setVisibility(View.VISIBLE);
                userNameEditTextOld.setVisibility(View.GONE);
                UserProfilePrefs.setUserName(HomeActivityOld.this, userNameEditTextOld.getText().toString().trim());
                onStart();
            }
        });


        settingsTextViewOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivityOld.this, SettingActivity.class));
            }
        });

        support_helpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(HomeActivityOld.this, "Need clarification about Support / Help.", Toast.LENGTH_SHORT).show();
            }
        });

        feedBackTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(HomeActivityOld.this, "Need clarification about FeedBack.", Toast.LENGTH_SHORT).show();
            }
        });

        shareTextViewOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "TownFeed");
                    String shareMessage = "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
//                    Toast.makeText(HomeActivityOld.this, "Oops ! Something went wrong. Please try after sometime.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        policyTextViewOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivityOld.this, WebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", "https://townfeed.in/privacy-policy.php");
                bundle.putString("title", "Policy");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        aboutUsTextViewOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivityOld.this, WebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", "https://townfeed.in/about-us.php");
                bundle.putString("title", "About Us");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        userNameTextViewOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNameEditTextOld.setVisibility(View.VISIBLE);
                userNameTextViewOld.setVisibility(View.GONE);
                userNameEditTextOld.requestFocus();
                UserProfilePrefs.setProfileUpdateToServer(HomeActivityOld.this, true);
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(
                        userNameEditTextOld.getApplicationWindowToken(),
                        InputMethodManager.SHOW_FORCED, 0);

            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePermission();
            }
        });

        if (AppPrefs.getUserImageBase64(HomeActivityOld.this) != null) {
            Log.d(TAG, "Image Base64 url : " + AppPrefs.getUserImageBase64(HomeActivityOld.this));
            byte[] imageByteArray = Base64.decode(AppPrefs.getUserImageBase64(HomeActivityOld.this), Base64.DEFAULT);
            Glide.with(HomeActivityOld.this)
                    .load(imageByteArray)
                    .placeholder(R.mipmap.ic_male_place_holder)
                    .into(profile_image);
        }
    }

    @Override
    public void onBackPressed() {
        switch (homePageViewPager.getCurrentItem()) {
            case 0:
                if (isBackPressed) {
                    super.onBackPressed();
                }
                isBackPressed = true;
                Toast.makeText(this, "Press again to exit the App", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isBackPressed = false;
                    }
                }, 2000);
                break;
            case 1:
//                if (WebViewFragment.webView.canGoBack()) {
//                    WebViewFragment.webView.goBack();
//                } else {
//                    homePageViewPager.setCurrentItem(1);
//                    isBackPressed = false;
//                }
                break;
            default:
                homePageViewPager.setCurrentItem(0);
                isBackPressed = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                homePageViewPager.setCurrentItem(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
