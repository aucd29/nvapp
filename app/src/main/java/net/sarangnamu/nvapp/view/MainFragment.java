package net.sarangnamu.nvapp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import net.sarangnamu.libfragment.BaseFragment;
import net.sarangnamu.nvapp.R;
import net.sarangnamu.nvapp.databinding.LayoutMainBinding;
import net.sarangnamu.nvapp.viewmodel.MainViewModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 10. <p/>
 */
public class MainFragment extends BaseFragment<LayoutMainBinding> {
    private static final Logger mLog = LoggerFactory.getLogger(MainFragment.class);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mLog.isDebugEnabled()) {
            mLog.debug("CREATE");
        }

        MainViewModel vmodel = viewModel(MainViewModel.class);
        vmodel.init();

        mBinding.setVmodel(vmodel);
    }

    @Override
    protected int layoutId() {
        return R.layout.layout_main;
    }
}
