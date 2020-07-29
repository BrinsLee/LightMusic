package com.brins.lightmusic.utils.GlideHelper;

import android.content.Context;
import android.os.AsyncTask;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.File;

/**
 * Created by yangyuan on 2017/10/24.
 */

public class GlideImageCache {

    private static final String TAG = "GlideImageCache";

    /**
     * 获取图片本地缓存
     */
    public static void getImageCache(Context context, String imgUrl) {
        new GetImageCacheTask(context).execute(imgUrl);
    }

    private static class GetImageCacheTask extends AsyncTask<String, Void, File> {
        private final Context context;

        public GetImageCacheTask(Context context) {
            this.context = context;
        }

        @Override
        protected File doInBackground(String... params) {
            String imgUrl = params[0];
            try {
                return Glide.with(context)
                        .load(imgUrl)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(File result) {
            if (result == null) {
                return;
            }
            String path = result.getPath();
        }
    }
}
