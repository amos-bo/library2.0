package com.xb.amosboutilslibrary.amosboadapter;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.lang.ref.WeakReference;

/**
 * @author : Amos_bo
 * @package: com.xb.myapplication.amosboadapter
 * @Created Time: 2018/8/6 14:23
 * @Changed Time: 2018/8/6 14:23
 * @email: 284285624@qq.com
 * @Org: SZKT
 * @version: V1.0
 * @describe: 缓存View类
 */
public class AmosBoHolderViewHelper implements View.OnClickListener, View.OnLongClickListener,
        CompoundButton.OnCheckedChangeListener {

    /**
     * 根布局
     */
    private View mRootView;

    /**
     * 控件集合
     */
    private SparseArray<WeakReference<View>> mViews;
    /**
     * 点击位置
     */
    public int mPosition;

    private AmosBoItemListener mAmosBoItemListener;


    public static AmosBoHolderViewHelper holderHelperFromRecyclerView(View convertView, ViewGroup
            parent, int layoutId) {
        if (convertView == null) {
            return new AmosBoHolderViewHelper(parent, layoutId);
        }
        return (AmosBoHolderViewHelper) convertView.getTag();
    }

    public static AmosBoHolderViewHelper holderHelperFromRecyclerView(View convertView) {
        return new AmosBoHolderViewHelper(convertView);
    }


    /**
     * @param viewGroup
     * @param layoutResId
     */
    public AmosBoHolderViewHelper(@NonNull ViewGroup viewGroup, @LayoutRes int layoutResId) {
        mRootView = LayoutInflater.from(viewGroup.getContext()).inflate(layoutResId, null);
        mViews = new SparseArray<>();
        this.mRootView.setTag(this);
    }

    /**
     * @param view
     */
    public AmosBoHolderViewHelper(@NonNull View view) {
        mRootView = view;
        mViews = new SparseArray<>();
        this.mRootView.setTag(this);
    }

    /**
     * 设置字控件监听
     *
     * @param listener
     */
    public void setAmosBoItemListener(AmosBoItemListener listener) {
        this.mAmosBoItemListener = listener;
    }

    /**
     * 设置点击
     *
     * @param viewId
     */
    public void setChildOnClick(@IdRes int viewId) {
        getViewById(viewId).setOnClickListener(this);
    }

    /**
     * 设置长按事件
     *
     * @param viewId
     */
    public void setChildOnLongClick(@IdRes int viewId) {
        getViewById(viewId).setOnLongClickListener(this);
    }

    /**
     * 复选框点击切换状态
     *
     * @param viewId
     */
    public void setChildonCheckedChanged(@IdRes int viewId) {
        if (getViewById(viewId) instanceof CompoundButton) {
            ((CompoundButton) getViewById(viewId)).setOnCheckedChangeListener(this);
        }
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

    /**
     * 设置点击位置
     *
     * @param position
     */
    public void setPosition(int position) {
        this.mPosition = position;
    }

    /**
     * 获取布局View
     *
     * @return
     */
    public View getRootView() {
        return mRootView;
    }

    @Override
    public void onClick(View view) {
        if (mAmosBoItemListener != null) {
            mAmosBoItemListener.onItemChildClick(view, mPosition);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (mAmosBoItemListener != null) {
            mAmosBoItemListener.onItemChildLongClick(view, mPosition);
        }
        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (mAmosBoItemListener != null) {
            mAmosBoItemListener.onItemChildCheckedChanged(compoundButton, mPosition, b);
        }
    }
}
