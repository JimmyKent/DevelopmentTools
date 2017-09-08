package com.jimmy.development.tools;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Locale;


public class NetworkUtil {
    public static final String TAG = NetworkUtil.class.getSimpleName();

    public static final int TYPE_CHINA_MOBILE = 1;
    public static final int TYPE_CHINA_UNION = 2;
    public static final int TYPE_CHINA_TELCOM = 3;
    public static final int TYPE_UNKNOWN = 0;
    /**
     * 没有网络
     */
    public static final String NETWORKTYPE_INVALID = "none";
    /**
     * wap网络
     */
    public static final String NETWORKTYPE_WAP = "wap";
    /**
     * 2G网络
     */
    public static final String NETWORKTYPE_2G = "2g";
    /**
     * 3G
     */
    public static final String NETWORKTYPE_3G = "3g";
    /**
     * 4G
     */
    public static final String NETWORKTYPE_4G = "4g";
    /**
     * wifi网络
     */
    public static final String NETWORKTYPE_WIFI = "wifi";

    public static boolean isWifiActive(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info != null && info.isAvailable()) {
                if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Returns whether the network is available for Activity or Service
     */
    public static boolean isNetworkAvailable(Context context) {
        boolean retval = false;
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
//            Logger.e(LogType.LOG_FOR_CLOUD, TAG, "[isNetworkAvailable]:Connectivity service is null");
            } else {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            String extraInfo = info[i].getExtraInfo();
                            if (extraInfo != null
                                    && extraInfo.toUpperCase().contains("CMWAP")) {
                                // wap 网络
                                // 可根据情况获取对应的信息
                                String data = info[i].toString();
                                // 设置HttpClient的proxy值
//                            HttpClient.setProxyHost("10.0.0.172");
//                            HttpClient.setProxyPort(80);
                                // HttpClient.setProxyPort(0);
                            } else {
//                            HttpClient.setProxyHost("");
//                            HttpClient.setProxyPort(0);
                            }
                            retval = true;
                            break;
                        }
                    }
                }

                if (!retval) {
                    NetworkInfo activeInfo = connectivity.getActiveNetworkInfo();
                    if (activeInfo != null && activeInfo.isConnected()) {
                        retval = true;
                    }
                }
            }
        } catch (Exception e) {
            Log.w(TAG, e);
        }

//        Logger.d(LogType.LOG_FOR_CLOUD, TAG, "[isNetworkAvailable]:check network available = " + retval);
        return retval;
    }

    /**
     * 获取网络状态，wifi,wap,2g,3g.
     *
     * @param context 上下文
     * @return int 网络状态 {@link #NETWORKTYPE_2G},{@link #NETWORKTYPE_3G},          *{@link #NETWORKTYPE_INVALID},{@link #NETWORKTYPE_WAP}* <p>{@link #NETWORKTYPE_WIFI}
     */

    public static String getNetWorkType(Context context) {
        String mNetWorkType = "";
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();

            if (type.equalsIgnoreCase("WIFI")) {
                mNetWorkType = NETWORKTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                networkInfo.getExtraInfo();
                String proxyHost = android.net.Proxy.getDefaultHost();

                if (TextUtils.isEmpty(proxyHost)) {
                    int mobile = isFastMobileNetwork(context);
                    if (mobile == 2) {
                        mNetWorkType = NETWORKTYPE_2G;
                    } else if (mobile == 3) {
                        mNetWorkType = NETWORKTYPE_3G;
                    } else {
                        mNetWorkType = NETWORKTYPE_4G;
                    }
                } else {
                    mNetWorkType = NETWORKTYPE_WAP;
                }
            }
        } else {
            mNetWorkType = NETWORKTYPE_INVALID;
        }

        return mNetWorkType;
    }

    /**
     * 2G到4G
     *
     * @param context
     * @return
     */
    private static int isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return 2; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return 2; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return 2; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return 3; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return 3; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return 2; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return 3; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return 3; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return 3; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return 3; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return 3; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return 3; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return 3; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return 2; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE:
                return 4; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return 2;
            default:
                return 2;
        }
    }

    //    1是移动，2是联通，3是电信
    public static int getNetworkISP(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (TextUtils.isEmpty(type)) {
                return TYPE_UNKNOWN;
            }
            if (type.equalsIgnoreCase("WIFI")) {
                // wifi
            } else if (type.equalsIgnoreCase("MOBILE")) {
                String extraInfo = networkInfo.getExtraInfo();
                if (TextUtils.isEmpty(extraInfo)) {
                    return TYPE_UNKNOWN;
                }
                if (extraInfo.equals("3gwap") || extraInfo.equals("uniwap")
                        || extraInfo.equals("3gnet") || extraInfo.equals("uninet")) {
                    return TYPE_CHINA_UNION;
                } else if (extraInfo.equals("cmnet") || extraInfo.equals("cmwap")) {
                    return TYPE_CHINA_MOBILE;
                } else if (extraInfo.equals("ctnet") || extraInfo.equals("ctwap")) {
                    return TYPE_CHINA_TELCOM;
                } else {
                    return getNetworkISPBySIM(context);
                }
            } else {

            }
        } else {

        }
        return TYPE_UNKNOWN;
    }

    /**
     * 通过sim卡获取运营商类型
     *
     * @param c
     * @return
     */
    public static int getNetworkISPBySIM(Context c) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
            String operator = telephonyManager.getSimOperator();
            if (operator == null || operator.equals("")) {
                operator = telephonyManager.getSubscriberId();
            }
            if (operator == null || operator.equals("")) {
//                ToastUtil tu = new ToastUtil(c);
//                tu.showDefultToast("未检测到sim卡信息!");
            }
            if (operator != null) {
                if (operator.startsWith("46000")
                        || operator.startsWith("46002")) {
                    return TYPE_CHINA_MOBILE;
                } else if (operator.startsWith("46001")) {
                    return TYPE_CHINA_UNION;
                } else if (operator.startsWith("46003")) {
                    return TYPE_CHINA_TELCOM;
                }
            }
        } catch (Exception e) {
            Log.w(TAG, e);
        }
        return TYPE_UNKNOWN;
    }

    /**
     * 获取当前网络IP地址
     *
     * @return ip地址
     */
    public static String getIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        // if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet6Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    public static final String getLanguage() {
        return Locale.getDefault().getLanguage().toLowerCase() + "-" + Locale.getDefault().getCountry().toLowerCase();
    }

    public static final String getLocationData(Context context) {
   /*     try {
            TelephonyManager teleManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            GsmCellLocation gcl = (GsmCellLocation) teleManager.getCellLocation();
            if (gcl != null) {
                int cid = gcl.getCid();
                int lac = gcl.getLac();
                ArrayList<NeighboringCellInfo> neighCelInfoList = new ArrayList<NeighboringCellInfo>();
                neighCelInfoList = (ArrayList<NeighboringCellInfo>) teleManager.getNeighboringCellInfo();
                String str = teleManager.getNetworkOperator();
                if (TextUtils.isEmpty(str)) {
                    return "";
                }
                int mcc = Integer.valueOf(str.substring(0, 3));
                int mnc = Integer.valueOf(str.substring(3, 5));

                StringBuilder builder = new StringBuilder();
                builder.append(cid).append(",").append(lac).append(",").append(mcc).append(",").append(mnc);

                final int MAX_LENGTH = 5;
                for (int i = 0; i < neighCelInfoList.size() && i < MAX_LENGTH; ++i) {
                    builder.append(";").append(neighCelInfoList.get(i).getCid()).append(",").append(neighCelInfoList.get(i).getLac()).append(",")
                            .append(mcc).append(",").append(mnc);
                }
                return builder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
//            MLog.w(GameCenterPlatformImpl.TAG, e.toString());
        }*/
        return "";
    }

    public static void setNetworkMethod(final Context context) {
        //提示对话框
        // TODO Auto-generated method stub
        if (context != null) {
            Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
            context.startActivity(intent);
        }
    }

    public static String getHost(String url) {
        String host = "";
        if (!TextUtils.isEmpty(url)) {
            Uri uri = Uri.parse(url);
            if (uri != null) {
                host = uri.getHost();
            }
        }
        return host;
    }

    public static String getURLPath(String url){
        String path = "";
        if (!TextUtils.isEmpty(url)) {
            Uri uri = Uri.parse(url);
            if (uri != null) {
                path = uri.getPath();
            }
        }
        return path;
    }
}
