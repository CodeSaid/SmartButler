package com.smartbutler.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.smartbutler.R;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

public class QrCodeActivity extends BaseActivity {
    private ImageView iv_qrcode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        initView();
    }

    private void initView() {
        iv_qrcode = findViewById(R.id.iv_qrcode);

        //屏幕的宽
        int width = getResources().getDisplayMetrics().widthPixels;
        Bitmap qrCodeBitmap = EncodingUtils.createQRCode("我是智能管家", width / 2, width / 2,
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        iv_qrcode.setImageBitmap(qrCodeBitmap);

    }
}
