package com.allen.send_message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.send_message.adapter.SendMessageAdapter;
import com.allen.send_message.base.ILoadingView;
import com.allen.send_message.bean.PoiItemsBean;
import com.allen.send_message.bean.SendMessageBean;
import com.allen.send_message.bean.SendPhotoBean;
import com.allen.send_message.bean.UpPhotoBean;
import com.allen.send_message.bean.ZoneBean;
import com.allen.send_message.location.LocationActivity;
import com.allen.send_message.network.api.ApiService;
import com.allen.send_message.network.retrofit.CommonRequest;
import com.allen.send_message.photos.SelectPhotosActivity;
import com.allen.send_message.utils.ToastUtils;
import com.allen.send_message.widget.LoadingDialog;
import com.allen.send_message.zone.ZoneActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Response;
import rx.Observable;

public class SendMessageActivity extends AppCompatActivity implements ILoadingView {

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
    private String content = "";
    private String id = "";
    private String latLonPoint = "";
    private String addr = "";
    private SendMessageAdapter adapter;
    private List<String> selectedPicture = new ArrayList<>();
    private LoadingDialog mLoadingDialog;

    private StringBuffer filePaths = new StringBuffer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        selectedPicture.clear();
        init();
    }

    private void init() {
        adapter = new SendMessageAdapter(this, selectedPicture);
        GridLayoutManager layout = new GridLayoutManager(this, 3);
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
        intent = new Intent();
        switch (view.getId()) {
            case R.id.select_cancel:
//                this.finish();
                break;
            case R.id.select_ok:
                if ("".equals(id)) {
                    ToastUtils.show("请选择大本营");
                    return;
                }
                content = sendContent.getText().toString().trim();
                String image = "";
                if (selectedPicture.size()>0){
                    for (int i = 0; i < selectedPicture.size(); i++) {
                        upPhoto(content, selectedPicture.get(i),i);
                    }
                }else {
                    sendMessage(content, id, filePaths, addr, latLonPoint);

                }

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
                intent.setClass(this, SelectPhotosActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void sendMessage(final String content, final String zone_id, final StringBuffer image, final String addr, final String xy) {
        new CommonRequest<SendMessageBean>(this) {
            @Override
            protected Observable<SendMessageBean> doBackground() {
                addParam("content", content);
                addParam("zone_id", zone_id);
                addParam("images", image);
                addParam("addr", addr);
                addParam("xy", xy);
                addParam("session_token", "r2QRsuS9VaeSvJKm4ryGNwqj0984f12154af8ee7");
                return createApi(ApiService.class).sendMessage(getParams());
            }

            @Override
            protected void onResponse(SendMessageBean result) {
                //就是不知道code=70000是什么意思？是成功还是怎么了
                int code = result.getError_code();
                ToastUtils.show("返回结果：code=" + code);
            }

            @Override
            protected void onError(Throwable e) {
                ToastUtils.show("返回结果：onError=" + e.getMessage());

            }

        }.isShowLoading(true).send();
    }

    private void upPhoto(final String content, final String upfilePath,final int i) {
        new CommonRequest<Response<UpPhotoBean>>(this) {
            @Override
            protected Observable<Response<UpPhotoBean>> doBackground() {
                File file = new File(upfilePath);

                Map<String, RequestBody> maps = new HashMap<>();

                // 创建 RequestBody，用于封装构建RequestBody
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("image/png"), file);

                RequestBody tBody =
                        RequestBody.create(
                                MediaType.parse("text/form-data"), "explore");

                RequestBody sessionBody =
                        RequestBody.create(
                                MediaType.parse("text/form-data"), "r2QRsuS9VaeSvJKm4ryGNwqj0984f12154af8ee7");

                maps.put("file\"; filename=\"" + file.getName() + "", requestFile);
                maps.put("t", tBody);
                maps.put("session_token", sessionBody);

                return createApi1(ApiService.class).upload(maps);
            }

            @Override
            protected void onResponse(Response<UpPhotoBean> result) {
                String filePath = result.body().getPath();
                if (filePaths.length()==0){
                    filePaths.append(filePath);
                }else {
                    filePaths.append(";"+filePath);
                }

                Log.e("发送图片成功", filePath);

                if (i==selectedPicture.size()-1){
                    sendMessage(content, id, filePaths, addr, latLonPoint);
                }
            }

            @Override
            protected void onError(Throwable e) {
                Log.d("allen", "onError: " + e.getMessage());
            }

        }.isShowLoading(true).send();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getZoneName(ZoneBean.DataBean event) {
        id = event.getId();
        String name = event.getName();
        if ("".equals(id)||null==id){
            strongholdTv.setText("选择一个大本营");
            closeStronghold.setVisibility(View.GONE);
        }else {
            strongholdTv.setText(name);
            closeStronghold.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getPosition(PoiItemsBean event) {
        latLonPoint = event.getLatLonPoint();
        addr = event.getTitle();

        if ("".equals(latLonPoint)||null==latLonPoint){
            seatTv.setText("位置");
            closeSeat.setVisibility(View.GONE);
        }else {
            seatTv.setText(addr);
            closeSeat.setVisibility(View.VISIBLE);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getPhotoPosition(SendPhotoBean data) {
        for (int i = 0; i < data.getData().size(); i++) {
            if (selectedPicture.size()<9){
                selectedPicture.add(data.getData().get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        mLoadingDialog.show();
    }

    @Override
    public void dismissLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }

    }

    private File getFile(List<String> filePath) {
        File file = new File(filePath.get(0));
        return file;
    }
}
