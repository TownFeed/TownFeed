package com.townfeednews.VerticalPagerHelper;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class VerticalViewPager extends ViewPager {

    private int min_distance = 100;
    private float downX, downY, upX, upY;
    View v;

    public VerticalViewPager(@NonNull Context context) {
        super(context);
        init();
    }

    public VerticalViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        setPageTransformer(true, new ViewPagerTransform());
        setOverScrollMode(OVER_SCROLL_NEVER);    // for bounce effect on ScrollView
    }

    private MotionEvent SwapXY(MotionEvent event) {
        float x = getWidth();
        float y = getHeight();
        float newX = (event.getY() / y) * y;
        float newY = (event.getX() / x) * x;

        event.setLocation(newX, newY);
        return event;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = super.onInterceptTouchEvent(SwapXY(ev));
        SwapXY(ev);
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        return super.onTouchEvent(SwapXY(ev));
    }

    public class ViewPagerTransform implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.65f;

        @Override
        public void transformPage(@NonNull View page, float position) {

            if (position < -1) {
                page.setAlpha(0);
            } else if (position <= 0) {
                page.setAlpha(1);
                page.setTranslationX(page.getWidth() * -position);
                page.setTranslationY(page.getHeight() * position);
                page.setScaleX(1);
                page.setScaleY(1);
            } else if (position <= 1) {
                page.setAlpha(1 - position);
                page.setTranslationX(page.getWidth() * -position);
                page.setTranslationY(0);
                float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
            } else if (position > 1) {
                page.setAlpha(0);
            }
        }
    }
}
