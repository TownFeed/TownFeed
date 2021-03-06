package com.townfeednews.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.townfeednews.IOClickListener.RefreshDataListener;
import com.townfeednews.R;
import com.townfeednews.VerticalPagerHelper.VerticalViewPager;
import com.townfeednews.VerticalPagerHelper.ViewPagerAdapter;
import com.townfeednews.activity.HomeActivityMain;
import com.townfeednews.adapterNew.MainViewPagerAdapter;
import com.townfeednews.connection.ApiInterface;
import com.townfeednews.connection.TownFeedBaseUrl;
import com.townfeednews.model.news.NewsResponseData;
import com.townfeednews.roomDB.News;
import com.townfeednews.utils.AppConstant;
import com.townfeednews.utils.AppPrefs;
import com.townfeednews.utils.AppPrefsMain;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.townfeednews.utils.AppConstant.newsResponseDataList;
import static com.townfeednews.utils.AppConstant.textToSpeech;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsContentFragment extends Fragment {
    public static SwipeRefreshLayout swipeRefreshLayout;
    private VerticalViewPager verticalViewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private FragmentActivity mContext;
    private RefreshDataListener refreshDataListener;
    private static final String TAG = "NewsContentFragment";

    public NewsContentFragment() {
        // Required empty public constructor

    }

    public NewsContentFragment(RefreshDataListener refreshDataListener) {
        this.refreshDataListener = refreshDataListener;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        mContext = (FragmentActivity) context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_content, container, false);
        initView(view);

        if (AppPrefsMain.getReadNewsEnabled(getContext())) {
            if (AppPrefsMain.getUserLanguage(getContext()).equalsIgnoreCase("eng")) {
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
                            String text = newsResponseDataList.get(0).getTitle();
                            textToSpeech.speak(text + "\n\n" + newsResponseDataList.get(0).getDetails(), TextToSpeech.QUEUE_FLUSH, null);
                            Log.d(TAG, "onPageSelected: news Details " + newsResponseDataList.get(0).getDetails());

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
                            String text = newsResponseDataList.get(0).getTitle();
                            textToSpeech.speak(text + "\n\n" + newsResponseDataList.get(0).getDetails(), TextToSpeech.QUEUE_FLUSH, null);
                            Log.d(TAG, "onPageSelected: news Title " + newsResponseDataList.get(0).getTitle());

                        }
                    }
                });
            }
        }
        return view;
    }

    private void initView(View view) {
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        verticalViewPager = view.findViewById(R.id.verticalViewPager);
        viewPagerAdapter = new ViewPagerAdapter(mContext.getSupportFragmentManager());

        verticalViewPager.setAdapter(viewPagerAdapter);
        verticalViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "onPageScrolled: page changing " + position);
            }

            @Override
            public void onPageSelected(final int position) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        AppConstant.webViewFragment.setCurrentItem(newsResponseDataList.get(position).getPermalink());
                    }
                };
                if (AppPrefsMain.getReadNewsEnabled(getContext())) {
                    if (textToSpeech.isSpeaking()) {
                        textToSpeech.stop();
                    }
                    if (AppPrefsMain.getUserLanguage(getContext()).equalsIgnoreCase("eng")) {
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
                                    String text = newsResponseDataList.get(position).getTitle()
                                            + newsResponseDataList.get(position).getDetails();
                                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                                    Log.d(TAG, "onPageSelected: news Title " + newsResponseDataList.get(0).getTitle());
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
                                    String text = newsResponseDataList.get(position).getTitle() + newsResponseDataList.get(position).getDetails();
                                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                                    Log.d(TAG, "onPageSelected: news Title " + newsResponseDataList.get(0).getTitle());
                                }
                            }
                        });
                    }
                    Log.d(TAG, "onPageSelected: page selected " + position);
                    Log.d(TAG, "onPageSelected: news Title " + newsResponseDataList.get(position).getTitle());
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d(TAG, "onPageScrollStateChanged: page scrolled : " + state);
            }
        });

        Log.d("TAG", "initView: Adapter called ");

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                refreshDataListener.refreshListData();
            }
        });
    }
}
