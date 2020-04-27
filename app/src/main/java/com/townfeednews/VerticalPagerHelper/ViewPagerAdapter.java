package com.townfeednews.VerticalPagerHelper;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.townfeednews.fragments.NewsChildFragment;
import com.townfeednews.fragments.NewsContentFragment;
import com.townfeednews.utils.AppConstant;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            NewsContentFragment.swipeRefreshLayout.setEnabled(true);
        } else {
            NewsContentFragment.swipeRefreshLayout.setEnabled(false);
        }
        NewsChildFragment news = new NewsChildFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        news.setArguments(bundle);
        return news;
    }

    @Override
    public int getCount() {
//        Log.d("TAG", "getCount: in View Pager Adapter " + AppConstant.newsResponseDataList.size());
        return AppConstant.newsResponseDataList.size();
    }
}
