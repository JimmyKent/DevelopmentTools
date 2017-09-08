package com.jimmy.development.http;

import android.app.Application;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jinguochong on 17-4-13.
 */
public class HttpEntrance {

    public static final int TIME_OUT = 15;//second

    public static OkHttpClient sOkHttpClient;

    public static boolean isInit;

    public static void initOkHttp(Application application) {
        if (isInit) {
            return;
        }
        isInit = true;
        OkHttpClient.Builder okClientBuilder = new OkHttpClient.Builder();
        okClientBuilder.connectTimeout(TIME_OUT * 1000, TimeUnit.SECONDS);
        okClientBuilder.readTimeout(TIME_OUT * 1000, TimeUnit.SECONDS);
        okClientBuilder.writeTimeout(TIME_OUT * 1000, TimeUnit.SECONDS);
        okClientBuilder.addInterceptor(new RetryInterceptor());
        okClientBuilder.followRedirects(true);
        sOkHttpClient = okClientBuilder.build();


    }

    private static class RetryInterceptor implements Interceptor {
        private int MAX_RETRY = 1;//最大重试次数
        private int retryNum = 0;

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            while (!response.isSuccessful() && retryNum < MAX_RETRY) {
                retryNum++;
                response = chain.proceed(request);
            }
            return response;
        }
    }
}
