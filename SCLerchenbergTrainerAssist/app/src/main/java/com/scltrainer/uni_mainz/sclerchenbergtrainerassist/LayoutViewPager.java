package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Julian on 15.03.2017.
 */

public class LayoutViewPager extends ViewPager {
    private boolean paging;

    public LayoutViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.paging = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return paging? super.onTouchEvent(event) : false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return paging? super.onInterceptTouchEvent(event) : false;
    }

    public void setPagingEnabled(boolean enabled) {
        this.paging = enabled;
    }

    public boolean isPagingEnabled() {
        return paging;
    }
}
