package com.townfeednews.utils;

import androidx.viewpager.widget.ViewPager;

import com.townfeednews.fragments.WebViewFragment;
import com.townfeednews.model.category.CategoryData;
import com.townfeednews.roomDBOld.TownFeedAppDB;
import com.townfeednews.model.newsOld.News;

import java.util.ArrayList;
import java.util.List;

public class AppConstant {
 public static ArrayList<News> newsArrayList = new ArrayList<>();
 public static String deviceId;
 public static ArrayList<CategoryData> categoryData = new ArrayList<>();
 public static TownFeedAppDB townFeedAppDB;
 public static ArrayList<String> selectedCategories = new ArrayList<>();
 public static boolean userProfileUpdated = false;
 public static String privacyPolicyUrl = "https://townfeed.in/privacy-policy.php";
 public static String aboutUsUrl = "https://townfeed.in/about-us.php";
 public static int selectedPositionHorizontalCategoryList = 0;
 public static List<com.townfeednews.model.news.News> newsResponseDataList = new ArrayList<>();
 public static boolean isInternetConnected = false;
 public static WebViewFragment webViewFragment;
 public static ViewPager mainViewPager;


}
