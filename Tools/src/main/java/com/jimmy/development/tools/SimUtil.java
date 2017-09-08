package com.jimmy.development.tools;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class SimUtil {

    private final static int SLOT_ID_2 = 1;

    public static enum SimOpType {
        NONE,
        UNKNOWN,
        CM,
        CU,
        CT
    }

    public static SimOpType getSimOp(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = tm.getSimState();
        if (simState == TelephonyManager.SIM_STATE_READY) {
            String sp = tm.getSimOperator();
            if (!TextUtils.isEmpty(sp)) {
                if ("46000".equals(sp) || "46002".equals(sp) || "46007".equals(sp)) {
                    return SimOpType.CM;
                } else if ("46001".equals(sp)) {
                    return SimOpType.CU;
                } else if ("46003".equals(sp)) {
                    return SimOpType.CT;
                } else {
                    return SimOpType.UNKNOWN;
                }
            } else {
                return SimOpType.NONE;
            }
        } else {
            return SimOpType.NONE;
        }
    }

    public static String getSimOpCode(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = tm.getSimState();
        if (simState == TelephonyManager.SIM_STATE_READY) {
            return tm.getSimOperator();
        }
        return "";
    }

    public static boolean isSimReady(Context context) {
        TelephonyManager phoneMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return isSimReady(phoneMgr);
    }

/*    public static boolean isSimReady(TelephonyManager phoneMgr) {
        boolean isSimReady = false;
        if (phoneMgr.getSimState() == TelephonyManager.SIM_STATE_READY) {
            isSimReady = true;
        } else {
            try {
                int state = Reflect.on(phoneMgr).call("getSimState", new Class[]{int.class}, new Object[]{SLOT_ID_2}).get();
                if (state == TelephonyManager.SIM_STATE_READY) {
                    isSimReady = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return isSimReady;
    }*/

    public static boolean isSimReady(TelephonyManager phoneMgr) {
        boolean isSimReady = false;
        if (phoneMgr.getSimState() == TelephonyManager.SIM_STATE_READY) {
            isSimReady = true;
        }
        return isSimReady;
    }

}
