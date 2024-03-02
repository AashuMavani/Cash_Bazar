package com.reward.cashbazar.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class Custom_ScrollView extends ScrollView {
    private boolean mScrollable = true;

    public Custom_ScrollView(Context context) {
        super(context);
    }

    public Custom_ScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Custom_ScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Custom_ScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mScrollable) return false;
        return super.onInterceptTouchEvent(ev);
    }

    public void lockScrollView(boolean flag) {
        mScrollable = flag;
    }
}
