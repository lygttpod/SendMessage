package com.allen.send_message.photos;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.send_message.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by allen on 2016/12/2.
 * <p>
 * 选择照片
 */

public class SelectPhotosActivity extends Activity {


    @BindView(R.id.select_cancel_bt)
    Button selectCancelBt;
    @BindView(R.id.select_ok_bt)
    Button selectOkBt;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.select_photo_gv)
    GridView selectPhotoGv;
    @BindView(R.id.activity_main)
    RelativeLayout activityMain;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_select_photo);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

    }

    @OnClick({R.id.select_cancel_bt, R.id.select_ok_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_cancel_bt:
                break;
            case R.id.select_ok_bt:
                break;
        }
    }
}
