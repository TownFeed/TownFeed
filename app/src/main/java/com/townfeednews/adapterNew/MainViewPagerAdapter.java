package com.townfeednews.adapterNew;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.townfeednews.IOClickListener.RefreshDataListener;
import com.townfeednews.fragments.NewsContentFragment;
import com.townfeednews.fragments.WebViewFragment;
import com.townfeednews.utils.AppConstant;

public class MainViewPagerAdapter extends FragmentStatePagerAdapter {
    private RefreshDataListener refreshDataListener;

    public MainViewPagerAdapter(@NonNull FragmentManager fm, RefreshDataListener refreshDataListener) {
        super(fm);
        this.refreshDataListener = refreshDataListener;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NewsContentFragment(refreshDataListener);
            case 1:
                AppConstant.webViewFragment = new WebViewFragment();
                return AppConstant.webViewFragment;
        }
        return new NewsContentFragment(refreshDataListener);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
