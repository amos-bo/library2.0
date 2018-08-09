package com.xb.amosboutilslibrary.amosbopopupwindow;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * @author : Amos_bo
 * @package: com.xb.myapplication.amosbodialog
 * @Created Time: 2018/7/31 14:57
 * @Changed Time: 2018/7/31 14:57
 * @email: 284285624@qq.com
 * @Org: SZKT
 * @version: V1.0
 * @describe: //对话框View处理类
 */
public class AmosBoPopWindosViewHelper {
    /**
     * 对话框根布局
     */
    private View mRootView = null;
    /**
     * 控件集合
     */
    private SparseArray<WeakReference<View>> mViews;


    public AmosBoPopWindosViewHelper() {
        mViews = new SparseArray<>();
    }

    public AmosBoPopWindosViewHelper(@NonNull Context context, @LayoutRes int layoutResId) {
        this();
        mRootView = LayoutInflater.from(context).inflate(layoutResId, null);
    }

    /**
     * 设置布局View
     *
     * @param contentView
     */
    public void setContentView(@NonNull View contentView) {
        this.mRootView = contentView;
    }

    /**
     * 设置文本信息
     *
     * @param viewId
     * @param text
     */
    public void setText(@IdRes int viewId, @NonNull CharSequence text) {
        TextView tv = getViewById(viewId);
        if (tv != null) {
            tv.setText(text);
        }
    }

    /**
     * 设置监听
     *
     * @param viewId
     * @param listener
     */
    public void setOnClickListner(@IdRes int viewId, @NonNull View.OnClickListener listener) {
        View view = getViewById(viewId);
        if (view != null) {
            view.setOnClickListener(listener);
        }
    }

    /**
     * 获取布局View
     *
     * @return
     */
    public View getRootView() {
        return mRootView;
    }


    /**
     * 减少查找View次数
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getViewById(@IdRes int viewId) {
        View view = null;
        WeakReference<View> weakReference = mViews.get(viewId);
        if (weakReference != null) {
            view = weakReference.get();
        }
        if (view == null) {
            view = mRootView.findViewById(viewId);
            mViews.put(viewId, new WeakReference<View>(view));
        }
        return (T) view;
    }
}
