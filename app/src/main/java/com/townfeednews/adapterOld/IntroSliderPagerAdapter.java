package com.townfeednews.adapterOld;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.townfeednews.fragments.Intro1Fragment;
import com.townfeednews.fragments.Intro2Fragment;
import com.townfeednews.fragments.Intro3Fragment;
import com.townfeednews.fragments.Intro4Fragment;

public class IntroSliderPagerAdapter extends FragmentStatePagerAdapter {
    public IntroSliderPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Intro1Fragment();
            case 1:
                return new Intro2Fragment();
            case 2:
                return new Intro3Fragment();
            case 3:
                return new Intro4Fragment();
            default:
                return new Intro1Fragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
