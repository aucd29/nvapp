package net.sarangnamu.widget;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;

import java.lang.reflect.Field;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 6. 29. <p/>
 *
 *
 * https://stackoverflow.com/questions/16754305/full-width-navigation-drawer
 */
public class FullScreenDrawerLayout extends DrawerLayout {
    public FullScreenDrawerLayout(Context context) {
        super(context);
        initLayout();
    }
    
    public FullScreenDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout();
    }
    
    public FullScreenDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout();
    }
    
    protected void initLayout() {
        try {
            Field field = getClass().getSuperclass().getDeclaredField("mMinDrawerMargin");
            field.setAccessible(true);
            field.set(this, Integer.valueOf(0));
        } catch (Exception e) {
            throw new IllegalStateException("android.support.v4.widget.DrawerLayout has changed and you have to fix this class.", e);
        }
    }
}
