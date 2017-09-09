package com.jimmy.development.tools;

import android.content.Context;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.io.File;
import java.util.Locale;


public class DeviceUtil {
    public static int sScreenWidth = -1;

    /*public static String getPropIMEI(Context context) {
        return SystemProperties.get(context, "gsm.sim.imei");
    }*/
    public static int sScreenHeight = -1;
    private static int sActionBarHeight = -1;
    private static int sStatusBarHeight = -1;

    /**
     * 获取设备的IMEI
     *
     * @param context
     * @return
     */
    public static String getPhoneIMEI(Context context) {
        String imei = PhoneUtils.getDefaultImei(context);
        return imei == null ? "" : imei;
    }




    public static String getSystemDisplayVersion() {
        return Build.DISPLAY;
    }

    /**
     * 获取“imei_sn”的组合信息
     *
     * @param imei
     * @param sn
     * @return
     */
    public static String generateDevice(String imei, String sn) {
        String device = imei;

        // 如果imei为空，给默认值
        if (TextUtils.isEmpty(device)) {
            device = "000000000000000";
        }
        if (!TextUtils.isEmpty(sn)) {
            device += "_" + sn;
        }
        return device;
    }

    public static void initScreen(Context context) {
        WindowManager windowManager =
                (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        sScreenWidth = windowManager.getDefaultDisplay().getWidth();
        sScreenHeight = windowManager.getDefaultDisplay().getHeight();
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth() {
        return sScreenWidth;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getScreenHeight() {
        return sScreenHeight;
    }

    public static String getScreen() {
        return "" + sScreenWidth + "x" + sScreenHeight;
    }

    public static String getLanguageLocal() {
        String res = "";
        if (!TextUtils.isEmpty(Locale.getDefault().getLanguage())) {
            res += Locale.getDefault().getLanguage().toLowerCase() + "-";
        }

        if (!TextUtils.isEmpty(Locale.getDefault().getCountry().toUpperCase())) {
            res += Locale.getDefault().getCountry().toUpperCase();
        }
        return res;
    }

    public static String getModel() {
        return PhoneUtils.getDeviceModel();
    }

    public static int getActionBarHeight(Context context) {
        // 推荐基于SDK开发者使用此方法获得ActionBar高度，但要避免频繁调用
        if (sActionBarHeight < 0) {
            TypedValue tv = new TypedValue();
            if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                sActionBarHeight = TypedValue.complexToDimensionPixelSize(
                        tv.data, context.getResources().getDisplayMetrics());
            } else {
                sActionBarHeight = 120;
            }
        }
        return sActionBarHeight;
    }

    public static int getStatusBarHeight(Context context) {
        if (sStatusBarHeight < 0) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId == 0) {// 找不到该资源的id
                sStatusBarHeight = 62;
            } else {
                sStatusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
            }
        }

        return sStatusBarHeight;
    }

    public static int getActionBarStatusBarHeight(Context context) {
        return getActionBarHeight(context) + getStatusBarHeight(context);
    }

    public static void setPaddingABarSBarSmartBar(Context context, ViewGroup viewGroup) {
        viewGroup.setClipChildren(false);
        viewGroup.setClipToPadding(false);
        viewGroup.setPadding(viewGroup.getPaddingLeft(), viewGroup.getPaddingTop() + DeviceUtil.getActionBarStatusBarHeight(context),
                viewGroup.getPaddingRight(), viewGroup.getPaddingBottom() + DeviceUtil.getActionBarHeight(context));
    }

    public static void setPaddingABarSBar(Context context, ViewGroup viewGroup) {
        viewGroup.setClipChildren(false);
        viewGroup.setClipToPadding(false);
        viewGroup.setPadding(viewGroup.getPaddingLeft(), viewGroup.getPaddingTop() + DeviceUtil.getActionBarStatusBarHeight(context),
                viewGroup.getPaddingRight(), viewGroup.getPaddingBottom());
    }

    public static void setPaddingSmartBar(Context context, ViewGroup viewGroup) {
        viewGroup.setClipChildren(false);
        viewGroup.setClipToPadding(false);
        viewGroup.setPadding(viewGroup.getPaddingLeft(), viewGroup.getPaddingTop(),
                viewGroup.getPaddingRight(), viewGroup.getPaddingBottom() + DeviceUtil.getActionBarHeight(context));
    }

    //获取mac地址
    public static String getDeviceMac(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        WifiInfo info = wifi.getConnectionInfo();

        return info.getMacAddress();
    }

    //
    public static String getDeviceMzosfull(Context context) {
        return Build.VERSION.RELEASE;
    }

    public static String getDeviceStorage(Context context) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(context, blockSize * totalBlocks);
    }

    public static String getDeviceOperator(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimOperatorName();
    }

    public static String getDeviceRoot() {
        int bool = 0;

        try {
            if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())) {
                bool = 0;
            } else {
                bool = 1;
            }
        } catch (Exception e) {
            Log.w("getDeviceRoot", e);
        }
        return String.valueOf(bool);
    }


    /**
     * 判断机器是否为国际版本
     *
     * @return 机器是否为国际版
     * @author dengxin@meizu.com
     * @since 2014-04-29
     */
    public static boolean isProductInternational() {
        try {
            Boolean b = (Boolean) ReflectHelper.invokeStatic("android.os.BuildExt", "isProductInternational", null);
            return b;
        } catch (Exception e) {
            Log.w("isProductInternational", e);
        }
        return false;
    }

    public static int getNavigationBarHeight(Context context) {
        if (hasNavigationBar(context)) {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                return resources.getDimensionPixelSize(resourceId);
            }
        }
        return 0;
    }

    public static boolean hasNavigationBar(Context context) {
        boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);

        if (!hasMenuKey && !hasBackKey) {
            // Do whatever you need to do, this device has a navigation bar
            return true;
        }
        return false;
    }
}
