package com.jimmy.development.tools;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;


public class DisplayUtils {
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        return p.y;
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        return p.x;
    }

    public static int getContentViewHeight(Context context) {
        int screenHeight = getScreenHeight(context);
        return screenHeight;
    }

    public static int dipToPixel(Context context, float dip) {
        return (int) (context.getResources().getDisplayMetrics().density * dip + 0.5);
    }

    public static int pxToDip(Context context, int px) {
        return (int) (px / context.getResources().getDisplayMetrics().density + 0.5);
    }

    public static int pxToSp(Context context, int pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int spToPx(Context context, int spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int getDensity(Context context) {
        return (int) (context.getResources().getDisplayMetrics().scaledDensity);
    }
}
