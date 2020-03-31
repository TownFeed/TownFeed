package com.townfeednews.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
//import com.townfeednews.IOInterface.OtpReceivedInterface;
import com.townfeednews.R;
import com.townfeednews.broadcast.SmsBroadcastReceiver;
import com.townfeednews.connection.ApiInterface;
import com.townfeednews.connection.BaseUrlOld;
import com.townfeednews.connection.TownFeedBaseUrl;
import com.townfeednews.model.newsOld.NewsResponseData;
import com.townfeednews.model.otp.OTPResponseData;
import com.townfeednews.utils.AppPrefs;
import com.townfeednews.utils.AppUtils;
import com.townfeednews.utils.UserProfilePrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.townfeednews.utils.AppConstant.newsArrayList;

public class SocialLoginAndLanguageold extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private TextView hindiTextView, englishTextView;
    private boolean isBackPressed = false;
    private TextView skipTextView;
    private FirebaseAuth mAuth;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private final static int RC_SIGN_IN = 2;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private CardView googleLoginCardView, facebookLoginCardView, twitterLoginCardView, mobileLoginCardView;
    private CallbackManager callbackManager;
    private LoginButton facebookLoginButton;
    private ApiInterface retrofit;
    private ProgressDialog progressDialog;
    private Button verifyNowButton;
    private EditText userMobileEditText;
    private String password;
    private EditText otpEditText;
    private TextView otpSentTextView;
    private TextView otpTimerTextView;
    private ImageView closeOTPScreenImageView;
    private CountDownTimer countDownTimer;


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_login_and_language);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getSupportActionBar().hide();
        initView();
        selectNewsContentLanguage();

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void initView() {
        hindiTextView = findViewById(R.id.hindiTextView);
        englishTextView = findViewById(R.id.englishTextView);
        skipTextView = findViewById(R.id.skipTextView);
        googleLoginCardView = findViewById(R.id.googleLoginCardView);
        facebookLoginCardView = findViewById(R.id.facebookLoginCardView);
        twitterLoginCardView = findViewById(R.id.twitterLoginCardView);
        mobileLoginCardView = findViewById(R.id.mobileLoginCardView);
        facebookLoginButton = findViewById(R.id.facebookLoginButton);
        callbackManager = CallbackManager.Factory.create();
        progressDialog = new ProgressDialog(SocialLoginAndLanguageold.this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mAuth = FirebaseAuth.getInstance();

        facebookLoginButton.setReadPermissions(Arrays.asList("email", "public_profile"));

        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//                ApiInterface retrofit = BaseUrlOld.getRetrofit().create(ApiInterface.class);
//                retrofit.getNewsData("dasdf","hi").enqueue(new Callback<NewsResponseData>() {
//                    @Override
//                    public void onResponse(Call<NewsResponseData> call, Response<NewsResponseData> response) {
//                        Log.d("TAG", "onResponse: News Data" + response);
//                        if (response.isSuccessful()) {
//                            newsArrayList = response.body().getNews();
//                            startActivity(new Intent(SocialLoginAndLanguageold.this, HomeActivityOld.class));
//                            finish();
//                        }
//                        progressDialog.dismiss();
//                        AppUtils.hideKeyboard(SocialLoginAndLanguageold.this);
//                    }

//                    @Override
//                    public void onFailure(Call<NewsResponseData> call, Throwable t) {
//                        progressDialog.dismiss();
//                        AppUtils.hideKeyboard(SocialLoginAndLanguageold.this);
//                        // TODO: 04-10-2019 No Internet connection screen.
//                    }
//                });
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        googleLoginCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        facebookLoginCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookLoginButton.performClick();
            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    String userName = firebaseAuth.getCurrentUser().getDisplayName();
                    String mob = firebaseAuth.getCurrentUser().getPhoneNumber();
                    String email = firebaseAuth.getCurrentUser().getEmail();

                    Log.d("TAG", "onAuthStateChanged: Name : " + userName + " Mobile : " + mob
                            + " Email : " + email);

                    UserProfilePrefs.setEmail(SocialLoginAndLanguageold.this, email);
                    UserProfilePrefs.setMobile(SocialLoginAndLanguageold.this, mob);
                    UserProfilePrefs.setUserName(SocialLoginAndLanguageold.this, userName);
                    UserProfilePrefs.setProfileUpdateToServer(SocialLoginAndLanguageold.this,true);
                    // TODO: 08-11-2019 start Home Activity create login session.
                    AppPrefs.setFirstTimeUserFalse(SocialLoginAndLanguageold.this);
                    FirebaseAuth.getInstance().signOut();

                    progressDialog.setTitle("Please wait");
                    progressDialog.setMessage("Loading...");

                    try {
                        progressDialog.show();
                    } catch (Exception e) {

                    }

//                    ApiInterface retrofit = BaseUrlOld.getRetrofit().create(ApiInterface.class);
//                    retrofit.getNewsData().enqueue(new Callback<NewsResponseData>() {
//                        @Override
//                        public void onResponse(Call<NewsResponseData> call, Response<NewsResponseData> response) {
//                            progressDialog.dismiss();
//                            Log.d("TAG", "onResponse: News Data" + response);
//                            if (response.isSuccessful()) {
//                                newsArrayList = response.body().getNews();
//                                startActivity(new Intent(SocialLoginAndLanguageold.this, HomeActivityOld.class));
//                                finish();
//                            }
//                            AppUtils.hideKeyboard(SocialLoginAndLanguageold.this);
//                        }
//
//                        @Override
//                        public void onFailure(Call<NewsResponseData> call, Throwable t) {
//                            progressDialog.dismiss();
//                            AppUtils.hideKeyboard(SocialLoginAndLanguageold.this);
//                            // TODO: 04-10-2019 No Internet connection screen.
//                        }
//                    });

                }
            }
        };

//        googleSingInButton = findViewById(R.id.googleSingInButton);
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(SocialLoginAndLanguageold.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        skipTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

//                ApiInterface retrofit = BaseUrlOld.getRetrofit().create(ApiInterface.class);
//                retrofit.getNewsData().enqueue(new Callback<NewsResponseData>() {
//                    @Override
//                    public void onResponse(Call<NewsResponseData> call, Response<NewsResponseData> response) {
//                        progressDialog.dismiss();
//                        Log.d("TAG", "onResponse: News Data" + response);
//                        if (response.isSuccessful()) {
//                            newsArrayList = response.body().getNews();
//                            AppPrefs.setUserLogOut(SocialLoginAndLanguageold.this);
//                            UserProfilePrefs.setUserName(SocialLoginAndLanguageold.this, "Guest User");
//                            UserProfilePrefs.setEmail(SocialLoginAndLanguageold.this, "");
//                            UserProfilePrefs.setGender(SocialLoginAndLanguageold.this, "");
//                            UserProfilePrefs.setMobile(SocialLoginAndLanguageold.this, "");
//                            startActivity(new Intent(SocialLoginAndLanguageold.this, HomeActivityOld.class));
//                            finish();
//                        }
//                        AppUtils.hideKeyboard(SocialLoginAndLanguageold.this);
//                    }
//
//                    @Override
//                    public void onFailure(Call<NewsResponseData> call, Throwable t) {
//                        progressDialog.dismiss();
//                        AppUtils.hideKeyboard(SocialLoginAndLanguageold.this);
//                        // TODO: 04-10-2019 No Internet connection screen.
//                    }
//                });

                skipTextView.setEnabled(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        skipTextView.setEnabled(true);
                    }
                }, 2000);
            }
        });


    }

    private void getNewsDataFromServer() {

    }

    private AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                Log.d("TAG", "User is logged out : ");
            } else {
                loadUserProfile(currentAccessToken);
            }
        }
    };

    private void loadUserProfile(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");
                    Log.d("TAG", "onCompleted: Facebook Login " + first_name + " Lastname " + last_name + " email "
                            + email + " id " + id);

                    UserProfilePrefs.setEmail(SocialLoginAndLanguageold.this, email);
                    UserProfilePrefs.setMobile(SocialLoginAndLanguageold.this, "");
                    UserProfilePrefs.setUserName(SocialLoginAndLanguageold.this, first_name + " " + last_name);
                    UserProfilePrefs.setProfileUpdateToServer(SocialLoginAndLanguageold.this,true);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();
                    LoginManager.getInstance().logOut();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Auth went wrong " + e.getMessage(), Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void selectNewsContentLanguage() {
        if (AppPrefs.getUserLanguage(SocialLoginAndLanguageold.this).equals("eng")) {
            englishTextView.setBackground(getResources().getDrawable(R.mipmap.btn_active_bg));
            hindiTextView.setBackground(getResources().getDrawable(R.mipmap.btn_inactive_bg));
        } else {
            hindiTextView.setBackground(getResources().getDrawable(R.mipmap.btn_active_bg));
            englishTextView.setBackground(getResources().getDrawable(R.mipmap.btn_inactive_bg));
        }

        hindiTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hindiTextView.setBackground(getResources().getDrawable(R.mipmap.btn_active_bg));
                englishTextView.setBackground(getResources().getDrawable(R.mipmap.btn_inactive_bg));
                AppUtils.getCategoryInfo("hi");
//                AppPrefs.setUserLanguage(SocialLoginAndLanguageold.this, "hi");
            }

        });

        englishTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                englishTextView.setBackground(getResources().getDrawable(R.mipmap.btn_active_bg));
                hindiTextView.setBackground(getResources().getDrawable(R.mipmap.btn_inactive_bg));
                AppUtils.getCategoryInfo("eng");
//                AppPrefs.setUserLanguage(SocialLoginAndLanguageold.this, "eng");
            }

        });
    }


    @Override
    public void onBackPressed() {
        if (isBackPressed) {
            finish();
        }
        Toast.makeText(this, "Please press again to exit the App", Toast.LENGTH_SHORT).show();
        isBackPressed = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isBackPressed = false;
            }
        }, 2000);
    }

    public void socialLogin(View view) {
        switch (view.getId()) {
            case R.id.mobileLoginCardView:
                otpAlertDialog();
                break;
            case R.id.twitterLoginCardView:
//                Toast.makeText(this, "Twitter Login", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void otpAlertDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_otpscreen_temp);
        dialog.setTitle("Sign Up with mobile");
        dialog.setCancelable(false);

        initViewDialog(dialog);
        dialog.show();

    }

    private void initViewDialog(final Dialog dialog) {

        userMobileEditText = dialog.findViewById(R.id.userMobileEditText);
        verifyNowButton = dialog.findViewById(R.id.verifyNowButton);
        progressDialog.setMessage("Please wait...");
        closeOTPScreenImageView = dialog.findViewById(R.id.closeOTPScreenImageView);
        progressDialog.setCancelable(false);
        final TextView userMobileTextView = dialog.findViewById(R.id.userMobileTextView);
        final LinearLayout editMobileNumberLinearLayout = dialog.findViewById(R.id.editMobileNumberLinearLayout);
        otpSentTextView = dialog.findViewById(R.id.otpSentTextView);
        otpEditText = dialog.findViewById(R.id.otpEditText);
        otpTimerTextView = dialog.findViewById(R.id.otpTimerTextView);
        retrofit = TownFeedBaseUrl.getRetrofit().create(ApiInterface.class);
        SmsBroadcastReceiver mSmsBroadcastReceiver = new SmsBroadcastReceiver();
//        mSmsBroadcastReceiver.setOnOtpListeners(SocialLoginAndLanguageold.this);
//        mSmsBroadcastReceiver.setOnOtpListeners(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        getApplicationContext().registerReceiver(mSmsBroadcastReceiver, intentFilter);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        closeOTPScreenImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

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
                AppUtils.hideKeyboard(SocialLoginAndLanguageold.this);
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
                    AppUtils.hideKeyboard(SocialLoginAndLanguageold.this);
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

    private void sendOTPtoVerify() {
        AppUtils.hideKeyboard(this);
        progressDialog.setTitle("Verifying");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        Call<OTPResponseData> pojoCall = retrofit.verifyOtp(userMobileEditText.getText().toString(), password);
        pojoCall.enqueue(new Callback<OTPResponseData>() {
            @Override
            public void onResponse(Call<OTPResponseData> call, Response<OTPResponseData> response) {
                progressDialog.dismiss();
                AppUtils.hideKeyboard(SocialLoginAndLanguageold.this);
//                if (response.body().getData().getMsg().equals("success")) {
//                    final ProgressDialog getNewsProgressDialog = new ProgressDialog(SocialLoginAndLanguageold.this);
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
//                                AppPrefs.setFirstTimeUserFalse(SocialLoginAndLanguageold.this);
//                                UserProfilePrefs.setEmail(SocialLoginAndLanguageold.this, "");
//                                UserProfilePrefs.setMobile(SocialLoginAndLanguageold.this, userMobileEditText.getText().toString().trim());
//                                UserProfilePrefs.setUserName(SocialLoginAndLanguageold.this, "");
//                                UserProfilePrefs.setProfileUpdateToServer(SocialLoginAndLanguageold.this,true);
//                                startActivity(new Intent(SocialLoginAndLanguageold.this, HomeActivityOld.class));
//                                finishAffinity();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<NewsResponseData> call, Throwable t) {
//                            getNewsProgressDialog.dismiss();
//                            // TODO: 04-10-2019 No Internet connection screen.
//                        }
//                    });
//                } else {
//                    Toast.makeText(SocialLoginAndLanguageold.this, "" + response.body().getData().getMsg(), Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onFailure(Call<OTPResponseData> call, Throwable t) {
                progressDialog.dismiss();
                AppUtils.hideKeyboard(SocialLoginAndLanguageold.this);
//                Toast.makeText(SocialLoginAndLanguageold.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendOTPfromServer(String mobileNumber) {
        Call<OTPResponseData> pojoCall = retrofit.getOTPSms(mobileNumber);
        pojoCall.enqueue(new Callback<OTPResponseData>() {
            @Override
            public void onResponse(Call<OTPResponseData> call, Response<OTPResponseData> response) {
                progressDialog.dismiss();
                AppUtils.hideKeyboard(SocialLoginAndLanguageold.this);
                verifyNowButton.setText("OTP Sent");
                otpSentTextView.setVisibility(View.VISIBLE);
                otpEditText.setVisibility(View.VISIBLE);
                otpTimerTextView.setVisibility(View.VISIBLE);
                countDownTimer.start();
//                if (response.body().getData().getMsg().equals("success")) {
//                    startSMSListener();
//                }

            }

            @Override
            public void onFailure(Call<OTPResponseData> call, Throwable t) {
                progressDialog.dismiss();
                AppUtils.hideKeyboard(SocialLoginAndLanguageold.this);
                Log.d("TAG", "onFailure: " + t.getMessage());
//                Toast.makeText(SocialLoginAndLanguageold.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

//    @Override
//    public void onOtpReceived(String otp) {
//        password = otp.substring(0, 4);
//        Log.d("TAG", "onOtpReceived: " + otp + " password " + password);
//        otpEditText.setText(password);
//    }

//    @Override
//    public void onOtpTimeout() {
//
//    }
}
