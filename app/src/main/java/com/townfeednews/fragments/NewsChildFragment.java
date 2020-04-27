package com.townfeednews.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.townfeednews.BuildConfig;
import com.townfeednews.R;
import com.townfeednews.activity.HomeActivityMain;
import com.townfeednews.utils.AppConstant;
import com.townfeednews.utils.AppPrefs;
import com.townfeednews.utils.AppPrefsMain;

import java.util.Locale;

import static com.townfeednews.utils.AppConstant.newsResponseDataList;
import static com.townfeednews.utils.AppConstant.textToSpeech;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsChildFragment extends Fragment {
    private ImageView newsImageView;
    private TextView mainTitleTextView, descriptionTextView, categoryTextView, sourceTextView, postedDateTextView;
    private int position;
    private ImageView shareNewsImageView;
    private ImageView speakTextImageView;
    private static final String TAG = "NewsChildFragment";
    private Context context;
    private boolean isVisible2User;

    public NewsChildFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment'
        Log.d(TAG, "onCreateView: chal gya ");
        View view = inflater.inflate(R.layout.fragment_news_child, container, false);
        newsImageView = view.findViewById(R.id.newsImageView);
        mainTitleTextView = view.findViewById(R.id.mainTitleTextView);
        descriptionTextView = view.findViewById(R.id.descriptionTextView);
        sourceTextView = view.findViewById(R.id.sourceTextView);
        postedDateTextView = view.findViewById(R.id.postedDateTextView);
        categoryTextView = view.findViewById(R.id.categoryTextView);
        shareNewsImageView = view.findViewById(R.id.shareNewsImageView);
        speakTextImageView = view.findViewById(R.id.speakTextImageView);

//        Log.d(TAG, "onCreateView: Read News Enabled" + AppPrefsMain.getReadNewsEnabled(getContext()));

        if (AppPrefsMain.getReadNewsEnabled(getContext())) {
            speakTextImageView.setImageResource(R.drawable.ic_text_to_speak);
            Log.d(TAG, "onCreateView: Read News is Enabled ");
        } else {
            speakTextImageView.setImageResource(R.drawable.ic_text_to_speak_mute);
            Log.d(TAG, "onCreateView: Read News is Disabled ");
        }

        speakTextImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "Clicked...", Toast.LENGTH_SHORT).show();
                if (AppPrefsMain.getReadNewsEnabled(getContext())) {
                    AppPrefsMain.setReadNewsEnabled(getContext(), false);
                    speakTextImageView.setImageResource(R.drawable.ic_text_to_speak_mute);
                    if (textToSpeech != null && textToSpeech.isSpeaking()) {
                        textToSpeech.stop();
                    }
                } else {
                    AppPrefsMain.setReadNewsEnabled(getContext(), true);
                    if (AppPrefsMain.getUserLanguage(getContext()).equalsIgnoreCase("eng")) {
                        speakTextImageView.setImageResource(R.drawable.ic_text_to_speak);
                        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int status) {
                                if (status != TextToSpeech.ERROR) {
                                    textToSpeech.setSpeechRate(0.8f);
//                    textToSpeech.setSpeechRate(0.8f);
                                    textToSpeech.setPitch(0.8f);
//                    textToSpeech.setPitch(0.8f);
//                    textToSpeech.setLanguage(Locale.US);
                                    textToSpeech.setLanguage(Locale.US);
                                    String text = mainTitleTextView.getText().toString() + descriptionTextView.getText().toString();
                                    textToSpeech.speak(text + "\n\n" + newsResponseDataList.get(0).getDetails(), TextToSpeech.QUEUE_FLUSH, null);
//                                    Log.d(TAG, "onPageSelected: news Details " + newsResponseDataList.get(0).getDetails());
                                }
                            }
                        });

                    } else {
                        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int status) {
                                if (status != TextToSpeech.ERROR) {
                                    textToSpeech.setSpeechRate(0.8f);
//                    textToSpeech.setSpeechRate(0.8f);
                                    textToSpeech.setPitch(0.8f);
//                    textToSpeech.setPitch(0.8f);
//                    textToSpeech.setLanguage(Locale.US);
                                    textToSpeech.setLanguage(Locale.forLanguageTag("hin"));
                                    String text = mainTitleTextView.getText().toString() + descriptionTextView.getText().toString();
                                    textToSpeech.speak(text + "\n\n" + newsResponseDataList.get(0).getDetails(), TextToSpeech.QUEUE_FLUSH, null);
//                                    Log.d(TAG, "onPageSelected: news Title " + newsResponseDataList.get(0).getTitle());
                                }
                            }
                        });
                    }
                }

                //Main Text goes here.
//                if (textToSpeech != null) {
//                    if (AppConstant.textToSpeech.isSpeaking()) {
//                        AppConstant.textToSpeech.stop();
//                        AppPrefsMain.setReadNewsEnabled(getContext(), false);
//
//                    }
//                } else {
//
//                }
            }
        });

        Bundle bundle = getArguments();

        position = bundle.getInt("position");

        Glide.with(this).load(AppConstant.newsResponseDataList.get(position).getHdImage())
                .transition(DrawableTransitionOptions.withCrossFade())
                .thumbnail(Glide.with(this).load(AppConstant.newsResponseDataList.get(position).getThumbnail()))
                .into(newsImageView);

        mainTitleTextView.setText(Html.fromHtml(AppConstant.newsResponseDataList.get(position).getTitle()));

        descriptionTextView.setText(Html.fromHtml(AppConstant.newsResponseDataList.get(position).getDetails()));
        sourceTextView.setText(AppConstant.newsResponseDataList.get(position).getSource());
        postedDateTextView.setText(AppConstant.newsResponseDataList.get(position).getDatetime());
        categoryTextView.setText(AppConstant.newsResponseDataList.get(position).getCategory());
        if (position == 0) {
            AppConstant.webViewFragment.setCurrentItem(AppConstant.newsResponseDataList.get(position).getPermalink());
        } else {
            AppConstant.webViewFragment.setCurrentItem(AppConstant.newsResponseDataList.get(position - 1).getPermalink());
        }

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AppConstant.mainViewPager.setCurrentItem(1);
                return false;
            }
        });
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AppConstant.mainViewPager.setCurrentItem(1);
//            }
//        });

        shareNewsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/html");  //image/jpeg, audio/mpeg4-generic, text/html, audio/mpeg
//                    audio/aac, audio/wav, audio/ogg, audio/midi, audio/x-ms-wma, video/mp4, video/x-msvideo
//                    video/x-ms-wmv, image/png, image/jpeg, image/gif, .xml ->text/xml, .txt -> text/plain
//                    .cfg -> text/plain, .csv -> text/plain, .conf -> text/plain, .rc -> text/plain
//                    .htm -> text/html,.html -> text/html,.pdf -> application/pdf,.apk -> application/vnd.android.package-archive
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "TownFeed");
                    String shareMessage = "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Oops ! Something went wrong. Please try after sometime.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;

    }

}
