package com.allen.send_message.zone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.allen.send_message.MyApplication;
import com.allen.send_message.R;
import com.allen.send_message.base.ILoadingView;
import com.allen.send_message.bean.ZoneBean;
import com.allen.send_message.network.api.ApiService;
import com.allen.send_message.network.retrofit.CommonRequest;
import com.allen.send_message.utils.ToastUtils;
import com.allen.send_message.widget.DividerItemDecoration;
import com.allen.send_message.widget.LoadingDialog;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;

/**
 * Created by allen on 2016/12/2.
 * <p>
 * 大本营
 */

public class ZoneActivity extends AppCompatActivity implements ILoadingView {
    @BindView(R.id.zone_recycler_view)
    RecyclerView zoneRecyclerView;

    @BindView(R.id.ok_tv)
    TextView okTv;
    @BindView(R.id.cancel_tv)
    TextView cancelTv;
    @BindView(R.id.title_tv)
    TextView titleTv;

    private LoadingDialog mLoadingDialog;

    private List<ZoneBean.DataBean> dataBeanList = new ArrayList<>();

    private ZoneListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_list_layout);
        ButterKnife.bind(this);

        dataBeanList = MyApplication.getDataBeanList();


        initTopBar();
        initView();
        if (dataBeanList.size()<=0){
            getZoneData();
        }
    }

    private void initTopBar() {
        cancelTv.setText("取消");
        titleTv.setText("大本营");
    }


    private void initView() {

        adapter = new ZoneListAdapter(this, dataBeanList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        zoneRecyclerView.setLayoutManager(layoutManager);
        zoneRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        zoneRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                ZoneBean.DataBean dataBean_post = new ZoneBean.DataBean();
                dataBean_post.setId(dataBeanList.get(position).getId());
                dataBean_post.setName(dataBeanList.get(position).getName());
                //清除以前选择的位置标志
                for (ZoneBean.DataBean dataBean : dataBeanList) {
                    if (dataBean.isSelect()) {
                        dataBean.setSelect(false);
                    }
                }
                dataBeanList.get(position).setSelect(true);


                EventBus.getDefault().post(dataBean_post);

                finish();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }


    private void getZoneData() {
        new CommonRequest<ZoneBean>(this) {
            @Override
            protected Observable<ZoneBean> doBackground() {
                return createApi(ApiService.class).getZoneListData();
            }

            @Override
            protected void onResponse(ZoneBean result) {

                setData(result);


            }

            @Override
            protected void onError(Throwable e) {

            }

        }.isShowLoading(true).send();
    }

    private void setData(ZoneBean result) {
        if (result.getData().size() > 0) {

            for (int i = 0; i < result.getData().size(); i++) {
                ZoneBean.DataBean dataBean = new ZoneBean.DataBean();
                if (i==0){
                    dataBean.setName("不添加大本营");

                    ZoneBean.DataBean.AdminBean adminBean = new ZoneBean.DataBean.AdminBean();
                    adminBean.setName("");
                    dataBean.setAdmin(adminBean);

                }else {
                    dataBean.setName(result.getData().get(i).getName());
                    dataBean.setId(result.getData().get(i).getId());

                    ZoneBean.DataBean.AdminBean adminBean = new ZoneBean.DataBean.AdminBean();
                    adminBean.setName(result.getData().get(i).getAdmin().getName());

                    dataBean.setAdmin(adminBean);
                }

                dataBeanList.add(dataBean);
                Log.d("allen", "onResponse: " + dataBeanList.get(i).getName());
            }
            adapter.notifyDataSetChanged();
        } else {
            ToastUtils.show("暂未获取到数据");
        }

    }


    @Override
    public void showLoading() {
        if (mLoadingDialog == null){
            mLoadingDialog = new LoadingDialog(this);
        }
        mLoadingDialog.show();
    }

    @Override
    public void dismissLoading() {
        if (mLoadingDialog != null){
            mLoadingDialog.dismiss();
        }
    }

    @OnClick({R.id.cancel_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_tv:
                finish();
                break;
        }
    }
}
