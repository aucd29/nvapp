package net.sarangnamu.libcore;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 20. <p/>
 */
public class AppTerminator {
    private static final Logger mLog = LoggerFactory.getLogger(AppTerminator.class);

    private static final int DELAY = 2000;

    private long mPressedTime = 0;
    private Snackbar mSnackbar;

    private Activity mActivity;
    private View mView;

    public static AppTerminator create(@NonNull Activity activity, @NonNull View view) {
        return new AppTerminator(activity, view);
    }

    private AppTerminator(@NonNull Activity activity, @NonNull View view) {
        mActivity = activity;
        mView = view;
    }

    private long time() {
        return mPressedTime + DELAY;
    }

    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        long delayTime   = time();

        if (currentTime > delayTime) {
            mPressedTime = System.currentTimeMillis();
            show();

            return ;
        }

        if (currentTime <= delayTime) {
            if (mSnackbar != null) {
                mSnackbar.dismiss();
            }

            ActivityCompat.finishAffinity(mActivity);
        }
    }

    private void show() {
        if (mView == null) {
            mLog.error("ERROR: mView == null");
            return ;
        }

        mSnackbar = Snackbar.make(mView, R.string.bkcore_click_exit_again, Snackbar.LENGTH_LONG);
        mSnackbar.show();
    }

}
