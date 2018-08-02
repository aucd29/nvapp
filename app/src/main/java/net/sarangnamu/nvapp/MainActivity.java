package net.sarangnamu.nvapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;

import net.sarangnamu.common.widget.BaseActivity;
import net.sarangnamu.libcore.AppTerminator;
import net.sarangnamu.libcore.Invoke;
import net.sarangnamu.libfragment.FragmentParams;
import net.sarangnamu.libtutorial.TutorialFragment;
import net.sarangnamu.libtutorial.TutorialParams;
import net.sarangnamu.libtutorial.viewmodel.TutorialViewModel;
import net.sarangnamu.nvapp.databinding.ActivityMainBinding;
import net.sarangnamu.nvapp.databinding.TutorialCategoryBinding;
import net.sarangnamu.nvapp.databinding.TutorialIntroBinding;
import net.sarangnamu.nvapp.databinding.TutorialPermissionBinding;
import net.sarangnamu.nvapp.view.MainFragment;
import net.sarangnamu.nvapp.view.tutorial.TutorialCategory;
import net.sarangnamu.nvapp.view.tutorial.TutorialIntro;
import net.sarangnamu.nvapp.view.tutorial.TutorialPermission;
import net.sarangnamu.nvapp.viewmodel.DrawerLayoutViewModel;
import net.sarangnamu.nvapp.viewmodel.NvLoginViewModel;
import net.sarangnamu.nvapp.viewmodel.MainViewModel;
import net.sarangnamu.nvapp.viewmodel.NavigationViewModel;
import net.sarangnamu.nvapp.model.DataManager;
import net.sarangnamu.nvapp.viewmodel.TutorialAnimViewModel;
import net.sarangnamu.nvapp.viewmodel.UserInfoViewModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    private static final Logger mLog = LoggerFactory.getLogger(MainActivity.class);

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
        initAppTerminator();
        initViewModel();
        initFragment();
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

            ViewManager.get().popBack();
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
        MainViewModel mainModel = viewModel(MainViewModel.class);
        mainModel.mDisposable = mDisposable;
        mBinding.setMainModel(mainModel);

        if (mLog.isTraceEnabled()) {
            mLog.trace("INIT NAVIGATION VIEW MODEL");
        }

        NavigationViewModel navigationModel = viewModel(NavigationViewModel.class);
        navigationModel.init(this);
        mBinding.navMain.setNavigationModel(navigationModel);

        if (mLog.isTraceEnabled()) {
            mLog.trace("INIT LOGIN VIEW MODEL");
        }

        NvLoginViewModel loginModel = viewModel(NvLoginViewModel.class);
        loginModel.mDisposable      = mDisposable;
        mBinding.navMain.setLoginModel(loginModel);

        if (mLog.isTraceEnabled()) {
            mLog.trace("INIT USER INFO VIEW MODEL");
        }

        UserInfoViewModel userModel = viewModel(UserInfoViewModel.class);
        userModel.init();
        mBinding.navMain.setUserModel(userModel);

        if (mLog.isTraceEnabled()) {
            mLog.trace("INIT DRAWER LAYOUT VIEW MODEL");
        }

        DrawerLayoutViewModel drawerModel = viewModel(DrawerLayoutViewModel.class);
        mBinding.navMain.setDrawerModel(drawerModel);

        mBinding.navMain.setDrawerLayout(mBinding.drawerLayout);
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

    private void initFragment() {
        matchParentNavigationView();
        ViewManager.get().setFragmentManager(this);

        if (mSavedInstanceState == null) {
            ViewManager.get().show(FragmentParams.builder()
                .containerId(R.id.layout_main)
                .backStack(false)
                .fragment(MainFragment.class)
                .commitAllowingStateLoss(true)  // screen off 일때 run 하면 문제가 될 수 있어서
                .build());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // TUTORIAL
    //
    ////////////////////////////////////////////////////////////////////////////////////

    private void loadTutorial() {
        TutorialViewModel vmodel        = viewModel(TutorialViewModel.class);
        TutorialAnimViewModel animModel = viewModel(TutorialAnimViewModel.class);
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
                Invoke.method(viewDataBinding, "setAniModel", animModel);

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

        ViewManager.get().show(FragmentParams.builder()
            .containerId(R.id.layout_main)
            .fragment(TutorialFragment.class).build());
    }

    public ActivityMainBinding binding() {
        return mBinding;
    }

    public CompositeDisposable disposable() {
        return mDisposable;
    }
}
