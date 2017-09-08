package com.jimmy.development.http;

import com.jimmy.development.http.request.DefaultRequest;
import com.jimmy.development.http.request.ImageRequest;
import com.jimmy.development.http.request.OriginalRequest;

/**
 * Created by jinguochong on 17-7-28.
 * 请求生成
 */
public class RequestBuilder {

    /**
     * 带log上报的请求需要带包名，如果不是特殊需要，请不要使用这个请求
     * @param url 请求的url
     * @param pkgName 游戏的包名
     * @return request
     */
    public static OriginalRequest createSDKRequest(String url, String pkgName) {
        return new SDKRequest(url, pkgName);
    }

    // 默认请求，不会记录log
    public static OriginalRequest createDefaultRequest(String url) {
        return new DefaultRequest(url);
    }

    public static ImageRequest createImgRequest(String url) {
        return new ImageRequest(url);
    }

}
