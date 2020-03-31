package com.townfeednews.connection;

import com.townfeednews.model.category.CategoriesNameResponseData;
import com.townfeednews.model.news.NewsResponseData;
import com.townfeednews.model.otp.OTPResponseData;
import com.townfeednews.model.userProfile.ProfileResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiInterface {

    //    get latest news content according to selected categories. send device_Id parameter.
    @Headers("Content-Type: application/json")
    @GET("news.php")
    Call<NewsResponseData> getNewsData(@Query("device_id") String deviceId, @Query("lang") String lang,@Query("timeStamp") String timeStamp);


    //    get otp sms give mobile number in String response will be success or fail
    @Headers("Content-Type: application/json")
    @GET("otp_send.php")
    Call<OTPResponseData> getOTPSms(@Query("mobile") String mobileNumber);

    //    verify user OTP(String) with generated otp response will be success or fail.
    @Headers("Content-Type: application/json")
    @GET("otp_receive.php")
    Call<OTPResponseData> verifyOtp(@Query("mobile") String mobileNumber, @Query("otp") String otp);

    //    getnews categories from server parameters language and deviceid.
    @Headers("Content-Type: application/json")
    @GET("category.php")
    Call<CategoriesNameResponseData> getCategoriesNamesFromServer(@Query("lang") String language, @Query("device_Id") String device_Id);

    //    update user profile with parameters
    @Headers("Content-Type: application/json")
    @GET("profile.php")
    Call<ProfileResponse> updateUserProfile(@Query("name") String userName,
                                            @Query("email") String userEmail,
                                            @Query("mobile") String userMobile,
                                            @Query("select_cat") String selectCat,
                                            @Query("device_id") String deviceId,
                                            @Query("lang") String language,
                                            @Query("gender") String gender,
                                            @Query("fcm_id") String fcmId,
                                            @Query("notification_enable") Boolean notificationEnable);
}
