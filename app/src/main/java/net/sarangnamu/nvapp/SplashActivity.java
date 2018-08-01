package net.sarangnamu.nvapp;

import android.os.Bundle;
import android.support.annotation.Nullable;

import net.sarangnamu.common.widget.BaseActivity;
import net.sarangnamu.nvapp.databinding.SplashMainBinding;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 11. <p/>
 */
public class SplashActivity extends BaseActivity<SplashMainBinding> {
    private static final Logger mLog = LoggerFactory.getLogger(SplashActivity.class);

    public static final int SPLASH_ACTION_ID = 1230;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // FIXME
        // theme 에서 설정한 값이 fullscreen 형태로 동작하다 보니 pixel 을 맞춰야 되는데
        // 일단 좀 귀찮은데 보기는 안 좋아서 theme 에는 색만 넣고 splash 화면을 fade in 하도록
        // 수정
        // 추후 맞춰서 진행해야 할 듯
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        initBinding();

        // TODO 서버에서 데이터를 전달 받아야할 작업들은 이곳에서 진행 한다.

        // SPLASH 최소 시간은 현재 1초
        findViewById(R.id.splash).postDelayed(this::closeSplash, 1000);
    }

    @Override
    protected int layoutId() {
        return R.layout.splash_main;
    }

    @Override
    public void onBackPressed() { }

    private void closeSplash() {
        finish();
        overridePendingTransition(0, android.R.anim.fade_out);
    }
}
