package com.townfeednews.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.townfeednews.R;
import com.townfeednews.broadcast.SmsBroadcastReceiver;
import com.townfeednews.connection.ApiInterface;
import com.townfeednews.connection.OtpReceivedInterface;
import com.townfeednews.connection.TownFeedBaseUrl;
import com.townfeednews.model.otp.OTPResponseData;
import com.townfeednews.utils.AppConstant;
import com.townfeednews.utils.AppPrefsMain;
import com.townfeednews.utils.AppUtils;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MobileVerification extends AppCompatActivity implements OtpReceivedInterface {
    private TextView titleTextView, messageTextView, resendTextView, timerTextView, phoneNumberTextView;
    private EditText phoneNumberEditText, userOTPEditText;
    private LinearLayout timerLinearLayout;
    private Button otpGetButton;
    private ApiInterface retrofit;
    private static final String TAG = "MobileVerification";
    private TextInputLayout phonenumberTextInputLayout;
    private static final long START_TIME_IN_MILLIS = 60000;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning = false;
    private String generatedOTP = "";
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verification);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initView();
    }

    private void initView() {

        titleTextView = findViewById(R.id.titleTextView);
        messageTextView = findViewById(R.id.messageTextView);
        resendTextView = findViewById(R.id.resendTextView);
        timerTextView = findViewById(R.id.timerTextView);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        userOTPEditText = findViewById(R.id.userOTPEditText);
        timerLinearLayout = findViewById(R.id.timerLinearLayout);
        otpGetButton = findViewById(R.id.otpGetButton);
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        phonenumberTextInputLayout = findViewById(R.id.phonenumberTextInputLayout);
        retrofit = TownFeedBaseUrl.getRetrofit().create(ApiInterface.class);

        userOTPEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 4) {
                    AppUtils.hideKeyboard(MobileVerification.this);
                    otpGetButton.setEnabled(true);
                    otpGetButton.setBackgroundResource(R.drawable.otp_button_shape);
                } else {
                    otpGetButton.setEnabled(false);
                    otpGetButton.setBackgroundResource(R.drawable.otp_button_shape_disable);
                }
            }
        });

        phoneNumberTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpGetButton.setEnabled(true);
                otpGetButton.setBackgroundResource(R.drawable.otp_button_shape);
                otpGetButton.setText("Get OTP");
                phoneNumberTextView.setVisibility(View.GONE);
                phoneNumberEditText.setVisibility(View.VISIBLE);
                phonenumberTextInputLayout.setVisibility(View.VISIBLE);
                userOTPEditText.setEnabled(false);
                userOTPEditText.setText("");
                titleTextView.setText("Phone Number");
                messageTextView.setText("OTP will be sent to this number");
                resendTextView.setTextColor(Color.WHITE);
                timerTextView.setTextColor(Color.WHITE);

                Log.d(TAG, "onClick: Phone Number TextView clicked...");
            }
        });

        timerLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOTPFromServer();
                mTimeLeftInMillis = START_TIME_IN_MILLIS;
                startTimer();
            }
        });

        otpGetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + otpGetButton.getText());
                if (otpGetButton.getText().toString().contains("Get OTP")) {
                    otpGetButton.setEnabled(false);
                    otpGetButton.setBackgroundResource(R.drawable.otp_button_shape_disable);
                    otpGetButton.setText("Please wait...");
                    resendTextView.setTextColor(Color.parseColor("#2680EB"));
                    timerTextView.setTextColor(Color.parseColor("#2680EB"));
                    phoneNumberTextView.setVisibility(View.VISIBLE);
                    phoneNumberTextView.setText(phoneNumberEditText.getText().toString());
                    phoneNumberEditText.setVisibility(View.GONE);
                    phonenumberTextInputLayout.setVisibility(View.GONE);
                    userOTPEditText.setEnabled(true);
                    timerLinearLayout.setEnabled(false);
                    titleTextView.setText("OTP Verification");
                    messageTextView.setText("Sit back and relax while we are verifying your phone number.");
                    Log.d(TAG, "onClick: otp button clicked...");
                    getOTPFromServer();
                    mTimeLeftInMillis = START_TIME_IN_MILLIS;
                    startTimer();
                } else {
                    if (userOTPEditText.getText().toString().equals(generatedOTP)) {
                        AppPrefsMain.setUserPhone(MobileVerification.this, phoneNumberEditText.getText().toString().trim());
                        AppPrefsMain.setUserLoggedIn(MobileVerification.this, true);
                        AppConstant.userProfileUpdated = true;
                        Intent intent = new Intent(getApplicationContext(), HomeActivityMain.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finishAffinity();
//                        finish();
//                        startActivity(new Intent(MobileVerification.this, HomeActivityMain.class));

                    } else {
                        Log.d(TAG, "onClick: otp mismatch");
                        Toast.makeText(MobileVerification.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        phoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 10) {
                    Log.d(TAG, "afterTextChanged: " + s.length());
                    otpGetButton.setBackgroundResource(R.drawable.otp_button_shape);
                    otpGetButton.setEnabled(true);
                    AppUtils.hideKeyboard(MobileVerification.this);
                } else {
                    Log.d(TAG, "afterTextChanged: length is too short");
                    otpGetButton.setBackgroundResource(R.drawable.otp_button_shape_disable);
                    otpGetButton.setEnabled(false);
                }
            }
        });


    }

    private void getOTPFromServer() {
        String mobileNumber = phoneNumberEditText.getText().toString();
        Log.d(TAG, "getOTPFromServer: " + mobileNumber);
        Call<OTPResponseData> pojoCall = retrofit.getOTPSms(mobileNumber);
        pojoCall.enqueue(new Callback<OTPResponseData>() {
            @Override
            public void onResponse(Call<OTPResponseData> call, Response<OTPResponseData> response) {
                if (response.body().getMsg().equals("success")) {
                    generatedOTP = response.body().getOtp();
                    SmsBroadcastReceiver mSmsBroadcastReceiver = new SmsBroadcastReceiver();

                    mSmsBroadcastReceiver.setOnOtpListeners(MobileVerification.this);
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
                    MobileVerification.this.registerReceiver(mSmsBroadcastReceiver, intentFilter);
                    startSMSListener();
                    otpGetButton.setText("Verify");
                    otpGetButton.setEnabled(false);
                    otpGetButton.setBackgroundResource(R.drawable.otp_button_shape_disable);
                }

            }

            @Override
            public void onFailure(Call<OTPResponseData> call, Throwable t) {
                otpGetButton.setEnabled(false);
//                Toast.makeText(MobileVerification.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startSMSListener() {
        SmsRetrieverClient mClient = SmsRetriever.getClient(this);
        Task<Void> mTask = mClient.startSmsRetriever();
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
        mTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void startTimer() {
        mTimerRunning = true;
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                timerTextView.setVisibility(View.VISIBLE);
                timerTextView.setTextColor(Color.parseColor("#2680EB"));
                resendTextView.setText("Resend:");
                resendTextView.setTextColor(Color.parseColor("#2680EB"));
                phoneNumberTextView.setEnabled(false);
                phoneNumberTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_edit_otp_screen_grey, 0);
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                timerLinearLayout.setEnabled(true);
                timerTextView.setTextColor(Color.WHITE);
                timerTextView.setVisibility(View.GONE);
                resendTextView.setTextColor(Color.BLUE);
                phoneNumberTextView.setEnabled(true);
                String myString = "Resend Now";
                phoneNumberTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_edit_otp_screen, 0);
                SpannableString content = new SpannableString(myString);
                content.setSpan(new UnderlineSpan(), 0, myString.length(), 0);
                resendTextView.setText(content);

            }
        }.start();

        mTimerRunning = true;
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        timerTextView.setText(timeLeftFormatted);
    }

    @Override
    public void onOtpReceived(String otp) {

        String smsOTP = otp.substring(0, 4);
        Log.d(TAG, "onOtpReceived: " + smsOTP);
        userOTPEditText.setText(smsOTP);
    }

    @Override
    public void onOtpTimeout() {

    }
}
