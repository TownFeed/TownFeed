package com.townfeednews.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
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
//import com.townfeednews.IOInterface.OtpReceivedInterface;
import com.townfeednews.R;
import com.townfeednews.broadcast.SmsBroadcastReceiver;
import com.townfeednews.connection.ApiInterface;
import com.townfeednews.connection.OtpReceivedInterface;
import com.townfeednews.connection.TownFeedBaseUrl;
import com.townfeednews.model.otp.OTPResponseData;
import com.townfeednews.utils.AppUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPScreenOld extends AppCompatActivity implements OtpReceivedInterface {
    private EditText userMobileEditText, otpEditText;
    private Button verifyNowButton;
    private ProgressDialog progressDialog;
    private TextView userMobileTextView, otpSentTextView, otpTimerTextView;
    private LinearLayout editMobileNumberLinearLayout, otpScreenHeaderLinearLayout;
    private CountDownTimer countDownTimer;
    private ApiInterface retrofit;
    private SmsBroadcastReceiver mSmsBroadcastReceiver;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpscreen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sign Up");

        initView();

    }

    private void initView() {
        userMobileEditText = findViewById(R.id.userMobileEditText);
        verifyNowButton = findViewById(R.id.verifyNowButton);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        userMobileTextView = findViewById(R.id.userMobileTextView);
        editMobileNumberLinearLayout = findViewById(R.id.editMobileNumberLinearLayout);
        otpScreenHeaderLinearLayout = findViewById(R.id.otpScreenHeaderLinearLayout);
        otpSentTextView = findViewById(R.id.otpSentTextView);
        otpEditText = findViewById(R.id.otpEditText);
        otpTimerTextView = findViewById(R.id.otpTimerTextView);
        retrofit = TownFeedBaseUrl.getRetrofit().create(ApiInterface.class);
        mSmsBroadcastReceiver = new SmsBroadcastReceiver();
        mSmsBroadcastReceiver.setOnOtpListeners(this);
        mSmsBroadcastReceiver.setOnOtpListeners(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        getApplicationContext().registerReceiver(mSmsBroadcastReceiver, intentFilter);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        countDownTimer = new CountDownTimer(120000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                otpTimerTextView.setText("Resend OTP in " + AppUtils.getMstoHHMMSS(millisUntilFinished));
                otpTimerTextView.setClickable(false);
            }

            @Override
            public void onFinish() {
                otpTimerTextView.setText("Resend OTP");
                otpTimerTextView.setClickable(true);
            }
        };

        otpTimerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                sendOTPfromServer(userMobileEditText.getText().toString().trim());
                countDownTimer.start();
            }
        });

        otpEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 4) {
                    sendOTPtoVerify();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editMobileNumberLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userMobileEditText.setVisibility(View.VISIBLE);
                editMobileNumberLinearLayout.setVisibility(View.GONE);
                verifyNowButton.setText("Verify Now");
                verifyNowButton.setBackgroundResource(R.drawable.button_bg_otp_screen_active);
                verifyNowButton.setTextColor(Color.WHITE);
                verifyNowButton.setEnabled(true);
            }
        });


        verifyNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                verifyNowButton.setEnabled(false);
                userMobileTextView.setText("+91 " + userMobileEditText.getText().toString().trim());
                userMobileEditText.setVisibility(View.GONE);
                editMobileNumberLinearLayout.setVisibility(View.VISIBLE);
                verifyNowButton.setBackgroundResource(R.drawable.button_bg_otp_screen_inactive);
                verifyNowButton.setTextColor(Color.parseColor("#777777"));
                otpScreenHeaderLinearLayout.setVisibility(View.GONE);
                countDownTimer.cancel();
                sendOTPfromServer(userMobileEditText.getText().toString());

            }


        });

        userMobileEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 10) {
                    verifyNowButton.setBackgroundResource(R.drawable.button_bg_otp_screen_active);
                    verifyNowButton.setEnabled(true);
                    verifyNowButton.setTextColor(Color.WHITE);
                    AppUtils.hideKeyboard(OTPScreenOld.this);
                } else {
                    verifyNowButton.setBackgroundResource(R.drawable.button_bg_otp_screen_inactive);
                    verifyNowButton.setEnabled(false);
                    verifyNowButton.setTextColor(Color.parseColor("#777777"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void sendOTPfromServer(String mobileNumber) {
        Call<OTPResponseData> pojoCall = retrofit.getOTPSms(mobileNumber);
        pojoCall.enqueue(new Callback<OTPResponseData>() {
            @Override
            public void onResponse(Call<OTPResponseData> call, Response<OTPResponseData> response) {
                progressDialog.dismiss();
                verifyNowButton.setText("OTP Sent");
                otpSentTextView.setVisibility(View.VISIBLE);
                otpEditText.setVisibility(View.VISIBLE);
                otpTimerTextView.setVisibility(View.VISIBLE);
                countDownTimer.start();
//                if (response.body().getData().getMsg().equals("success")){
//                    startSMSListener();
//                }

            }

            @Override
            public void onFailure(Call<OTPResponseData> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("TAG", "onFailure: " + t.getMessage());
//                Toast.makeText(OTPScreenOld.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startSMSListener() {
        SmsRetrieverClient mClient = SmsRetriever.getClient(this);
        Task<Void> mTask = mClient.startSmsRetriever();
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                Toast.makeText(OTPScreenOld.this, "SMS Retriever starts", Toast.LENGTH_LONG).show();
            }
        });
        mTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(OTPScreenOld.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendOTPtoVerify() {
        AppUtils.hideKeyboard(OTPScreenOld.this);
        progressDialog.setTitle("Verifying");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        Call<OTPResponseData> pojoCall = retrofit.verifyOtp(userMobileEditText.getText().toString(),password);
        pojoCall.enqueue(new Callback<OTPResponseData>() {
            @Override
            public void onResponse(Call<OTPResponseData> call, Response<OTPResponseData> response) {
                progressDialog.dismiss();
//                if (response.body().getData().getMsg().equals("success")){
//                    final ProgressDialog getNewsProgressDialog = new ProgressDialog(OTPScreenOld.this);
//                    getNewsProgressDialog.setTitle("Please wait");
//                    getNewsProgressDialog.setMessage("Loading...");
//                    getNewsProgressDialog.setCancelable(false);
//                    getNewsProgressDialog.show();
//
//                    ApiInterface retrofit = BaseUrlOld.getRetrofit().create(ApiInterface.class);
//                    retrofit.getNewsData().enqueue(new Callback<NewsResponseData>() {
//                        @Override
//                        public void onResponse(Call<NewsResponseData> call, Response<NewsResponseData> response) {
//                            Log.d("TAG", "onResponse: News Data" + response);
//                            getNewsProgressDialog.dismiss();
//                            if (response.isSuccessful()) {
//                                newsArrayList = response.body().getNews();
//                                AppPrefs.setFirstTimeUserFalse(OTPScreenOld.this);
//                                startActivity(new Intent(OTPScreenOld.this, HomeActivityOld.class));
//                                finishAffinity();
//                            }
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<NewsResponseData> call, Throwable t) {
//                            getNewsProgressDialog.dismiss();
//                            // TODO: 04-10-2019 No Internet connection screen.
//                        }
//                    });
//                } else {
//                    Toast.makeText(OTPScreenOld.this, "" + response.body().getData().getMsg(), Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onFailure(Call<OTPResponseData> call, Throwable t) {
                progressDialog.dismiss();
//                Toast.makeText(OTPScreenOld.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public void onOtpReceived(String otp) {
        password = otp.substring(0,4) ;
        Log.d("TAG", "onOtpReceived: " + otp + " password " + password);
        otpEditText.setText(password);
    }

    @Override
    public void onOtpTimeout() {
//        Toast.makeText(this, "OTP time out", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}