package com.allen.send_message.location;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.send_message.MyApplication;
import com.allen.send_message.R;
import com.allen.send_message.bean.PoiItemsBean;
import com.allen.send_message.widget.DividerItemDecoration;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by allen on 2016/12/2.
 * <p>
 * 定位
 */

public class LocationActivity extends AppCompatActivity implements AMapLocationListener, PoiSearch.OnPoiSearchListener {

    @BindView(R.id.cancel_tv)
    TextView cancelTv;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.ok_tv)
    TextView okTv;
    @BindView(R.id.location_recycler_view)
    RecyclerView locationRecyclerView;

    private AMapLocationClient aMapLocationClient;
    private AMapLocationClientOption aMapLocationClientOption;

    private double mLatitude;
    private double mLongitude;

    private String address;
    private String mCityCode;

    private List<PoiItemsBean> poiItemsBeans = new ArrayList<>();


    private LocationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);

        address = MyApplication.getAddress();
        poiItemsBeans = MyApplication.getPoiItemsBeanList();

        initView();
        initMap();
    }

    private void initView() {

        adapter = new LocationAdapter(this, poiItemsBeans);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        locationRecyclerView.setLayoutManager(layoutManager);
        locationRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        locationRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //清除以前选择的位置标志
                for (PoiItemsBean p : poiItemsBeans) {
                    if (p.isSelect()) {
                        p.setSelect(false);
                    }
                }
                poiItemsBeans.get(position).setSelect(true);
                finish();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    /**
     * 初始化位置服务信息
     */
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
     * 检索附近位置
     *
     * @param latitude
     * @param longitude
     * @param cityCode
     */
    private void poi_Search( double latitude, double longitude, String cityCode) {
        PoiSearch.Query mPoiSearchQuery = new PoiSearch.Query("", "", cityCode);
        mPoiSearchQuery.requireSubPois(true);   //true 搜索结果包含POI父子关系; false
        mPoiSearchQuery.setPageSize(50);
        mPoiSearchQuery.setPageNum(0);
        PoiSearch poiSearch = new PoiSearch(this, mPoiSearchQuery);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latitude, longitude), 5000));
        poiSearch.searchPOIAsyn();
    }

    /**
     * @param aMapLocation 获取当前位置回调方法
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                mLatitude = aMapLocation.getLatitude();//纬度
                mLongitude = aMapLocation.getLongitude();//经度
                mCityCode = aMapLocation.getCityCode();
                if (address == null) {
                    MyApplication.setAddress(aMapLocation.getAddress());
                    poi_Search( mLatitude, mLongitude, mCityCode);

                } else {
                    if (!address.equals(aMapLocation.getAddress())) {
                        MyApplication.setAddress(aMapLocation.getAddress());
                        poi_Search(mLatitude, mLongitude, mCityCode);
                    }
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("allen", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            doPoiResult(poiResult);
        } else {
            Toast.makeText(this, rCode, Toast.LENGTH_LONG).show();
        }
    }



    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    /**
     * 处理附近位置列表数据
     * @param poiResult
     */
    private void doPoiResult(PoiResult poiResult) {

        if (poiResult != null) {
            List<PoiItem> poiItems = poiResult.getPois();
            poiItemsBeans.clear();
            for (PoiItem poiItem : poiItems) {
                PoiItemsBean poiItemBean = new PoiItemsBean(poiItem.getTitle(), poiItem.getSnippet(), poiItem.getLatLonPoint().toString());
                poiItemsBeans.add(poiItemBean);
            }
            MyApplication.setPoiItemsBeanList(poiItemsBeans);
            Log.d("allen", "onPoiSearched----------: " + poiItemsBeans.toString());

            adapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.cancel_tv, R.id.title_tv, R.id.ok_tv,R.id.search_location_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_tv:
                finish();
                break;
            case R.id.title_tv:
                break;
            case R.id.search_location_ll:
                Intent i = new Intent();
                i.putExtra("latitude",mLatitude);
                i.putExtra("longitude",mLongitude);
                i.putExtra("cityCode",mCityCode);
                i.setClass(this,SearchLocationActivity.class);
                startActivity(i);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        aMapLocationClient.stopLocation();
    }

}
