package com.allen.send_message.network.retrofit;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Allen on 2016/3/27.
 */
public class RequestInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {

        String token = "v5KkY2JS5g36T7M4N4ftFkEq506a749c8fb1038e";

        Request oldRequest = chain.request();
        // 添加新的参数
        HttpUrl.Builder authorizedUrlBuilder = oldRequest.url()
                .newBuilder()
                .scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host());
//                .addQueryParameter("mApiKey", mApiKey);
        // 重新组成新的请求
        Request newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(authorizedUrlBuilder.build())
                .addHeader("session_token", token)
                .addHeader("Content-type", "application/json")
//                .addHeader("User-Agent", System.getProperty("http.agent"))
                .addHeader("User-Agent", "a/1.0.0/4.2/meizu351/wifi")
                .build();
        Response response = null;
        try {
            response = chain.proceed(newRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


}
