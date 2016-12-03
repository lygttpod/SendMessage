package com.allen.send_message.location;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.send_message.R;
import com.allen.send_message.bean.PoiItemsBean;
import com.allen.send_message.widget.DividerItemDecoration;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by allen on 2016/12/2.
 * 搜索附近位置
 */
public class SearchLocationActivity extends AppCompatActivity implements PoiSearch.OnPoiSearchListener {

    @BindView(R.id.back_iv)
    ImageView backIv;
    @BindView(R.id.cancel_tv)
    TextView cancelTv;
    @BindView(R.id.search_recycler_view)
    RecyclerView searchRecyclerView;
    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.show_no_data_ll)
    LinearLayout noDataTv;


    private double mLatitude;
    private double mLongitude;

    private String mCityCode;

    private List<PoiItemsBean> poiItemsBeans = new ArrayList<>();

    private LocationAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_layout);
        ButterKnife.bind(this);

        getExtraData();
        setSearchView();
        initRecyclerView();
    }

    /**
     * 获取外部传入的数据
     */
    private void getExtraData() {
        mLatitude = getIntent().getDoubleExtra("latitude", 0);
        mLongitude = getIntent().getDoubleExtra("longitude", 0);
        mCityCode = getIntent().getStringExtra("cityCode");
    }

    /**
     * 初始化SearchView
     */
    private void setSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                poi_Search(query, mLatitude, mLongitude, mCityCode);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        cancelSearchViewLine();

    }

    /**
     * 取消searchView底部默认的横线
     */
    private void cancelSearchViewLine() {
        Class<?> c = searchView.getClass();
        try {
            Field f = c.getDeclaredField("mSearchPlate");//通过反射，获得类对象的一个属性对象
            f.setAccessible(true);//设置此私有属性是可访问的
            View v = (View) f.get(searchView);//获得属性的值
            v.setBackgroundResource(R.drawable.search_gray_bg);//设置此view的背景
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {

        adapter = new LocationAdapter(this, poiItemsBeans);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        searchRecyclerView.setLayoutManager(layoutManager);
        searchRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        searchRecyclerView.setAdapter(adapter);

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
     * 检索附近位置
     *
     * @param str
     * @param latitude
     * @param longitude
     * @param cityCode
     */
    private void poi_Search(String str, double latitude, double longitude, String cityCode) {
        PoiSearch.Query mPoiSearchQuery = new PoiSearch.Query(str, "", cityCode);
        mPoiSearchQuery.requireSubPois(true);   //true 搜索结果包含POI父子关系; false
        mPoiSearchQuery.setPageSize(50);
        mPoiSearchQuery.setPageNum(0);
        PoiSearch poiSearch = new PoiSearch(this, mPoiSearchQuery);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latitude, longitude), 5000));
        poiSearch.searchPOIAsyn();
    }


    @OnClick({R.id.back_iv, R.id.cancel_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.cancel_tv:
                finish();
                break;
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
     * 处理附近数据
     *
     * @param poiResult
     */
    private void doPoiResult(PoiResult poiResult) {
        if (poiResult != null) {
            List<PoiItem> poiItems = poiResult.getPois();
            if (poiItems.size() > 0) {
                noDataTv.setVisibility(View.GONE);
                poiItemsBeans.clear();
                for (PoiItem poiItem : poiItems) {
                    PoiItemsBean poiItemBean = new PoiItemsBean(poiItem.getTitle(), poiItem.getSnippet(), poiItem.getLatLonPoint().toString());
                    poiItemsBeans.add(poiItemBean);
                }
                Log.d("allen", "onPoiSearched----------: " + poiItemsBeans.toString());
                adapter.notifyDataSetChanged();
            } else {
                noDataTv.setVisibility(View.VISIBLE);
            }

        }
    }
}
