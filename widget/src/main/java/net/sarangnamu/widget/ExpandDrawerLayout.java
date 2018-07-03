package net.sarangnamu.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;

import java.util.ArrayList;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 6. 29. <p/>
 */
public final class ExpandDrawerLayout extends DrawerLayout {
    private final ArrayList<ExpandListener> mExpandList = new ArrayList<>();

    public ExpandDrawerLayout(Context context) {
        super(context);
    }
    
    public ExpandDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public ExpandDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public void addExpandListener(@NonNull final ExpandListener event) {
        if (!mExpandList.contains(event)) {
            mExpandList.add(event);
        }
    }

    @Override
    public void openDrawer(int gravity, boolean animate) {
        for (final ExpandListener listener : mExpandList) {
            listener.onExpand();
        }

        super.openDrawer(gravity, animate);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // IExpandListener
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public interface ExpandListener {
        void onExpand();
    }
}
