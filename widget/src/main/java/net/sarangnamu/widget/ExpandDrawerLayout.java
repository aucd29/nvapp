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
    private final ArrayList<IExpandListener> mExpandList = new ArrayList<>();

    public ExpandDrawerLayout(Context context) {
        super(context);
        initLayout();
    }
    
    public ExpandDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout();
    }
    
    public ExpandDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout();
    }
    
    protected void initLayout() {
    
    }

    public void addExpandListener(@NonNull final IExpandListener event) {
        if (!mExpandList.contains(event)) {
            mExpandList.add(event);
        }
    }

    @Override
    public void openDrawer(int gravity, boolean animate) {
        for (final IExpandListener listener : mExpandList) {
            listener.onExpand();
        }

        super.openDrawer(gravity, animate);
    }

    public interface IExpandListener {
        void onExpand();
    }
}
