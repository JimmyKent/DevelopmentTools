package com.jimmy.development.request;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by jinguochong on 2017/8/16.
 *
 */
public class OkHttpTest {

    public static void main(String[] args) {
        String url = "http://gank.io/api/data/Android/10/1";

        try {
            OkHttpClient client = new OkHttpClient();
            FormBody.Builder params = new FormBody.Builder();
            //params.add("key", "value"); //XXX retrofit
            RequestBody body = params.build();
            Request request = new Request.Builder()
                    .url(url)
                    //.post(body)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("onFailure currentThread : " + Thread.currentThread().getName());
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    //回调不在主线程
                    final String data = response.body().string();
                    System.out.println("currentThread : " + Thread.currentThread().getName());
                    System.out.println(data);
                    /*new Handler(Looper.getMainLooper()).post(new Runnable() {//XXX RxJava
                        @Override
                        public void run() {
                            System.out.println(data);
                            //data -> bean XXX Gson
                        }
                    });*/
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
