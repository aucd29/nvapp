package net.sarangnamu.nvapp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import net.sarangnamu.libfragment.BaseFragment;
import net.sarangnamu.nvapp.R;
import net.sarangnamu.nvapp.databinding.LayoutNavigationBinding;
import net.sarangnamu.nvapp.viewmodel.NavigationViewModel;
import net.sarangnamu.nvapp.viewmodel.UserInfoViewModel;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 21. <p/>
 */
public class NavigationFragment extends BaseFragment<LayoutNavigationBinding> {
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        NavigationViewModel vmodel = viewModel(NavigationViewModel.class);
        UserInfoViewModel user     = viewModel(UserInfoViewModel.class);

        mBinding.setVmodel(vmodel);
        mBinding.setUser(user);
    }

    @Override
    protected int layoutId() {
        return R.layout.layout_navigation;
    }
}
