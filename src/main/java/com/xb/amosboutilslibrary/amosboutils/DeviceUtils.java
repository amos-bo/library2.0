/**
 * @项目名称：DateScrollPicker
 * @文件名：DeviceUitls.java
 * @版本信息：
 * @日期：2015年7月30日
 * @Copyright 2015 www.517na.com Inc. All rights reserved.
 */
package com.xb.amosboutilslibrary.amosboutils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * @author xiangbo
 * @version 1.0
 * @作者 E-mail:284285624@qq.com
 * @date 创建时间：2015年8月19日 下午3:21:53
 * @描述：设备处理
 */
public class DeviceUtils {
    private static PhoneInfo mPhoneInfo;

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);

    }

    /**
     * px转换成dp
     */
    private int px2dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转换成px
     */
    private int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转换成sp
     */
    private int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     *
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration
                .SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 获取当前设备SDK版本号
     *
     * @return
     */
    public static int isThisSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取屏幕宽高
     *
     * @param context
     * @return int[0] width,int[1] height
     */
    public static int[] getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return new int[]{outMetrics.widthPixels, outMetrics.heightPixels};
    }

    /**
     * 获取手机信息
     *
     * @return PhoneInfo(手机信息)
     */
    synchronized public static PhoneInfo getPhoneInfo(Application application) {
        if (mPhoneInfo == null) {
            mPhoneInfo = new PhoneInfo();
            long appVersionCode = getVersionCode(application);
            String appVersion = getVersion(application);
            String brand = getBrand();
            String simOperatorName = getSimOperatorName(application);
            String model = getModel();
            String SDK = Build.VERSION.SDK_INT + "";
            String release = getRelease();
            String manufacturer = getManufacturer();
            String platform = "Android";
            String IMEI = getIME(application);
            String IP = NetUtils.getIpAddress();
            simOperatorName = StringUtils.dealWithStringSpace(simOperatorName);
            LogUtils.i(PhoneInfo.class.getName(), "Version:" + appVersion + " VersionCode:" +
                    appVersionCode
                    + " Brand:" + brand + "" +
                    " SimOperatorName:" + simOperatorName
                    + " Model:" + model + " Release" + release + " Manufacturer" + manufacturer +
                    " " +
                    "SDK:" + SDK + " Platform:" + platform + " IMEI:" + IMEI + " IP:" + IP);
            mPhoneInfo.setAppVersionCode(appVersionCode);
            mPhoneInfo.setAppVersion(appVersion);
            mPhoneInfo.setBrand(brand);
            mPhoneInfo.setSimOperatorName(simOperatorName);
            mPhoneInfo.setModel(model);
            mPhoneInfo.setSDK(SDK);
            mPhoneInfo.setRelease(release);
            mPhoneInfo.setManufacturer(manufacturer);
            mPhoneInfo.setPlatform(platform);
            mPhoneInfo.setImei(IMEI);
            mPhoneInfo.setIp(IP);
        }
        return mPhoneInfo;
    }

    /**
     * Retrieves application's version number from the manifest
     *
     * @return long
     */
    private static long getVersionCode(Application app) {
        long version = 0;
        PackageManager packageManager = app.getApplicationContext().getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    app.getApplicationContext().getPackageName(), 0);
            version = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * Retrieves application's version number from the manifest
     *
     * @return String
     */
    private static String getVersion(Application app) {
        String version = "0.0.0";
        try {
            PackageManager packageManager = app.getApplicationContext().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    app.getApplicationContext().getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 获得手机品牌
     *
     * @return String
     */
    private static String getBrand() {
        return Build.BRAND;
    }

    /**
     * 获取手机运营商
     */
    private static String getSimOperatorName(Application app) {
        TelephonyManager tm = (TelephonyManager) app.getApplicationContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimOperatorName();
    }

    /**
     * 获得手机型号
     *
     * @return String
     */
    private static String getModel() {
        return Build.MODEL;
    }

    /**
     * 获得固件版本
     *
     * @return String
     */
    private static String getRelease() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 生产商家
     *
     * @return String
     */
    private static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * @param context
     * @return 是否存在虚拟按键
     */
    public static boolean hasNavBar(Context context) {
        try {
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
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断虚拟按键栏是否重写
     *
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
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        try {
            if (hasNavBar(context)) {
                Resources res = context.getResources();
                int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    result = res.getDimensionPixelSize(resourceId);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 状态栏高度
     *
     * @return int
     */
    public int getStatusBarHeight(@NonNull Application application) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = application.getApplicationContext().getResources().getDimensionPixelSize
                    (x);
            return sbar;
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    /**
     * 得到本机手机号,未安装SIM卡或者SIM卡中未写入手机号，都会获取不到
     *
     * @return String
     */
    @SuppressLint("HardwareIds")
    public static String getPhoneNumber(Application app) {
        TelephonyManager tm = (TelephonyManager) app.getApplicationContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(app, Manifest.permission.READ_SMS) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(app,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        return tm.getLine1Number();
    }

    /**
     * 获取唯一标识
     *
     * @return String
     */
    @SuppressLint("HardwareIds")
    private static String getIME(Application app) {
        TelephonyManager tm = (TelephonyManager) app.getApplicationContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = "";
        try {
            if (ActivityCompat.checkSelfPermission(app, Manifest.permission.READ_PHONE_STATE) !=
                    PackageManager.PERMISSION_GRANTED) {
                return "";
            }
            imei = tm.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(imei)) {
            try {
                imei = Settings.Secure.getString(app.getApplicationContext()
                        .getContentResolver(), Settings.Secure.ANDROID_ID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return imei;
    }

    /**
     * 打电话
     *
     * @param phone String
     */
    public static void callPhone(@NonNull String phone, @NonNull Application application) {
        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                + phone));
        application.getApplicationContext().startActivity(phoneIntent);
    }

    /**
     * 发短信
     *
     * @param phone   String
     * @param content String
     */
    public static void sendSMS(@NonNull Application app, @NonNull String phone, String content) {
        Uri uri = null;
        if (!TextUtils.isEmpty(phone)) {
            uri = Uri.parse("smsto:" + phone);
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            intent.putExtra("sms_body", content);
            app.getApplicationContext().startActivity(intent);
        }
    }

    /**
     * 屏幕分辨率
     *
     * @return float
     */
    public static float getDip(@NonNull Application app) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
                app.getApplicationContext().getResources().getDisplayMetrics());
    }

    /**
     * 开始震动
     *
     * @param context Context
     * @param repeat  0重复 -1不重复
     * @param pattern long
     */
    @SuppressLint("NewApi")
    public synchronized void doVibrate(Context context, int repeat,
                                       long... pattern) {
        if (pattern == null) {
            pattern = new long[]{1000, 1000, 1000};
        }
        Vibrator mVibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        if (mVibrator.hasVibrator()) {
            mVibrator.vibrate(pattern, repeat);
        }
    }


    public static class PhoneInfo implements Serializable {

        /**
         * app版本VersionCode
         */
        private long appVersionCode;
        /**
         * app版本号码
         */
        private String appVersion;
        /**
         * 手机品牌
         */
        private String brand;
        /**
         * 手机运营商
         */
        private String simOperatorName;
        /**
         * 手机型号
         */
        private String model;
        /**
         * 手机当前SDK版本
         */
        private String SDK;
        /**
         * 手机获得固件版本
         */
        private String release;
        /**
         * 手机生产商家
         */
        private String manufacturer;
        /**
         * 手机操作平台
         */
        private String platform;
        /**
         * 手机IMEI
         */
        private String imei;
        /**
         * 手机IP
         */
        private String ip;

        public PhoneInfo() {
        }

        public PhoneInfo(long appVersionCode, String appVersion, String brand, String
                simOperatorName, String model, String SDK, String release,
                         String manufacturer,
                         String platform, String imei, String ip) {
            this.appVersionCode = appVersionCode;
            this.appVersion = appVersion;
            this.brand = brand;
            this.simOperatorName = simOperatorName;
            this.model = model;
            this.SDK = SDK;
            this.release = release;
            this.manufacturer = manufacturer;
            this.platform = platform;
            this.imei = imei;
            this.ip = ip;
        }

        public long getAppVersionCode() {
            return appVersionCode;
        }

        public void setAppVersionCode(long appVersionCode) {
            this.appVersionCode = appVersionCode;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getSimOperatorName() {
            return simOperatorName;
        }

        public void setSimOperatorName(String simOperatorName) {
            this.simOperatorName = simOperatorName;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getSDK() {
            return SDK;
        }

        public void setSDK(String SDK) {
            this.SDK = SDK;
        }

        public String getRelease() {
            return release;
        }

        public void setRelease(String release) {
            this.release = release;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public void setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }
    }

}
