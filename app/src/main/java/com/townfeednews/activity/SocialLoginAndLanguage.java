package com.townfeednews.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import com.townfeednews.R;
import com.townfeednews.broadcast.SmsBroadcastReceiver;
import com.townfeednews.connection.ApiInterface;
import com.townfeednews.connection.BaseUrlOld;
import com.townfeednews.connection.TownFeedBaseUrl;
import com.townfeednews.model.newsOld.NewsResponseData;
import com.townfeednews.model.otp.OTPResponseData;
import com.townfeednews.utils.AppPrefsMain;
import com.townfeednews.utils.AppUtils;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.townfeednews.utils.AppConstant.newsArrayList;

public class SocialLoginAndLanguage extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private TextView googleTextView, facebookTextView, twitterTextView, phoneTextView;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;
    private final static int RC_SIGN_IN = 2;
    private FirebaseAuth mAuth;
    private CallbackManager callbackManager;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ProgressDialog progressDialog;
    private LoginButton facebookLoginButton;
    private boolean stopTimer = false;
    private ApiInterface retrofit;
    private String password;
    private AlertDialog alertDialog;
    private TwitterLoginButton twitterLoginButton;
    private String userOTP="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);
        setContentView(R.layout.activity_social_login_screen);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initView();


    }

    private void initView() {
        googleTextView = findViewById(R.id.googleTextView);
        facebookTextView = findViewById(R.id.facebookTextView);
        twitterTextView = findViewById(R.id.twitterTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        mAuth = FirebaseAuth.getInstance();
        facebookLoginButton = findViewById(R.id.facebookLoginButton);
        callbackManager = CallbackManager.Factory.create();
        retrofit = TownFeedBaseUrl.getRetrofit().create(ApiInterface.class);
        SmsBroadcastReceiver mSmsBroadcastReceiver = new SmsBroadcastReceiver();
//        mSmsBroadcastReceiver.setOnOtpListeners(SocialLoginAndLanguage.this);
//        mSmsBroadcastReceiver.setOnOtpListeners(SocialLoginAndLanguage.this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        getApplicationContext().registerReceiver(mSmsBroadcastReceiver, intentFilter);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        twitterLoginButton = findViewById(R.id.twitterLoginButton);

        twitterLoginButton.setCallback(new com.twitter.sdk.android.core.Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                final TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;

                TwitterAuthClient authClient = new TwitterAuthClient();
                authClient.requestEmail(session, new com.twitter.sdk.android.core.Callback<String>() {
                    @Override
                    public void success(Result<String> result) {
//                        Toast.makeText(SocialLoginAndLanguage.this, "Email : " + result.data, Toast.LENGTH_SHORT).show();
                        final String email = result.data;
                        TwitterCore.getInstance().getApiClient(session).getAccountService().verifyCredentials(false, true, false).enqueue(new com.twitter.sdk.android.core.Callback<User>() {
                            @Override
                            public void success(Result<User> result) {
                                try {
                                    String name = result.data.name;

                                    Log.d("TAG", "Success twitter login : " + email + " user Name : " + name);
                                    saveUserProfileToPrefs(name, email);

                                } catch (Exception e) {

                                }
                            }

                            @Override
                            public void failure(TwitterException exception) {

                            }
                        });
                    }

                    @Override
                    public void failure(TwitterException exception) {
//                        Toast.makeText(SocialLoginAndLanguage.this, "Did't get user information.", Toast.LENGTH_SHORT).show();
                        final String email = "";
                        TwitterCore.getInstance().getApiClient(session).getAccountService().verifyCredentials(false, true, false).enqueue(new com.twitter.sdk.android.core.Callback<User>() {
                            @Override
                            public void success(Result<User> result) {
                                try {
                                    String name = result.data.name;

                                    Log.d("TAG", "Success twitter login : " + email + " user Name : " + name);
                                    saveUserProfileToPrefs(name, email);
                                } catch (Exception e) {

                                }
                            }

                            @Override
                            public void failure(TwitterException exception) {

                            }
                        });
                    }
                });
//                Toast.makeText(SocialLoginAndLanguage.this, "Twitter login success.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(TwitterException exception) {
//                Toast.makeText(SocialLoginAndLanguage.this, "Twitter login fail", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "failure twitter login: " + exception.getMessage());
            }
        });

//        progressDialog = new ProgressDialog(SocialLoginAndLanguage.this);
//        progressDialog.setTitle("Please wait");
//        progressDialog.setMessage("Loading...");
//        progressDialog.setCancelable(false);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(SocialLoginAndLanguage.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    String userName = firebaseAuth.getCurrentUser().getDisplayName();
                    String mob = firebaseAuth.getCurrentUser().getPhoneNumber();
                    String email = firebaseAuth.getCurrentUser().getEmail();

                    Log.d("TAG", "onAuthStateChanged: Name : " + userName + " Mobile : " + mob
                            + " Email : " + email);

                    saveUserProfileToPrefs(userName, email);
//                    Toast.makeText(SocialLoginAndLanguage.this, "Gmail login success.", Toast.LENGTH_SHORT).show();
//                    UserProfilePrefs.setProfileUpdateToServer(SocialLoginAndLanguage.this,true);
                    // TODO: 08-11-2019 start Home Activity create login session.
//                    AppPrefs.setFirstTimeUserFalse(SocialLoginAndLanguage.this);
                    FirebaseAuth.getInstance().signOut();

//                    progressDialog.setTitle("Please wait");
//                    progressDialog.setMessage("Loading...");

                    try {
//                        progressDialog.show();
                    } catch (Exception e) {

                    }
                }
            }
        };

        facebookLoginButton.setReadPermissions(Arrays.asList("email", "public_profile"));

        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                ApiInterface retrofit = BaseUrlOld.getRetrofit().create(ApiInterface.class);
//                retrofit.getNewsData().enqueue(new Callback<NewsResponseData>() {
//                    @Override
//                    public void onResponse(Call<NewsResponseData> call, Response<NewsResponseData> response) {
//                        Log.d("TAG", "onResponse: News Data" + response);
//                        if (response.isSuccessful()) {
//                            newsArrayList = response.body().getNews();
////                            startActivity(new Intent(SocialLoginAndLanguage.this, HomeActivityOld.class));
////                            finish();
//                        }
////                        progressDialog.dismiss();
//                        AppUtils.hideKeyboard(SocialLoginAndLanguage.this);
//                    }
//
//                    @Override
//                    public void onFailure(Call<NewsResponseData> call, Throwable t) {
////                        progressDialog.dismiss();
//                        AppUtils.hideKeyboard(SocialLoginAndLanguage.this);
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

        googleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        facebookTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookLoginButton.performClick();
            }
        });

        twitterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twitterLoginButton.performClick();
            }
        });

        phoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneVerification();
            }
        });
    }

    private void phoneVerification() {
        startActivity(new Intent(SocialLoginAndLanguage.this,MobileVerification.class));
    }


    private void sendOTPfromServer(String mobileNumber) {
        Call<OTPResponseData> pojoCall = retrofit.getOTPSms(mobileNumber);
        pojoCall.enqueue(new Callback<OTPResponseData>() {
            @Override
            public void onResponse(Call<OTPResponseData> call, Response<OTPResponseData> response) {
//                progressDialog.dismiss();
//                if (response.body().getData().getMsg().equals("success")) {
//
//                    startSMSListener();
//                }

            }

            @Override
            public void onFailure(Call<OTPResponseData> call, Throwable t) {

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

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
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
//                Toast.makeText(this, "Auth went wrong " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
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
                    String name = first_name + " " + last_name;
                    saveUserProfileToPrefs(name, email);

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

    private void saveUserProfileToPrefs(String name, String email) {
        Log.d("TAG", "saveUserProfileToPrefs: " + name + " email " + email);
        AppPrefsMain.setUserName(SocialLoginAndLanguage.this, name);
        AppPrefsMain.setUserEmail(SocialLoginAndLanguage.this, email);

        AppPrefsMain.setUserLoggedIn(SocialLoginAndLanguage.this, true);
        startActivity(new Intent(SocialLoginAndLanguage.this, ChooseNewsLanguageActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

//    @Override
//    public void onOtpReceived(String otp) {
//        password = otp.substring(0, 4);
//
//        EditText otpEditText1 = alertDialog.findViewById(R.id.otpEditText1);
//        EditText otpEditText2 = alertDialog.findViewById(R.id.otpEditText2);
//        EditText otpEditText3 = alertDialog.findViewById(R.id.otpEditText3);
//        EditText otpEditText4 = alertDialog.findViewById(R.id.otpEditText4);
//        final EditText[] otpEditTexts = {otpEditText1, otpEditText2, otpEditText3, otpEditText4};
//
//        for (int i = 0; i <= otpEditTexts.length; i++) {
//            otpEditTexts[i].setText(String.valueOf(password.charAt(i)));
//            Log.d("TAG", "onOtpReceived: password " + password.charAt(i));
//        }
//    }

//    @Override
//    public void onOtpTimeout() {
//
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
