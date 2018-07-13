package net.sarangnamu.nvapp.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.io.Serializable;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 12. <p/>
 */
public class SplashViewModel extends ViewModel implements Serializable {
    public MutableLiveData<Boolean> loaded = new MutableLiveData<>();
}
