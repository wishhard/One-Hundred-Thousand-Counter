package com.wishhard.ohtc.helpers;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import java.util.List;

public class AnimatioHelper {

    private ObjectAnimator animUp,animDown,originalPos;
    private AnimatorSet animSet;


    private void animateForward(View v) {
        int v_height = v.getHeight();
        animSet = new AnimatorSet();

        animUp = ObjectAnimator.ofFloat(v,"translationY",0,-v_height + (v_height /2));
        animDown = ObjectAnimator.
                ofFloat(v,"translationY",-v_height + (v_height /2),v_height - (v_height /2));

        originalPos = ObjectAnimator.ofFloat(v,"translationY",v_height - (v_height /2),0);
        animSet.playSequentially(animUp,animDown,originalPos);
        animSet.setDuration(50);
        animSet.setInterpolator(new AccelerateInterpolator(0));
        animSet.start();

    }

    private void animateReverse(View v) {
        int v_height = v.getHeight();
        animSet = new AnimatorSet();

        animUp = ObjectAnimator.ofFloat(v,"translationY",0,-v_height + (v_height /2));
        animDown = ObjectAnimator.
                ofFloat(v,"translationY",-v_height + (v_height /2),v_height - (v_height /2));

        originalPos = ObjectAnimator.ofFloat(v,"translationY",v_height - (v_height /2),0);
        animSet.playSequentially(animDown,animUp,originalPos);
        animSet.setDuration(50);
        animSet.setInterpolator(new AccelerateInterpolator(0));
        animSet.start();

    }


    public void goForward(List<TextView> tv) {

        for (View v: tv) {
            animateForward(v);
        }
    }

    public void goReverse(List<TextView> tv) {

        for (View v: tv) {
            animateReverse(v);
        }
    }
}
