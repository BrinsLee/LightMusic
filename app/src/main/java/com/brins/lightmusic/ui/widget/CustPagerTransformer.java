package com.brins.lightmusic.ui.widget;

import android.view.View;
import androidx.viewpager.widget.ViewPager;

/**
 * 实现ViewPager左右滑动时的时差
 * Created by xmuSistone on 2016/9/18.
 */
public class CustPagerTransformer implements ViewPager.PageTransformer {


    public void transformPage(View view, float position) {
        float alpha = 0.0f;
        if (0.0f <= position && position <= 1.0f) {
            alpha = 1.0f - position;
        } else if (-1.0f <= position && position < 0.0f) {
            alpha = position + 1.0f;
        }
        view.setAlpha(alpha);
    }
}
