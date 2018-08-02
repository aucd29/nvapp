package net.sarangnamu.nvapp.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableFloat;
import android.databinding.ObservableLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 13. <p/>
 */
public class TutorialAnimViewModel extends ViewModel {
    public ObservableLong logoFadeDuration  = new ObservableLong(700);
    public ObservableLong titleFadeDuration = new ObservableLong(700);
    public ObservableFloat titleTransitionY = new ObservableFloat(200);  // 임시 값 (dp 로 변경해야 함)
}
