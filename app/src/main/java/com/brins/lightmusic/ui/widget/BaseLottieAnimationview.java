package com.brins.lightmusic.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.AttributeSet;
import com.airbnb.lottie.LottieAnimationView;

public class BaseLottieAnimationview extends LottieAnimationView {
    public BaseLottieAnimationview(Context context) {
        super(context);
    }

    public BaseLottieAnimationview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseLottieAnimationview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        useHardwareAcceleration();
    }

    public Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }
}
