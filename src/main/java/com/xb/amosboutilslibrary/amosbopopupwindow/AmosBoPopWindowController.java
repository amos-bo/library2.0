package com.xb.amosboutilslibrary.amosbopopupwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * @author : Amos_bo
 * @package: com.xb.myapplication.amosbodialog
 * @Created Time: 2018/7/31 17:48
 * @Changed Time: 2018/7/31 17:48
 * @email: 284285624@qq.com
 * @Org: SZKT
 * @version: V1.0
 * @describe: //对话框控制类
 */
public class AmosBoPopWindowController {

    private AmosBoPopWindow mDialog;
    private Window mWindow;

    private AmosBoPopWindosViewHelper mViewHelper;

    public AmosBoPopWindowController(AmosBoPopWindow dialog, Window window) {
        this.mDialog = dialog;
        this.mWindow = window;
    }

    public void setViewHelper(AmosBoPopWindosViewHelper viewHelper) {
        this.mViewHelper = viewHelper;
    }

    /**
     * 设置文本
     *
     * @param viewId
     * @param text
     */
    public void setText(int viewId, CharSequence text) {
        mViewHelper.setText(viewId, text);
    }

    /**
     * 设置点击事件
     *
     * @param viewId
     * @param listener
     */
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        mViewHelper.setOnClickListner(viewId, listener);
    }

    /**
     * 获取View
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        return mViewHelper.getViewById(viewId);
    }

    /**
     * 获取dialog
     *
     * @return
     */
    public AmosBoPopWindow getDialog() {
        return mDialog;
    }

    /**
     * 获取dialog的window
     *
     * @return
     */
    public Window getWindow() {
        return mWindow;
    }

    public void setBackGroundLevel(float level) {
        WindowManager.LayoutParams params = mWindow.getAttributes();
        params.alpha = level;
        mWindow.setAttributes(params);
    }


    public static class PopWindowParams {
        public Context mContext;
        /**
         * 点击空白是否可以取消对话框
         **/
        public boolean mCanceable = true;
        /**
         * dialog  Dismiss监听
         */
        public PopupWindow.OnDismissListener mOnDismissListener;
        /**
         * 布局的View
         */
        public View mRootView;
        /**
         * 布局的Layout
         */
        public int mViewLayoutResId;

        /**
         * 存放字体的修改
         */
        public SparseArray<CharSequence> mTextArray = new SparseArray<>();
        /**
         * 存放点击事件
         */
        public SparseArray<View.OnClickListener> mClickArray = new SparseArray<>();
        /**
         * 宽度
         */
        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        /**
         * 高度
         */
        public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        /**
         * 动画
         */
        public int mAnimation = 0;
        /**
         * 位置
         */
        public int mGravity = Gravity.CENTER;
        /**
         * 设置背景透明度
         */
        public float mLevel = 0.5f;
        /**
         * 是否显示背景
         */
        public boolean mIsShowBg;

        public PopWindowParams(Context context) {
            this.mContext = context;
        }

        /**
         * 绑定和设置参数
         *
         * @param mController
         */
        public void apply(AmosBoPopWindowController mController) {
            //设置布局
            AmosBoPopWindosViewHelper viewHelper = null;
            if (mViewLayoutResId != 0) {
                viewHelper = new AmosBoPopWindosViewHelper(mContext, mViewLayoutResId);
            }
            if (mRootView != null) {
                viewHelper = new AmosBoPopWindosViewHelper();
            }
            if (viewHelper == null) {
                throw new IllegalArgumentException("请设置布局setContentView()");
            }
            //1.给dialog设置布局
            mController.getDialog().setContentView(viewHelper.getRootView());
            //设置Controller的辅助类
            mController.setViewHelper(viewHelper);


            mController.getDialog().setBackgroundDrawable(new ColorDrawable(0x00000000));
            mController.getDialog().setOutsideTouchable(true);
            //设置是否允许PopupWindow的范围超过屏幕范围
            mController.getDialog().setClippingEnabled(true);

            //2，设置文本
            int textArraySize = mTextArray.size();
            for (int i = 0; i < textArraySize; i++) {
                mController.setText(mTextArray.keyAt(i), mTextArray.valueAt(i));
            }
            //3，设置点击
            int clickArraySize = mClickArray.size();
            for (int i = 0; i < clickArraySize; i++) {
                mController.setOnClickListener(mClickArray.keyAt(i), mClickArray.valueAt(i));
            }
            //设置PopupWindow动画
            if (mAnimation != 0) {
                mController.getDialog().setAnimationStyle(mAnimation);
            }
            //设置宽高
            mController.getDialog().setHeight(mHeight);
            mController.getDialog().setWidth(mWidth);
            //设置透明度
            if (mIsShowBg) {
                WindowManager.LayoutParams params = mController.getWindow().getAttributes();
                params.alpha = mLevel;
                mController.getWindow().setAttributes(params);
            }
        }
    }
}
