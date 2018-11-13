package com.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.smartbutler.R;
import com.smartbutler.entity.GirlData;
import com.smartbutler.utils.PicassoUtils;

import java.util.List;

/**
 * 妹子适配器
 */

public class GirlAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<GirlData> mList;
    private GirlData mGirlData;

    // 屏幕的宽
    private int width;
    private WindowManager mWindowManager;

    public GirlAdapter(Context mContext, List<GirlData> mList) {
        this.mContext = mContext;
        this.mList = mList;

        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width = mWindowManager.getDefaultDisplay().getWidth();
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
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.girl_item, null);
            viewHolder.iv_girl = convertView.findViewById(R.id.iv_girl);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        mGirlData = mList.get(position);
        // 解析图片
        String url = mGirlData.getUrl();
        PicassoUtils.loadImageViewSize(mContext, url, width / 2, 500, viewHolder.iv_girl);

        return convertView;
    }

    class ViewHolder {
        private ImageView iv_girl;
    }
}
