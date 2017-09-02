package com.jimmy.development.plugin.imgplugin.request;

import android.support.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jinguochong on 2017/8/30.
 */

public class RequestBuilder {


    public static Retrofit buildRequest(@NonNull String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                //这里建议：- Base URL: 总是以/结尾；- @Url: 不要以/开头
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public static <T> void enqueue(Call call, final IHttpListener<T> listener) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                T data = response.body();
                listener.ok(data);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                listener.fail(-1,t.getMessage());
            }
        });
    }

}
