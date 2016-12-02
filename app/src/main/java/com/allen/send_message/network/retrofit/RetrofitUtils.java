package com.allen.send_message.network.retrofit;

import com.allen.send_message.network.api.ApiAddress;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by renyu on 2016/3/21.
 */
class RetrofitUtils {

    private static RetrofitUtils instance;

    //okhttpBuilder对象
    private static OkHttpClient.Builder okhttpBuilder;
    //请求头
    private static RequestInterceptor requestInterceptor;

    //RetrofitBuilder对象
    private Retrofit.Builder retrofitBuilder;

    private RetrofitUtils() {
        retrofitBuilder = new Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        requestInterceptor = new RequestInterceptor();
    }

    public synchronized static RetrofitUtils getInstance() {
        if (instance == null) {
            instance = new RetrofitUtils();
        }
        okhttpBuilder = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS);
        okhttpBuilder.addInterceptor(requestInterceptor);
        return instance;
    }


    /**
     * 添加额外的拦截器
     * @param
     * @return
     */

    public Retrofit getRetrofit(String baseUrl) {
        return retrofitBuilder.addConverterFactory(GsonConverterFactory.create()).client(okhttpBuilder.build()).baseUrl(baseUrl).build();
    }

    public Retrofit getRetrofit() {
        return retrofitBuilder.addConverterFactory(GsonConverterFactory.create()).client(okhttpBuilder.build()).baseUrl(ApiAddress.BaseUrl).build();
    }
}
