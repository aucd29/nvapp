package net.sarangnamu.nvapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import net.sarangnamu.nvapp.viewmodel.SplashViewModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 11. <p/>
 */
public class SplashActivity extends AppCompatActivity {
    private static final Logger mLog = LoggerFactory.getLogger(SplashActivity.class);

    public static final String FINISH = MainApp.context.getPackageName() + ".bk_activity_finish";

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (mLog.isDebugEnabled()) {
                mLog.debug("RECEIVED ACTION : " + action);
            }

            if (FINISH.equals(action)) {
                finish();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(0, 0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_main);

        if (mLog.isDebugEnabled()) {
            mLog.debug("REGISTER INTENT FILTER : " + FINISH);
        }

        registerReceiver(mReceiver, new IntentFilter(FINISH));
    }

    @Override
    protected void onDestroy() {
        if (mLog.isDebugEnabled()) {
            mLog.debug("UNREGISTER INTENT FILTER : " + FINISH);
        }

        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() { }
}
