package com.townfeednews.helper;

import com.google.gson.annotations.SerializedName;

public class UserProfile {

    @SerializedName("name")
    private String name;
    @SerializedName("gender")
    private String gender;
    @SerializedName("email")
    private String email;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("select_cat")
    private String select_cat;
    @SerializedName("device_id")
    private String device_id;
    @SerializedName("date")
    private String date;
    @SerializedName("lang")
    private String lang;

    public UserProfile(String name, String gender, String email, String mobile, String select_cat, String device_id, String date, String lang) {
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.mobile = mobile;
        this.select_cat = select_cat;
        this.device_id = device_id;
        this.date = date;
        this.lang = lang;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSelect_cat() {
        return select_cat;
    }

    public void setSelect_cat(String select_cat) {
        this.select_cat = select_cat;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
