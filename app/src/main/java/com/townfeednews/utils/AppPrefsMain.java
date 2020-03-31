package com.townfeednews.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppPrefsMain {
    private static SharedPreferences mPrefs;
    private static SharedPreferences.Editor mPrefsEditor;

    public static Boolean isAllCategorySelected(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getBoolean("categoryStatus", true);
    }

    public static void setAllCategorySelected(Context ctx,boolean status) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putBoolean("categoryStatus", status);
        mPrefsEditor.apply();
    }

    public static Boolean isHDImageEnabled(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getBoolean("hdImage", true);
    }

    public static void setHDImageEnabled(Context ctx,boolean status) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putBoolean("hdImage", status);
        mPrefsEditor.apply();
    }

    public static Boolean isNightModeOn(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getBoolean("nightMode", false);
    }

    public static void setNightMode(Context ctx,boolean status) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putBoolean("nightMode", status);
        mPrefsEditor.apply();
    }

    public static Boolean areAllSelected(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getBoolean("allSelected", true);
    }

    public static void setAllSelected(Context ctx,boolean status) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putBoolean("allSelected", status);
        mPrefsEditor.apply();
    }

    public static Boolean isCategorySelected(Context ctx,String categoryNumber) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getBoolean(categoryNumber, true);
    }

    public static void setCategorySelected(Context ctx, String categoryNumber,boolean status) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putBoolean(categoryNumber, status);
        mPrefsEditor.apply();
    }

    public static Boolean getReadNewsEnabled(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getBoolean("readNews", false);
    }

    public static void setReadNewsEnabled(Context ctx, boolean readNews) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putBoolean("readNews", readNews);
        mPrefsEditor.apply();
    }

    public static Boolean getNotifications(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getBoolean("notifications", true);
    }

    public static void setNotifications(Context ctx, boolean notifications) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putBoolean("notifications", notifications);
        mPrefsEditor.apply();
    }

    public static Boolean isUserAdult(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getBoolean("user_adult", false);
    }

    public static void setUserAdult(Context ctx, boolean isAdult) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putBoolean("user_adult", isAdult);
        mPrefsEditor.apply();
    }

    public static String getUserName(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString("user_name", "");
    }

    public static void setUserName(Context ctx, String userName) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString("user_name", userName);
        mPrefsEditor.apply();
    }

    public static String getUserEmail(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString("user_email", "");
    }

    public static void setUserEmail(Context ctx, String userEmail) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString("user_email", userEmail);
        mPrefsEditor.apply();
    }

    public static String getUserPhone(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString("phone", "");
    }

    public static void setUserPhone(Context ctx, String phone) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString("phone", phone);
        mPrefsEditor.apply();
    }


    public static Boolean isUserLoggedIn(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getBoolean("loggedIn", false);
    }

    public static void setUserLoggedIn(Context ctx, Boolean loggedIn) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putBoolean("loggedIn", loggedIn);
        mPrefsEditor.apply();
    }

    public static void setUserLanguage(Context ctx, String lang) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString("lang", lang);
        mPrefsEditor.commit();
    }

    public static String getUserLanguage(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString("lang", "eng");
    }


    public static void setUserGender(Context ctx, String gender) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString("gender", gender);
        mPrefsEditor.apply();
    }

    public static String getUserGender(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString("gender", "");
    }


}
