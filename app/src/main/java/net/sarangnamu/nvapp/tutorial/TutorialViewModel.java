package net.sarangnamu.nvapp.tutorial;

import android.app.Application;
import android.support.annotation.NonNull;

import net.sarangnamu.libtutorial.viewmodel.BaseTutorialViewModel;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 11. <p/>
 */
public class TutorialViewModel extends BaseTutorialViewModel {
    public TutorialViewModel(@NonNull Application application) {
        super(application);
    }
}
