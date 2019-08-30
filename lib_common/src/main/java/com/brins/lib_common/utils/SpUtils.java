package com.brins.lib_common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class SpUtils {
    private SharedPreferences mSharedPreferences;

    private static Map<String, SoftReference<SharedPreferences>> sMap = new HashMap<>();

    private SpUtils(SharedPreferences mSharedPreferences) {
        this.mSharedPreferences = mSharedPreferences;
    }

    /**
     * 获取SpUtils对象
     *
     * @param spName
     * @return
     */
    public static SpUtils obtain(String spName, Context context) {
        SharedPreferences sharedPreferences = null;
        if (sMap.containsKey(spName)) {
            SoftReference<SharedPreferences> softReference = sMap.get(spName);
            sharedPreferences = softReference.get();
        }

        if (null == sharedPreferences) {
            sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
            sMap.put(spName, new SoftReference<>(sharedPreferences));
        }
        return new SpUtils(sharedPreferences);
    }

/*    public static SharedPreferences getSharedPreferences(String spName) {
        return BaseApplication.getInstance().getSharedPreferences(spName, Context.MODE_MULTI_PROCESS);
    }*/

    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    public SharedPreferences.Editor getEditor() {
        return mSharedPreferences.edit();
    }

    /**
     * 原子操作的高效率异步提交
     */
    public void save(String key, boolean value) {
        getEditor().putBoolean(key, value).apply();
    }

    /**
     * 带返回结果的同步提交
     */
    public boolean saveASyn(String key, boolean value) {
        return getEditor().putBoolean(key, value).commit();
    }

    public void save(String key, int value) {
        getEditor().putInt(key, value).apply();
    }

    public boolean saveASyn(String key, int value) {
        return getEditor().putInt(key, value).commit();
    }

    public void save(String key, long value) {
        getEditor().putLong(key, value).apply();
    }

    public boolean saveASyn(String key, long value) {
        return getEditor().putLong(key, value).commit();
    }

    public void save(String key, String value) {
        getEditor().putString(key, value).apply();
    }

    public boolean saveASyn(String key, String value) {
        return getEditor().putString(key, value).commit();
    }

    public void save(String key, float value) {
        getEditor().putFloat(key, value).apply();
    }

    public boolean saveASyn(String key, float value) {
        return getEditor().putFloat(key, value).commit();
    }

    public void remove(String... keys) {
        if (null != keys && keys.length > 0) {
            SharedPreferences.Editor editor = getEditor();
            for (String key : keys) {
                editor.remove(key);
            }
            editor.apply();
        }
    }

    public void clear() {
        getEditor().clear().commit();
    }

    public void removeASyn(String... keys) {
        if (null != keys && keys.length > 0) {
            SharedPreferences.Editor editor = getEditor();
            for (String key : keys) {
                editor.remove(key);
            }
            editor.commit();
        }
    }

    public String getString(String key) {
        return mSharedPreferences.getString(key, null);
    }

    public String getString(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    public boolean getBoolean(String key) {
        return mSharedPreferences.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    public int getInt(String key) {
        return mSharedPreferences.getInt(key, -1);
    }

    public int getInt(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    public void increase(String key) {
        int count = getInt(key, 0);
        save(key, ++count);
    }

    public long getLong(String key) {
        return mSharedPreferences.getLong(key, -1L);
    }

    public long getLong(String key, long defaultValue) {
        return mSharedPreferences.getLong(key, defaultValue);
    }

    public float getFloat(String key) {
        return mSharedPreferences.getFloat(key, -1f);
    }
}
