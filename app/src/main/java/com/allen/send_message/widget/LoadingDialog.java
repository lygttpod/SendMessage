package com.allen.send_message.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.allen.send_message.R;


/**
 * 自定义透明的dialog
 */
public class LoadingDialog extends Dialog {

    private ImageView contentImg;

    private Animation loadingAnim;

    private Context context;

    public LoadingDialog(Context context) {
        super(context, R.style.CustomDialog);
        this.context = context;
        initView();
    }

    @Override
    public void show() {
        if (!isShowing()) {
            super.show();
            startAnim();
        }
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            stopAnim();
            super.dismiss();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            dismiss();
        }
        return true;
    }

    private void initView() {
        setContentView(R.layout.loading_layout);
        contentImg = (ImageView) findViewById(R.id.ring_img);
        initAnimObjects();
        setCanceledOnTouchOutside(true);
        setCancelable(false);
    }

    private void initAnimObjects() {
        //loadingAnim = AnimationUtils.loadAnimation(context, R.anim.loading_ring);
        loadingAnim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        loadingAnim.setDuration(900);
        loadingAnim.setRepeatCount(Animation.INFINITE);
        loadingAnim.setRepeatMode(Animation.RESTART);
        loadingAnim.setInterpolator(new LinearInterpolator());
    }

    private void startAnim() {
        contentImg.startAnimation(loadingAnim);
    }

    private void stopAnim() {
        loadingAnim.cancel();
        //contentImg.clearAnimation();
    }
}