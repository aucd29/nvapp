package net.sarangnamu.nvapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import net.sarangnamu.nvapp.databinding.TutorialCategoryBinding;
import net.sarangnamu.nvapp.databinding.TutorialIntroBinding;
import net.sarangnamu.nvapp.view.MainFragment;
import net.sarangnamu.nvapp.viewmodel.CategoryViewModel;
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
            .view(R.layout.tutorial_intro)
            .view(R.layout.tutorial_category)
            .viewDataBindingListener((res, viewDataBinding) -> {
                if (mLog.isDebugEnabled()) {
                    mLog.debug("RECEIVED viewDataBindingListener");
                }

                Invoke.method(viewDataBinding, "setNvmodel", nvmodel);

                if (viewDataBinding instanceof TutorialIntroBinding) {
                    tutorialIntroEvent((TutorialIntroBinding) viewDataBinding);
                } else if (viewDataBinding instanceof TutorialCategoryBinding) {
                    tutorialCategoryEvent((TutorialCategoryBinding) viewDataBinding);
                }
            })
            .finishedListener((result, obj) -> ViewManager.get().popBack())
            .build();

        ViewManager.get().show(FragmentParams.builder().containerId(R.id.layout_main)
            .fragment(TutorialFragment.class).build());
    }

    private void tutorialIntroEvent(@NonNull TutorialIntroBinding binding) {
        int screenWidth     = MainApp.screenX;
        int phoneFrameWidth = (int) (screenWidth * 0.7f);
        int margin          = (screenWidth - phoneFrameWidth) / 2;
        int sideX           = margin / 2;
        int halfHeight      = (int) (MainApp.screenY / 1.8f);

        binding.phoneFrame.getLayoutParams().width   = phoneFrameWidth;
        binding.panelLeft.getLayoutParams().width    = phoneFrameWidth;
        binding.panelCenter.getLayoutParams().width  = phoneFrameWidth;
        binding.panelRight.getLayoutParams().width   = phoneFrameWidth;
        binding.panelRight2 .getLayoutParams().width = phoneFrameWidth;

        ObjectAnimator fadeInPhoneFrame = ObjectAnimator.ofFloat(binding.phoneFrame, "alpha", 0, 1);
        ObjectAnimator transYPhoneFrame = ObjectAnimator.ofFloat(binding.phoneFrame, "translationY", halfHeight, 0);

        ObjectAnimator fadeInPanelCenter = ObjectAnimator.ofFloat(binding.panelCenter, "alpha", 0, 1);
        ObjectAnimator transYPanelCenter = ObjectAnimator.ofFloat(binding.panelCenter, "translationY", halfHeight, 0);

        ObjectAnimator transXPanelLeft = ObjectAnimator.ofFloat(binding.panelLeft, "translationX", 0, sideX);
        ObjectAnimator transYPanelLeft = ObjectAnimator.ofFloat(binding.panelLeft, "translationY", halfHeight, 0);

        ObjectAnimator transXPanelRight = ObjectAnimator.ofFloat(binding.panelRight, "translationX", 0, sideX * -1);
        ObjectAnimator transYPanelRight = ObjectAnimator.ofFloat(binding.panelRight, "translationY", halfHeight, 0);

        final AnimatorSet aniSet = new AnimatorSet();
        aniSet.setDuration(750);
        aniSet.playTogether(fadeInPhoneFrame, transYPhoneFrame, fadeInPanelCenter, transYPanelCenter,
            transXPanelLeft, transYPanelLeft, transXPanelRight, transYPanelRight);
        aniSet.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationStart(Animator animation, boolean isReverse) {}
            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
                aniSet.removeAllListeners();

                final ObjectAnimator transYButtonLayout = ObjectAnimator.ofFloat(binding.buttonLayout,
                    "translationY", binding.buttonLayout.getHeight(), 0);
                transYButtonLayout.setDuration(500);
                transYButtonLayout.addListener(new AnimatorListenerAdapter() {
                    @Override public void onAnimationStart(Animator animation, boolean isReverse) { }
                    @Override
                    public void onAnimationEnd(Animator animation, boolean isReverse) {
                        transYButtonLayout.removeAllListeners();

                        int moveX = phoneFrameWidth + sideX;

                        ObjectAnimator left   = ObjectAnimator.ofFloat(binding.panelLeft, "translationX", moveX + sideX);
                        ObjectAnimator center = ObjectAnimator.ofFloat(binding.panelCenter, "translationX", moveX);
                        ObjectAnimator right  = ObjectAnimator.ofFloat(binding.panelRight, "translationX", moveX - sideX);
                        ObjectAnimator right2 = ObjectAnimator.ofFloat(binding.panelRight2, "translationX", moveX - sideX);

                        AnimatorSet set = new AnimatorSet();
                        set.setDuration(700);
                        set.playTogether(left, center, right, right2);
                        set.start();
                    }
                });
                transYButtonLayout.start();
            }
        });
        aniSet.start();

//
//        float panelWidth = DimUtils.dpToPixel(MainActivity.this, 300);
//        float margin     = screenWidth - panelWidth;
//        float leftMargin = margin / 2f - DimUtils.dpToPixel(MainActivity.this,
//            NvAppTutorialViewModel.PANEL_MOVE_X);
//        float gap        = (panelWidth + leftMargin) * -1 ;
//        int startDelay   = 3000;
//
//        // [전체크기]
//
//        // 애니메이션 관련 작업
//        ObjectAnimator left   = ObjectAnimator.ofFloat(binding.panelLeft, "translationX", gap + leftMargin);
//        ObjectAnimator center = ObjectAnimator.ofFloat(binding.panelCenter, "translationX", gap);
//        ObjectAnimator right  = ObjectAnimator.ofFloat(binding.panelRight, "translationX", gap - leftMargin);
//        ObjectAnimator right2 = ObjectAnimator.ofFloat(binding.panelRight2, "translationX", gap - leftMargin);
//
//        AnimatorSet set = new AnimatorSet();
//        set.setStartDelay(startDelay);
//        set.setDuration(700);
//        set.playTogether(left, center, right, right2);
//        set.start();
    }

    private void tutorialCategoryEvent(@NonNull TutorialCategoryBinding binding) {
        CategoryViewModel cmodel = viewModel(CategoryViewModel.class);
        cmodel.init(this);

        binding.setCmodel(cmodel);
    }
}
