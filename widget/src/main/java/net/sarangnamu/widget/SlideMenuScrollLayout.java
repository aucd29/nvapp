package net.sarangnamu.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import java.util.ArrayList;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 6. 29. <p/>
 */
public final class SlideMenuScrollLayout extends ScrollView {
    private boolean mLock = true;
    private final ArrayList<ScrollChangedListener> mViewList = new ArrayList<>();

    public SlideMenuScrollLayout(Context context) {
        super(context);
    }

    public SlideMenuScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideMenuScrollLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mLock && super.onInterceptTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mLock && super.onTouchEvent(ev);
    }

    public void unlock() {
        mLock = false;
    }

    public void addViewListener(@NonNull ScrollChangedListener listener) {
        if (!mViewList.contains(listener)) {
            mViewList.add(listener);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        for(ScrollChangedListener listener: mViewList) {
            listener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ScrollChangedListener
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public interface ScrollChangedListener {
        void onScrollChanged(View parent, int l, int t, int oldl, int oldt);
    }
}
