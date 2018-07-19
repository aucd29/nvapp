package net.sarangnamu.libcore;

import android.animation.Animator;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 19. <p/>
 */
public abstract class AnimationEndListener implements Animator.AnimatorListener {
    @Override public void onAnimationStart(Animator animator) { }
    @Override public void onAnimationCancel(Animator animator) { }
    @Override public void onAnimationRepeat(Animator animator) { }
}
