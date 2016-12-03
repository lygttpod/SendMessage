package com.allen.send_message.zone;

import android.content.Context;

import com.allen.send_message.R;
import com.allen.send_message.bean.ZoneBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by Allen on 2016/12/3.
 *
 */

public class ZoneListAdapter extends CommonAdapter<ZoneBean.DataBean> {


    public ZoneListAdapter(Context context, List<ZoneBean.DataBean> datas) {
        super(context, R.layout.zone_list_item_layout, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ZoneBean.DataBean dataBean, int position) {
        holder.setText(R.id.zone_name_tv,dataBean.getName());

        if ("".equals(dataBean.getAdmin().getName())){
            holder.setVisible(R.id.zone_admin_name_tv, false);
        }else {
            holder.setVisible(R.id.zone_admin_name_tv, true);
            holder.setText(R.id.zone_admin_name_tv,dataBean.getAdmin().getName());

        }

        if (dataBean.isSelect()){
            holder.setVisible(R.id.isSelect_icon_iv,true);
        }else {
            holder.setVisible(R.id.isSelect_icon_iv,false);

        }
    }
}
