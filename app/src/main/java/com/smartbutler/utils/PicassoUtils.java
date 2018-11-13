package com.smartbutler.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * Picasso封装
 */

public class PicassoUtils {
    // 默认加载
    public static void loadImageView(Context mContext, String url, ImageView imageView) {
        if (!TextUtils.isEmpty(url)) {
            Picasso.with(mContext).load(url).into(imageView);
        }
    }

    /**
     * 加载指定大小图片
     *
     * @param mContext
     * @param url
     * @param width
     * @param height
     * @param imageView
     */
    public static void loadImageViewSize(Context mContext, String url, int width, int height,
                                         ImageView imageView) {
        if (!TextUtils.isEmpty(url)) {
            Picasso.with(mContext).
                    load(url).
                    resize(width, height).
                    centerCrop().into(imageView);
        }
    }

    /**
     * 加载正确/错误/默认图片
     *
     * @param mContext
     * @param url
     * @param loadImg
     * @param errorImg
     */
    public static void loadImageViewViewHolder(Context mContext, String url, int loadImg,
                                               int errorImg, ImageView imageView) {
        if (!TextUtils.isEmpty(url)) {
            Picasso.with(mContext).
                    load(url).
                    placeholder(loadImg).
                    error(errorImg).
                    into(imageView);
        }
    }

    public static void loadImageViewViewHolder(Context mContext, String url, ImageView imageView) {
        if (!TextUtils.isEmpty(url)) {
            Picasso.with(mContext).
                    load(url).
                    transform(new CropSquareTransformation()).
                    into(imageView);
        }
    }

    // 按比例裁剪，矩形
    public static class CropSquareTransformation implements Transformation {

        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override
        public String key() {
            return "codesaid";
        }
    }
}
