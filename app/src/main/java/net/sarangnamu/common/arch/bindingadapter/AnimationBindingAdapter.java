package net.sarangnamu.common.arch.bindingadapter;

import android.databinding.BindingAdapter;
import android.view.View;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 13. <p/>
 */
public class AnimationBindingAdapter {
    private static final Logger mLog = LoggerFactory.getLogger(AnimationBindingAdapter.class);

    @BindingAdapter("bindFadeIn")
    public static void fadeIn(View view, long duration) {
        if (mLog.isDebugEnabled()) {
            mLog.debug("FADE IN (" + duration + ")");
        }

        view.setAlpha(0);
        view.animate().alpha(1).setDuration(duration).start();
    }

    @BindingAdapter({"bindTransitionY", "bindFadeIn"})
    public static void alphaTransitionY(View view, float translationY, long duration) {
        if (mLog.isDebugEnabled()) {
            mLog.debug("ALPHA TRANSITION (" + translationY + ", " + duration + ")");
        }

        view.setAlpha(0);
        view.setTranslationY(translationY);
        view.animate().alpha(1).translationY(0).setDuration(duration).start();
    }


}
