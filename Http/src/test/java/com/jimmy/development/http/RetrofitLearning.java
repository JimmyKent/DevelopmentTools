package com.jimmy.development.http;

import com.jimmy.development.http.data.ImageListBean;
import com.jimmy.development.http.data.ReturnDataListTest;

import org.junit.Test;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by jinguochong on 2017/9/9.
 * 从只用okhttp逐步增加Retrofit框架
 * 1.构建一个请求的 url header body key-value 参数；//retrofit 用interface + 注解
 * 2.把请求加到请求队列；//还是okhttp的队列
 * 3.发起请求，得到response流；
 * 4.转bean；//GsonConverterFactory
 * 5.post到主线程。
 */
public class RetrofitLearning {

    //get请求不带参

    //get请求带参

    //post请求带参

    @Test
    public void testGet() {
        Retrofit retrofit = new Retrofit.Builder()
                //这里建议：- Base URL: 总是以/结尾；- @Url: 不要以/开头
                .baseUrl("http://gank.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IImgList iImgList = retrofit.create(IImgList.class);
        retrofit2.Call<ReturnDataListTest<ImageListBean>> call = iImgList.getImgList(1);
        //同步调用
        try {
            retrofit2.Response<ReturnDataListTest<ImageListBean>> bodyResponse = call.execute();
            String body = bodyResponse.body().toString();
            System.out.println("body=" + body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetWithPath() {
        Retrofit retrofit = new Retrofit.Builder()
                //这里建议：- Base URL: 总是以/结尾；- @Url: 不要以/开头
                .baseUrl("http://www.weather.com.cn/")
                .build();
        ApiStores apiStores = retrofit.create(ApiStores.class);
        Call<ResponseBody> call = apiStores.getWeather("101010100");
        //同步调用
        try {
            Response<ResponseBody> bodyResponse = call.execute();
            String body = bodyResponse.body().string();//获取返回体的字符串
            System.out.println("body=" + body);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //异步调用
        /*call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String body = response.body().string();//获取返回体的字符串
                    System.out.println("body=" + body);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("onFailure=" + t.getMessage());
            }

        });*/
    }

    /*Query

    如果链接是http://ip.taobao.com/service/getIpInfo.php?ip=202.202.33.33
*/

    @Test
    public void testGetWithParams() {
        Retrofit retrofit = new Retrofit.Builder()
                //这里建议：- Base URL: 总是以/结尾；- @Url: 不要以/开头
                .baseUrl("http://ip.taobao.com/")
                .build();
        IpInfo apiStores = retrofit.create(IpInfo.class);
        Call<ResponseBody> call = apiStores.getIp("202.202.33.33");
        //同步调用
        try {
            Response<ResponseBody> bodyResponse = call.execute();
            String body = bodyResponse.toString();//获取返回体的字符串
            System.out.println("body=" + body);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private interface IImgList {
        @GET("api/data/Android/10/{page}")
        Call<ReturnDataListTest<ImageListBean>> getImgList(@Path("page") int page);
    }

    /**
     * Call<T> get();必须是这种形式,这是2.0之后的新形式
     * 如果不需要转换成Json数据,可以用了ResponseBody;
     * 你也可以使用Call<GsonBean> get();这样的话,需要添加Gson转换器
     */
    public interface ApiStores {
        @GET("adat/sk/{cityId}.html")
        Call<ResponseBody> getWeather(@Path("cityId") String cityId);
    }

    //http://ip.taobao.com/service/getIpInfo.php?ip=202.202.33.33
    public interface IpInfo {
        @GET("service/getIpInfo.php")
        Call<ResponseBody> getIp(@Query("ip") String ip);

        //传入一个数组
        /*@GET("/list")
        Call<ResponseBody> list(@Query("category") String... categories);*/
    }


    /*//使用默认URL编码
    @GET("/search")
    Call<ResponseBody> list(@QueryMap Map<String, String> filters);
    //不使用默认URL编码
    @GET("/search")
    Call<ResponseBody> list(@QueryMap(encoded=true) Map<String, String> filters);*/



}
