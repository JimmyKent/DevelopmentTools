package com.jimmy.development.plugin.imgplugin.request;

import com.jimmy.development.plugin.imgplugin.request.data.ImageListBean;
import com.jimmy.development.plugin.imgplugin.request.data.ReturnDataList;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by jinguochong on 2017/8/23.
 * https://github.com/ikidou/Retrofit2Demo/blob/master/client/src/main/java/com/github/ikidou/Example02.java
 */

public class ImageRequest {


    public static void main(String[] args) throws IOException {
        getImageListByRetrofit(1);
    }

    public static void getImageListByRetrofit(int page) {
        Retrofit retrofit = RequestBuilder.buildRequest("http://gank.io/");
        IImgList iImgList = retrofit.create(IImgList.class);
        retrofit2.Call<ReturnDataList<ImageListBean>> call = iImgList.getImgList(page);
        //同步调用
        /*try {
            retrofit2.Response<ResponseBody> bodyResponse = call.execute();
            String body = bodyResponse.body().string();
            System.out.println("wxl" + "  body=" + body);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //异步调用
        RequestBuilder.enqueue(call, new IHttpListener<ReturnDataList<ImageListBean>>() {
            @Override
            public void ok(ReturnDataList<ImageListBean> data) {
                System.out.println("  bean= " + data);
            }

            @Override
            public void fail(int code, String msg) {

            }
        });
    }

//http://www.jianshu.com/p/7c907686f6c5
    private interface IImgList {
        @GET("api/data/Android/10/{page}")
        Call<ReturnDataList<ImageListBean>> getImgList(@Path("page") int page);
    }
}
