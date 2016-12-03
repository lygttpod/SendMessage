package com.allen.send_message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.send_message.adapter.SendMessageAdapter;
import com.allen.send_message.bean.PoiItemsBean;
import com.allen.send_message.bean.SendPhotoBean;
import com.allen.send_message.bean.ZoneBean;
import com.allen.send_message.location.LocationActivity;
import com.allen.send_message.location.LocationAdapter;
import com.allen.send_message.photos.SelectPhotosActivity1;
import com.allen.send_message.widget.DividerItemDecoration;
import com.allen.send_message.zone.ZoneActivity;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendMessageActivity extends AppCompatActivity {

    @BindView(R.id.select_cancel)
    Button selectCancel;
    @BindView(R.id.select_ok)
    Button selectOk;
    @BindView(R.id.send_content)
    EditText sendContent;
    @BindView(R.id.select_photo_gv)
    RecyclerView selectPhoto;
    @BindView(R.id.send_message_seat_tv)
    TextView seatTv;
    @BindView(R.id.send_message_closeseat)
    ImageView closeSeat;
    @BindView(R.id.send_message_seat_rl)
    RelativeLayout seatRl;
    @BindView(R.id.send_message_stronghold_tv)
    TextView strongholdTv;
    @BindView(R.id.send_message_closestronghold)
    ImageView closeStronghold;
    @BindView(R.id.send_message_stronghold_rl)
    RelativeLayout strongholdRl;
    @BindView(R.id.send_message_addphoto)
    ImageView addphoto;
    private Intent intent;
    private String id;
    private String LatLonPoint;
    private SendMessageAdapter adapter;
    private List<String> selectedPicture = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        init();
    }

    private void init() {
        adapter = new SendMessageAdapter(this, selectedPicture);
        GridLayoutManager layout= new GridLayoutManager(this,3);
        selectPhoto.setLayoutManager(layout);
        selectPhoto.setAdapter(adapter);
    }

    @OnClick({R.id.select_cancel,
            R.id.select_ok,
            R.id.send_message_closeseat,
            R.id.send_message_closestronghold,
            R.id.send_message_seat_rl,
            R.id.send_message_stronghold_rl,
            R.id.send_message_addphoto})
    public void onClick(View view) {
        intent=new Intent();
        switch (view.getId()) {
            case R.id.select_cancel:
//                this.finish();
                break;
            case R.id.select_ok:

                break;
            case R.id.send_message_closeseat:
                seatTv.setText("位置");
                closeSeat.setVisibility(View.GONE);
                break;
            //选择位置
            case R.id.send_message_seat_rl:
                intent.setClass(this, LocationActivity.class);
                startActivity(intent);
                break;
            case R.id.send_message_closestronghold:
                strongholdTv.setText("选择一个大本营");
                closeStronghold.setVisibility(View.GONE);
                break;
            //选择大本营
            case R.id.send_message_stronghold_rl:
                intent.setClass(this, ZoneActivity.class);
                startActivity(intent);
                break;
            case R.id.send_message_addphoto:
                intent.setClass(this, SelectPhotosActivity1.class);
                startActivity(intent);
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getZoneName(ZoneBean.DataBean event) {
         id = event.getId();
        String name = event.getName();
        strongholdTv.setText(name);
        closeStronghold.setVisibility(View.VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getPosition(PoiItemsBean event) {
        LatLonPoint = event.getLatLonPoint();
        String title = event.getTitle();
        seatTv.setText(title);
        closeStronghold.setVisibility(View.VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getPhotoPosition(SendPhotoBean data) {
        for (int i= 0;i<data.getData().size();i++){
            selectedPicture.add(data.getData().get(i));
        }
        adapter.notifyDataSetChanged();
    }
}
