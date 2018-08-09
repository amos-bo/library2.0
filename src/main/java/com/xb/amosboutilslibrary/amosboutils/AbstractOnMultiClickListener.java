package com.xb.amosboutilslibrary.amosboutils;

import android.view.View;

/**
 * @author : Amos_bo
 * @package: com.ktcd.malc.utilslibrary.tools
 * @Created Time: 2018/5/10 11:49
 * @Changed Time: 2018/5/10 11:49
 * @email: 284285624@qq.com
 * @Org: SZKT
 * @version: V1.0
 * @describe: 防止短时间重复点击
 */

abstract class AbstractOnMultiClickListener implements View.OnClickListener {
    /**
     * 两次点击按钮之间的点击间隔不能少于1000毫秒
     */
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    /**
     * 点击按钮实现
     *
     * @param v
     */
    public abstract void onMultiClick(View v);

    @Override
    public void onClick(View v) {
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            // 超过点击间隔后再将lastClickTime重置为当前点击时间
            lastClickTime = curClickTime;
            onMultiClick(v);
        }
    }

    /**
     * 防止多次点击
     *
     * @param view View
     */
    public static void noMultiClick(final View view) {

        if (view == null) {
            return;
        }
        view.setEnabled(false);

        view.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (view.getContext() == null) {
                    return;
                }
                view.setEnabled(true);


            }
        }, 500);


    }
}
