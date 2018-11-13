package com.smartbutler.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartbutler.R;
import com.smartbutler.entity.WeChatData;
import com.smartbutler.utils.L;
import com.smartbutler.utils.PicassoUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.List;

/**
 * 微信精选adapter
 */

public class WeChatAdapter extends BaseAdapter {

    private final int screenWidth;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<WeChatData> mList;
    private WeChatData mWeChatData;

    private WindowManager mWindowManager;

    public WeChatAdapter(Context mContext, List<WeChatData> mList) {
        this.mContext = mContext;
        this.mList = mList;

        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = mWindowManager.getDefaultDisplay().getWidth();
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
            convertView = mLayoutInflater.inflate(R.layout.wechat_item, null);
            viewHolder.iv_image = convertView.findViewById(R.id.iv_image);
            viewHolder.tv_title = convertView.findViewById(R.id.tv_title);
            viewHolder.tv_content = convertView.findViewById(R.id.tv_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 设置数据
        mWeChatData = mList.get(position);
        viewHolder.tv_title.setText(mWeChatData.getTitle());
        viewHolder.tv_content.setText(mWeChatData.getSource());

        // 加载图片
        String url = mWeChatData.getImgUrl();
        Log.d("WeChatAdapter", "url: " + url);

        //        if (!TextUtils.isEmpty(url)) {
        //            Picasso.with(mContext).load(url).into(viewHolder.iv_image);
        //        }

        PicassoUtils.loadImageViewSize(mContext, url, screenWidth / 3, 200, viewHolder.iv_image);

        return convertView;
    }

    class ViewHolder {
        ImageView iv_image;
        TextView tv_title;
        TextView tv_content;
    }
}
