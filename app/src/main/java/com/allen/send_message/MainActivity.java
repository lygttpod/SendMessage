package com.allen.send_message;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.allen.send_message.location.LocationActivity;
import com.allen.send_message.photos.SelectPhotosActivity1;
import com.allen.send_message.zone.ZoneActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }
    /**
     * 开启设置页面位置服务
     */
    private void openSetting() {
        Intent mIntent = new Intent(Settings.ACTION_SETTINGS);
        startActivity(mIntent);
    }
    @OnClick({R.id.send_message_btn, R.id.location_btn, R.id.photos_btn, R.id.zone_btn})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.send_message_btn:
//                openSetting();
                intent.setClass(this, SendMessageActivity.class);
                startActivity(intent);
                break;
            case R.id.location_btn:
                intent.setClass(this, LocationActivity.class);
                startActivity(intent);
                break;
            case R.id.photos_btn:
                intent.setClass(this, SelectPhotosActivity1.class);
                startActivity(intent);
                break;
            case R.id.zone_btn:
                intent.setClass(this, ZoneActivity.class);
                startActivity(intent);
                break;
        }
    }
}
