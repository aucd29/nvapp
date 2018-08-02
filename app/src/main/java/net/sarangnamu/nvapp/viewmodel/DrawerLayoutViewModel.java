package net.sarangnamu.nvapp.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 8. 2. <p/>
 */
public class DrawerLayoutViewModel extends ViewModel {
    private static final Logger mLog = LoggerFactory.getLogger(DrawerLayoutViewModel.class);

    public void show(DrawerLayout drawerLayout) {
        if (!drawerLayout.isDrawerOpen(Gravity.START)) {
            if (mLog.isTraceEnabled()) {
                mLog.trace("SHOW NAVIGATION");
            }

            drawerLayout.openDrawer(Gravity.START);
        }
    }

    public void hide(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            if (mLog.isTraceEnabled()) {
                mLog.trace("HIDE NAVIGATION");
            }

            drawerLayout.closeDrawer(Gravity.START);
        }
    }
}
