package com.townfeednews.model.otp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OTPResponseData {
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("otp")
    @Expose
    private String otp;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
