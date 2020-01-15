package com.brins.lightmusic.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;


public class GlideImageLoader implements BaseImageLoaderStrategy {
    @Override
    public void loadImage(Context context, ImageLoader imageLoader) {
        RequestOptions options = new RequestOptions();
        if (imageLoader.getAssignWidth() != 0 && imageLoader.getAssignHeight() != 0) {
            options.override(imageLoader.getAssignWidth(), imageLoader.getAssignHeight());
        }
        switch (imageLoader.getScaleModeType()) {
            case ImageLoadreUtils.SCALE_MODE_CENTER_CROP:
                options.centerCrop();
                break;
            case ImageLoadreUtils.SCALE_MODE_FIT_CENTER:
                options.fitCenter();
                break;
            default:
                options.fitCenter();
                break;
        }

        if (imageLoader.getBitmapTransformation() != null) {
            options.transforms(imageLoader.getBitmapTransformation());
        } else if (imageLoader.isRound()) {
            options.circleCrop();
        }


        if (imageLoader.getErrorHolder() != 0) {
            options.error(imageLoader.getErrorHolder());
        }

        if (imageLoader.getPlaceHolder() != 0) {
            options.placeholder(imageLoader.getPlaceHolder());
        }
//        if (imageLoader.getBitmapTransformation() != null) {
//            options.bitmapTransform(imageLoader.getBitmapTransformation());
//        }
//        options.skipMemoryCache(false);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
//        drawableTypeRequest.error(R.drawable.default_avatar_icon);
        if (imageLoader.getFile() != null) {
            Glide.with(context).load(imageLoader.getFile()).listener(imageLoader.getRequestListener()).transition(DrawableTransitionOptions.withCrossFade()).apply(options).into(imageLoader.getImgView());
        } else {
            Glide.with(context).load(imageLoader.getUrl()).listener(imageLoader.getRequestListener()).transition(DrawableTransitionOptions.withCrossFade()).apply(options).into(imageLoader.getImgView());
        }

    }

    @Override
    public void loadImage(Context context, ImageLoader imageLoader, SimpleTarget<Bitmap> simpleTarget) {
        RequestOptions options = new RequestOptions();
        if (imageLoader.getAssignWidth() != 0 && imageLoader.getAssignHeight() != 0) {
            options.override(imageLoader.getAssignWidth(), imageLoader.getAssignHeight());
        }
        switch (imageLoader.getScaleModeType()) {
            case ImageLoadreUtils.SCALE_MODE_CENTER_CROP:
                options.centerCrop();
                break;
            case ImageLoadreUtils.SCALE_MODE_FIT_CENTER:
                options.fitCenter();
                break;
            default:
                options.fitCenter();
                break;
        }

        if (imageLoader.getBitmapTransformation() != null) {
            options.transforms(imageLoader.getBitmapTransformation());
        } else if (imageLoader.isRound()) {
            options.circleCrop();
        }


        if (imageLoader.getErrorHolder() != 0) {
            options.error(imageLoader.getErrorHolder());
        }

        if (imageLoader.getPlaceHolder() != 0) {
            options.placeholder(imageLoader.getPlaceHolder());
        }
//        if (imageLoader.getBitmapTransformation() != null) {
//            options.bitmapTransform(imageLoader.getBitmapTransformation());
//        }
//        options.skipMemoryCache(false);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        if (imageLoader.getUrl() != null){
            Glide.with(context).asBitmap().load(imageLoader.getUrl()).apply(options)
                    .into(simpleTarget);
        }
    }

    @Override
    public FutureTarget<Bitmap> loadBitmap(Context context, ImageLoader imageLoader) {
        RequestOptions options = new RequestOptions();
        if (imageLoader.getAssignWidth() != 0 && imageLoader.getAssignHeight() != 0) {
            options.override(imageLoader.getAssignWidth(), imageLoader.getAssignHeight());
        }
        switch (imageLoader.getScaleModeType()) {
            case ImageLoadreUtils.SCALE_MODE_CENTER_CROP:
                options.centerCrop();
                break;
            case ImageLoadreUtils.SCALE_MODE_FIT_CENTER:
                options.fitCenter();
                break;
            default:
                options.fitCenter();
                break;
        }

        if (imageLoader.getBitmapTransformation() != null) {
            options.transforms(imageLoader.getBitmapTransformation());
        } else if (imageLoader.isRound()) {
            options.circleCrop();
        }


        if (imageLoader.getErrorHolder() != 0) {
            options.error(imageLoader.getErrorHolder());
        }

        if (imageLoader.getPlaceHolder() != 0) {
            options.placeholder(imageLoader.getPlaceHolder());
        }
//        if (imageLoader.getBitmapTransformation() != null) {
//            options.bitmapTransform(imageLoader.getBitmapTransformation());
//        }
//        options.skipMemoryCache(false);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        if (imageLoader.getUrl() != null){
            return Glide.with(context).asBitmap().load(imageLoader.getUrl()).apply(options).submit();
        }
        return null;
    }
}
