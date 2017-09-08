package com.jimmy.development.tools;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.meizu.oauth.NetworkProxy;

//import com.meizu.oauth.NetworkProxy;

public class Utility {
    public static final String TAG = Utility.class.getSimpleName();
    public static final String PUNCTUATION = "~!@#$%^&*()_+`-={}|:\"<>?[]\\;',./ ";
    private final static char[] hexDigits = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static int sActionBarHeight = -1;
    public static int mStatusBarHeight = -1;

    //	public static boolean isNetworkAvailable(Context context){
//		ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
//		if( connectivity == null ){
//			return false;
//		}else{
//			NetworkInfo info = connectivity.getActiveNetworkInfo();
//			if(info != null && info.isConnected()){
//				NetworkProxy.updateProxyStatus(info.getExtraInfo());
//				return true;
//			}
//		}
//        return false;
//    }
    public static boolean isWifiNetwork(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null) {
                return info.getType() == ConnectivityManager.TYPE_WIFI;
            }
        }
        return false;
    }

    // 对金额进行格式化
    public static String getFormatAmountString(double amount) {
        int nTemp = (int) amount;
        if (amount - nTemp == 0) {
            return String.valueOf(nTemp);
        } else {
            nTemp = (int) (amount * 10);
            if (amount * 10 - nTemp == 0) {
                return String.format("%.1f", amount);
            } else {
                return String.format("%.2f", amount);
            }
        }
    }

    public static String getRMBAmountString(double amount) {
        return "￥ " + getFormatAmountString(amount);
    }

    /**
     * 本地是否安装了该packageName的软件
     *
     * @param context
     * @param packageName
     * @return
     */
    public static final boolean isLocalInstall(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(packageName, 0);
            if (info != null) {
                return true;
            }
        } catch (Exception e) {
            // not found
            Log.w(TAG, e.getMessage() + " not Found");
        }
        return false;
    }

    /**
     * 本地是否安装了该packageName的软件，并且版本号需要相同
     *
     * @param context
     * @param packageName
     * @param verCode
     * @return
     */
    public static final boolean isLocalInstall(Context context, String packageName, int verCode) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageInfo(packageName, 0);
            if (info != null && info.versionCode == verCode) {
                return true;
            }
        } catch (Exception e) {
            // not found
        }
        return false;
    }

    public static final int getAppVersionCode(String packageName, Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageInfo(packageName, 0);
            return info.versionCode;
        } catch (Exception e) {
            Log.w(TAG, e.getMessage() + " not Found");
        }
        return 0;
    }

    /**
     * 获取当前软件的版本,失败返回""
     */
    public static final String getAppVersionString(String packageName, Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageInfo(packageName, 0);
            return info.versionName;
        } catch (Exception e) {
            Log.w(TAG, e);
        }
        return "";
    }

    public static String getSimOperatorName(Context context) {
        TelephonyManager mTm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String operatorName = mTm.getSimOperatorName();
        if (TextUtils.isEmpty(operatorName)) {
            // Use NetworkOperatorName as second choice in case there is no
            // SPN (Service Provider Name on the SIM). Such as with T-mobile.
            operatorName = mTm.getNetworkOperatorName();
        }

        return operatorName;
    }

    public static String getDevice() {
        return Build.DEVICE;
    }

    public static WifiManager getWifiManager(Context context) {
        return (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public static String getMacAddress(Context context) {
        WifiInfo wifiInfo = getWifiManager(context).getConnectionInfo();
        String WifiMac = null;
        if ((WifiMac = wifiInfo.getMacAddress()) == null) {
            WifiMac = "";
        }
        return WifiMac;
    }

    public static final String getAppName(String packageName, Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageInfo(packageName, 0);
            return info.applicationInfo.loadLabel(pm).toString();
        } catch (Exception e) {
            Log.w(TAG, e);
        }
        return "";
    }

    public static String encodeHex(final byte[] data) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = hexDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = hexDigits[0x0F & data[i]];
        }
        return new String(out);
    }

    public static String md5sum(String original) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(original.getBytes("utf-8"));
            String str = encodeHex(md5.digest());
            return str;
        } catch (Exception e) {
            Log.w(TAG, e);
        }
        return null;
    }

    public static final double getRealVaule(double value, int resLen) {
        if (resLen == 0) {
            return Math.round(value * 10 + 5) / 10;
        }
        double db = Math.pow(10, resLen);
        return Math.round(value * db) / db;
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
        // 推荐基于SDK开发者使用此方法获得StatusBar高度，但要避免频繁调用
        if (mStatusBarHeight < 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("status_bar_height");
                int height = Integer.parseInt(field.get(obj).toString());
                mStatusBarHeight = context.getResources().getDimensionPixelSize(height);
            } catch (Exception e) {
                Log.w(TAG, e);
                mStatusBarHeight = 62;
            }
        }

        return mStatusBarHeight;
    }

    public static boolean hasSDCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }


    public static void hideInputMethod(Context context, View v) {
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }


    /**
     * 生成签名
     *
     * @param params
     * @param appkey
     * @return
     */
    public static String generateSign(HashMap<String, String> params, String appkey) {

        List<Map.Entry<String, String>> mHashMapEntryList = new ArrayList<>(params.entrySet());
        //排序
        Collections.sort(mHashMapEntryList, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return (o1.getKey()).toString().compareTo(o2.getKey());
            }
        });
        StringBuffer buffer = new StringBuffer();

        for (Map.Entry<String, String> entry : mHashMapEntryList) {
            buffer.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        String sign = buffer.toString();
        if (!TextUtils.isEmpty(sign)) {
            sign = sign.substring(0, sign.length() - 1);
            if (!TextUtils.isEmpty(sign)) {
                sign = sign.concat(":").concat(appkey);
            }
        }
        String md5Sign = MD5Util.MD5Encode(sign, "UTF-8");
        return md5Sign;
    }


}
