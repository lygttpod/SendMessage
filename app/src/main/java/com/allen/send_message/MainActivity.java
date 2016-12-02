package com.allen.send_message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.allen.send_message.location.LocationActivity;
import com.allen.send_message.photos.SelectPhotosActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.send_message_btn, R.id.location_btn, R.id.photos_btn, R.id.zone_btn})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.send_message_btn:
                break;
            case R.id.location_btn:
                intent.setClass(this, LocationActivity.class);
                startActivity(intent);
                break;
            case R.id.photos_btn:
                intent.setClass(this, SelectPhotosActivity.class);
                startActivity(intent);
                break;
            case R.id.zone_btn:
                break;
        }
    }
}
