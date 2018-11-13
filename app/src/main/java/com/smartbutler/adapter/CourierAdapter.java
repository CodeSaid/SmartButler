package com.smartbutler.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartbutler.R;
import com.smartbutler.entity.CourierData;

import java.util.List;

/**
 * 快递查询adapter
 */

public class CourierAdapter extends BaseAdapter {

    private Context mContext;
    private List<CourierData> mList;
    private CourierData mCourierData;

    // 布局加载器
    private LayoutInflater mLayoutInflater;

    public CourierAdapter(Context context, List<CourierData> mList) {
        mContext = context;
        this.mList = mList;
        //获取系统服务
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        // 第一次加载
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.layout_courier_item, null);

            viewHolder.tv_datetime = convertView.findViewById(R.id.tv_datetime);
            viewHolder.tv_zone = convertView.findViewById(R.id.tv_zone);
            viewHolder.tv_remark = convertView.findViewById(R.id.tv_remark);

            // 设置缓存
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 设置数据
        mCourierData = mList.get(position);
        viewHolder.tv_remark.setText(mCourierData.getRemark());
        viewHolder.tv_datetime.setText(mCourierData.getDatetime());
        viewHolder.tv_zone.setText(mCourierData.getZone());

        return convertView;
    }

    class ViewHolder {
        TextView tv_zone;
        TextView tv_remark;
        TextView tv_datetime;
    }
}
