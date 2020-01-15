package com.brins.lightmusic.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.SimpleTarget;

public interface BaseImageLoaderStrategy {

    void loadImage(Context context, ImageLoader imageLoader);

    void loadImage(Context context, ImageLoader imageLoader, SimpleTarget<Bitmap> simpleTarget);

    FutureTarget<Bitmap> loadBitmap(Context context, ImageLoader imageLoader);
}
