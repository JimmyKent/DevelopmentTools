package com.jimmy.development.tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import com.jimmy.development.constants.PackageConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Locale;
import java.util.UUID;

public class DeviceInfo {
    private static final String PREFS_DEVICE_ID = "prefs_device_id";

    public static final int SIP_ENABLE_FLAG = 1;
    private static final String TAG = "DeviceInfo";
    public static int sScreenWidth = -1;
    public static int sScreenHeight = -1;
    private static Boolean sHasSmartBar = null;
    private static String mDeviceId;

    public static String getPhoneSN(Context context) {
        String seqNum = PhoneUtils.getPhoneSn(context);
        return seqNum == null ? "" : seqNum;
    }

    public static String getPhoneIMEI(Context context) {
        String imei = PhoneUtils.getDefaultImei(context);
        return imei == null ? "" : imei;
    }


    /**
     * 读出保存在程序文件系统中的表示该设备在此程序上的唯一标识符。。
     *
     * @param file 保存唯一标识符的File对象。
     * @return 唯一标识符。
     * @throws IOException IO异常。
     */
    public static String readStringFromFile(File file) throws IOException {
        RandomAccessFile accessFile = new RandomAccessFile(file, "r");
        byte[] bs = new byte[(int) accessFile.length()];
        accessFile.readFully(bs);
        accessFile.close();
        return new String(bs);
    }


    /**
     * 将表示此设备在该程序上的唯一标识符写入程序文件系统中
     *
     * @param file 保存唯一标识符的File对象。
     * @throws IOException IO异常。
     */
    private static void writeStringToFile(File file, String uuid) {
        try {
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);

            out.write(uuid.getBytes());
            out.close();
        } catch (Exception e) {
            Log.w(TAG, e);
        }


    }


//    public static final boolean isChinaUnicom(Context context){
//    	return BuildEx.isChinaUnicom(context);
//    }

    public static boolean isSdcardEnable() {
        return android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
    }



    public static final boolean isPackageDisabled(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            int status = pm.getApplicationEnabledSetting(packageName);
            return status == PackageManager.COMPONENT_ENABLED_STATE_DISABLED || status == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER;
        } catch (Exception e) {
            Log.w(TAG, e);
        }
        return true;
    }

//	// 判断当前使用的网络是否是中国的,如果没插sim卡，也返回false
//	public static boolean isInChina(Context context){
//		try{
//			TelephonyManager teleManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//			String networkContryIso = teleManager.getNetworkCountryIso();
//			String networkOperator = teleManager.getNetworkOperator();
//			// 如果没插sim卡，networkContryIso和networkOperator都是空的字符串
//			if(networkContryIso.toLowerCase().equals("cn")&& networkOperator.startsWith("460")){
//				return true;
//			}
//			boolean isInternational = isInternational();
//			if(!isInternational){
//				return true;
//			}
//			return false;
//		}catch(Exception e){
//			return true;
//		}
//	}

    public static String getModel() {
        return PhoneUtils.getDeviceModel();
    }

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

    public static final int getAppVersionCode(String packageName, Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageInfo(packageName, 0);
            return info.versionCode;
        } catch (Exception e) {
            Log.w(TAG, e);
        }
        return 0;
    }

    //获取SIM卡的状态
    public static int getSimState(Context context) {
        try {
            TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            int simState = mTelephonyManager.getSimState();
            Log.w(TAG, "[getSimState] simState = " + simState);
            return simState;
        } catch (Exception e) {
            Log.w(TAG, e);
        }
        return TelephonyManager.SIM_STATE_UNKNOWN;
    }

    //获取电话卡的IMSI
    public static String getPhoneIMSI(Context context) {
        String imsi = null;
        if (getSimState(context) == TelephonyManager.SIM_STATE_READY) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imsi = tm.getSubscriberId();
        }
        return imsi == null ? "" : imsi;
    }

    public static final Drawable getAppIcon(String packageName, Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageInfo(packageName, 0);
            if (info == null)
                return null;

            return info.applicationInfo.loadIcon(pm);
        } catch (Exception e) {
            Log.w(TAG, e);
        }
        return null;
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

    // FIXME 要修改为是否为Flyme系统
    @Deprecated
    public static boolean isMeizuDevice() {
        boolean retval = "MEIZU".equalsIgnoreCase(Build.BRAND);
        if (!retval) {
            String brand = Build.BRAND;
            if (!TextUtils.isEmpty(brand) && brand.startsWith("alps")) {
                //临时兼容，后续可去掉
                retval = true;
            }
        }
        return retval;
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
