package com.allen.send_message.location;

import android.content.Context;

import com.allen.send_message.R;
import com.allen.send_message.bean.PoiItemsBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by allen on 2016/12/2.
 * 位置列表adapter
 */

public class LocationAdapter extends CommonAdapter<PoiItemsBean> {

    public LocationAdapter(Context context, List<PoiItemsBean> datas) {
        super(context, R.layout.poi_item_layout, datas);
    }

    @Override
    protected void convert(ViewHolder holder, PoiItemsBean poiItemsBean, int position) {
        holder.setText(R.id.location_title_tv, poiItemsBean.getTitle());
        if ("".equals(poiItemsBean.getSnippet())) {
            holder.setVisible(R.id.location_snippet_tv, false);
        } else {
            holder.setVisible(R.id.location_snippet_tv, true);

            holder.setText(R.id.location_snippet_tv, poiItemsBean.getSnippet());

        }
        if (poiItemsBean.isSelect()) {
            holder.setVisible(R.id.isSelect_icon_iv, true);
        } else {
            holder.setVisible(R.id.isSelect_icon_iv, false);

        }
    }
}
