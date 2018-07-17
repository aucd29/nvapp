package net.sarangnamu.nvapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;

import net.sarangnamu.common.util.DimUtils;
import net.sarangnamu.common.util.Invoke;
import net.sarangnamu.common.widget.BaseActivity;
import net.sarangnamu.libcore.TimeLoger;
import net.sarangnamu.libfragment.FragmentParams;
import net.sarangnamu.libtutorial.TutorialFragment;
import net.sarangnamu.libtutorial.TutorialParams;
import net.sarangnamu.libtutorial.viewmodel.TutorialViewModel;
import net.sarangnamu.nvapp.databinding.ActivityMainBinding;
import net.sarangnamu.nvapp.databinding.Tutorial0Binding;
import net.sarangnamu.nvapp.view.MainFragment;
import net.sarangnamu.nvapp.viewmodel.NavigationViewModel;
import net.sarangnamu.nvapp.model.DataManager;
import net.sarangnamu.nvapp.viewmodel.NvAppTutorialViewModel;
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

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // SPLASH
    //
    ////////////////////////////////////////////////////////////////////////////////////

    private void loadSplash() {
        if (mLog.isDebugEnabled()) {
            mLog.debug("SPLASH START");
        }

        Intent intent = new Intent(this, SplashActivity.class);
        startActivityForResult(intent, SplashActivity.SPLASH_ACTION_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case SplashActivity.SPLASH_ACTION_ID:
                loadTutorial();
                break;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // FRAGMENTS
    //
    ////////////////////////////////////////////////////////////////////////////////////

    private void matchParentNavigationView() {
        DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) mBinding.navView.getLayoutParams();

        lp.width = MainApp.screenX;
        mBinding.navView.setLayoutParams(lp);
    }

    private void initNavigation() {
        final TimeLoger.TimeLog log = TimeLoger.start("NAVIGATION");

        matchParentNavigationView();
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
                runOnUiThread(this::loadMain);
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
        NvAppTutorialViewModel nvmodel = viewModel(NvAppTutorialViewModel.class);
        if (vmodel.isFinished()) {
            return ;
        }

        if (mLog.isDebugEnabled()) {
            mLog.debug("TUTORIAL START");
        }

        vmodel.params = TutorialParams.builder()
            .view(R.layout.tutorial_0)
            .view(R.layout.tutorial_1)
            .viewDataBindingListener((res, viewDataBinding) -> {
                if (mLog.isDebugEnabled()) {
                    mLog.debug("RECEIVED viewDataBindingListener");
                }

                Invoke.method(viewDataBinding, "setNvmodel", nvmodel);

                if (viewDataBinding instanceof Tutorial0Binding) {
                    tutorial0Event((Tutorial0Binding) viewDataBinding);
                }
            })
            .finishedListener((result, obj) -> ViewManager.get().popBack())
            .build();

        ViewManager.get().show(FragmentParams.builder().containerId(R.id.layout_main)
            .fragment(TutorialFragment.class).build());
    }

    private void tutorial0Event(Tutorial0Binding binding) {
        int screenWidth  = MainApp.screenX;
        float panelWidth = DimUtils.dpToPixel(MainActivity.this, 300);
        float margin     = screenWidth - panelWidth;
        float leftMargin = margin / 2f - DimUtils.dpToPixel(MainActivity.this,
            NvAppTutorialViewModel.PANEL_MOVE_X);
        float gap        = (panelWidth + leftMargin) * -1 ;
        int startDelay   = 3000;

        // [전체크기]

        // 애니메이션 관련 작업
        ObjectAnimator left   = ObjectAnimator.ofFloat(binding.panelLeft, "translationX", gap + leftMargin);
        ObjectAnimator center = ObjectAnimator.ofFloat(binding.panelCenter, "translationX", gap);
        ObjectAnimator right  = ObjectAnimator.ofFloat(binding.panelRight, "translationX", gap - leftMargin);
        ObjectAnimator right2 = ObjectAnimator.ofFloat(binding.panelRight2, "translationX", gap - leftMargin);

        AnimatorSet set = new AnimatorSet();
        set.setStartDelay(startDelay);
        set.setDuration(700);
        set.playTogether(left, center, right, right2);
        set.start();
    }
}
