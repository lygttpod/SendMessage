package com.allen.send_message.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.allen.send_message.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.*;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by lenovo on 2016/12/3.
 */

public class SendMessageAdapter extends CommonAdapter<String> {
    private Context context;
    private List<String> datas;

    public SendMessageAdapter(Context context, List<String> datas) {
        super(context, R.layout.grid_item_photo, datas);
        this.context = context;
        this.datas=datas;
    }

    @Override
    protected void convert(ViewHolder holder, String s, final int position) {
        if(datas.size()>=9){
            Glide.with(context).load(s).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.mipmap.ic_launcher).crossFade(500)
                    .into((ImageView) holder.getView(R.id.iv));
            holder.setOnClickListener(R.id.close, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDatas().remove(position);
                    notifyDataSetChanged();
                }
            });
        }else{
            Glide.with(context).load(s).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.mipmap.ic_launcher).crossFade(500)
                    .into((ImageView) holder.getView(R.id.iv));
        }
    }
}
