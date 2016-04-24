package com.vojkovladimir.gallery.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author vojkovladimir.
 */
public class HackyViewPager extends ViewPager {

    public HackyViewPager(@NonNull Context context) {
        super(context);
    }

    public HackyViewPager(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
