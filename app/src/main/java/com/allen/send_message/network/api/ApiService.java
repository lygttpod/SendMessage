package com.allen.send_message.network.api;

import com.allen.send_message.bean.ZoneBean;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by allen on 2016/12/2.
 *
 */

public interface ApiService {
    @GET("zone/hot")
    Observable<ZoneBean> getZoneListData();
}
