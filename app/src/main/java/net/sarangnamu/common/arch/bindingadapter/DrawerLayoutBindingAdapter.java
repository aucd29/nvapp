package net.sarangnamu.common.arch.bindingadapter;

import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 24. <p/>
 */
public class DrawerLayoutBindingAdapter {
    @BindingAdapter("bindDrawerLockMode")
    public static void bindDrawerLayoutLockMode(@NonNull DrawerLayout view, int mode) {
        view.setDrawerLockMode(mode);
    }
}
