package net.sarangnamu.libtutorial.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import net.sarangnamu.libcore.OnResultListener;
import net.sarangnamu.libcore.SharedPref;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 11. <p/>
 */
public abstract class BaseTutorialViewModel extends AndroidViewModel {
    private static final Logger mLog = LoggerFactory.getLogger(BaseTutorialViewModel.class);

    private static final String K_FIRST = "tutorial_first";
    private static final String K_COUNT = "tutorial_key";

    private SharedPref prefs = new SharedPref(getApplication(), "tutorial-pref");

    public MutableLiveData<Integer> index = new MutableLiveData<>();

    public BaseTutorialViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(int listSize) {
        boolean first = isFirst();
        if (first) {
            prefs.set(K_COUNT, listSize);
            prefs.set(K_FIRST, false);
        }

        int count = prefs.get(K_COUNT, 0);

        this.index.setValue(count);
    }

    public void next() {
        int count = prefs.get(K_COUNT, 0);
        prefs.set(K_COUNT, --count);
        index.setValue(count);
    }

    public boolean isFinished() {
        boolean res = prefs.get(K_COUNT, 9) == -1;
        if (mLog.isDebugEnabled()) {
            mLog.debug("TUTORIAL FINISHED : " + res);
        }

        return res;
    }

    public boolean isFirst() {
        boolean res = prefs.get(K_FIRST, true);
        if (mLog.isDebugEnabled()) {
            mLog.debug("TUTORIAL FIRST : " + res);
        }

        return res;
    }
}
