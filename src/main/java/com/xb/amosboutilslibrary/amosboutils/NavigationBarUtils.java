package com.xb.amosboutilslibrary.amosboutils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.ViewConfiguration;

import java.lang.reflect.Method;

/**
 * 虚拟按键工具类
 * Created by jianyang on 2016/12/5.
 */

public class NavigationBarUtils {



    public static boolean hasNavBar(Context context) {
        try{
            if (Build.VERSION.SDK_INT >= 14) {
                Resources res = context.getResources();
                int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
                if (resourceId != 0) {
                    boolean hasNav = res.getBoolean(resourceId);
                    // check override flag
                    String sNavBarOverride = getNavBarOverride();
                    if ("1".equals(sNavBarOverride)) {
                        hasNav = false;
                    } else if ("0".equals(sNavBarOverride)) {
                        hasNav = true;
                    }
                    return hasNav;
                } else { // fallback
                    return !ViewConfiguration.get(context).hasPermanentMenuKey();
                }

            }
        }catch (Throwable e){
            e.printStackTrace();
        }

        return false;

    }



    /**
     * 判断虚拟按键栏是否重写
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return sNavBarOverride;
    }



    /**
     * 获取虚拟按键栏高度
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        try{
            if (hasNavBar(context)){
                Resources res = context.getResources();
                int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    result = res.getDimensionPixelSize(resourceId);
                }
            }
        }catch (Throwable e){
            e.printStackTrace();
        }

        return result;
    }

}
