package com.jimmy.development.http.log;

public class LogConstants {
    // 1：点击游戏（游戏启动次数）
    // 2：点击登录（登录请求次数）
    // 3：登录成功（登录成功次数）
    // 4：登录失败（登录失败次数）
    // 5：点击取消登录（取消登录次数）
    // 6：点击退出登录（退出登录次数）
    // 7：点击购买，创建订单（扣费数）
    // 8：订单创建成功返回到客户端（预扣费订单数）
    // 9：点击支付（确定扣费数）
    // 10：点击跳转充值（跳转充值数）
    // 11：第三方支付返回（包括余额支付）（支付成功数）

    public static final int CLICK_GAME = 1;
    public static final int CLICK_LOGIN = 2;
    public static final int LOGIN_SUCCESS = 3;
    public static final int LOGIN_FAIL = 4;
    public static final int CANCEL_LOGIN = 5;
    public static final int CLICK_LOGOUT = 6;
    public static final int CLICK_BUY = 7;
    public static final int ORDER_CREATED = 8;
    public static final int CLICK_PAY = 9;
    public static final int CLICK_CHARGE = 10;
    public static final int PAY_BACK = 11;


    public static final String CREATE_LOG = "https://api.game.meizu.com/game/log/createlog";

    public static final String PARAM_APP_ID = "app_id";
    public static final String PARAM_UID = "uid";
    public static final String PARAM_NET_TYPE = "net_type";                    // 运营商
    public static final String PARAM_DEVICE_TYPE = "device_type";                // 设备名称，如MX2
    public static final String PARAM_ACTIVE_TYPE = "actvie_type";                        // 日志类型：1～11
    public static final String PARAM_PARTNER_ID = "partner_id";                    // 商家唯一订单号码
    public static final String PARAM_WIFI_MAC = "wifi_mac";                    // wifi地址
    public static final String PARAM_RESIDENCE = "residence";                                    //
    public static final String PARAM_IMEI = "imei";            // imei
    public static final String PARAM_TS = "ts"; //
    public static final String PARAM_SIGN = "sign"; // 参数签名
    public static final String PARAM_SIGN_TYPE = "sign_type"; // 常量值md5
    public static final String PARAM_APP_PACKAGE = "app_package"; // 游戏包名
    public static final String PARAM_VERSION_CODE = "version_code"; // 游戏版本号
    public static final String PARAM_VERSION_NAME = "version_name"; // 游戏版本名
    public static final String PARAM_APP_NAME = "app_name"; // 游戏名
    public static final String PARAM_ACTIVE_CONTENT = "active_content"; // 操作内容(活动为11时填写支付金额 其他活动号可酌情填写)
    public static final String PARAM_ACTIVE_TIME = "active_time"; // 行为时间
    public static final String PARAM_CREATE_TIME = "create_time"; // 日志采集时间

    public static final String CHANNEL_NO = "channel_no"; // 日志采集时间

}
