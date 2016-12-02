package com.allen.send_message.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.allen.send_message.R;
import com.allen.send_message.utils.ImageItem;
import com.allen.send_message.utils.SelectPhotosEvent;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.allen.send_message.R.id.iv;

/**
 * 加载图片的适配器
 * Created by Sun on 2016/12/2.
 */

public class PictureAdapter extends BaseAdapter implements View.OnClickListener{
    private Context context;
    private List<ImageItem> images;
    private static int MAX_NUM = 6;
   private  ImageItem item;
    /**
     * 已选择的图片
     */
    private ArrayList<String> selectedPicture = new ArrayList<>();

    public PictureAdapter(Context context, List<ImageItem> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.grid_item_picture, null);
            holder = new ViewHolder();
            holder.iv = (ImageView) convertView.findViewById(iv);
            holder.checkBox = (Button) convertView.findViewById(R.id.check);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            holder.iv.setImageResource(R.mipmap.pickphotos_to_camera_normal);
            holder.checkBox.setVisibility(View.INVISIBLE);
        } else {
            position = position - 1;
            holder.checkBox.setVisibility(View.VISIBLE);
            item = images.get(position);
            String path="file://"+item.getPath();
            Glide.with(context).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.mipmap.ic_launcher).crossFade(500).into(holder.iv);
            boolean isSelected = selectedPicture.contains(item.getPath());
            holder.checkBox.setOnClickListener(this);
            holder.checkBox.setSelected(isSelected);
        }
        return convertView;
    }
    @Override
    public void onClick(View v) {
            if (!v.isSelected() && selectedPicture.size() + 1 > MAX_NUM) {
                Toast.makeText(context, "最多选择" + MAX_NUM + "张", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedPicture.contains(item.getPath())) {
                selectedPicture.remove(item.getPath());
            } else {
                selectedPicture.add(item.getPath());
            }
            SelectPhotosEvent selectphotosEvent=new SelectPhotosEvent();
            selectphotosEvent.setSize(selectedPicture.size());
            EventBus.getDefault().post(selectphotosEvent);
            v.setSelected(selectedPicture.contains(item.getPath()));
    }
}

class ViewHolder {
    ImageView iv;
    Button checkBox;
}