package com.xb.amosboutilslibrary.amsbobaseui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xb.amosboutilslibrary.amosboutils.LogUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 项目中所有Fragment的父类
 *
 * @author AmosBo
 * @date 15/6/4
 */
public abstract class BaseFragment extends Fragment {

    public Context mContext;

    /**
     * 加载在fragment中的根View
     */
    public View mRootView;

    public LayoutInflater mInflater;

    protected Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public abstract void initView(Bundle savedInstanceState);

    public abstract void initData(Bundle savedInstanceState);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        mContext = getActivity();
        if (mRootView == null) {
            this.mInflater = inflater;
            try {
                initView(savedInstanceState);
                initData(savedInstanceState);
            } catch (Throwable e) {
                e.printStackTrace();
                LogUtils.e(e.toString());
            }

            if (mRootView != null) {
                ViewGroup parent = (ViewGroup) mRootView.getParent();
                if (parent != null) {
                    parent.removeView(mRootView);
                }
            }
        }
        return mRootView;
    }

    /**
     * 设置fagment视图和activity用法一样必须在initView()方法中调用
     */

    public void setContentView(int layoutId) {
        mRootView = mInflater.inflate(layoutId, null);
        unbinder = ButterKnife.bind(this, mRootView);
    }

    /**
     * 设置fagment视图和activity用法一样必须在initView()方法中调用
     */
    public void setContentView(View view) {
        mRootView = view;
    }

    /**
     * 找到子视图
     */
    public View findViewById(int id) {
        return mRootView.findViewById(id);
    }

    /**
     * 得到根视图
     */
    public View getRootView() {
        return mRootView;
    }


    @Override
    public void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
        super.onDestroy();
        mContext = null;
        mInflater = null;
        mRootView = null;
    }
    protected void startActivity(Class<? extends Activity> cls) {
        startActivity(cls, null);
    }

    protected void startActivity(Class<? extends Activity> cls, Bundle bundle) {
        Intent intent = new Intent(mContext, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    protected void startActivityForResult(Class<? extends Activity> cls, Bundle bundle, int
            requestCode) {
        Intent localIntent = new Intent();
        if (bundle != null) {
            localIntent.putExtras(bundle);
        }
        localIntent.setClass(mContext, cls);
        startActivityForResult(localIntent, requestCode);
    }

    protected void startActivityForResult(Class<? extends Activity> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }
}
