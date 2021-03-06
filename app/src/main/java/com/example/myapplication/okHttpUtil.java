package com.example.myapplication;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class okHttpUtil {
    static ExecutorService threadpoll = Executors.newFixedThreadPool(30);
    private static Map<String, List<Cookie>> cookieStore = new HashMap<>();
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .cookieJar(new CookieJar()
            {
                @Override
                public void saveFromResponse(@NonNull HttpUrl httpUrl, @NonNull List<Cookie> list)
                {
                    cookieStore.put(httpUrl.host(), list);
                }

                @Override
                public List<Cookie> loadForRequest(@NonNull HttpUrl httpUrl)
                {
                    List<Cookie> cookies = cookieStore.get(httpUrl.host());
                    return cookies == null ? new ArrayList<>() : cookies;
                }
            }).build();
    public static String getRequest(final String url) throws ExecutionException, InterruptedException {
        FutureTask futureTask = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Response response = okHttpClient.newCall(request).execute();
                if(response.isSuccessful()&&response.body()!=null){
                    return response.body().string();
                }else {
                    return null;
                }
            }
        });
        threadpoll.execute(futureTask);
        return futureTask.get().toString();
    }
    public static String postRequest(final String url, Map<String,String> map) throws ExecutionException, InterruptedException {
        FutureTask futureTask = new FutureTask(new Callable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public Object call() throws Exception {
                FormBody.Builder builder= new FormBody.Builder();
                map.forEach(builder::add);
                FormBody formBody = builder.build();
                builder.add("name","78");
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();
                Response response = okHttpClient.newCall(request).execute();
                if(response.isSuccessful()&&response.body()!=null){
                    return response.body().string();
                }else {
                    return null;
                }
            }
        });
        threadpoll.execute(futureTask);
        return futureTask.get().toString();
    }

}
