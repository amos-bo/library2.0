package com.xb.amosboutilslibrary.amosbodialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

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
public class AmosBoDialogController {

    private AmosBoDialog mDialog;

    private Window mWindow;

    private AmosBoDialogViewHelper mViewHelper;

    public AmosBoDialogController(AmosBoDialog dialog, Window window) {
        this.mDialog = dialog;
        this.mWindow = window;
    }

    public void setViewHelper(AmosBoDialogViewHelper viewHelper) {
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
    public AmosBoDialog getDialog() {
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

    public static class DialogParams {
        public Context mContext;
        public int mThemeResId;
        /**
         * 点击空白是否可以取消对话框
         **/
        public boolean mCanceable = true;
        /**
         * dialog Cancel监听
         */
        public DialogInterface.OnCancelListener mOnCancelListener;
        /**
         * dialog  Dismiss监听
         */
        public DialogInterface.OnDismissListener mOnDismissListener;
        /**
         * dialog  Key监听
         */
        public DialogInterface.OnKeyListener mOnKeyListener;
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

        public DialogParams(Context context, int themeResId) {
            this.mContext = context;
            this.mThemeResId = themeResId;
        }

        /**
         * 绑定和设置参数
         *
         * @param mController
         */
        public void apply(AmosBoDialogController mController) {
            //设置布局
            AmosBoDialogViewHelper viewHelper = null;
            if (mViewLayoutResId != 0) {
                viewHelper = new AmosBoDialogViewHelper(mContext, mViewLayoutResId);
            }
            if (mRootView != null) {
                viewHelper = new AmosBoDialogViewHelper();
            }
            if (viewHelper == null) {
                throw new IllegalArgumentException("请设置布局setContentView()");
            }
            //1.给dialog设置布局
            mController.getDialog().setContentView(viewHelper.getRootView());
            //设置Controller的辅助类
            mController.setViewHelper(viewHelper);

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
            //4，配置自定义效果，全屏，从底部弹出  默认动画
            Window window = mController.getWindow();
            //设置位置
            window.setGravity(mGravity);
            //设置动画
            if (mAnimation != 0) {
                window.setWindowAnimations(mAnimation);
            }
            //设置宽高
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = mWidth;
            params.height = mHeight;
            window.setAttributes(params);
        }
    }
}
