package com.jimmy.development.tools;

import android.content.Context;
import android.os.Build;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;


import java.lang.reflect.Method;


public class PhoneUtils {

    // 魅族同事提供的SN获取方式
    private static String sPhoneSn;
    private static String sImei;

    public synchronized static String getMzPhoneSn(Context context) {
        if (sPhoneSn == null) {
            sPhoneSn = SystemProperties.get("ro.serialno");
        }
        //Log.e("PhoneUtils", "Get Mz Phone SN " + sPhoneSn + "xxx");
        return sPhoneSn;
    }

    public static String getPhoneSn(Context context) {
        return getMzPhoneSn(context);
    }

    public static String getDeviceModel() {
        String model = null;
        if (!isFlymeRom()) {
            try {
                model = (String) ReflectHelper.getStaticField("android.os.BuildExt", "MZ_MODEL");
            } catch (Exception e) {
            }
        }
        if (TextUtils.isEmpty(model)) {
            model = Build.MODEL;
        }
        return model;
    }

    /**
     * Returns if Flyme is running on a third party device.
     *
     * @return true if the rom is Flyme running on a third party device, false if the rom is Flyme proudly running on Meizu Phone.
     */
    public static boolean isFlymeRom() {
        boolean b = false;
        try {
            b = (Boolean) ReflectHelper.invokeStatic("android.os.BuildExt", "isFlymeRom", null);
        } catch (Exception e) {
        }
        return b;
    }

    public static String getDefaultImei(Context context) {
        if (TextUtils.isEmpty(sImei)) {
            /*try {
                final String MZ_T_M = "android.telephony.MzTelephonyManager";
                final String METHOD_GET_DEVICE_ID = "getDeviceId";
                sImei = (String) ReflectHelper.invokeStatic(MZ_T_M, METHOD_GET_DEVICE_ID, null, null);
            } catch (Exception ignore) {
                //ignore.printStackTrace();
                Log.w("PhoneUtils","NotFound android.telephony.MzTelephonyManager");
            }

            if (TextUtils.isEmpty(sImei)) {
                try {
                    final String MZ_T_M = "com.meizu.telephony.MzTelephonymanager";
                    final String METHOD_GET_DEVICE_ID = "getDeviceId";
                    sImei = (String) ReflectHelper.invokeStatic(MZ_T_M, METHOD_GET_DEVICE_ID, new Class<?>[]{Context.class, int.class}, new Object[]{context, 0});
                } catch (Exception ignore) {
                    Log.w("PhoneUtils","NotFound android.telephony.MzTelephonymanager");
                }
            }*/

            // 这个处理是非必须的，因为METHOD_GET_DEVICE_ID本身做了这个处理；为了运行在其它手机平台，这里兼容处理
            try {
                if (TextUtils.isEmpty(sImei)) {
                    if (context == null) {
                        return sImei;
                    }
                    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    sImei = tm.getDeviceId();
                }
            } catch (Exception e) {
                Log.w("PhoneUtils",e);
            }
        }
        return sImei;
    }

    private static String getPhoneSnByService(Context context) {
        // 通过service的方法获取暂时用不了
        boolean tryOldMethod = false;
        String sn = "";
        try {
            IBinder binder = ServiceManager.checkService("phone_ext");
            if (binder == null) {
                tryOldMethod = true;
            } else {
                Object iInterface = ReflectHelper.invokeStatic("com.meizu.telephony.ITelephonyExt$Stub", "asInterface", new Class<?>[]{IBinder.class}, new Object[]{binder});
                Method querySN = Class.forName("com.meizu.telephony.ITelephonyExt").getMethod("queryProductSeqNo");
                sn = (String) querySN.invoke(iInterface);
                if (sn == null) {
                    sn = "";
                }
            }
        } catch (NoSuchMethodException e) {
            tryOldMethod = true;
        } catch (ClassNotFoundException e) {
            tryOldMethod = true;
        } catch (Exception e) {
            // ignore exception
            Log.w("getPhoneSnByService", e);
        }
        if (tryOldMethod) {
            sn = getOldPhoneSn(context);
        }

        Log.w("PhoneUtils", "Get Phone SN " + sn + "XXX");
        return sn;
    }

    private static String getOldPhoneSn(Context context) {
        try {
            Object binder = ServiceManager.checkService("phone");
            Object iInterface = ReflectHelper.invokeStatic("com.android.internal.telephony.ITelephony$Stub", "asInterface", new Class<?>[]{IBinder.class}, new Object[]{binder});
            Method querySN = Class.forName("com.android.internal.telephony.ITelephony").getMethod("queryProductSeqNo");
            String sn = (String) querySN.invoke(iInterface);
            if (sn == null) {
                sn = "";
            }
            return sn;
        } catch (Exception e) {
            // ignore all exception
            Log.w("getOldPhoneSn", e);
        }
        return "";
    }
}
