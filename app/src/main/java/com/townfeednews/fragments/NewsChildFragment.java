package com.townfeednews.fragments;


import android.os.Bundle;
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
import com.townfeednews.R;
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

    public NewsChildFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
                Toast.makeText(getContext(), "Clicked...", Toast.LENGTH_SHORT).show();
                if (AppPrefsMain.getReadNewsEnabled(getContext())) {
                    AppPrefsMain.setReadNewsEnabled(getContext(), false);
                    speakTextImageView.setImageResource(R.drawable.ic_text_to_speak_mute);
                    if (textToSpeech != null && textToSpeech.isSpeaking()) {
                        textToSpeech.stop();
                    }
                } else {
                    AppPrefsMain.setReadNewsEnabled(getContext(), true);

                }

                //Main Text goes here.
                if (textToSpeech != null) {
                    if (AppConstant.textToSpeech.isSpeaking()) {
                        AppConstant.textToSpeech.stop();
                        AppPrefsMain.setReadNewsEnabled(getContext(), false);

                    }
                } else {
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

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstant.mainViewPager.setCurrentItem(1);
            }
        });

        shareNewsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "Clicked..", Toast.LENGTH_SHORT).show();
            }
        });

        return view;

    }
}
