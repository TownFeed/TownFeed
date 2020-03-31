package com.townfeednews.model.userProfile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("select_cat")
    @Expose
    private String selectCat;
    @SerializedName("device_id")
    @Expose
    private String deviceId;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("fcm_id")
    @Expose
    private String fcmId;
    @SerializedName("notification_enable")
    @Expose

    private Boolean notificationEnable;


    public Profile(String name, String email, String mobile, String selectCat, String deviceId, String lang, String gender, String fcmId, Boolean notificationEnable) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.selectCat = selectCat;
        this.deviceId = deviceId;
        this.lang = lang;
        this.gender = gender;
        this.fcmId = fcmId;
        this.notificationEnable = notificationEnable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getSelectCat() {
        return selectCat;
    }

    public void setSelectCat(String selectCat) {
        this.selectCat = selectCat;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFcmId() {
        return fcmId;
    }

    public void setFcmId(String fcmId) {
        this.fcmId = fcmId;
    }

    public Boolean getNotificationEnable() {
        return notificationEnable;
    }

    public void setNotificationEnable(Boolean notificationEnable) {
        this.notificationEnable = notificationEnable;
    }

}
