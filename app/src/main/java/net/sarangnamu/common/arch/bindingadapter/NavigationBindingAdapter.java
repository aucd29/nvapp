package net.sarangnamu.common.arch.bindingadapter;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Point;
import android.support.design.widget.NavigationView;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 10. <p/>
 */
public class NavigationBindingAdapter {
    @BindingAdapter("bindFullscreen")
    public static void fullsceen(NavigationView view, int value) {
//        Context activity = view.getContext();
//        WindowManager manager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
//
//        Point pt = new Point();
//        manager.getDefaultDisplay().getSize(pt);
//
//        ViewGroup.LayoutParams lp = view.getLayoutParams();
//        lp.width = pt.x;
//
//        view.setLayoutParams(lp);
    }
}
