package net.sarangnamu.nvapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import net.sarangnamu.nvapp.callback.FragmentCallback;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 20. <p/>
 */
public class LoginViewModel extends AndroidViewModel {
    public FragmentCallback mFragmentCallback;

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public void back() {

    }

    public void login() {
        if (mFragmentCallback == null) {
            return ;
        }

//        mFragmentCallback.showFragment();
    }


}
