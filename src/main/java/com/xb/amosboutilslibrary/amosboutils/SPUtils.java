package com.xb.amosboutilslibrary.amosboutils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.util.Iterator;
import java.util.Set;

/**
 * @author xiangbo
 * @version 1.0
 * @作者 E-mail:284285624@qq.com
 * @date 创建时间：2015年8月19日 下午3:17:11
 * @描述：
 */
public class SPUtils {

    private Context mContext;

    private SharedPreferences mSp = null;

    private Editor mEdit = null;
    /**
     * 持久化-文件名
     */
    private static final String FILE_NAME = "user_info";

    private static volatile SPUtils mSPUtils = null;

    private static SPUtils getInstance(Context context) {
        if (mSPUtils == null) {
            synchronized (SPUtils.class) {
                if (mSPUtils == null) {
                    mSPUtils = new SPUtils(context.getApplicationContext(),FILE_NAME);
                }
            }
        }
        return mSPUtils;
    }

    /**
     * Create DefaultSharedPreferences
     *
     * @param mContext
     */
    public SPUtils(Context mContext) {
        this(mContext, PreferenceManager.getDefaultSharedPreferences(mContext));
    }

    /**
     * Create SharedPreferences by filename
     *
     * @param mContext
     * @param filename
     */
    public SPUtils(Context mContext, String filename) {
        this(mContext, mContext.getSharedPreferences(filename, Context.MODE_MULTI_PROCESS |
                Context.MODE_PRIVATE));
    }

    /**
     * Create SharedPreferences by SharedPreferences
     *
     * @param context
     * @param sp
     */
    public SPUtils(Context context, SharedPreferences sp) {
        this.mContext = context;
        this.mSp = sp;
        mEdit = sp.edit();
    }

    /**
     * @param key
     * @param value
     * @description
     * @date 2015年8月5日
     */
    public void setValue(String key, boolean value) {
        mEdit.putBoolean(key, value);
        mEdit.commit();
    }

    /**
     * @param resKey
     * @param value
     * @description
     * @date 2015年8月5日
     */
    public void setValue(int resKey, boolean value) {
        setValue(this.mContext.getString(resKey), value);
    }

    /**
     * @param key
     * @param value
     * @description
     * @date 2015年8月5日
     */
    public void setValue(String key, float value) {
        mEdit.putFloat(key, value);
        mEdit.commit();
    }

    /**
     * @param resKey
     * @param value
     * @description
     * @date 2015年8月5日
     */
    public void setValue(int resKey, float value) {
        setValue(this.mContext.getString(resKey), value);
    }

    /**
     * @param key
     * @param value
     * @description
     * @date 2015年8月5日
     */
    public void setValue(String key, int value) {
        mEdit.putInt(key, value);
        mEdit.commit();
    }

    /**
     * @param resKey
     * @param value
     * @description
     * @date 2015年8月5日
     */
    public void setValue(int resKey, int value) {
        setValue(this.mContext.getString(resKey), value);
    }

    /**
     * @param key
     * @param value
     * @description
     * @date 2015年8月5日
     */
    // Long
    public void setValue(String key, long value) {
        mEdit.putLong(key, value);
        mEdit.commit();
    }

    /**
     * @param resKey
     * @param value
     * @description
     * @date 2015年8月5日
     */
    public void setValue(int resKey, long value) {
        setValue(this.mContext.getString(resKey), value);
    }

    /**
     * @param key
     * @param value
     * @description
     * @date 2015年8月5日
     */
    // String
    public void setValue(String key, String value) {
        mEdit.putString(key, value);
        mEdit.commit();
    }

    /**
     * @param resKey
     * @param value
     * @description
     * @date 2015年8月5日
     */
    public void setValue(int resKey, String value) {
        setValue(this.mContext.getString(resKey), value);
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     * @description
     * @date 2015年8月5日
     */
    public boolean getValue(String key, boolean defaultValue) {
        return mSp.getBoolean(key, defaultValue);
    }

    /**
     * @param resKey
     * @param defaultValue
     * @return
     * @description
     * @date 2015年8月5日
     */
    public boolean getValue(int resKey, boolean defaultValue) {
        return getValue(this.mContext.getString(resKey), defaultValue);
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     * @description
     * @date 2015年8月5日
     */
    // Float
    public float getValue(String key, float defaultValue) {
        return mSp.getFloat(key, defaultValue);
    }

    /**
     * @param resKey
     * @param defaultValue
     * @return
     * @description
     * @date 2015年8月5日
     */
    public float getValue(int resKey, float defaultValue) {
        return getValue(this.mContext.getString(resKey), defaultValue);
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     * @description
     * @date 2015年8月5日
     */
    // Integer
    public int getValue(String key, int defaultValue) {
        return mSp.getInt(key, defaultValue);
    }

    /**
     * @param resKey
     * @param defaultValue
     * @return
     * @description
     * @date 2015年8月5日
     */
    public int getValue(int resKey, int defaultValue) {
        return getValue(this.mContext.getString(resKey), defaultValue);
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     * @description
     * @date 2015年8月5日
     */
    // Long
    public long getValue(String key, long defaultValue) {
        return mSp.getLong(key, defaultValue);
    }

    /**
     * @param resKey
     * @param defaultValue
     * @return
     * @description
     * @date 2015年8月5日
     */
    public long getValue(int resKey, long defaultValue) {
        return getValue(this.mContext.getString(resKey), defaultValue);
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     * @description
     * @date 2015年8月5日
     */
    // String
    public String getValue(String key, String defaultValue) {
        return mSp.getString(key, defaultValue);
    }

    /**
     * @param resKey
     * @param defaultValue
     * @return
     * @description
     * @date 2015年8月5日
     */
    public String getValue(int resKey, String defaultValue) {
        return getValue(this.mContext.getString(resKey), defaultValue);
    }

    /**
     * @param key
     * @description
     * @date 2015年8月5日
     */
    // Delete
    public void remove(String key) {
        mEdit.remove(key);
        mEdit.commit();
    }

    /**
     * @description
     * @date 2015年8月5日
     */
    // Clear
    public void clear() {
        mEdit.clear();
        mEdit.commit();
    }

    /**
     * @param indexStr
     * @description 移除所有的包含指定字符串的配置项
     * @date 2016年3月3日
     */
    public void removeContainString(String indexStr) {
        Set<String> keys = mSp.getAll().keySet();
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            String key = it.next();
            if (key.endsWith(indexStr)) {
                mEdit.remove(key);
            }
        }
        mEdit.commit();
    }

}
