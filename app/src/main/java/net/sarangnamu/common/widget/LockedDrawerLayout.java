package net.sarangnamu.common.widget;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.lang.reflect.Field;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 6. 29. <p/>
 *
 *
 * https://stackoverflow.com/questions/16754305/full-width-navigation-drawer
 */
public class LockedDrawerLayout extends DrawerLayout {
    private boolean mLocked = true;

    public LockedDrawerLayout(Context context) {
        super(context);
    }
    
    public LockedDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public LockedDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLocked) {
            return true;
        }

        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        if (mLocked) {
            return true;
        }

        return super.onInterceptHoverEvent(event);
    }
}
