package com.allen.send_message.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.allen.send_message.R;
import com.allen.send_message.utils.ImageItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HoomSun on 2016/12/2.
 */

public class PictureAdapter extends BaseAdapter {
   private Context context;
    private List <ImageItem> images;
    private ImageLoader loader;
    private DisplayImageOptions options;
    private ContentResolver mContentResolver;
    private static int MAX_NUM = 6;
    /**
     * 已选择的图片
     */
    private ArrayList<String> selectedPicture = new ArrayList<String>();

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
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.grid_item_picture, null);
            holder = new ViewHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.iv);
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
            final ImageItem item = images.get(position);
            loader.displayImage("file://" + item.path, holder.iv, options);
            boolean isSelected = selectedPicture.contains(item.path);
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!v.isSelected() && selectedPicture.size() + 1 > MAX_NUM) {
                        Toast.makeText(context, "最多选择" + MAX_NUM + "张", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (selectedPicture.contains(item.path)) {
                        selectedPicture.remove(item.path);
                    } else {
                        selectedPicture.add(item.path);
                    }
//                    btn_ok.setEnabled(selectedPicture.size()>0);
//                    btn_ok.setText("完成" + selectedPicture.size() + "/" + MAX_NUM);
                    v.setSelected(selectedPicture.contains(item.path));
                }
            });
            holder.checkBox.setSelected(isSelected);
        }
        return convertView;
    }
}

class ViewHolder {
    ImageView iv;
    Button checkBox;
}