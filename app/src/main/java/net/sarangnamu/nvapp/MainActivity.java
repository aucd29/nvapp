package net.sarangnamu.nvapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;

import net.sarangnamu.common.widget.BaseActivity;
import net.sarangnamu.libcore.TimeLoger;
import net.sarangnamu.libfragment.FragmentParams;
import net.sarangnamu.libtutorial.TutorialFragment;
import net.sarangnamu.libtutorial.TutorialParams;
import net.sarangnamu.libtutorial.viewmodel.TutorialViewModel;
import net.sarangnamu.nvapp.databinding.ActivityMainBinding;
import net.sarangnamu.nvapp.view.MainFragment;
import net.sarangnamu.nvapp.viewmodel.NavigationViewModel;
import net.sarangnamu.nvapp.model.DataManager;
import net.sarangnamu.nvapp.viewmodel.UserInfoViewModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    private static final Logger mLog = LoggerFactory.getLogger(MainActivity.class);

    private CompositeDisposable mDisposable = new CompositeDisposable();
    private Integer mCounter = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        loadSplash();

        super.onCreate(savedInstanceState);

        // https://developer.android.com/topic/performance/vitals/launch-time
        setTheme(R.style.AppTheme);

        initBinding();
        ViewManager.get().setFragmentManager(this);

        initNavigation();
        initUserInfo();
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(Gravity.START)) {
            mBinding.drawerLayout.closeDrawer(Gravity.START);
            return ;
        }

        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        DataManager.get().destroy();

        mDisposable.clear();
        mDisposable.dispose();

        super.onDestroy();

        if (mLog.isDebugEnabled()) {
            mLog.debug("MAIN END");
        }
    }

    private void loadSplash() {
        if (mLog.isDebugEnabled()) {
            mLog.debug("SPLASH START");
        }

        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
    }

    private void closeSplash() {
        if (mLog.isDebugEnabled()) {
            mLog.debug("SPLASH END");
        }

        // 임시 코드 splash activity 초기화 보다 close 가 먼저들어와서 일단 임시로
        // 코드에 delay 줌

        mBinding.drawerLayout.postDelayed(() -> {
            sendBroadcast(new Intent(SplashActivity.FINISH).setPackage(getPackageName()));
        }, 2000);
    }

    private void matchParentNaviationView() {
        DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) mBinding.navView.getLayoutParams();

        lp.width = MainApp.screenX;
        mBinding.navView.setLayoutParams(lp);
    }

    private void initNavigation() {
        final TimeLoger.TimeLog log = TimeLoger.start("NAVIGATION");

        matchParentNaviationView();
        mDisposable.add(Observable.just(viewModel(NavigationViewModel.class))
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe(vmodel -> {
                vmodel.init(MainActivity.this);
                mBinding.navMain.setVmodel(vmodel);

                log.end();
                loadFragments();
            }));
    }

    private void initUserInfo() {
        final TimeLoger.TimeLog log = TimeLoger.start("USER INFO");

        mDisposable.add(Observable.just(viewModel(UserInfoViewModel.class))
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe(vmodel -> {
                vmodel.init();
                mBinding.navMain.setUser(vmodel);

                log.end();
                loadFragments();
            }));
    }

    private void loadFragments() {
        synchronized (mCounter) {
            if (++mCounter > 1) {
                runOnUiThread(() -> {
                    loadMain();
                    loadTutorial();

                    closeSplash();
                });
            }
        }
    }

    private void loadMain() {
        if (mLog.isDebugEnabled()) {
            mLog.debug("MAIN START");
        }

        ViewManager.get().show(FragmentParams.builder().containerId(R.id.layout_main)
            .fragment(MainFragment.class).addMode().build());
    }

    private void loadTutorial() {
        TutorialViewModel vmodel = viewModel(TutorialViewModel.class);
        if (vmodel.isFinished()) {
            return ;
        }

        if (mLog.isDebugEnabled()) {
            mLog.debug("TUTORIAL START");
        }

        vmodel.params = TutorialParams.builder()
            .view(R.layout.tutorial_0)
            .view(R.layout.tutorial_1)
            .viewDataBindingListener((res, viewDataBindingListener) -> {
                if (mLog.isDebugEnabled()) {
                    mLog.debug("RECEIVED viewDataBindingListener");
                }

            })
            .finishedListener((result, obj) -> ViewManager.get().popBack())
            .build();

        ViewManager.get().show(FragmentParams.builder().containerId(R.id.layout_main)
            .fragment(TutorialFragment.class).build());
    }
}
