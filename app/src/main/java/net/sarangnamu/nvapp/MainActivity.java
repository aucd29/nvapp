package net.sarangnamu.nvapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;

import net.sarangnamu.common.util.DimUtils;
import net.sarangnamu.common.util.Invoke;
import net.sarangnamu.common.widget.BaseActivity;
import net.sarangnamu.libcore.AnimationEndListener;
import net.sarangnamu.libcore.AppTerminator;
import net.sarangnamu.libcore.TimeLoger;
import net.sarangnamu.libfragment.FragmentParams;
import net.sarangnamu.libtutorial.TutorialFragment;
import net.sarangnamu.libtutorial.TutorialParams;
import net.sarangnamu.libtutorial.viewmodel.TutorialViewModel;
import net.sarangnamu.nvapp.callback.MainCallback;
import net.sarangnamu.nvapp.databinding.ActivityMainBinding;
import net.sarangnamu.nvapp.databinding.TutorialCategoryBinding;
import net.sarangnamu.nvapp.databinding.TutorialIntroBinding;
import net.sarangnamu.nvapp.view.MainFragment;
import net.sarangnamu.nvapp.viewmodel.CategoryViewModel;
import net.sarangnamu.nvapp.viewmodel.MainViewModel;
import net.sarangnamu.nvapp.viewmodel.NavigationViewModel;
import net.sarangnamu.nvapp.model.DataManager;
import net.sarangnamu.nvapp.viewmodel.NvAppTutorialViewModel;
import net.sarangnamu.nvapp.viewmodel.UserInfoViewModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements MainCallback {
    private static final Logger mLog = LoggerFactory.getLogger(MainActivity.class);

    private CompositeDisposable mDisposable = new CompositeDisposable();
    private Integer mCounter = 0;
    private AppTerminator mAppTermiator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        loadSplash();

        super.onCreate(savedInstanceState);

        // https://developer.android.com/topic/performance/vitals/launch-time
        setTheme(R.style.AppTheme);

        initBinding();

        mAppTermiator = AppTerminator.create(MainActivity.this, mBinding.drawerLayout);
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

        Fragment frgmt = ViewManager.get().getCurrentFragment();
        if (frgmt instanceof TutorialFragment) {
            // 현재 Fragment 가 tutorial 이면 back 키를 무시 한다.
            return ;
        }

        mAppTermiator.onBackPressed();
//        super.onBackPressed();
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

        viewModel(MainViewModel.class).mainCallback = this;
    }

    @Override
    public void showNavigation() {
        if (!mBinding.drawerLayout.isDrawerOpen(Gravity.START)) {
            mBinding.drawerLayout.openDrawer(Gravity.START);
        }
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

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // TUTORIAL
    //
    ////////////////////////////////////////////////////////////////////////////////////

    private static final String TRANS_Y = "translationY";
    private static final String TRANS_X = "translationX";

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

    // 처음엔 공통으로 써보려고 애니메이션 관련 binding adapter 를 구성하였으나
    // 객체 하나만 움직이는게 아니고 다수의 객체가 시간차를 두고 움직여야 되는 문제 때문에
    // view 를 직접 코드로 컨트롤 하기로 변경 함
    private void tutorialIntroEvent(@NonNull TutorialIntroBinding binding) {
        int screenWidth     = MainApp.screenX;
        int phoneFrameWidth = (int) (screenWidth * 0.7f);
        int margin          = (screenWidth - phoneFrameWidth) / 2;
        int sideX           = margin / 2;
        int moveY           = (int) (MainApp.screenY / 1.8f);

        if (mLog.isDebugEnabled()) {
            mLog.debug("SCREEN WIDTH : " + screenWidth + "\n" +
                       "FRAME        : " + phoneFrameWidth + "\n" +
                       "SIDE X       : " + sideX + "\n" +
                       "MOVE Y       : " + moveY);
        }

        binding.phoneFrame.getLayoutParams().width   = phoneFrameWidth;
        binding.panelLeft.getLayoutParams().width    = phoneFrameWidth;
        binding.panelCenter.getLayoutParams().width  = phoneFrameWidth;
        binding.panelRight.getLayoutParams().width   = phoneFrameWidth;
        binding.panelRight2 .getLayoutParams().width = phoneFrameWidth;

        ObjectAnimator fadeInPhoneFrame  = ObjectAnimator.ofFloat(binding.phoneFrame,
            "alpha", 0, 1);
        ObjectAnimator transYPhoneFrame  = ObjectAnimator.ofFloat(binding.phoneFrame,
            TRANS_Y, moveY, 0);

        ObjectAnimator fadeInPanelCenter = ObjectAnimator.ofFloat(binding.panelCenter,
            "alpha", 0, 1);
        ObjectAnimator transYPanelCenter = ObjectAnimator.ofFloat(binding.panelCenter,
            TRANS_Y, moveY, 0);

        ObjectAnimator transXPanelLeft   = ObjectAnimator.ofFloat(binding.panelLeft,
            TRANS_X, 0, sideX);
        ObjectAnimator transYPanelLeft   = ObjectAnimator.ofFloat(binding.panelLeft,
            TRANS_Y, moveY, 0);

        ObjectAnimator transXPanelRight  = ObjectAnimator.ofFloat(binding.panelRight,
            TRANS_X, 0, sideX * -1);
        ObjectAnimator transYPanelRight  = ObjectAnimator.ofFloat(binding.panelRight,
            TRANS_Y, moveY, 0);

        if (mLog.isDebugEnabled()) {
            mLog.debug("TUTORIAL ANIMATION START");
        }

        final AnimatorSet aniSet = new AnimatorSet();
        aniSet.setDuration(750);
        aniSet.playTogether(fadeInPhoneFrame, transYPhoneFrame, fadeInPanelCenter, transYPanelCenter,
            transXPanelLeft, transYPanelLeft, transXPanelRight, transYPanelRight);
        aniSet.addListener(new AnimationEndListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                animator.removeListener(this);

                if (mLog.isDebugEnabled()) {
                    mLog.debug("TUTORIAL BUTTON LAYOUT SHOW");
                }

                final ObjectAnimator transYButtonLayout = ObjectAnimator.ofFloat(binding.buttonLayout,
                    TRANS_Y, binding.buttonLayout.getHeight(), 0);
                transYButtonLayout.setDuration(500);
                transYButtonLayout.addListener(new AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        animator.removeListener(this);

                        if (mLog.isDebugEnabled()) {
                            mLog.debug("SCROLLING PANELS");
                        }

                        int moveX = (phoneFrameWidth + sideX) * -1;

                        ObjectAnimator left   = ObjectAnimator.ofFloat(binding.panelLeft,
                            TRANS_X, moveX + sideX);
                        ObjectAnimator center = ObjectAnimator.ofFloat(binding.panelCenter,
                            TRANS_X, moveX);
                        ObjectAnimator right  = ObjectAnimator.ofFloat(binding.panelRight,
                            TRANS_X, moveX - sideX);
                        ObjectAnimator right2 = ObjectAnimator.ofFloat(binding.panelRight2,
                            TRANS_X, moveX - sideX);

                        AnimatorSet set = new AnimatorSet();
                        set.setDuration(700);
                        set.playTogether(left, center, right, right2);
                        set.start();
                    }
                });
                transYButtonLayout.start();

                binding.buttonLayout.setVisibility(View.VISIBLE);
            }
        });
        aniSet.start();
    }

    private void tutorialCategoryEvent(@NonNull TutorialCategoryBinding binding) {
        CategoryViewModel cmodel = viewModel(CategoryViewModel.class);
        cmodel.init(this);

        binding.setCmodel(cmodel);
    }

}
