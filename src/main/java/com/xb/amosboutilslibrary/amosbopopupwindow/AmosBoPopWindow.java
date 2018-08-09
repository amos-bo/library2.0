package com.xb.amosboutilslibrary.amosbopopupwindow;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.xb.amosboutilslibrary.R;

/**
 * @author : Amos_bo
 * @package: com.xb.myapplication.amosbodialog
 * @Created Time: 2018/7/31 17:55
 * @Changed Time: 2018/7/31 17:55
 * @email: 284285624@qq.com
 * @Org: SZKT
 * @version: V1.0
 * @describe: //TODO 添加描述
 */
public class AmosBoPopWindow extends PopupWindow {

    private AmosBoPopWindowController mController;

    public AmosBoPopWindow(Context context) {
        super(context);
        mController = new AmosBoPopWindowController(this, ((Activity) context).getWindow());
    }

    /**
     * 设置文本
     *
     * @param viewId
     * @param text
     */

    public void setText(int viewId, CharSequence text) {
        mController.setText(viewId, text);
    }

    /**
     * 设置点击事件
     *
     * @param viewId
     * @param listener
     */
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        mController.setOnClickListener(viewId, listener);
    }

    public <T extends View> T getView(int viewId) {
        return mController.getView(viewId);
    }

    public static class Builder {

        private AmosBoPopWindowController.PopWindowParams mDialogParams;

        public Builder(Context context) {
            mDialogParams = new AmosBoPopWindowController.PopWindowParams(context);
        }

        public Builder setContentView(View view) {
            mDialogParams.mRootView = view;
            mDialogParams.mViewLayoutResId = 0;
            return this;
        }

        public Builder setContentView(int layoutId) {
            mDialogParams.mRootView = null;
            mDialogParams.mViewLayoutResId = layoutId;
            return this;
        }

        public Builder setText(int viewId, CharSequence text) {
            mDialogParams.mTextArray.put(viewId, text);
            return this;
        }

        public Builder setOnClickListener(int view, View.OnClickListener listener) {
            mDialogParams.mClickArray.put(view, listener);
            return this;
        }

        //配置一些万能的参数
        //全屏
        public Builder fullWidth() {
            mDialogParams.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        //从底部弹出
        public Builder fromBottom(boolean isAnimation) {
            if (isAnimation) {
                mDialogParams.mAnimation = R.style.dialog_from_bottom_anim;
            }
            mDialogParams.mGravity = Gravity.BOTTOM;
            return this;
        }

        //设置宽高
        public Builder fromWidthHeight(int width, int height) {
            mDialogParams.mWidth = width;
            mDialogParams.mHeight = height;
            return this;
        }

        //设置默认动画
        public Builder addDefaultAnimation() {
            mDialogParams.mAnimation = R.style.dialog;
            return this;
        }

        //设置自定义动画
        public Builder addAnimations(int styleAnimation) {
            mDialogParams.mAnimation = styleAnimation;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            mDialogParams.mCanceable = cancelable;
            return this;
        }

        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            mDialogParams.mOnDismissListener = onDismissListener;
            return this;
        }

        /**
         * 设置透明度
         *
         * @param level
         * @return
         */
        public Builder setBackGroundLevel(float level) {
            mDialogParams.mLevel = level;
            mDialogParams.mIsShowBg = true;
            return this;
        }

        /**
         * 设置外部是否可以点击
         *
         * @param outSideTouchable
         * @return
         */
        public Builder setOutsideTouchable(boolean outSideTouchable) {
            mDialogParams.mCanceable = outSideTouchable;
            return this;
        }

        private AmosBoPopWindow create() {
            final AmosBoPopWindow dialog = new AmosBoPopWindow(mDialogParams.mContext);
            mDialogParams.apply(dialog.mController);
            dialog.setOnDismissListener(mDialogParams.mOnDismissListener);
            return dialog;
        }

        /**
         * 默认显示在View 下方
         *
         * @param targetView
         * @return
         */
        public AmosBoPopWindow show(View targetView) {
            final AmosBoPopWindow dialog = create();
            dialog.showAsDropDown(targetView);
            return dialog;
        }

        /**
         * View  偏移坐标
         *
         * @param targetView
         * @param xoff
         * @param yoff
         * @return
         */
        public AmosBoPopWindow show(View targetView, int xoff, int yoff) {
            final AmosBoPopWindow dialog = create();
            dialog.showAsDropDown(targetView, xoff, yoff);
            return dialog;
        }

        /**
         * 显示在父控件位置
         *
         * @param parent
         * @param gravity
         * @param x
         * @param y
         * @return
         */
        public AmosBoPopWindow show(View parent, int gravity, int x, int y) {
            final AmosBoPopWindow dialog = create();
            dialog.showAtLocation(parent, gravity, x, y);
            return dialog;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mController.setBackGroundLevel(1.0f);
    }
}
