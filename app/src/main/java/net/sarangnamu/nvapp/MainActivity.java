package net.sarangnamu.nvapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import net.sarangnamu.common.util.Invoke;
import net.sarangnamu.common.widget.BaseActivity;
import net.sarangnamu.libcore.AppTerminator;
import net.sarangnamu.libcore.OnResultListener;
import net.sarangnamu.libcore.TimeLoger;
import net.sarangnamu.libcore.dialog.Dialog;
import net.sarangnamu.libcore.dialog.DialogParams;
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
import net.sarangnamu.nvapp.databinding.TutorialPermissionBinding;
import net.sarangnamu.nvapp.view.MainFragment;
import net.sarangnamu.nvapp.view.tutorial.TutorialCategory;
import net.sarangnamu.nvapp.view.tutorial.TutorialIntro;
import net.sarangnamu.nvapp.view.tutorial.TutorialPermission;
import net.sarangnamu.nvapp.viewmodel.NvLoginViewModel;
import net.sarangnamu.nvapp.viewmodel.MainViewModel;
import net.sarangnamu.nvapp.viewmodel.NavigationViewModel;
import net.sarangnamu.nvapp.model.DataManager;
import net.sarangnamu.nvapp.viewmodel.NvAppTutorialViewModel;
import net.sarangnamu.nvapp.viewmodel.UserInfoViewModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity<ActivityMainBinding>
    implements MainCallback, FragmentCallback, NvLoginCallback {

    private static final Logger mLog = LoggerFactory.getLogger(MainActivity.class);

    private Integer mCounter = 0;
    private Bundle mSavedInstanceState = null;
    private AppTerminator mAppTerminator;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        loadSplash();
        setTheme(R.style.AppTheme);
        mSavedInstanceState = savedInstanceState;

        super.onCreate(savedInstanceState);

        if (mLog.isDebugEnabled()) {
            mLog.debug("ON CREATE");
        }

        initBinding();
        initViewModel();
        initAppTerminator();
        initNavigation();
        initUserInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mLog.isDebugEnabled()) {
            mLog.debug("ON RESUME");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mLog.isDebugEnabled()) {
            mLog.debug("ON PAUSE");
        }
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

        mAppTerminator.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        DataManager.get().destroy();

        mDisposable.clear();
        mDisposable.dispose();

        super.onDestroy();

        if (mLog.isDebugEnabled()) {
            mLog.debug("ON DESTROY");
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
        mBinding.navMain.setLoginmodel(loginModel);
    }

    private void initAppTerminator() {
        // back pressed 설정
        mAppTerminator = AppTerminator.create(MainActivity.this, mBinding.drawerLayout);
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
        if (mSavedInstanceState == null) {
            showFragment(FragmentParams.builder()
                .containerId(R.id.layout_main)
                .backStack(false)
                .fragment(MainFragment.class)
                .commitAllowingStateLoss(true)  // screen off 일때 run 하면 문제가 될 수 있어서
                .build());
        } else {
            if (mLog.isInfoEnabled()) {
                mLog.info("mSaveInstanceStateNull == FALSE");
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // TUTORIAL
    //
    ////////////////////////////////////////////////////////////////////////////////////

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
            .view(R.layout.tutorial_permission)
            .viewDataBindingListener((res, viewDataBinding) -> {
                Invoke.method(viewDataBinding, "setNvmodel", nvmodel);

                if (viewDataBinding instanceof TutorialIntroBinding) {
                    TutorialIntro.event((TutorialIntroBinding) viewDataBinding);
                } else if (viewDataBinding instanceof TutorialCategoryBinding) {
                    TutorialCategory.event(this, mDisposable,
                        (TutorialCategoryBinding) viewDataBinding);
                } else if (viewDataBinding instanceof TutorialPermissionBinding) {
                    TutorialPermission.event(this,
                        (TutorialPermissionBinding) viewDataBinding);
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

    @Override
    public void login() {
        NvLoginViewModel vmodel = viewModel(NvLoginViewModel.class);
        vmodel.login();
    }

    @Override
    public void logout() {
        NvLoginViewModel vmodel = viewModel(NvLoginViewModel.class);
        vmodel.logout();
    }

    @Override
    public void permission(List<String> permissions) {
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
