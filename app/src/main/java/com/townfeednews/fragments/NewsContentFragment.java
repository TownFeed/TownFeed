package com.townfeednews.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.townfeednews.utils.AppPrefsMain;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.townfeednews.utils.AppConstant.newsResponseDataList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsContentFragment extends Fragment {
    public static SwipeRefreshLayout swipeRefreshLayout;
    private VerticalViewPager verticalViewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private FragmentActivity mContext;
    private RefreshDataListener refreshDataListener;

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
        return view;
    }

    private void initView(View view) {
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        verticalViewPager = view.findViewById(R.id.verticalViewPager);
        viewPagerAdapter = new ViewPagerAdapter(mContext.getSupportFragmentManager());

        verticalViewPager.setAdapter(viewPagerAdapter);

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
