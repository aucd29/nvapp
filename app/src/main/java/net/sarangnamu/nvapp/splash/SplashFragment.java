package net.sarangnamu.nvapp.splash;

import android.os.Bundle;
import android.support.annotation.Nullable;

import net.sarangnamu.libfragment.BaseFragment;
import net.sarangnamu.nvapp.R;
import net.sarangnamu.nvapp.databinding.SplashMainBinding;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 11. <p/>
 */
public class SplashFragment extends BaseFragment<SplashMainBinding> {
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO
    }

    @Override
    protected int layoutId() {
        return R.layout.splash_main;
    }
}
