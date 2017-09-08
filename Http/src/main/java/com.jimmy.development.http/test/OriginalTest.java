package com.jimmy.development.http.test;

import android.content.Context;

import com.jimmy.development.logger.MLog;
import com.jimmy.development.tools.ThreadUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;

/**
 * Created by jinguochong on 17-4-18.
 * <p>
 * ## get
 * async
 * sync
 * <p>
 * ## post
 * form
 * key-value
 * json
 * stream
 * file
 * String
 * multi-part
 * cache
 */
public class OriginalTest {

    private Context context;
    private OkHttpClient okHttpClient;

    public OriginalTest(Context context) {
        this.context = context;
        okHttpClient = new OkHttpClient();
        asyncGet();
        syncGet();
    }

    /**
     * 异步get请求
     */
    public void asyncGet() {
        Request request = new Request.Builder()
                .url("com.jimmy.development.http://publicobject.com/helloworld.txt")
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    ResponseBody responseBody = response.body();
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        MLog.e("jgc", "asyncGet:" + responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }
                    MLog.e("jgc", "asyncGet:" + responseBody.string());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 同步Get
     */
    public void syncGet() {
        final Request request = new Request.Builder()
                .url("https://raw.github.com/square/okhttp/master/README.md")
                .get()
                .build();
        ThreadUtils.doInBackground(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = okHttpClient.newCall(request).execute();
                    MLog.e("jgc", "syncGet:" + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");

    public void postFile() {

        File file = new File("README.md");

        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);

            MLog.e("jgc", "postFile:" + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void postForm() {
        RequestBody formBody = new FormBody.Builder()
                .add("search", "Jurassic Park")
                .build();
        Request request = new Request.Builder()
                .url("https://en.wikipedia.org/w/index.php")
                .post(formBody)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            MLog.e("jgc", "postForm:" + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final String IMGUR_CLIENT_ID = "9199fdef135c122";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    public void postMultiPart() {
        // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", "Square Logo")
                .addFormDataPart("image", "logo-square.png",
                        RequestBody.create(MEDIA_TYPE_PNG, new File("website/static/logo-square.png")))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                .url("https://api.imgur.com/3/image")
                .post(requestBody)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            MLog.e("jgc", "postMultiPart:" + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void postStream() {
        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MEDIA_TYPE_MARKDOWN;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.writeUtf8("Numbers\n");
                sink.writeUtf8("-------\n");
                for (int i = 2; i <= 997; i++) {
                    sink.writeUtf8(String.format(" * %s = %s\n", i, factor(i)));
                }
            }

            private String factor(int n) {
                for (int i = 2; i < n; i++) {
                    int x = n / i;
                    if (x * i == n) return factor(x) + " × " + i;
                }
                return Integer.toString(n);
            }
        };

        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(requestBody)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            MLog.e("jgc", "postStream:" + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void postString() {
        String postBody = ""
                + "Releases\n"
                + "--------\n"
                + "\n"
                + " * _1.0_ May 6, 2013\n"
                + " * _1.1_ June 15, 2013\n"
                + " * _1.2_ August 11, 2013\n";

        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, postBody))
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            MLog.e("jgc", "postString:" + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //------------------------------------------------------------
    public void progress() {
    }

    public void addHeaders() {
    }

    public void cacheResponse() {
    }

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public void cancelRequest() {

        Request request = new Request.Builder()
                .url("com.jimmy.development.http://httpbin.org/delay/2") // This URL is served with a 2 second delay.
                .build();

        final long startNanos = System.nanoTime();
        final Call call = okHttpClient.newCall(request);

        // Schedule a job to cancel the call in 1 second.
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.printf("%.2f Canceling call.%n", (System.nanoTime() - startNanos) / 1e9f);
                call.cancel();
                System.out.printf("%.2f Canceled call.%n", (System.nanoTime() - startNanos) / 1e9f);
            }
        }, 1, TimeUnit.SECONDS);

        System.out.printf("%.2f Executing call.%n", (System.nanoTime() - startNanos) / 1e9f);
        try {
            Response response = call.execute();
            System.out.printf("%.2f Call was expected to fail, but completed: %s%n",
                    (System.nanoTime() - startNanos) / 1e9f, response);
        } catch (IOException e) {
            System.out.printf("%.2f Call failed as expected: %s%n",
                    (System.nanoTime() - startNanos) / 1e9f, e);
        }
    }


}
