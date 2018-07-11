package net.sarangnamu.nvapp;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;

import net.sarangnamu.common.widget.BaseActivity;
import net.sarangnamu.libfragment.FragmentParams;
import net.sarangnamu.libtutorial.TutorialFragment;
import net.sarangnamu.libtutorial.TutorialParams;
import net.sarangnamu.nvapp.databinding.ActivityMainBinding;
import net.sarangnamu.nvapp.main.MainFragment;
import net.sarangnamu.nvapp.main.viewmodel.NavigationViewModel;
import net.sarangnamu.nvapp.model.DataManager;
import net.sarangnamu.nvapp.splash.SplashFragment;
import net.sarangnamu.nvapp.tutorial.TutorialViewModel;
import net.sarangnamu.nvapp.viewmodel.UserInfoViewModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.internal.operators.observable.ObservableInterval;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    private static final Logger mLog = LoggerFactory.getLogger(MainActivity.class);

    private CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NavigationViewModel nav = viewModel(NavigationViewModel.class);
        UserInfoViewModel user  = viewModel(UserInfoViewModel.class);

        nav.init(this);
        user.init();

        mBinding.navMain.setVmodel(nav);
        mBinding.navMain.setUser(user);

        fullscreenNaviView();

        if (savedInstanceState == null) {
            ViewManager.get().setFragmentManager(this);

            loadMain();
            loadSplash();
        }
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

    private void fullscreenNaviView() {
        DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) mBinding.navView.getLayoutParams();
        lp.width = MainApp.screenX;
        mBinding.navView.setLayoutParams(lp);
    }

    private void loadMain() {
        if (mLog.isDebugEnabled()) {
            mLog.debug("MAIN START");
        }

        ViewManager.get().show(FragmentParams.builder().containerId(R.id.layout_main)
            .fragment(MainFragment.class).addMode().build());
    }

    private void loadSplash() {
        if (mLog.isDebugEnabled()) {
            mLog.debug("SPLASH START");
        }

        ViewManager.get().show(FragmentParams.builder().containerId(R.id.layout_main)
            .fragment(SplashFragment.class).build());

        // network 작업 후 진행 되어야 하는데 임시로 일단 코드로 수정
        mDisposable.add(Observable.interval(3, TimeUnit.SECONDS)
            .take(1)
            .subscribe(v -> {
                if (mLog.isDebugEnabled()) {
                    mLog.debug("SPLASH END");
                }

                ViewManager.get().popBack();
                loadTutorial();
            }));
    }

    private void loadTutorial() {
        TutorialViewModel vmodel = viewModel(TutorialViewModel.class);
        if (!vmodel.isFinished()) {
            if (mLog.isDebugEnabled()) {
                mLog.debug("TUTORIAL START");
            }

            ViewManager.get().show(FragmentParams.builder().containerId(R.id.layout_main)
                .fragment(TutorialFragment.class).bundle(TutorialParams.builder()
                    .view(R.layout.tutorial_0)
                    .view(R.layout.tutorial_1)
                    .viewModel(vmodel).resultListener((result, obj) -> {
                        ViewManager.get().popBack();
                    }).build().bundle())
                .build());
        }
    }
}
