package net.sarangnamu.common.widget;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 24. <p/>
 */
public class LockedNavigationView extends NavigationView {
    private boolean mLocked = true;

    public LockedNavigationView(Context context) {
        super(context);
    }

    public LockedNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LockedNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLocked) {
            return false;
        }

        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        if (mLocked) {
            return false;
        }

        return super.onInterceptHoverEvent(event);
    }
}
