package com.allen.send_message.zone;

import com.allen.send_message.R;
import com.allen.send_message.base.BaseActivity;
import com.allen.send_message.bean.ZoneBean;
import com.allen.send_message.network.api.ApiService;
import com.allen.send_message.network.retrofit.CommonRequest;
import com.allen.send_message.utils.ToastUtils;

import rx.Observable;

/**
 * Created by allen on 2016/12/2.
 *
 * 大本营
 */

public class ZoneActivity extends BaseActivity{
    @Override
    protected int bindLayout() {
        return R.layout.activity_zone_list_layout;
    }

    @Override
    protected void afterViewCreated() {
        getZoneData();

    }

    private void getZoneData() {
        new CommonRequest<ZoneBean>(this){
            @Override
            protected Observable<ZoneBean> doBackground() {
                return createApi(ApiService.class).getZoneListData();
            }

            @Override
            protected void onResponse(ZoneBean result) {
                ToastUtils.show(result.getData().get(0).getName()+"\n"+result.getData().get(0).getAdmin().getName());
            }

            @Override
            protected void onError(Throwable e) {

            }
        }.send();
    }
}
