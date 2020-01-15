package com.brins.lightmusic.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.request.RequestListener;

import java.io.File;

public class ImageLoader {
    private String url; //需要解析的 url
    private File file;//图片文件
    private int placeHolder; //当没有成功加载的时候显示的图片
    private int errorHolder = 0; //当错误加载时显示的图片
    private ImageView imgView; //ImageView 的实例
    private int assignWidth;//指定宽
    private int assignHeight;//指定高
    private int scaleModeType;//缩放模式
    private Transformation bitmapTransformation;
    private boolean isRound;
    private RequestListener requestListener;


    private ImageLoader(Builder builder) {
        this.url = builder.url;
        this.placeHolder = builder.placeHolder;
        this.errorHolder = builder.errorHolder;
        this.imgView = builder.imgView;
        this.assignWidth = builder.assignWidth;
        this.assignHeight = builder.assignHeight;
        this.scaleModeType = builder.scaleModeType;
        this.bitmapTransformation = builder.bitmapTransformation;
        this.file = builder.file;
        this.isRound = builder.isRound;
        this.requestListener = builder.mRequestListener;

    }

    public String getUrl() {
        return url;
    }

    public int getPlaceHolder() {
        return placeHolder;
    }

    public ImageView getImgView() {
        return imgView;
    }

    public int getAssignWidth() {
        return assignWidth;
    }

    public int getAssignHeight() {
        return assignHeight;
    }

    public int getScaleModeType() {
        return scaleModeType;
    }

    public int getErrorHolder() {
        return errorHolder;
    }

    public Transformation getBitmapTransformation() {
        return bitmapTransformation;
    }


    public boolean isRound() {
        return isRound;
    }

    public File getFile() {
        return file;
    }

    public RequestListener getRequestListener() {
        return requestListener;
    }

    public static class Builder {
        private String url; //需要解析的url
        private File file; //图片文件
        private int placeHolder; //当没有成功加载的时候显示的图片
        private int errorHolder; //当错误加载时显示的图片
        private ImageView imgView; //ImageView的实例
        private int assignWidth; //指定宽
        private int assignHeight;//指定高
        private int scaleModeType;//缩放模式
        private Transformation bitmapTransformation;
        private Transformation<Bitmap>[] bitmapTransformations;
        private boolean isRound;
        private RequestListener mRequestListener;

        public Builder() {
            this.url = "";
            this.placeHolder = 0;
            this.imgView = null;
            this.isRound = false;
        }

        //图片占位符
        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder placeHolder(int placeHolder) {
            this.placeHolder = placeHolder;
            return this;
        }

        public Builder error(int error) {
            this.errorHolder = error;
            return this;
        }

        //图片控件
        public Builder imgView(ImageView imgView) {
            this.imgView = imgView;
            return this;
        }

        //图片宽度
        public Builder assignWidth(int assignWidth) {
            this.assignWidth = assignWidth;
            return this;
        }

        //图片高度
        public Builder assignHeight(int assignHeight) {
            this.assignHeight = assignHeight;
            return this;
        }

        //缩放模式 默认fitcenter
        public Builder scaleModeType(int scaleModeType) {
            this.scaleModeType = scaleModeType;
            return this;
        }


        public Builder bitmapTransformation(Transformation bitmapTransformation) {
            this.bitmapTransformation = bitmapTransformation;
            return this;
        }

        public Builder setRequestListenner(RequestListener requestListenner) {
            this.mRequestListener = requestListenner;
            return this;
        }


        public Builder isRound(boolean round) {
            this.isRound = round;
            return this;
        }


        //图片文件
        public Builder file(File file) {
            this.file = file;
            return this;
        }


        public ImageLoader bulid() {
            return new ImageLoader(this);
        }
    }
}
