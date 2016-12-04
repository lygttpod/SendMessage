package com.allen.send_message.network.api;

import com.allen.send_message.bean.SendMessageBean;
import com.allen.send_message.bean.UpPhotoBean;
import com.allen.send_message.bean.ZoneBean;

import java.util.Map;
import java.util.Objects;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by allen on 2016/12/2.
 *
 */

public interface ApiService {
    @GET("v1/zone/hot")
    Observable<ZoneBean> getZoneListData();

    @POST("attachment")
    Observable<UpPhotoBean> upPhoto(@Body  Map string);

    @POST("v1/message/message")
    Observable<SendMessageBean> sendMessage(@Body  Map string);
}
