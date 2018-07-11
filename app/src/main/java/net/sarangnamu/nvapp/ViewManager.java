package net.sarangnamu.nvapp;

import net.sarangnamu.libfragment.BaseFragmentManager;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 10. <p/>
 */
public class ViewManager extends BaseFragmentManager {
    private static ViewManager mInst;

    public static ViewManager get() {
        if (mInst == null) {
            mInst = new ViewManager();
        }

        return mInst;
    }

    private ViewManager() {

    }
}
