package com.jimmy.development.request;

import com.jimmy.development.GetExample;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jinguochong on 2017/8/16.
 * 从只用okhttp逐步增加其他框架
 * 1.构建一个请求的 url header body key-value 参数；
 * 2.把请求加到请求队列；
 * 3.发起请求，得到response流；
 * 4.转bean；
 * 5.post到主线程。
 */

public class RequestEvolution {

    OkHttpClient client = new OkHttpClient();

    public void main(String[] args) throws IOException {
        String url = "http://gank.io/api/data/Android/10/1";
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    response.body().string();
                    System.out.println(response);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
