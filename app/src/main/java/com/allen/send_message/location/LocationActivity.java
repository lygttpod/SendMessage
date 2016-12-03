package com.allen.send_message.location;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

import static android.widget.Toast.LENGTH_SHORT;

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
    @BindView(R.id.show_no_open_service_ll)
    LinearLayout showNoOpenServiceLl;

    private AMapLocationClient aMapLocationClient;
    private AMapLocationClientOption aMapLocationClientOption;

    private double mLatitude;
    private double mLongitude;

    private String address;
    private String mCityCode;
    private String mCityName;

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
                PoiItemsBean poiItemsBean= new PoiItemsBean();

                poiItemsBean.setTitle(poiItemsBeans.get(position).getTitle());
                poiItemsBean.setLatLonPoint(poiItemsBeans.get(position).getLatLonPoint());
                //清除以前选择的位置标志
                for (PoiItemsBean p : poiItemsBeans) {
                    if (p.isSelect()) {
                        p.setSelect(false);
                    }
                }
                poiItemsBeans.get(position).setSelect(true);

                EventBus.getDefault().post(poiItemsBean);

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


    @Override
    protected void onResume() {
        super.onResume();
        PermissionGen
                .with(this)
                .addRequestCode(100)
                .permissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS)
                .request();
    }

    /**
     * 检索附近位置
     *
     * @param latitude
     * @param longitude
     * @param cityCode
     */
    private void poi_Search(double latitude, double longitude, String cityCode) {
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
                mCityName = aMapLocation.getCity();
                if (address == null) {
                    address = aMapLocation.getAddress();
                    MyApplication.setAddress(aMapLocation.getAddress());
                    poi_Search(mLatitude, mLongitude, mCityCode);

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
     *
     * @param poiResult
     */
    private void doPoiResult(PoiResult poiResult) {

        if (poiResult != null) {
            List<PoiItem> poiItems = poiResult.getPois();
            poiItemsBeans.clear();
            for (int i = 0; i < poiItems.size(); i++) {
                if (i == 0) {
                    PoiItemsBean poiItemBean = new PoiItemsBean("不显示位置", "", "");
                    poiItemsBeans.add(poiItemBean);
                } else if (i==1){
                    PoiItemsBean poiItemBean = new PoiItemsBean(mCityName, "", mLatitude + "," + mLongitude);
                    poiItemsBeans.add(poiItemBean);
                }
                else {
                    PoiItemsBean poiItemBean = new PoiItemsBean(poiItems.get(i).getTitle(), poiItems.get(i).getSnippet(), poiItems.get(i).getLatLonPoint().toString());
                    poiItemsBeans.add(poiItemBean);
                }

            }
            MyApplication.setPoiItemsBeanList(poiItemsBeans);
            Log.d("allen", "onPoiSearched----------: " + poiItemsBeans.toString());

            adapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.cancel_tv, R.id.title_tv, R.id.ok_tv, R.id.search_location_ll, R.id.open_setting_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_tv:
                finish();
                break;
            case R.id.title_tv:
                break;
            case R.id.open_setting_btn:
                openSetting();
                break;
            case R.id.search_location_ll:
                Intent i = new Intent();
                i.putExtra("latitude", mLatitude);
                i.putExtra("longitude", mLongitude);
                i.putExtra("cityCode", mCityCode);
                i.setClass(this, SearchLocationActivity.class);
                startActivity(i);
                break;
        }
    }

    /**
     * 开启设置页面位置服务
     */
    private void openSetting() {

        Intent mIntent = new Intent(Settings.ACTION_SETTINGS);

        startActivity(mIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        aMapLocationClient.stopLocation();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = 100)
    public void startLocation() {

        showNoOpenServiceLl.setVisibility(View.GONE);
        initMap();
    }

    @PermissionFail(requestCode = 100)
    public void doFailSomething() {
        showNoOpenServiceLl.setVisibility(View.VISIBLE);
    }

}
