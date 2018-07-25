package net.sarangnamu.nvapp;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import net.sarangnamu.common.util.Invoke;
import net.sarangnamu.common.widget.BaseActivity;
import net.sarangnamu.libcore.AnimationEndListener;
import net.sarangnamu.libcore.AppTerminator;
import net.sarangnamu.libcore.TimeLoger;
import net.sarangnamu.libfragment.FragmentParams;
import net.sarangnamu.libtutorial.TutorialFragment;
import net.sarangnamu.libtutorial.TutorialParams;
import net.sarangnamu.libtutorial.viewmodel.TutorialViewModel;
import net.sarangnamu.nvapp.callback.FragmentCallback;
import net.sarangnamu.nvapp.callback.MainCallback;
import net.sarangnamu.nvapp.callback.NvLoginCallback;
import net.sarangnamu.nvapp.databinding.ActivityMainBinding;
import net.sarangnamu.nvapp.databinding.TutorialCategoryBinding;
import net.sarangnamu.nvapp.databinding.TutorialIntroBinding;
import net.sarangnamu.nvapp.view.MainFragment;
import net.sarangnamu.nvapp.viewmodel.CategoryViewModel;
import net.sarangnamu.nvapp.viewmodel.NvLoginViewModel;
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

public class MainActivity extends BaseActivity<ActivityMainBinding>
    implements MainCallback, FragmentCallback, NvLoginCallback {

    private static final Logger mLog = LoggerFactory.getLogger(MainActivity.class);

    private CompositeDisposable mDisposable = new CompositeDisposable();
    private Integer mCounter = 0;
    private AppTerminator mAppTermiator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        loadSplash();
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);

        initBinding();
        initViewModel();
        initAppTerminator();
        initNavigation();
        initUserInfo();
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onBackPressed() {
        if (mLog.isTraceEnabled()) {
            mLog.trace("BACK KEY");
        }

        int navigationLayoutChildCount = mBinding.layoutNavi.getChildCount();
        if (navigationLayoutChildCount > 0) {
            if (mLog.isDebugEnabled()) {
                mLog.debug("BACK COUNT : " + navigationLayoutChildCount);
            }

            hideFragment();
            return ;
        }

        if (mBinding.drawerLayout.isDrawerOpen(Gravity.START)) {
            mBinding.drawerLayout.closeDrawer(Gravity.START);
            if (mLog.isTraceEnabled()) {
                mLog.trace("CLOSE NAVIGATION MENU");
            }

            return ;
        }

        Fragment frgmt = ViewManager.get().getCurrentFragment();
        if (frgmt instanceof TutorialFragment) {
            // 현재 Fragment 가 tutorial 이면 back 키를 무시 한다.
            if (mLog.isTraceEnabled()) {
                mLog.trace("IGNORE BACK KEY");
            }

            return ;
        }

        mAppTermiator.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        DataManager.get().destroy();

        mDisposable.clear();
        mDisposable.dispose();

        super.onDestroy();

        if (mLog.isDebugEnabled()) {
            mLog.debug("MAIN DESTROY");
        }
    }

    private void initViewModel() {
        if (mLog.isTraceEnabled()) {
            mLog.trace("INIT MAIN VIEW MODEL");
        }

        // main layout 설정
        MainViewModel vmodel = viewModel(MainViewModel.class);
        vmodel.mDisposable   = mDisposable;
        mBinding.setVmodel(vmodel);

        if (mLog.isTraceEnabled()) {
            mLog.trace("INIT NV LOGIN VIEW MODEL");
        }

        // NV LOGIN 설정
        NvLoginViewModel loginModel = viewModel(NvLoginViewModel.class);
        loginModel.mMainCallback    = this;
        loginModel.mNvLoginCallback = this;
        loginModel.mDisposable      = mDisposable;
    }

    private void initAppTerminator() {
        // back pressed 설정
        mAppTermiator = AppTerminator.create(MainActivity.this, mBinding.drawerLayout);
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
        ViewManager.get().setFragmentManager(this);

        final TimeLoger.TimeLog log = TimeLoger.start("NAVIGATION");

        matchParentNavigationView();
        mDisposable.add(Observable.just(viewModel(NavigationViewModel.class))
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe(vmodel -> {
                if (mLog.isTraceEnabled()) {
                    mLog.trace("INIT NAVIGATION VIEW MODEL");
                }

                vmodel.mMainCallback     = MainActivity.this;
                vmodel.mFragmentCallback = MainActivity.this;
                vmodel.init(MainActivity.this);

                mBinding.navMain.setVmodel(vmodel);

                loadFragments();
                log.end();
            }));

        viewModel(MainViewModel.class).mMainCallback = this;
    }

    private void initUserInfo() {
        final TimeLoger.TimeLog log = TimeLoger.start("USER INFO");

        mDisposable.add(Observable.just(viewModel(UserInfoViewModel.class))
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe(vmodel -> {
                if (mLog.isTraceEnabled()) {
                    mLog.trace("INIT USER INFO VIEW MODEL");
                }

                vmodel.init();
                mBinding.navMain.setUser(vmodel);

                log.end();
                loadFragments();
            }));
    }

    private void loadFragments() {
        // 이거 밖에 방법이 없나? 찾아봐야할 듯 ???
        synchronized (mCounter) {
            if (++mCounter > 1) {
                runOnUiThread(this::loadMain);
            }
        }
    }

    private void loadMain() {
        showFragment(FragmentParams.builder()
            .containerId(R.id.layout_main)
            .add().backStack(false)
            .fragment(MainFragment.class)
            .build());
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // TUTORIAL
    //
    ////////////////////////////////////////////////////////////////////////////////////

    private static final String TRANS_Y = "translationY";
    private static final String TRANS_X = "translationX";
    private static final String ALPHA   = "alpha";

    // 여기 코드를 따로 빼놓던지 해야할 듯
    private void loadTutorial() {
        TutorialViewModel vmodel       = viewModel(TutorialViewModel.class);
        NvAppTutorialViewModel nvmodel = viewModel(NvAppTutorialViewModel.class);
        if (vmodel.isFinished()) {
            if (mLog.isTraceEnabled()) {
                mLog.trace("IGNORE TUTORIAL");
            }

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
            .finishedListener((result, obj) -> {
                if (mLog.isDebugEnabled()) {
                    mLog.debug("TUTORIAL END");
                }

                ViewManager.get().popBack();
            })
            .build();

        ViewManager.get().show(FragmentParams.builder().containerId(R.id.layout_main)
            .fragment(TutorialFragment.class).build());
    }

    // 처음엔 공통으로 써보려고 애니메이션 관련 binding adapter 를 구성하였으나
    // 객체 하나만 움직이는게 아니고 다수의 객체가 시간차를 두고 움직여야 되는 문제 때문에
    // view 를 직접 코드로 컨트롤 하기로 변경 함
    private void tutorialIntroEvent(@NonNull TutorialIntroBinding binding) {
        if (mLog.isTraceEnabled()) {
            mLog.trace("TURORIAL INTRO");
        }

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
            ALPHA, 0, 1);
        ObjectAnimator transYPhoneFrame  = ObjectAnimator.ofFloat(binding.phoneFrame,
            TRANS_Y, moveY, 0);

        ObjectAnimator fadeInPanelCenter = ObjectAnimator.ofFloat(binding.panelCenter,
            ALPHA, 0, 1);
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

        if (mLog.isTraceEnabled()) {
            mLog.trace("TUTORIAL ANIMATION START");
        }

        final AnimatorSet aniSet = new AnimatorSet();
        aniSet.setDuration(750);
        aniSet.playTogether(fadeInPhoneFrame, transYPhoneFrame, fadeInPanelCenter, transYPanelCenter,
            transXPanelLeft, transYPanelLeft, transXPanelRight, transYPanelRight);
        aniSet.addListener(new AnimationEndListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                animator.removeListener(this);

                if (mLog.isTraceEnabled()) {
                    mLog.trace("TUTORIAL BUTTON LAYOUT SHOW");
                }

                final ObjectAnimator transYButtonLayout = ObjectAnimator.ofFloat(binding.buttonLayout,
                    TRANS_Y, binding.buttonLayout.getHeight(), 0);
                transYButtonLayout.setDuration(500);
                transYButtonLayout.addListener(new AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        animator.removeListener(this);

                        if (mLog.isTraceEnabled()) {
                            mLog.trace("SCROLLING PANELS");
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
        if (mLog.isTraceEnabled()) {
            mLog.trace("TUTORIAL CATEGORY");
        }

        CategoryViewModel cmodel = viewModel(CategoryViewModel.class);
        cmodel.mDisposable = mDisposable;
        cmodel.init(this);

        binding.setCmodel(cmodel);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // MainCallback
    //
    ////////////////////////////////////////////////////////////////////////////////////
    
    @Override
    public void showNavigation() {
        if (!mBinding.drawerLayout.isDrawerOpen(Gravity.START)) {
            if (mLog.isTraceEnabled()) {
                mLog.trace("SHOW NAVIGATION");
            }

            mBinding.drawerLayout.openDrawer(Gravity.START);
        }
    }

    @Override
    public void hideNavigation() {
        if (mBinding.drawerLayout.isDrawerOpen(Gravity.START)) {
            if (mLog.isTraceEnabled()) {
                mLog.trace("HIDE NAVIGATION");
            }

            mBinding.drawerLayout.closeDrawer(Gravity.START);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // FragmentCallback
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void showFragment(@NonNull FragmentParams params) {
        if (mLog.isDebugEnabled()) {
            mLog.debug("SHOW FRAGMENT : " + params.fragment.getSimpleName());
        }

        ViewManager.get().show(params);
    }

    @Override
    public void hideFragment() {
        if (mLog.isDebugEnabled()) {
            mLog.debug("HIDE FRAGMENT");
        }
        
        ViewManager.get().popBack();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // NvLoginCallback
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void login(@NonNull final OAuthLogin oauth, @NonNull final OAuthLoginHandler handler) {
        if (mLog.isDebugEnabled()) {
            mLog.debug("REQUEST LOGIN");
        }

        oauth.startOauthLoginActivity(this, handler);
    }
}
