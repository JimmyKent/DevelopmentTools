package com.jimmy.development.request;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;


/**
 * Created by jinguochong on 2017/8/16.
 * http://wuxiaolong.me/2016/01/15/retrofit/
 */
public class RetrofitTest {


    public interface IGetRequest {
        @GET("ajax.php?a=fy&f=auto&t=auto&w=hello%20world")
        Call<Translation> getCall();
        // 注解里传入 网络请求 的部分URL地址
        // Retrofit把网络请求的URL分成了两部分：一部分放在Retrofit对象里，另一部分放在网络请求接口里
        // 如果接口里的url是一个完整的网址，那么放在Retrofit对象里的URL可以忽略
        // getCall()是接受网络请求数据的方法
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

    public static void main(String... args) throws IOException {
        original();
        //addGson();
    }

    private static void original() {
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
            System.out.println("wxl" + "  body=" + body);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //异步调用
        /*call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String body = response.body().string();//获取返回体的字符串
                    System.out.println("wxl" + "  body=" + body);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("wxl" + "  onFailure=" + t.getMessage());
            }

        });*/
    }

    private static void addGson() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fy.iciba.com/") //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .build();

        // 步骤5:创建 网络请求接口 的实例
        IGetRequest request = retrofit.create(IGetRequest.class);

        //对 发送请求 进行封装
        Call<Translation> call = request.getCall();

        //步骤6:发送网络请求(异步)
        call.enqueue(new Callback<Translation>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<Translation> call, Response<Translation> response) {
                // 步骤7：处理返回的数据结果
                response.body().show();
                //String s = response.toString();
                //System.out.println(s);
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<Translation> call, Throwable throwable) {
                System.out.println("连接失败");
            }
        });
    }

}
