package com.townfeednews.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UserProfilePrefs {
    private static SharedPreferences mPrefs;
    private static SharedPreferences.Editor mPrefsEditor;

    public static void setUserName(Context ctx, String userName) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString("userName", userName);
        mPrefsEditor.commit();
    }

    public static String getUserName(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString("userName", "GuestUser");
    }


    public static void setGender(Context ctx, String gender) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString("gender", gender);
        mPrefsEditor.commit();
    }

    public static String getGender(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString("gender", "");
    }

    public static void setEmail(Context ctx, String email) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString("email", email);
        mPrefsEditor.commit();
    }

    public static String getEmail(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString("email", "");
    }

    public static void setMobile(Context ctx, String mobile) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString("mobile", mobile);
        mPrefsEditor.commit();
    }

    public static String getMobile(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString("mobile", "");
    }

    public static void setSelectedCategories(Context ctx, String categories) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString("categories", categories);
        mPrefsEditor.commit();
    }

    public static String getSelectedCategories(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString("categories", "");
    }

    public static void setProfileUpdateToServer(Context ctx, Boolean updateToServer) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putBoolean("updateToServer", updateToServer);
        mPrefsEditor.commit();
    }

    public static Boolean getProfileUpdateToServer(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getBoolean("updateToServer", false);
    }

}
