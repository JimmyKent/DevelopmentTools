package com.jimmy.development.plugin.imgplugin.request;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jinguochong on 2017/8/30.
 */

public class RequestInit {

    private static final int TIME_OUT = 15;//second

    private static boolean isInit;

    private static OkHttpClient sOkHttpClient;

    public static void initRequest() {
        if (isInit) {
            return;
        }
        isInit = true;
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
        okBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        okBuilder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        okBuilder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
        okBuilder.addInterceptor(new RetryInterceptor());
        sOkHttpClient = okBuilder.build();
    }

    private static class RetryInterceptor implements Interceptor {

        private int MAX_RETRY = 1;
        private int retryNum = 0;

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            while (!response.isSuccessful() && retryNum < MAX_RETRY) {
                retryNum++;
                response = chain.proceed(request);
            }
            return null;
        }
    }
}
