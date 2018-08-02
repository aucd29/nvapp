package net.sarangnamu.nvapp.view.tutorial;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.view.View;

import net.sarangnamu.libcore.AnimationEndListener;
import net.sarangnamu.nvapp.MainApp;
import net.sarangnamu.nvapp.databinding.TutorialIntroBinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 8. 1. <p/>
 */
public class TutorialIntro {
    private static final Logger mLog = LoggerFactory.getLogger(TutorialIntro.class);

    private static final String TRANS_Y = "translationY";
    private static final String TRANS_X = "translationX";
    private static final String ALPHA   = "alpha";

    // 처음엔 공통으로 써보려고 애니메이션 관련 binding adapter 를 구성하였으나
    // 객체 하나만 움직이는게 아니고 다수의 객체가 시간차를 두고 움직여야 되는 문제 때문에
    // view 를 직접 코드로 컨트롤 하기로 변경 함
    public static void event(@NonNull TutorialIntroBinding binding) {
        if (mLog.isTraceEnabled()) {
            mLog.trace("TUTORIAL INTRO");
        }

        int screenWidth     = MainApp.screenX;
        int phoneFrameWidth = (int) (screenWidth * 0.7f);
        int margin          = (screenWidth - phoneFrameWidth) / 2;
        int sideX           = margin / 2;
        int moveY           = (int) (MainApp.screenY / 1.8f);

        if (mLog.isDebugEnabled()) {
            mLog.debug("SCREEN WIDTH : " + screenWidth + "\n" +
                "FRAME        : " + phoneFrameWidth + "\n" +
                "SIDE X       : " + sideX + "\n" +
                "MOVE Y       : " + moveY);
        }

        binding.phoneFrame.getLayoutParams().width   = phoneFrameWidth;
        binding.panelLeft.getLayoutParams().width    = phoneFrameWidth;
        binding.panelCenter.getLayoutParams().width  = phoneFrameWidth;
        binding.panelRight.getLayoutParams().width   = phoneFrameWidth;
        binding.panelRight2 .getLayoutParams().width = phoneFrameWidth;

        ObjectAnimator fadeInPhoneFrame  = ObjectAnimator.ofFloat(binding.phoneFrame,
            ALPHA, 0, 1);
        ObjectAnimator transYPhoneFrame  = ObjectAnimator.ofFloat(binding.phoneFrame,
            TRANS_Y, moveY, 0);

        ObjectAnimator fadeInPanelCenter = ObjectAnimator.ofFloat(binding.panelCenter,
            ALPHA, 0, 1);
        ObjectAnimator transYPanelCenter = ObjectAnimator.ofFloat(binding.panelCenter,
            TRANS_Y, moveY, 0);

        ObjectAnimator transXPanelLeft   = ObjectAnimator.ofFloat(binding.panelLeft,
            TRANS_X, 0, sideX);
        ObjectAnimator transYPanelLeft   = ObjectAnimator.ofFloat(binding.panelLeft,
            TRANS_Y, moveY, 0);

        ObjectAnimator transXPanelRight  = ObjectAnimator.ofFloat(binding.panelRight,
            TRANS_X, 0, sideX * -1);
        ObjectAnimator transYPanelRight  = ObjectAnimator.ofFloat(binding.panelRight,
            TRANS_Y, moveY, 0);

        if (mLog.isTraceEnabled()) {
            mLog.trace("TUTORIAL ANIMATION START");
        }

        final AnimatorSet aniSet = new AnimatorSet();
        aniSet.setDuration(750);
        aniSet.playTogether(fadeInPhoneFrame, transYPhoneFrame, fadeInPanelCenter, transYPanelCenter,
            transXPanelLeft, transYPanelLeft, transXPanelRight, transYPanelRight);
        aniSet.addListener(new AnimationEndListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                animator.removeListener(this);

                if (mLog.isTraceEnabled()) {
                    mLog.trace("TUTORIAL BUTTON LAYOUT SHOW");
                }

                final ObjectAnimator transYButtonLayout = ObjectAnimator.ofFloat(binding.buttonLayout,
                    TRANS_Y, binding.buttonLayout.getHeight(), 0);
                transYButtonLayout.setDuration(500);
                transYButtonLayout.addListener(new AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        animator.removeListener(this);

                        if (mLog.isTraceEnabled()) {
                            mLog.trace("SCROLLING PANELS");
                        }

                        int moveX = (phoneFrameWidth + sideX) * -1;

                        ObjectAnimator left   = ObjectAnimator.ofFloat(binding.panelLeft,
                            TRANS_X, moveX + sideX);
                        ObjectAnimator center = ObjectAnimator.ofFloat(binding.panelCenter,
                            TRANS_X, moveX);
                        ObjectAnimator right  = ObjectAnimator.ofFloat(binding.panelRight,
                            TRANS_X, moveX - sideX);
                        ObjectAnimator right2 = ObjectAnimator.ofFloat(binding.panelRight2,
                            TRANS_X, moveX - sideX);

                        AnimatorSet set = new AnimatorSet();
                        set.setDuration(700);
                        set.playTogether(left, center, right, right2);
                        set.start();
                    }
                });
                transYButtonLayout.start();

                binding.buttonLayout.setVisibility(View.VISIBLE);
            }
        });
        aniSet.start();
    }
}
