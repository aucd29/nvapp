package net.sarangnamu.nvapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Observer;
import android.databinding.BindingAdapter;
import android.databinding.ObservableFloat;
import android.databinding.ObservableLong;
import android.support.annotation.NonNull;
import android.view.View;

import net.sarangnamu.common.util.DimUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 13. <p/>
 */
public class NvAppTutorialViewModel extends AndroidViewModel {
    private static final Logger mLog = LoggerFactory.getLogger(NvAppTutorialViewModel.class);

    public ObservableLong logoFadeDuration  = new ObservableLong(700);
    public ObservableLong titleFadeDuration = new ObservableLong(700);
    public ObservableFloat titleTransitionY = new ObservableFloat(200);  // 임시 값 (dp 로 변경해야 함)

    public NvAppTutorialViewModel(@NonNull Application application) {
        super(application);
    }

    public void login() {
        if (mLog.isDebugEnabled()) {
            mLog.debug("LOGIN");
        }
    }
}
