package com.jimmy.development.http.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jimmy.development.logger.MLog;
import com.jimmy.development.constants.DebugManager;
import com.jimmy.development.http.HttpConstants;
import com.jimmy.development.http.HttpEntrance;
import com.jimmy.development.http.IHttpListener;
import com.jimmy.development.http.IHttpStringListener;
import com.jimmy.development.http.request.OriginalRequest;
import com.jimmy.development.tools.ThreadUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by jinguochong on 17-7-28.
 * 把请求放进队列里
 */
public class RequestManager {
    private static final String TAG = RequestManager.class.getSimpleName();


    public static <T> Call add2RequestQueue(final OriginalRequest originalRequest, final IHttpListener<T> listener) {

        Call call = HttpEntrance.sOkHttpClient.newCall(originalRequest.getOkRequest());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                MLog.e(TAG, "IOException onFailure " + e.toString());
                fail(originalRequest, -1, "网络连接出错", listener);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    if (!response.isSuccessful()) { // code != 200

                        //MLog.e(TAG, "response.isSuccessful fail : " + response.code() + "," + response.message());
                        /*int code = response.code();
                        if (code == HttpConstants.ERR_CODE_LOGIN_TOKEN_INVALID
                                || code == HttpConstants.ERR_CODE_PAY_TOKEN_INVALID) {
                            fail(originalRequest, response.code(), GameContextManager.getPluginContext().getResources().getString(R.string.token_invalid_try_relogin), listener);
                            return;
                        }*/
                        fail(originalRequest, response.code(), response.message(), listener);
                        return;
                    }

                    ResponseBody body = response.body();
                    if (body == null) {
                        fail(originalRequest, -1, "网络连接出错", listener);
                        return;
                    }
                    String data = body.string();//IOException
                    body.close();

                    //[ (RequestManager.java:71)#OnResponse ] onResponse:{"code":198001,"message":"游戏不存在。","value":null}
                    //增加业务code判断，如果不是200，需要返回错误
                    try {
                        //{@link com.jimmy.development.http.GsonTest}
                        JSONObject json = new JSONObject(data);
                        int code = json.optInt("code", 200);
                        String msg = json.optString("message");
                        if (code != 200) {
                            //token过期有两种，401和198004
                            if (code == HttpConstants.ERR_CODE_LOGIN_TOKEN_INVALID
                                    || code == HttpConstants.ERR_CODE_PAY_TOKEN_INVALID) {
                                msg = "账户失效，请尝试重新登录";
                            }
                            fail(originalRequest, code, msg, listener);
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    TypeToken<T> type = listener.createTypeToken();
                    final T t = new Gson().fromJson(data, type.getType());//JsonSyntaxException
                    /* 这种对开发者不友好
                    Type type = listener.createTypeToken();
                    T t = new Gson().fromJson(data, type);//JsonSyntaxException*/
                    if (t == null) {
                        fail(originalRequest, -1, "网络连接出错", listener);
                        return;
                    }
                    /*if (t instanceof ReturnData) {
                        ReturnData returnData = (ReturnData) t;
                        int code = returnData.code;
                        //token过期有两种，401和198004
                        if (code == HttpConstants.ERR_CODE_LOGIN_TOKEN_INVALID
                                || code == HttpConstants.ERR_CODE_PAY_TOKEN_INVALID) {
                            fail(originalRequest, response.code(), GameContextManager.getPluginContext().getResources().getString(R.string.token_invalid_try_relogin), listener);
                            return;
                        }
                        if (code != 200) {
                            MLog.e(TAG, "response.isSuccessful fail : " + data);

                            fail(originalRequest, returnData.code, returnData.message, listener);
                            return;
                        }
                    }*/

                    success(listener, t);
                    logSuccess(originalRequest, data);


                } catch (JsonSyntaxException e) {
                    //{@link com.meizu.gameservice.com.jimmy.development.http.GsonTest}
                    /*try {
                        JSONObject json = new JSONObject(data);
                        int code = json.optInt("code", -1);
                        if (code == 200) {
                            String value = json.optString("value", null);
                            if (TextUtils.isEmpty(value)) {
                                success(listener, null);
                                return;
                            }
                        }
                        if (originalRequest instanceof LogRequest) {
                            ((LogRequest) originalRequest).logBusinessError(200, "json解析错误");
                        }
                        MLog.e(TAG, "JsonSyntaxException fail : " + response.code() + "," + "json解析错误");
                        fail(originalRequest, response.code(), "json解析错误", listener);
                    } catch (JSONException jsonException) {
                        if (originalRequest instanceof LogRequest) {
                            ((LogRequest) originalRequest).logBusinessError(200, "json解析错误");
                        }
                        MLog.e(TAG, "JsonSyntaxException fail : " + response.code() + "," + "json解析错误");
                        fail(originalRequest, response.code(), "json解析错误", listener);
                    }*/

                    MLog.e(TAG, "JsonSyntaxException fail : " + response.code() + "," + "json解析错误");
                    fail(originalRequest, response.code(), "json解析错误", listener);

                } catch (IOException e) {

                    MLog.e(TAG, "IOException fail : " + response.code() + "," + response.message());
                    fail(originalRequest, response.code(), response.message(), listener);

                    //MLog.e(TAG, e.toString());
                }
            }
        });
        return call;
    }

    public static Call add2RequestQueue(final OriginalRequest originalRequest, final IHttpStringListener listener) {

        Call call = HttpEntrance.sOkHttpClient.newCall(originalRequest.getOkRequest());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                fail(originalRequest, -1, "网络连接出错", listener);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    if (!response.isSuccessful()) {

                        /*int code = response.code();
                        if (code == HttpConstants.ERR_CODE_LOGIN_TOKEN_INVALID
                                || code == HttpConstants.ERR_CODE_PAY_TOKEN_INVALID) {
                            fail(originalRequest, response.code(), GameContextManager.getPluginContext().getResources().getString(R.string.token_invalid_try_relogin), listener);
                            return;
                        }*/
                        fail(originalRequest, response.code(), response.message(), listener);
                        return;
                    }

                    ResponseBody body = response.body();
                    if (body == null) {
                        fail(originalRequest, -1, "网络连接出错", listener);
                        return;
                    }
                    final String data = body.string();//IOException
                    body.close();

                    success(listener, data);
                    logSuccess(originalRequest, data);


                } catch (IOException e) {

                    fail(originalRequest, response.code(), response.message(), listener);
                    //MLog.e(TAG, e.toString());
                }
            }
        });
        return call;
    }

    private static <T> void success(final IHttpListener<T> listener, final T data) {
        ThreadUtils.doOnMainThread(new Runnable() {
            @Override
            public void run() {
                listener.success(data);
            }
        });

    }

    private static void success(final IHttpStringListener listener, final String data) {
        ThreadUtils.doOnMainThread(new Runnable() {
            @Override
            public void run() {
                listener.success(data);
            }
        });
    }


    //对应两个不同的回调
    private static <T> void fail(OriginalRequest request, final int code, final String msg, final IHttpListener<T> listener) {
        ThreadUtils.doOnMainThread(new Runnable() {
            @Override
            public void run() {
                listener.fail(code, msg);
            }
        });

        logFail(request, code, msg);
    }

    private static void fail(OriginalRequest request, final int code, final String msg, final IHttpStringListener listener) {
        ThreadUtils.doOnMainThread(new Runnable() {
            @Override
            public void run() {
                listener.fail(code, msg);
            }
        });

        logFail(request, code, msg);
    }

    private static void logSuccess(OriginalRequest request, String data) {
        if (DebugManager.DEBUG_HTTP) {
            StringBuilder sb = new StringBuilder();
            Map<String, String> headers = request.getLogHeader();
            Map<String, String> params = request.getLogParams();
            sb.append("success : " + request.getOkRequest().url());
            if (headers != null) {
                sb.append(" \n headers -- ");
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    sb.append(header.getKey()).append(" : ").append(header.getValue()).append(" ; ");
                }
            }

            if (params != null) {
                sb.append(" \n params -- ");
                for (Map.Entry<String, String> param : params.entrySet()) {
                    sb.append(param.getKey()).append(" : ").append(param.getValue()).append(" ; ");
                }
            }
            sb.append(" \n response success-- ").append(data);
            MLog.e(TAG, sb.toString());
        }
    }


    private static void logFail(OriginalRequest request, final int code, final String msg) {
        if (DebugManager.DEBUG_HTTP) {
            StringBuilder sb = new StringBuilder();
            Map<String, String> headers = request.getLogHeader();
            Map<String, String> params = request.getLogParams();
            sb.append("fail : " + request.getOkRequest().url());
            if (headers != null) {
                sb.append(" \n headers -- ");
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    sb.append(header.getKey()).append(" : ").append(header.getValue()).append(" ; ");
                }
            }

            if (params != null) {
                sb.append(" \n params -- ");
                for (Map.Entry<String, String> param : params.entrySet()) {
                    sb.append(param.getKey()).append(" : ").append(param.getValue()).append(" ; ");
                }
            }
            sb.append(" \n response fail-- ").append(code).append(" , ").append(msg);
            MLog.e(TAG, sb.toString());
        }
    }
}
