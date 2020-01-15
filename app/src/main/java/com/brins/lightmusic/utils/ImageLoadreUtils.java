package com.brins.lightmusic.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.SimpleTarget;

public class ImageLoadreUtils {
    private static ImageLoadreUtils mInstance;
    private BaseImageLoaderStrategy imageLoaderStrategy;
    public static final int SCALE_MODE_FIT_CENTER = 1;
    public static final int SCALE_MODE_CENTER_CROP = 2;

    private ImageLoadreUtils() {
        //默认使用 Glide 加载模式
        imageLoaderStrategy = new GlideImageLoader();
    }

    public static ImageLoadreUtils getInstance() {
        if (mInstance == null) {
            synchronized (ImageLoadreUtils.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoadreUtils();
                    return mInstance;
                }
            }
        }
        return mInstance;
    }

    /**
     * 设置使用的图片加载框架
     *
     * @param imageLoaderStrategy
     */
    public void setImageLoaderStrategy(BaseImageLoaderStrategy imageLoaderStrategy) {
        this.imageLoaderStrategy = imageLoaderStrategy;
    }

    /**
     * 加载图片
     *
     * @param context
     * @param imageLoader
     */
    public void loadImage(Context context, ImageLoader imageLoader) {
        imageLoaderStrategy.loadImage(context, imageLoader);
    }

    /**
     * 异步下载图片
     * @param context
     * @param imageLoader
     * @param simpleTarget
     */
    public void loadImage(Context context, ImageLoader imageLoader, SimpleTarget<Bitmap> simpleTarget) {
        imageLoaderStrategy.loadImage(context, imageLoader, simpleTarget);
    }

    /**
     * 同步下载图片
     * @param context
     * @param imageLoader
     * @return
     */
    public FutureTarget<Bitmap> loadBitmap(Context context, ImageLoader imageLoader) {
        return imageLoaderStrategy.loadBitmap(context, imageLoader);
    }
}
