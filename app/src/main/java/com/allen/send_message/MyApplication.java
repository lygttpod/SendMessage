package com.allen.send_message;

import android.app.Application;
import android.content.Context;

import com.allen.send_message.bean.PoiItemsBean;
import com.allen.send_message.bean.ZoneBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by allen on 2016/12/2.
 */

public class MyApplication extends Application {

    private static Context mAppContext;

    private static String address;

    private static List<PoiItemsBean> poiItemsBeanList = new ArrayList<>();
    private static List<ZoneBean.DataBean> dataBeanList = new ArrayList<>();

    public static List<ZoneBean.DataBean> getDataBeanList() {
        return dataBeanList;
    }

    public static void setDataBeanList(List<ZoneBean.DataBean> dataBeanList) {
        MyApplication.dataBeanList = dataBeanList;
    }

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        MyApplication.address = address;
    }

    public static List<PoiItemsBean> getPoiItemsBeanList() {
        return poiItemsBeanList;
    }

    public static void setPoiItemsBeanList(List<PoiItemsBean> poiItemsBeanList) {
        MyApplication.poiItemsBeanList = poiItemsBeanList;
    }


    public static Context getAppContext() {
        return mAppContext;
    }

    public static void setAppContext(Context mAppContext) {
        MyApplication.mAppContext = mAppContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.mAppContext = getApplicationContext();

    }
}
