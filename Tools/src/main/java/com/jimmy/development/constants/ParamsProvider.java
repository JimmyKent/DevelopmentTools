package com.jimmy.development.constants;

import android.content.Context;
import android.os.Build;

import com.jimmy.development.logger.MLog;
import com.jimmy.development.tools.DeviceUtil;
import com.jimmy.development.tools.Utility;
import com.jimmy.development.tools.NetworkUtil;
import com.jimmy.development.tools.ReflectHelper;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by jinguochong on 17-8-1.
 * http请求需要的一些公共参数
 */
public class ParamsProvider {

    public static final String OS = "os";                               // integer	Android 系统版本	8/16/17
    public static final String MZOS = "mzos";                           // string	Flyme 系统版本	4.0
    public static final String SCREEN_SIZE = "screen_size";             // string	屏幕大小	960x640
    public static final String LANGUAGE = "language";                   // string	系统语言	zh_CN
    public static final String IMEI = "imei";                           // 设备imei
    public static final String VERSIONCODE = "version_code";
    public static final String SN = "sn";                               // 设备sn
    public static final String DEVICE_MODEL = "device_model";           // m353
    public static final String V = "v";                                 // version
    public static final String VC = "vc";                               // versioncode
    public static final String NET = "net";                             // WIFI/3G
    public static final String UID = "uid";                             // uid
    public static final String FIRMWARE = "firmware";                   //
    public static final String LOCALE = "locale";                       //
    public static final String MPV = "mpv";                             // 阿里标识参数

    public static final String VERSION = "version";                     //接口版本

    public static final String VALUE_APPS_ALI = "apps_ali";             // 阿里标识

    public static final String HEAD_ACCEPT_LANGUAGE = "Accept-Language";

    public static final String TIME = "time";

    public static final String OP_CATEGORY = "op_category";

    public static final String MAC = "mac";                             //
    public static final String OPERATOR = "operator";                   // 运营商
    //中国移动、中国联通、中国电信缩写
    public static final String ISP_MAP[] = new String[]{"unknown", "cmcc", "cucc", "ctcc"};


    public int os = Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    public String mzos = "";
    public String screenSize = "";
    public String language = "";
    public String imei = "";
    public String sn = "";
    public String deviceModel = "";
    public String net = "";
    public String op_category = "";
    public String firmware = "";
    public String locale = "";
    public String v;
    public int vc;


//    functionid	Y	客户端随机生成的唯一编码，请求响应的functionid相同
//    app_id	Y	游戏ID
//    timestamp	Y	日间戳，精确到毫秒
//    time	Y	请求响应耗时
//    imei	Y	机器IMEI
//    net	Y	网络类型包含运营商(2g/3g/wifi,)
//    op_category		区分中国移动，中国联通
//    os	Y	跟三大中心通用参数一致
//    device_type	Y	设备类型跟三大中心通用参数一致
//    url	Y	请求的URL地址
//    params	N	异常时将异常参数上报
//    returnCode	Y	响应状态
//    statusCode	Y	客户端自定义的扩展信息，如网络异常,-100网络异常 ，http status code
//    data	N	returnCode!=200时，response

    public Map<String, String> getParameters() {
        HashMap<String, String> map = new HashMap<>();
        map.put(OS, String.valueOf(os));
        map.put(MZOS, mzos);
        map.put(SCREEN_SIZE, screenSize);
        map.put(LANGUAGE, language);
        map.put(IMEI, imei);
        map.put(SN, sn);
        map.put(DEVICE_MODEL, deviceModel);
        map.put(FIRMWARE, firmware);
        map.put(NET, net);
        map.put(LOCALE, locale);
        map.put(OP_CATEGORY, op_category);
        map.put(V, v);
        map.put(VC, String.valueOf(vc));

        return map;
    }

    public static String getISP(Context context) {
        return ISP_MAP[NetworkUtil.getNetworkISP(context)];
    }

    public static String getMzOS() {
        String mzos = "";
        try {
            mzos = (String) ReflectHelper.getStaticField("android.content.res.flymetheme.FlymeThemeUtils", "FLYME_THEME_OS");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mzos;
    }

    //------------------------------------------------------------------------------------------
    private static ParamsProvider ourInstance;

    public static void init(Context context) {
        if (ourInstance == null) {
            ourInstance = new ParamsProvider(context);
        }
    }

    public static ParamsProvider getInstance() {
        return ourInstance;
    }

    private ParamsProvider(Context context) {
        os = Build.VERSION.SDK_INT;
        mzos = getMzOS();
        DeviceUtil.initScreen(context);
        screenSize = DeviceUtil.getScreen();
        language = DeviceUtil.getLanguageLocal();

        imei = DeviceUtil.getPhoneIMEI(context);

        deviceModel = Build.DEVICE;
        firmware = DeviceUtil.getSystemDisplayVersion();

        net = NetworkUtil.getNetWorkType(context);
        locale = Locale.getDefault().getCountry();
        op_category = getISP(context);

        v = Utility.getAppVersionString(context.getPackageName(), context);
        vc = Utility.getAppVersionCode(context.getPackageName(), context);
    }
}
