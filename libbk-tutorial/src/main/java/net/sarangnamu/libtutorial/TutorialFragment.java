package net.sarangnamu.libtutorial;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

import net.sarangnamu.libfragment.BaseFragment;
import net.sarangnamu.libtutorial.databinding.TutorialMainBinding;
import net.sarangnamu.libtutorial.viewmodel.BaseTutorialViewModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Collections;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 11. <p/>
 */
public final class TutorialFragment extends BaseFragment<TutorialMainBinding> {
    private static final Logger mLog = LoggerFactory.getLogger(TutorialFragment.class);

    public static final String PARAMS = "params";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final TutorialParams params = (TutorialParams) getArguments().getSerializable(PARAMS);

        assert params != null;
        assert params.viewList != null;
        assert params.viewList.size() == 0;

        final BaseTutorialViewModel vmodel = params.viewModel;

        // 계산하기 편하게 반전
        Collections.reverse(params.viewList);

        final LayoutInflater inflater = LayoutInflater.from(getContext());

        vmodel.init(params.viewList.size() - 1);
        vmodel.index.observe(this, index -> {
            if (index < 0) {
                // index 값이 0 이하면 화면에 표현해야할 view 의 개수가 없는 것이므로
                // 이 fragment 를 닫도록 요구 한다.
                if (mLog.isDebugEnabled()) {
                    mLog.debug("TUTORIAL END");
                }
                
                if (params.resultListener != null) {
                    params.resultListener.onResult(true, null);
                }
                
                return ;
            }

            mBinding.tutorialMain.removeAllViews();

            ViewDataBinding binding = DataBindingUtil.inflate(inflater, params.viewList.get(index),
                mBinding.tutorialMain, true);

            try {
                Method method = binding.getClass().getDeclaredMethod("setVmodel",
                    new Class<?>[]{ vmodel.getClass() });
                method.invoke(binding, new Object[]{ vmodel });
            } catch (Exception e) {
                e.printStackTrace();
                mLog.error("ERROR: " + e.getMessage());
            }
        });
    }

    @Override
    protected int layoutId() {
        return R.layout.tutorial_main;
    }
}
