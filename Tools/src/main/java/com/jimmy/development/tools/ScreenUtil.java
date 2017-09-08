package com.jimmy.development.tools;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.WindowManager;

/**
 * 屏幕相关参数获取和操作的工具类
 */
public class ScreenUtil {

    public static boolean isScreenLandscape(Context context) {
        Configuration mConfiguration = context.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向

        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
            return true;
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {//竖屏
            return false;
        }
        return false;
    }

    /**
     * 通过该方法，在acticity create时，锁定当前的屏幕方向
     */
    public static final void lockOrientation(Activity activity) {
        // make sure activity won't change orientation
        int rotation = getRotation(activity);
        int targetOrientation = rotation2orientation(rotation);
        activity.setRequestedOrientation(targetOrientation);
    }

    public static final void lockOrientation(Activity activity, int forceRotation) {
        // make sure activity won't change orientation
        int targetOrientation = rotation2orientation(forceRotation);
        activity.setRequestedOrientation(targetOrientation);
    }

    private static final int rotation2orientation(int rotation) {
        switch (rotation) {
            case Surface.ROTATION_90:
                return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            case Surface.ROTATION_270:
                return ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
            case Surface.ROTATION_180:
                return ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
            default:
                return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }
    }

    /**
     * 获取当前屏幕旋转方向度数
     *
     * @param activity
     * @return
     */
    public static final int getRotation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        return rotation;
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }
}
