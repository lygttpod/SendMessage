package com.allen.send_message;

import android.app.Application;

import com.allen.send_message.bean.PoiItemsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by allen on 2016/12/2.
 */

public class MyApplication extends Application {


    private static String address;

    private static List<PoiItemsBean> poiItemsBeanList = new ArrayList<>();

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




    @Override
    public void onCreate() {
        super.onCreate();
    }
}
