package com.townfeednews.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppPrefs {

    private static SharedPreferences mPrefs;
    private static SharedPreferences.Editor mPrefsEditor;

    public static void clearAllPrefs(Context ctx) {
        AppPrefs.mPrefsEditor.clear();
        mPrefsEditor.apply();
    }


    public static void setAdultChecked(Context ctx, Boolean checked) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putBoolean("checked", checked);
        mPrefsEditor.commit();
    }

    public static Boolean getAdultChecked(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getBoolean("checked", false);
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

    public static void setUserImageBase64(Context ctx, String imageBase64Code) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString("prf_img", imageBase64Code);
        mPrefsEditor.commit();
    }

    public static String getUserImageBase64(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString("prf_img", null);
    }

    public static void setFirstTimeUserFalse(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putBoolean("user_type", false);
        mPrefsEditor.commit();
    }

    public static void setUserLogOut(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putBoolean("user_type", true);
        mPrefsEditor.commit();
    }

    public static boolean isFirstTimeUser(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getBoolean("user_type", true);
    }

    public static void setUrlForWebView(Context ctx, String url) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString("webViewUrl", url);
        mPrefsEditor.commit();
    }

    public static String getUrlForWebView(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString("webViewUrl", "");
    }

    public static void setNightMode(Context ctx, Boolean nightMode) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putBoolean("nightMode", nightMode);
        mPrefsEditor.commit();
    }

    public static Boolean getNightMode(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getBoolean("nightMode", false);
    }

    public static void setNotification(Context ctx, Boolean notification) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putBoolean("notification", notification);
        mPrefsEditor.commit();
    }

    public static Boolean getNotification(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getBoolean("notification", false);
    }

    public static void setHDImage(Context ctx, Boolean hdImage) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putBoolean("hdImage", hdImage);
        mPrefsEditor.commit();
    }

    public static Boolean getHDImage(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getBoolean("hdImage", false);
    }

    public static void setReadNews(Context ctx, Boolean readNews) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putBoolean("readNews", readNews);
        mPrefsEditor.commit();
    }

    public static Boolean getReadNews(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getBoolean("readNews", false);
    }
}
