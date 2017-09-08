package com.jimmy.development.tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

public class AppSignHelper {

    public static String getAppSignPublicKey(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            //使用plugin的packageInfo
            PackageInfo info = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            Signature[] signs = info.signatures;
            if (signs != null && signs.length == 1) {
                String keyValue = Base64.encodeToString(signs[0].toByteArray(), Base64.NO_WRAP);
                return keyValue;
            } else {
                Log.w(AppSignHelper.class.getSimpleName(), "APP sign illegal!");
            }
        } catch (Exception e) {
            Log.w("getAppSignPublicKey", e);
        }
        return "";
    }

    public synchronized static String getAppSignPublicKeyMd5(Context context, String packageName) {
        long timestamp = System.currentTimeMillis();
   /*     String signaturesKey = "SIGNATURES_" + Utility.getAppVersionString(GameContextManager.getPluginContext().getPackageName()
                ,GameContextManager.getPluginContext());
        String signaturesValue = PreferenceManager.getDefaultSharedPreferences(context).getString(signaturesKey,"");
        if(!TextUtils.isEmpty(signaturesValue)){
            MLog.i("MzGameSDK","getAPPSign:" + (System.currentTimeMillis() - timestamp));
            return signaturesValue;
        }*/
        String key = getAppSignPublicKey(context, packageName);
        Log.i("MzGameSDK","getAPPSign:" + (System.currentTimeMillis() - timestamp));
        if (!TextUtils.isEmpty(key)) {
            key = Utility.md5sum(key);
        }
        //PreferenceManager.getDefaultSharedPreferences(context).edit().putString(signaturesKey,key).commit();
        return key == null ? "" : key;
    }



    public synchronized static String getAppSignPublicKeyMd5(Context context, PackageInfo info) {
        Signature[] signs = info.signatures;
        String key = "";
        if (signs != null && signs.length == 1) {
            key = Base64.encodeToString(signs[0].toByteArray(), Base64.NO_WRAP);
        } else {
            Log.w(AppSignHelper.class.getSimpleName(), "APP sign illegal!");
        }
        if (!TextUtils.isEmpty(key)) {
            key = Utility.md5sum(key);
        }
        return key;
    }
}
