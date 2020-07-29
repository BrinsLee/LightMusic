package com.brins.lightmusic.utils.GlideHelper;

import android.graphics.Bitmap;

import com.bumptech.glide.request.target.SimpleTarget;

/**
 * Created by hzhenxa on 2017/10/24.
 */

public abstract class BitmapTarget extends SimpleTarget<Bitmap> {
    private Object tag;

    public Object getTag() {
        return tag;
    }

    public BitmapTarget setTag(Object tag) {
        this.tag = tag;
        return this;
    }

}
