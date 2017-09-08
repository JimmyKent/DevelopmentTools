package com.jimmy.development.http.request;


import com.jimmy.development.http.IHttpListener;
import com.jimmy.development.http.IHttpStringListener;
import com.jimmy.development.constants.ParamsProvider;
import com.jimmy.development.http.utils.RequestManager;
import com.jimmy.development.tools.NetworkUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by jinguochong on 17-4-13.
 * 业务请求基类
 */
public class OriginalRequest {


    private static final String TAG = OriginalRequest.class.getSimpleName();

    //public static final int REQUEST_TYPE_POST_NORMAL = 0;
    //public static final int REQUEST_TYPE_GET = 1;
    //public static final int REQUEST_TYPE_POST_FORM = 2;
    //public static final int REQUEST_TYPE_POST_STRING = 3;
    //public static final int REQUEST_TYPE_POST_STREAM = 4;
    //public static final int REQUEST_TYPE_POST_FILE = 5;

    private static final String COOKIE_LANGUAGE = "Accept-Language";
    private static final String COOKIE_NETWORK = "netType";
    private static final String HEADER_KEY_ACCEPT_CODE = "Accept-Encoding";
    private static final String HEADER_ACCEPT_CODE_GZIP = "gzip";

    protected String url;
    protected Map<String, String> headers;
    protected Map<String, String> params;
    private Request request;
    private Call call;

    public OriginalRequest(String url) {
        this.url = url;
    }

    public OriginalRequest addHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public OriginalRequest addParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public OriginalRequest post(final IHttpListener listener) {
        addPublicHeader();
        addAuthHeader();
        Request.Builder requestBuilder = new Request.Builder().url(url);
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                requestBuilder.addHeader(header.getKey(), header.getValue());
            }
        }
        FormBody.Builder paramsBuilder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                paramsBuilder.add(param.getKey(), param.getValue());
            }
        }
        RequestBody body = paramsBuilder.build();
        request = requestBuilder.post(body).build();

        call = RequestManager.add2RequestQueue(this, listener);
        return this;
    }

    public OriginalRequest get(IHttpListener listener) {
        addPublicHeader();
        addAuthHeader();

        Request.Builder reqBuild = new Request.Builder();
        HttpUrl httpUrl = HttpUrl.parse(url);
        if (httpUrl == null) {
            throw new NullPointerException("url parse exception!");
        }
        HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
        if (params != null) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                urlBuilder.addQueryParameter(param.getKey(), param.getValue() == null ? "" : param.getValue());
            }
        }
        HttpUrl fullUrl = urlBuilder.build();
        //MLog.e("get : " + fullUrl);
        reqBuild.url(fullUrl);

        request = reqBuild.build();
        call = RequestManager.add2RequestQueue(this, listener);
        return this;
    }

    //------------------------ String Listener ---------------------------------------------------
    public OriginalRequest post(final IHttpStringListener listener) {
        addPublicHeader();
        addAuthHeader();
        Request.Builder requestBuilder = new Request.Builder().url(url);
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet())
                requestBuilder.addHeader(header.getKey(), header.getValue());
        }
        FormBody.Builder paramsBuilder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                // XXX MD5签名对null 和 "" 处理不一样
                paramsBuilder.add(param.getKey(), param.getValue() == null ? "" : param.getValue());
            }
        }
        RequestBody body = paramsBuilder.build();
        request = requestBuilder.post(body).build();

        call = RequestManager.add2RequestQueue(this, listener);
        return this;
    }

    public OriginalRequest get(IHttpStringListener listener) {
        addPublicHeader();
        addAuthHeader();

        Request.Builder reqBuild = new Request.Builder();
        HttpUrl httpUrl = HttpUrl.parse(url);
        if (httpUrl == null) {
            throw new NullPointerException("url parse exception!");
        }
        HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
        if (params != null) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                urlBuilder.addQueryParameter(param.getKey(), param.getValue() == null ? "" : param.getValue());
            }
        }
        HttpUrl fullUrl = urlBuilder.build();
        //MLog.e("get : " + fullUrl);
        reqBuild.url(fullUrl);

        request = reqBuild.build();
        call = RequestManager.add2RequestQueue(this, listener);
        return this;
    }


    public void progress() {
    }

    //public void cacheResponse() {}

    public boolean isCanceled() {
        if (call == null) {
            return true;
        }
        return call.isCanceled();
    }

    public void cancelRequest() {
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
    }

    public void retry() {
        //XXX
        /*if (mRetryPolicy != null) {
            mRetryPolicy.retry(this, e);
        }*/
    }

    public Request getOkRequest() {
        return request;
    }

    protected void addAuthHeader() {

    }

    //log
    public Map<String, String> getLogHeader() {
        return headers;
    }

    public Map<String, String> getLogParams() {
        return params;
    }

    private void addPublicHeader() {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        // 加上固定header
        try {
            String host = new URL(url).getHost();
            this.headers.put("X-Online-Host", host);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.headers.put(COOKIE_LANGUAGE, NetworkUtil.getLanguage());
        this.headers.put(HEADER_KEY_ACCEPT_CODE, HEADER_ACCEPT_CODE_GZIP);

    }
}
