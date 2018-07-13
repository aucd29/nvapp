package net.sarangnamu.nvapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Observer;
import android.databinding.BindingAdapter;
import android.databinding.ObservableFloat;
import android.databinding.ObservableLong;
import android.support.annotation.NonNull;
import android.view.View;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 13. <p/>
 */
public class NvAppTutorialViewModel extends AndroidViewModel {
    private static final Logger mLog = LoggerFactory.getLogger(NvAppTutorialViewModel.class);
    
    public ObservableLong logoFade              = new ObservableLong(700);

    public ObservableLong titleFade             = new ObservableLong(700);
    public ObservableFloat titleTransition      = new ObservableFloat(200);

    public ObservableLong phoneFrameFade        = new ObservableLong(700);
    public ObservableFloat phoneFrameTransition = new ObservableFloat(500);

    public NvAppTutorialViewModel(@NonNull Application application) {
        super(application);
    }

    public void login() {
        if (mLog.isDebugEnabled()) {
            mLog.debug("LOGIN");
        }
        
    }

    @BindingAdapter("bindStartCellStartAnimation")
    public static void bindStartCellStartAnimation(View view, long duration) {
        view.setTranslationX(-300);
        view.setTranslationY(100);
        view.animate().translationX(-50).translationY(0).start();
    }

    @BindingAdapter("bindEndCellStartAnimation")
    public static void bindEndCellStartAnimation(View view, long duration) {

    }

    @BindingAdapter("bindDelayTransitionY")
    public static void bindDelayTransitionY(View view, Object dumy) {
        float height = 500f; //view.getHeight();

        if (mLog.isDebugEnabled()) {
            mLog.debug("DELAY TRANSITION Y (" + height + ")");
        }

        view.setTranslationY(height);
        view.animate().setStartDelay(1500).translationY(0).setDuration(700).start();
    }
}