package com.xb.amosboutilslibrary.amosboutils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.Settings.System;
import android.view.WindowManager;

/**
 * 亮度控制器
 */
public class LightnessController {

    /**
     * 判断是否开启了自动亮度调节
     *
     * @param act
     * @return
     */
    public static boolean isAutoBrightness(Activity act) {
        boolean automicBrightness = false;
        ContentResolver aContentResolver = act.getContentResolver();
        try {
            automicBrightness = System.getInt(aContentResolver,
                    System.SCREEN_BRIGHTNESS_MODE) == System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return automicBrightness;
    }

    /**
     * 改变亮度
     *
     * @param act
     * @param value
     */
    public static void setLightness(Activity act, int value) {
        setLightness(act, (value <= 0 ? 1 : value) / 255f);
    }

    // 改变亮度
    public static void setLightness(Activity act, float value) {
        try {
            WindowManager.LayoutParams lp = act.getWindow().getAttributes();
            lp.screenBrightness = value;
            act.getWindow().setAttributes(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取亮度
     *
     * @param context
     * @return
     */
    public static int getLightness(Activity context) {
        return System.getInt(context.getContentResolver(), System.SCREEN_BRIGHTNESS, -1);
    }


    /**
     * 停止自动亮度调节
     */
    public static void stopAutoBrightness(Activity activity) {
        System.putInt(activity.getContentResolver(),
                System.SCREEN_BRIGHTNESS_MODE,
                System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }

    /**
     * 开启亮度自动调节
     *
     * @param activity
     */
    public static void startAutoBrightness(Activity activity) {
        System.putInt(activity.getContentResolver(),
                System.SCREEN_BRIGHTNESS_MODE,
                System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
    }

    /**
     * 保存亮度设置状态
     *
     * @param context
     * @param brightness
     */
    public static void saveBrightness(Context context, int brightness) {
        Uri uri = System
                .getUriFor(System.SCREEN_BRIGHTNESS);
        System.putInt(context.getContentResolver(),
                System.SCREEN_BRIGHTNESS, brightness);

        context.getContentResolver().notifyChange(uri, null);
    }
}
