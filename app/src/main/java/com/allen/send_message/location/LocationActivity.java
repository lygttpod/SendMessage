package com.allen.send_message.location;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.allen.send_message.R;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * Created by allen on 2016/12/2.
 *
 * 定位
 */

public class LocationActivity extends AppCompatActivity implements AMapLocationListener {

    private AMapLocationClient aMapLocationClient;
    private AMapLocationClientOption aMapLocationClientOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        initMap();
    }

    private void initMap() {
        aMapLocationClient = new AMapLocationClient(this);

        aMapLocationClientOption = new AMapLocationClientOption();

        aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        aMapLocationClientOption.setInterval(10000);

        aMapLocationClient.setLocationOption(aMapLocationClientOption);

        aMapLocationClient.startLocation();

        aMapLocationClient.setLocationListener(this);
    }

    /**
     * @param aMapLocation
     * 获取当前位置回调方法
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

    }
}
