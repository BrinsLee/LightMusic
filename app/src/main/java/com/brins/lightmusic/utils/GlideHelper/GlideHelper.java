package com.brins.lightmusic.utils.GlideHelper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;


/**
 * Created by zhouwen on 2017/10/18.
 */

public class GlideHelper {

    private static final String TAG = "GlideHelper";

    /********************************  普通图片加载  *********************************/
    public static void setImageResource(ImageView imageView, String imageUrl, int defaultRes, boolean isTransition, RequestListener<Drawable> listener) {
        try {
            if (imageView == null) return;
            Glide.get(imageView.getContext()).setMemoryCategory(MemoryCategory.NORMAL);
            RequestBuilder requestBuilder;
            requestBuilder = Glide.with(imageView.getContext()).load(imageUrl);
            requestBuilder.apply(new RequestOptions().placeholder(defaultRes));
//            if (!TextUtils.isEmpty(imageUrl)) {
//                requestBuilder = Glide.with(imageView.getContext()).load(imageUrl);
//            } else {
//                requestBuilder = Glide.with(imageView.getContext()).load(defaultRes);
//            }
//            if (!TextUtils.isEmpty(imageUrl) && defaultRes != 0) {
//                requestBuilder.apply(new RequestOptions().placeholder(defaultRes));
//            }
            if (isTransition) {
                requestBuilder.transition(DrawableTransitionOptions.withCrossFade());
            }
            if (listener != null) {
                requestBuilder.listener(listener);
            }
            requestBuilder.into(imageView);
        } catch (IllegalArgumentException e) {
        }
    }

    public static void setImageResource(ImageView imageView, String imageUrl) {
        setImageResource(imageView, imageUrl, 0, true, null);
    }

    public static void setImageResource(ImageView imageView, int resId) {
        setImageResource(imageView, null, resId, true, null);
    }

    public static void setImageResource(ImageView view, String imageUrl, int defaultRes) {
        setImageResource(view, imageUrl, defaultRes, true, null);
    }

    public static void setImageResource(ImageView imageView, String imageUrl, RequestListener<Drawable> listener) {
        setImageResource(imageView, imageUrl, 0, true, listener);
    }

    public static void setImageResource(ImageView imageView, String imageUrl, int defaultRes, RequestListener<Drawable> listener) {
        setImageResource(imageView, imageUrl, defaultRes, true, listener);
    }

    public static void setNonTransitionImageResource(ImageView imageView, String imageUrl) {//无过渡效果
        setImageResource(imageView, imageUrl, 0, false, null);
    }

    public static void setImageResource(Context context, String imageUrl, BitmapTarget simpleTarget) {//特殊情况，只需要返回bitmap
        try {
            if (context != null) {
                Glide.get(context).setMemoryCategory(MemoryCategory.NORMAL);
                Glide.with(context).asBitmap().load(imageUrl).into(simpleTarget);
            }
        } catch (IllegalArgumentException e) {
        }
    }

    public static void setBlurImageResource(Context context, String imageUrl, float radius, int width, int height, BitmapTarget simpleTarget) {
        if (context != null) {
            Glide.get(context).setMemoryCategory(MemoryCategory.NORMAL);
            RequestOptions requestOptions = RequestOptions.bitmapTransform(new BlurTransformation(context, radius));
            requestOptions.override(width, height);
            Glide.with(context).asBitmap().load(imageUrl).apply(requestOptions).into(simpleTarget);
        }
    }

    /********************************  圆形图片加载  *********************************/

    public static void setCircleImageResource(ImageView imageView, String imageUrl, int defaultRes, boolean isTransition, boolean isCacheMemory, RequestListener<Drawable> listener) {
        try {
            if (imageView == null) {
                return;
            }
            if (isCacheMemory) {
                Glide.get(imageView.getContext()).setMemoryCategory(MemoryCategory.NORMAL);
            }
            RequestBuilder requestBuilder;
            if (!TextUtils.isEmpty(imageUrl)) {
                requestBuilder = Glide.with(imageView.getContext()).load(imageUrl);
            } else {
                requestBuilder = Glide.with(imageView.getContext()).load(defaultRes);
            }
            if (!TextUtils.isEmpty(imageUrl) && defaultRes != 0) {
                requestBuilder.apply(new RequestOptions().placeholder(defaultRes));
            }
            if (isTransition) {
                requestBuilder.transition(DrawableTransitionOptions.withCrossFade());
            }
            if (listener != null) {
                requestBuilder.listener(listener);
            }
            requestBuilder.apply(RequestOptions.bitmapTransform(new GlideCircleTransform(imageView.getContext()))).into(imageView);
        } catch (IllegalArgumentException e) {
        }
    }

    public static void setCircleImageResource(ImageView imageView, String imageUrl, boolean isCacheMemory) {
        setCircleImageResource(imageView, imageUrl, 0, true, isCacheMemory, null);
    }

    public static void setCircleImageResource(ImageView imageView, String imageUrl) {
        setCircleImageResource(imageView, imageUrl, 0, true, true, null);
    }

    public static void setCircleImageResource(ImageView imageView, String imageUrl, int defaultRes) {
        setCircleImageResource(imageView, imageUrl, defaultRes, false, true, null);
    }

    public static void setCircleImageResource(ImageView imageView, int resId, boolean isCacheMemory) {
        setCircleImageResource(imageView, null, resId, true, isCacheMemory, null);
    }

    public static void setCircleImageResource(ImageView imageView, int resId) {
        setCircleImageResource(imageView, null, resId, true, true, null);
    }

    /**************************特殊处理圆形图片*/
    public static void setCircleImageResource(Context context, String res, BitmapTarget simpleTarget) {
        try {
            if (context != null) {
                Glide.get(context).setMemoryCategory(MemoryCategory.NORMAL);
                Glide.with(context).asBitmap()
                        .load(res)
                        .apply(RequestOptions.bitmapTransform(new GlideCircleTransform(context)))
                        .into(simpleTarget);
            }
        } catch (IllegalArgumentException e) {
        }
    }

    public static void setCircleImageResource(Context context, String res, SimpleTarget<Drawable> simpleTarget, boolean isCacheMemory) {
        try {
            if (context != null) {
                if (isCacheMemory) {
                    Glide.get(context).setMemoryCategory(MemoryCategory.NORMAL);
                }
                Glide.with(context).load(res)
                        .apply(new RequestOptions().transform(new BlurTransformation(context)))
                        .into(simpleTarget);
            }
        } catch (IllegalArgumentException e) {
        }
    }

    public static void setCircleImageResource(Context context, String res, SimpleTarget<Drawable> simpleTarget) {
        setCircleImageResource(context, res, simpleTarget, true);
    }


    /********************************  高斯模糊图片加载   *********************************/
    public static void setBlurImageResource(ImageView imageView, String imageUrl, int defaultRes, boolean isTransition, float radius, int color, RequestListener<Drawable> listener) {
        try {
            if (imageView == null) {
                return;
            }
            Glide.get(imageView.getContext()).setMemoryCategory(MemoryCategory.NORMAL);

            RequestBuilder requestBuilder;
            if (!TextUtils.isEmpty(imageUrl)) {
                requestBuilder = Glide.with(imageView.getContext()).load(imageUrl);
            } else {
                requestBuilder = Glide.with(imageView.getContext()).load(defaultRes);
            }
            if (!TextUtils.isEmpty(imageUrl) && defaultRes != 0) {
                requestBuilder.apply(new RequestOptions().placeholder(defaultRes));
            }
            if (isTransition) {
                requestBuilder.transition(DrawableTransitionOptions.withCrossFade());
            }
            if (listener != null) {
                requestBuilder.listener(listener);
            }
            if (color != 0) {
                requestBuilder.apply(RequestOptions.bitmapTransform(new BlurTransformation(imageView.getContext(), radius, color)));
            } else {
                requestBuilder.apply(RequestOptions.bitmapTransform(new BlurTransformation(imageView.getContext(), radius)));
            }
            requestBuilder.into(imageView);
        } catch (IllegalArgumentException e) {
        }
    }

    public static void setBlurImageResource(ImageView imageView, String imageUrl) {
        setBlurImageResource(imageView, imageUrl, 0, true, 25, 0, null);
    }

    public static void setBlurImageResource(ImageView imageView, String imageUrl, int defaultRes, float radius) {
        setBlurImageResource(imageView, imageUrl, defaultRes, true, radius, 0, null);
    }

    public static void setBlurImageResource(ImageView imageView, String imageUrl, float radius) {
        setBlurImageResource(imageView, imageUrl, 0, true, radius, 0, null);
    }

    public static void setBlurImageResource(ImageView imageView, String imageUrl, float radius, RequestListener<Drawable> listener) {
        setBlurImageResource(imageView, imageUrl, 0, true, 25, 0, listener);
    }

    public static void setBlurImageResource(ImageView imageView, int rid, float radius, int color) {
        setBlurImageResource(imageView, null, rid, true, radius, color, null);
    }

    public static void setBlurImageResource(ImageView imageView, String imageUrl, float radius, int color) {
        setBlurImageResource(imageView, imageUrl, 0, true, radius, color, null);
    }

    public static void setBlurImageResource(ImageView imageView, int rid, float radius) {
        setBlurImageResource(imageView, null, rid, true, radius, 0, null);
    }


    public static void setRoundImageResource(ImageView imageView, String url, int radius) {
        setRoundImageResource(imageView, url, radius, null);
    }

    private static void setRoundImageResource(ImageView imageView, String imageUrl, int radius, RequestListener<Drawable> listener) {
        try {
            if (imageView == null) {
                return;
            }
            Glide.get(imageView.getContext()).setMemoryCategory(MemoryCategory.NORMAL);
            RequestBuilder requestBuilder;
            requestBuilder = Glide.with(imageView.getContext()).load(imageUrl);
            if (listener != null) {
                requestBuilder.listener(listener);
            }
            requestBuilder.apply(RequestOptions.bitmapTransform(new GlideRoundTransform(imageView.getContext(), radius)));
            requestBuilder.into(imageView);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /********************************  gif图片加载   *********************************/

    public static void setGifImageResource(ImageView imageView, String gifUrl, RequestListener<Drawable> glideGifListener) {
        try {
            if (imageView != null && imageView.getContext() != null) {
                Glide.get(imageView.getContext()).setMemoryCategory(MemoryCategory.NORMAL);
                Glide.with(imageView.getContext()).load(gifUrl).
                        listener(glideGifListener).into(new DrawableImageViewTarget(imageView));
            }
        } catch (IllegalArgumentException e) {
        }
    }

    /*****************  其他通用方法  **************/
    /**
     * 暂停加载
     *
     * @param context
     */
    public static void pauseRequests(Context context) {
        try {
            Glide.with(context).pauseRequests();
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * 重新恢复加载
     *
     * @param context
     */
    public static void resumeRequest(Context context) {
        try {
            Glide.with(context).resumeRequests();
        } catch (IllegalArgumentException e) {
        }
    }

    public static void onStop(Context context) {
        try {
            Glide.with(context).onStop();
        } catch (IllegalArgumentException e) {
        }
    }

    public static void onGlideDestroy(Context context) {
        try {
            Glide.with(context).onDestroy();
        } catch (IllegalArgumentException e) {
        }
    }

}
