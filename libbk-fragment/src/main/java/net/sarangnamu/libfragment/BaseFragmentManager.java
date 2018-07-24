package net.sarangnamu.libfragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 10. <p/>
 */
public abstract class BaseFragmentManager {
    private static final Logger mLog = LoggerFactory.getLogger(BaseFragmentManager.class);

    protected FragmentManager mFrgmtManager;

    public void setFragmentManager(FragmentActivity act) {
        if (act == null) {
            mLog.error("setFragmentManager act is null");
            return;
        }

        mFrgmtManager = act.getSupportFragmentManager();
    }

    public void setFragmentManager(Fragment frgmt) {
        if (frgmt == null) {
            mLog.error("setFragmentManager frgmt is null");
            return;
        }

        mFrgmtManager = frgmt.getChildFragmentManager();
    }

    @Nullable
    public Fragment show(@NonNull FragmentParams params) {
        try {
            Fragment frgmt = instanceFragment(params.fragment);
            if (frgmt == null) {
                mLog.error("ERROR: frgmt == null");
                return null;
            }

            if (frgmt.isVisible()) {
                if (mLog.isDebugEnabled()) {
                    mLog.debug("VISIBLE FRAGMENT " + params.fragment.getSimpleName());
                }

                return frgmt;
            }

            if (params.bundle != null) {
                frgmt.setArguments(params.bundle);
            }

            FragmentTransaction trans = mFrgmtManager.beginTransaction();
            String tagName            = frgmt.getClass().getName();

            // 미리 지정해둔 애니메이션 효과를 사용할 경우
            if (!TextUtils.isEmpty(params.anim)) {
                switch (params.anim.toLowerCase()) {
                    case "left":
                        trans.setCustomAnimations(R.anim.slide_in_current, R.anim.slide_in_next,
                            R.anim.slide_out_current, R.anim.slide_out_prev);
                        break;

                    case "right":
                        trans.setCustomAnimations(R.anim.slide_out_current, R.anim.slide_out_prev
                            ,R.anim.slide_in_current, R.anim.slide_in_next);
                        break;

                    case "up":
                        trans.setCustomAnimations(R.anim.slide_up_current, R.anim.slide_up_next,
                            R.anim.slide_down_current, R.anim.slide_down_prev);
                        break;
                }
            } else if (params.transitionListener != null) {
                // 사용자가 직접 커스텀한 애니메이션을 사용할 경우
                params.transitionListener.onEvent(this, trans);
            }

            if (params.addMode) {
                trans.add(params.containerViewId, frgmt, tagName);
            } else {
                trans.replace(params.containerViewId, frgmt, tagName);
            }

            if (params.backStack) {
                trans.addToBackStack(tagName);
            }

            trans.commit();

            return frgmt;
        } catch (Exception e) {
            e.printStackTrace();
            mLog.error("ERROR: " + e.getMessage());
        }

        return null;
    }

    @Nullable
    private Fragment instanceFragment(Class<?> clazz) {
        Fragment frgmt = mFrgmtManager.findFragmentByTag(clazz.getName());
        if (frgmt != null) {
            return frgmt;
        }

        try {
            return (Fragment) clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            mLog.error("ERROR: " + e.getMessage());
        }

        return null;
    }

    public void popBack() {
        if (mFrgmtManager != null) {
            mFrgmtManager.popBackStack();
        }
    }

    public void popBackAll() {
        if (mFrgmtManager == null) {
            mLog.error("setFragmentManager fm is null");
            return;
        }

        int count = mFrgmtManager.getBackStackEntryCount();
        for (int i = 0; i < count; ++i) {
            mFrgmtManager.popBackStack(i, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public Fragment getCurrentFragment() {
        if (mFrgmtManager == null) {
            return null;
        }

        int count = mFrgmtManager.getBackStackEntryCount();
        if (count > 0) {
            FragmentManager.BackStackEntry frgmt = mFrgmtManager.getBackStackEntryAt(count - 1);
            return mFrgmtManager.findFragmentByTag(frgmt.getName());
        }

        return null;
    }

    public Fragment getFragment(Class<?> cls) {
        if (mFrgmtManager == null) {
            return null;
        }

        return mFrgmtManager.findFragmentByTag(cls.getName());
    }

    public int getChildCount() {
        if (mFrgmtManager == null) {
            return 0;
        }

        return mFrgmtManager.getBackStackEntryCount();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // Transition Callback
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public interface TransitionListener {
        void onEvent(BaseFragmentManager manager, FragmentTransaction trans);
    }
}

